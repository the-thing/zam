package com.github.thething.zam.reciter;

import com.github.thething.zam.common.Strings;
import com.github.thething.zam.io.Resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Registry of {@link ReciterRule}s indexed by the first character of their pattern for O(1)
 * bucket lookup.
 *
 * <p>The default set of rules is bundled as a classpath resource and is loaded via
 * {@link #load()}.  Custom rule files can be loaded with {@link #load(String)}.
 */
public final class ReciterRuleRegistry {

    private static final String DEFAULT_RECITER_RULES_RESOURCE = "rules/reciter-rules.txt";

    private final ReciterRule[][] rulesByChar;

    private ReciterRuleRegistry(ReciterRule[][] rulesByChar) {
        this.rulesByChar = rulesByChar;
    }

    /**
     * Loads the default reciter rules from the bundled classpath resource.
     *
     * @return a populated {@link ReciterRuleRegistry}
     * @throws IOException if the resource cannot be read or contains invalid rules
     */
    public static ReciterRuleRegistry load() throws IOException {
        return load(DEFAULT_RECITER_RULES_RESOURCE);
    }

    /**
     * Loads reciter rules from the named classpath resource.
     *
     * @param name the classpath-relative resource name (e.g. {@code "rules/reciter-rules.txt"})
     * @return a populated {@link ReciterRuleRegistry}
     * @throws IOException      if the resource cannot be read or contains invalid rules
     * @throws RuntimeException if the resource contains rules that cannot be parsed
     */
    public static ReciterRuleRegistry load(String name) throws IOException {
        try {
            return loadInternal(name);
        } catch (IOException e) {
            throw new IOException("Failed to parse rules", e);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to parse rules", e);
        }
    }

    private static ReciterRuleRegistry loadInternal(String name) throws IOException {
        List<ReciterRule> rules = new ArrayList<>();

        Resources.forEachLine(name, (lineNumber, line) -> {
            if (line.isBlank()) {
                return;
            }

            line = line.trim();
            line = stripExtraCharacters(line);

            if (line.charAt(0) == ']') {

                if (line.length() != 2) {
                    throw new IOException("Invalid char group: " + line);
                }

                return;
            }

            ReciterRule rule = ReciterRule.parse(line);
            rules.add(rule);
        });

        ReciterRule[][] rulesByChar = convertToArray(rules);

        return new ReciterRuleRegistry(rulesByChar);
    }

    @SuppressWarnings("unchecked")
    private static ReciterRule[][] convertToArray(List<ReciterRule> rules) throws IOException {
        List<ReciterRule>[] rulesByChar = new List[128];

        for (int i = 0; i < rulesByChar.length; i++) {
            rulesByChar[i] = new ArrayList<>();
        }

        for (int i = 0; i < rules.size(); i++) {
            ReciterRule rule = rules.get(i);
            char c = rule.getFirstPatternChar();

            if (c >= 128) {
                throw new IOException("Invalid first character: '" + c + "' in rule: " + rule);
            }

            rulesByChar[c].add(rule);
        }

        ReciterRule[][] newRulesByChar = new ReciterRule[128][];

        for (int i = 0; i < rulesByChar.length; i++) {
            newRulesByChar[i] = rulesByChar[i].toArray(ReciterRule[]::new);
        }

        return newRulesByChar;
    }

    private static String stripExtraCharacters(String text) {
        text = Strings.strip(text, '\'');
        text = Strings.strip(text, ',');
        return text;
    }

    /**
     * Finds the first rule in this registry that matches at the given position in the text.
     *
     * @param text  the input text
     * @param index the position at which to attempt matching
     * @return the first matching {@link ReciterRule}, or {@code null} if no rule matches
     */
    public ReciterRule findMatchingRule(String text, int index) {
        char c = text.charAt(index);

        if (c >= rulesByChar.length) {
            return null;
        }

        c = Character.toUpperCase(c);
        ReciterRule[] rules = rulesByChar[c];

        for (ReciterRule rule : rules) {
            if (rule.isMatch(text, index)) {
                return rule;
            }
        }

        return null;
    }
}
