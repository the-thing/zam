package com.github.impleo.zam.sam;

// A voiced consonant (B, D, V, G, J, L, M, N, R, W, Z)
final class IsVoicedConsonant implements ContextPredicate {

    private static boolean[] VOICED_CONSONANTS;

    static {
        VOICED_CONSONANTS = new boolean[256];

        VOICED_CONSONANTS['b'] = true;
        VOICED_CONSONANTS['B'] = true;

        VOICED_CONSONANTS['d'] = true;
        VOICED_CONSONANTS['D'] = true;

        VOICED_CONSONANTS['v'] = true;
        VOICED_CONSONANTS['V'] = true;

        VOICED_CONSONANTS['g'] = true;
        VOICED_CONSONANTS['G'] = true;

        VOICED_CONSONANTS['j'] = true;
        VOICED_CONSONANTS['J'] = true;

        VOICED_CONSONANTS['l'] = true;
        VOICED_CONSONANTS['L'] = true;

        VOICED_CONSONANTS['m'] = true;
        VOICED_CONSONANTS['M'] = true;

        VOICED_CONSONANTS['n'] = true;
        VOICED_CONSONANTS['N'] = true;

        VOICED_CONSONANTS['r'] = true;
        VOICED_CONSONANTS['R'] = true;

        VOICED_CONSONANTS['w'] = true;
        VOICED_CONSONANTS['W'] = true;

        VOICED_CONSONANTS['z'] = true;
        VOICED_CONSONANTS['Z'] = true;
    }

    @Override
    public boolean isMatching(CharSequence cs, int index) {
        char c = cs.charAt(index);

        if (c < 0 || c >= VOICED_CONSONANTS.length) {
            return false;
        }

        return VOICED_CONSONANTS[c];
    }
}
