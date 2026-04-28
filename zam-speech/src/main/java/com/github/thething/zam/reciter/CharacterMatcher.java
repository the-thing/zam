package com.github.thething.zam.reciter;

/**
 * {@link ContextMatcher} that matches a specific character (case-insensitive).
 */
public final class CharacterMatcher implements ContextMatcher {

    private final char lowerCase;
    private final char upperCase;
    private final String context;

    /**
     * Creates a matcher for the given character; matching is performed case-insensitively.
     *
     * @param c the character to match (upper- or lower-case)
     */
    public CharacterMatcher(char c) {
        this.lowerCase = Characters.toLowerCase(c);
        this.upperCase = Characters.toUpperCase(c);
        this.context = String.valueOf(upperCase);
    }

    @Override
    public int getMatchingLength(CharSequence cs, int index) {
        if (index < 0 || index >= cs.length()) {
            return -1;
        }

        char c = cs.charAt(index);

        return c == lowerCase || c == upperCase ? 1 : -1;
    }

    @Override
    public String getExpression() {
        return context;
    }
}
