package com.github.thething.zam.reciter;

/**
 * Interface for context matchers used in pronunciation rule evaluation.
 *
 * <p>A {@code ContextMatcher} inspects the characters around the current match position and
 * reports how many characters it consumed (or {@code -1} when the context requirement is not met).
 */
public interface ContextMatcher {

    /**
     * Attempts to match the context at the given position in {@code cs}.
     *
     * <p>For left-context (prefix) matchers the index points to the character immediately to the
     * <em>left</em> of the pattern start and the matcher scans backwards; for right-context
     * (suffix) matchers the index points to the character immediately to the <em>right</em> of the pattern end and the
     * matcher scans forwards.
     *
     * @param cs    the character sequence to match against
     * @param index the starting index for the match attempt
     * @return the number of characters consumed ({@code >= 0}) on success, or {@code -1} on failure
     */
    int getMatchingLength(CharSequence cs, int index);

    /**
     * Returns the canonical expression string for this matcher as it appears in a rule definition (e.g. {@code "^"} for
     * a consonant matcher, {@code ":"} for any-consonant, etc.).
     *
     * @return the expression string
     */
    String getExpression();

    /**
     * Factory method: creates the appropriate {@link ContextMatcher} for the given context symbol.
     *
     * @param c           the context symbol character (e.g. {@code '^'}, {@code ':'}, etc.)
     * @param leftToRight {@code true} if the matcher will be used in a right-context (suffix), {@code false} for a
     *                    left-context (prefix)
     * @return the corresponding {@link ContextMatcher}
     * @throws IllegalArgumentException if {@code c} is not a recognized context symbol
     */
    static ContextMatcher create(char c, boolean leftToRight) {
        // ' ' - Word break
        // '^' - Consonant
        // '.' - Voiced consonant (B, D, G, J, L, M, N, R, V, W, Z)
        // '@' - Alternative voiced consonant (T, S, R, D, L, Z, N, J)
        // ':' - Zero or more consonants
        // '#' - Vowel (A, E, I, O, U, Y)
        // '&' - Sibilant (S, C, G, Z, X, J, CH, SH)
        // '+' - One of E, I, Y (front vowel)
        // '%' - Suffix like ER, E, ES, ED, ING, ELY, EFUL
        return switch (c) {

            case WordBreakMatcher.CHAR -> WordBreakMatcher.INSTANCE;

            case ConsonantMatcher.CHAR -> ConsonantMatcher.INSTANCE;

            case AnyConsonantMatcher.CHAR -> leftToRight ?
                    AnyConsonantMatcher.LEFT_TO_RIGHT_INSTANCE :
                    AnyConsonantMatcher.RIGHT_TO_LEFT_INSTANCE;

            case VowelMatcher.CHAR -> VowelMatcher.INSTANCE;

            case VoicedConsonantMatcher.CHAR -> VoicedConsonantMatcher.INSTANCE;

            case AltVoicedConsonantMatcher.CHAR -> AltVoicedConsonantMatcher.INSTANCE;

            case SibilantMatcher.CHAR -> SibilantMatcher.INSTANCE;

            case FrontVowelMatcher.CHAR -> FrontVowelMatcher.INSTANCE;

            case SuffixMatcher.CHAR -> SuffixMatcher.INSTANCE;

            default -> {

                if (c == '\'' || Characters.isUpperCaseLetter(c)) {
                    yield new CharacterMatcher(c);
                }

                throw new IllegalArgumentException("Unsupported context matcher character: " + c);
            }
        };
    }

    /**
     * Parses a subsequence of a rule string as a context expression and returns the appropriate
     * {@link ContextMatcher}.
     *
     * @param rule        the full rule string
     * @param fromIndex   start of the context substring (inclusive)
     * @param toIndex     end of the context substring (exclusive)
     * @param leftToRight {@code true} for right-context (suffix), {@code false} for left-context (prefix)
     * @return an {@link EmptyMatcher} if the substring is empty, a single matcher if it is one character long, or a
     * {@link CompositeMatcher} otherwise
     * @throws RuntimeException if the substring contains invalid context characters
     */
    static ContextMatcher parseContext(CharSequence rule, int fromIndex, int toIndex, boolean leftToRight) {
        validateContext(rule, fromIndex, toIndex, leftToRight);

        if (fromIndex < 0 || fromIndex >= toIndex) {
            return EmptyMatcher.INSTANCE;
        } else if (fromIndex == toIndex - 1) {
            return create(rule.charAt(fromIndex), leftToRight);
        } else {
            ContextMatcher[] matchers = new ContextMatcher[toIndex - fromIndex];

            for (int i = 0; i < matchers.length; i++) {
                matchers[i] = ContextMatcher.create(rule.charAt(i + fromIndex), leftToRight);
            }

            return new CompositeMatcher(leftToRight, matchers);
        }
    }

    private static void validateContext(CharSequence rule, int fromIndex, int toIndex, boolean leftToRight) {
        for (int i = fromIndex; i < toIndex; i++) {
            char c = rule.charAt(i);

            if (!Characters.isContextChar(rule.charAt(i))) {
                throw new RuntimeException("Invalid rule character: " + c + " at index: " + i);
            }

            if (c == SuffixMatcher.CHAR) {
                if (leftToRight) {
                    if (rule.charAt(toIndex) != '=') {
                        throw new RuntimeException("Suffix matcher must be last character in right context");
                    }
                } else {
                    throw new RuntimeException("Suffix matcher is not allowed in left context: '" + c + "' at index: " + i);
                }
            }
        }
    }
}