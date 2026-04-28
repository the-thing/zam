package com.github.thething.zam.reciter;

/**
 * {@link ContextMatcher} that matches a single consonant character, corresponding to the {@code ^} context symbol.
 */
final class ConsonantMatcher implements ContextMatcher {

    public static final ConsonantMatcher INSTANCE = new ConsonantMatcher();
    public static final char CHAR = '^';
    public static final String CONTEXT = new String(new char[]{CHAR});

    @Override
    public int getMatchingLength(CharSequence cs, int index) {
        if (index < 0 || index >= cs.length()) {
            return -1;
        }

        return Characters.isConsonant(cs.charAt(index)) ? 1 : -1;
    }

    @Override
    public String getExpression() {
        return CONTEXT;
    }
}
