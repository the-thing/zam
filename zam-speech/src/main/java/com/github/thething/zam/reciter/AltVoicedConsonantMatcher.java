package com.github.thething.zam.reciter;

/**
 * {@link ContextMatcher} that matches one of the alternative voiced consonants T, S, R, D, L, Z, N, J (both cases),
 * corresponding to the {@code @} context symbol.
 */
public final class AltVoicedConsonantMatcher implements ContextMatcher {

    public static final AltVoicedConsonantMatcher INSTANCE = new AltVoicedConsonantMatcher();
    public static final char CHAR = '@';
    public static final String CONTEXT = new String(new char[]{CHAR});

    @Override
    public int getMatchingLength(CharSequence cs, int index) {
        if (index < 0 || index >= cs.length()) {
            return -1;
        }

        return Characters.isAltVoicedConsonant(cs.charAt(index)) ? 1 : -1;
    }

    @Override
    public String getExpression() {
        return CONTEXT;
    }
}
