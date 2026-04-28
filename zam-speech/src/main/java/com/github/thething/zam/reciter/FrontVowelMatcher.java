package com.github.thething.zam.reciter;

/**
 * {@link ContextMatcher} that matches a front vowel (E, I, Y – both cases), corresponding to the {@code +} context
 * symbol.
 */
public final class FrontVowelMatcher implements ContextMatcher {

    public static final FrontVowelMatcher INSTANCE = new FrontVowelMatcher();
    public static final char CHAR = '+';
    public static final String CONTEXT = new String(new char[]{CHAR});

    @Override
    public int getMatchingLength(CharSequence cs, int index) {
        if (index < 0 || index >= cs.length()) {
            return -1;
        }

        return Characters.isFrontVowel(cs.charAt(index)) ? 1 : -1;
    }

    @Override
    public String getExpression() {
        return CONTEXT;
    }
}
