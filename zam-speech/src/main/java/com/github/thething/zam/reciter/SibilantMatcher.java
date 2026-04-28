package com.github.thething.zam.reciter;

/**
 * {@link ContextMatcher} that matches a sibilant sound, corresponding to the {@code &} context symbol.
 *
 * <p>Single-letter sibilants matched: C, G, J, S, X, Z (both cases).
 * Two-letter sibilants matched: CH, SH (the index must point at the {@code H}).
 */
public final class SibilantMatcher implements ContextMatcher {

    public static final SibilantMatcher INSTANCE = new SibilantMatcher();
    public static final char CHAR = '&';
    public static final String CONTEXT = new String(new char[]{CHAR});

    @Override
    public int getMatchingLength(CharSequence cs, int index) {
        if (index < 0 || index >= cs.length()) {
            return -1;
        }

        char c = cs.charAt(index);

        if (Characters.isSingleLetterSibilant(c)) {
            return 1;
        }

        // match extra sibilants: CH, SH

        if (c != 'h' && c != 'H') {
            // doesn't match second letter
            return -1;
        }

        if (index == 0) {
            // no previous character to match
            return -1;
        }

        char previous = cs.charAt(index - 1);

        if (previous == 'c' || previous == 'C' || previous == 's' || previous == 'S') {
            return 2;
        }

        return -1;
    }

    @Override
    public String getExpression() {
        return CONTEXT;
    }
}
