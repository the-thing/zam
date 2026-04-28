package com.github.thething.zam.reciter;

public final class VowelMatcher implements ContextMatcher {

    public static final VowelMatcher INSTANCE = new VowelMatcher();
    public static final char CHAR = '#';
    public static final String CONTEXT = new String(new char[]{CHAR});

    @Override
    public int getMatchingLength(CharSequence cs, int index) {
        if (index < 0 || index >= cs.length()) {
            return -1;
        }

        return Characters.isVowel(cs.charAt(index)) ? 1 : -1;
    }

    @Override
    public String getExpression() {
        return CONTEXT;
    }
}
