package com.github.thething.zam.reciter;

import com.github.thething.zam.common.Strings;

import static java.util.Objects.requireNonNull;

/**
 * Converts English text into SAM phonetic notation using a rule-based lookup.
 *
 * <p>The conversion is driven by a {@link ReciterRuleRegistry} that indexes pronunciation rules
 * by the first character of their pattern.  Whitespace is normalized, periods are passed through as-is, and any
 * characters not matched by a rule are silently skipped.
 */
public final class Reciter {

    private final ReciterRuleRegistry reciterRuleRegistry;

    /**
     * Creates a reciter backed by the given rule registry.
     *
     * @param reciterRuleRegistry the pronunciation rule registry; must not be {@code null}
     */
    public Reciter(ReciterRuleRegistry reciterRuleRegistry) {
        this.reciterRuleRegistry = requireNonNull(reciterRuleRegistry);
    }

    /**
     * Converts English text into SAM phonetic notation.
     *
     * @param text the English input text
     * @return a phonetic string in SAM notation suitable for {@link com.github.thething.zam.enricher.PhonemeEnricher}
     */
    public String recite(String text) {
        StringBuilder phonemes = new StringBuilder(text.length());
        recite(text, phonemes);
        return phonemes.toString();
    }

    /**
     * Converts English text into SAM phonetic notation and appends the result to the supplied builder.
     *
     * @param text the English input text
     * @param out  the builder to which the phonetic output is appended
     */
    public void recite(String text, StringBuilder out) {
        int index = 0;

        boolean lastWhitespace = false;

        // advance to first non whitespace character
        while (index < text.length() && Characters.isWhitespace(text.charAt(index))) {
            index++;
        }

        while (index < text.length()) {
            char c = text.charAt(index);
            c = Characters.toUpperCase(c);

            if (Characters.isWhitespace(c)) {
                if (!lastWhitespace) {
                    out.append(' ');
                }

                lastWhitespace = true;
                index++;
                continue;
            }

            lastWhitespace = false;

            if (c == '.') {
                out.append('.');
                index++;
                continue;
            }

            ReciterRule rule = reciterRuleRegistry.findMatchingRule(text, index);

            if (rule != null) {
                out.append(rule.getPhonemes());
                index += Math.max(rule.getPattern().length(), 1);
                continue;
            }

            // unrecognized characters are skipped
            index++;
        }

        Strings.trimTail(out);
    }
}
