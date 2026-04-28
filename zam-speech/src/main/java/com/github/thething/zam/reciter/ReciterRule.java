package com.github.thething.zam.reciter;

import com.github.thething.zam.common.Strings;

import static java.util.Objects.requireNonNull;

/**
 * A single pronunciation rule that maps a text pattern (with optional left and right context) to a sequence of SAM
 * phonemes.
 *
 * <p>Rules are expressed in the form {@code prefix(pattern)suffix=phonemes}, where:
 * <ul>
 *   <li>{@code prefix} – left context matcher (may be empty)</li>
 *   <li>{@code pattern} – the literal text to match (case-insensitive)</li>
 *   <li>{@code suffix} – right context matcher (may be empty)</li>
 *   <li>{@code phonemes} – the SAM phonetic output produced when this rule fires</li>
 * </ul>
 */
public final class ReciterRule {

    private final ContextMatcher prefix;
    private final String pattern;
    private final ContextMatcher suffix;
    private final String phonemes;
    private final String expression;

    /**
     * Creates a rule from its pre-parsed components.
     *
     * @param prefix   left context matcher; must not be {@code null}
     * @param pattern  the literal text pattern to match; must not be {@code null}
     * @param suffix   right context matcher; must not be {@code null}
     * @param phonemes the SAM phonetic output; must not be {@code null}
     */
    public ReciterRule(ContextMatcher prefix, String pattern, ContextMatcher suffix, String phonemes) {
        this.prefix = requireNonNull(prefix);
        this.pattern = requireNonNull(pattern);
        this.suffix = requireNonNull(suffix);
        this.phonemes = requireNonNull(phonemes);
        this.expression = buildExpression(prefix, pattern, suffix, phonemes);
    }

    private static String buildExpression(ContextMatcher prefix, CharSequence pattern, ContextMatcher suffix, CharSequence phonemes) {
        return prefix.getExpression() + '(' + pattern + ')' + suffix.getExpression() + '=' + phonemes;
    }

    /**
     * Parses a rule expression of the form {@code prefix(pattern)suffix=phonemes}.
     *
     * @param rule the rule expression to parse
     * @return the parsed {@link ReciterRule}
     * @throws RuntimeException if the expression is malformed
     */
    public static ReciterRule parse(CharSequence rule) {
        int leftBracketIndex = Strings.indexOf(rule, '(');

        if (leftBracketIndex == -1) {
            throw new RuntimeException("No pattern opening bracket: " + rule);
        }

        int rightBracketIndex = Strings.indexOf(rule, ')', leftBracketIndex + 1);

        if (rightBracketIndex == -1) {
            throw new RuntimeException("No pattern closing bracket: " + rule);
        }

        if (leftBracketIndex > rightBracketIndex) {
            throw new RuntimeException("Invalid pattern bracket position: " + rule);
        }

        int equalsIndex = Strings.lastIndexOf(rule, '=');

        if (equalsIndex == -1) {
            throw new RuntimeException("No equals sign: " + rule);
        }

        ContextMatcher prefix = ContextMatcher.parseContext(rule, 0, leftBracketIndex, false);
        String prefixExpression = prefix.getExpression();

        if (!Strings.contentEquals(prefixExpression, 0, prefixExpression.length(), rule, 0, leftBracketIndex)) {
            throw new RuntimeException("Failed to parse left context expression: " + rule.subSequence(0, leftBracketIndex));
        }

        ContextMatcher suffix = ContextMatcher.parseContext(rule, rightBracketIndex + 1, equalsIndex, true);
        String suffixExpression = suffix.getExpression();

        if (!Strings.contentEquals(suffixExpression, 0, suffixExpression.length(), rule, rightBracketIndex + 1, equalsIndex)) {
            throw new RuntimeException("Failed to parse right context expression: " + rule.subSequence(rightBracketIndex + 1, equalsIndex));
        }

        String pattern = parsePattern(rule, leftBracketIndex + 1, rightBracketIndex);
        String phonemes = parsePhonemes(rule, equalsIndex + 1);

        return new ReciterRule(prefix, pattern, suffix, phonemes);
    }

    private static String parsePhonemes(CharSequence rule, int fromIndex) {
        for (int i = fromIndex; i < rule.length(); i++) {
            if (!Characters.isPhonemeChar(rule.charAt(i))) {
                throw new RuntimeException("Invalid pattern character: '" + rule.charAt(i) + "', ");
            }
        }

        return rule.toString().substring(fromIndex);
    }

    private static String parsePattern(CharSequence rule, int fromIndex, int toIndex) {
        for (int i = fromIndex; i < toIndex; i++) {
            char c = rule.charAt(i);

            if (!Characters.isPatternChar(c)) {
                throw new RuntimeException("Invalid pattern character: '" + c + "' at index: " + i);
            }
        }

        if (fromIndex == toIndex) {
            throw new RuntimeException("Empty pattern");
        }

        return rule.toString().substring(fromIndex, toIndex);
    }

    /**
     * Returns {@code true} if this rule matches at the given position in the text.
     *
     * @param text  the input text to match against
     * @param index the position in {@code text} at which to start matching the pattern
     * @return {@code true} if the rule matches; {@code false} otherwise
     */
    public boolean isMatch(CharSequence text, int index) {
        if (index < 0 || index >= text.length()) {
            return false;
        }

        if (!isMatchingPattern(pattern, text, index)) {
            return false;
        }

        int leftMatchingCount = prefix.getMatchingLength(text, index - 1);

        if (leftMatchingCount < 0) {
            return false;
        }

        int rightMatchingCount = suffix.getMatchingLength(text, index + pattern.length());

        return rightMatchingCount >= 0;
    }

    private static boolean isMatchingPattern(String pattern, CharSequence text, int index) {
        if (pattern.length() > text.length() - index) {
            return false;
        }

        for (int i = 0; i < pattern.length(); i++) {
            char patternChar = pattern.charAt(i);
            patternChar = Characters.toUpperCase(patternChar);

            char textChar = text.charAt(index + i);
            textChar = Characters.toUpperCase(textChar);

            if (patternChar != textChar) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the first character of this rule's pattern.
     *
     * @return the first character of the pattern
     */
    public char getFirstPatternChar() {
        return pattern.charAt(0);
    }

    /**
     * Returns the left context matcher.
     *
     * @return the prefix {@link ContextMatcher}
     */
    public ContextMatcher getPrefix() {
        return prefix;
    }

    /**
     * Returns the literal text pattern.
     *
     * @return the pattern string
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * Returns the right context matcher.
     *
     * @return the suffix {@link ContextMatcher}
     */
    public ContextMatcher getSuffix() {
        return suffix;
    }

    /**
     * Returns the SAM phonetic output produced when this rule fires.
     *
     * @return the phoneme string
     */
    public String getPhonemes() {
        return phonemes;
    }

    /**
     * Returns the full rule expression in the canonical form {@code prefix(pattern)suffix=phonemes}.
     *
     * @return the rule expression
     */
    public String getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "Rule{" +
                ", prefix=" + prefix +
                ", pattern='" + pattern + '\'' +
                ", suffix=" + suffix +
                ", phonemes='" + phonemes + '\'' +
                ", expression='" + expression + '\'' +
                '}';
    }
}
