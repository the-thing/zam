package com.github.thething.zam.reciter;

public final class WordBreakMatcher implements ContextMatcher {

    public static final WordBreakMatcher INSTANCE = new WordBreakMatcher();
    public static final char CHAR = ' ';
    public static final String CONTEXT = new String(new char[]{CHAR});

    @Override
    public int getMatchingLength(CharSequence cs, int index) {
        if (index < 0 || index >= cs.length()) {
            // we treat out of bounds as word break
            return 0;
        }

        char c = cs.charAt(index);

        // anything that is not a letter or digit is a word break e.g. '?', '!', '-'
        if (!Character.isLetter(c) && !Character.isDigit(c)) {
            return 1;
        }

        return -1;
    }

    @Override
    public String getExpression() {
        return CONTEXT;
    }
}
