package com.github.impleo.zam.sam;

//      &    A sibilant (S, C, G, Z, X, J, CH, SH)
final class IsSibilant implements ContextPredicate {

    private static final boolean[] SIBILANTS;

    static {
        SIBILANTS = new boolean[256];

        SIBILANTS['s'] = true;
        SIBILANTS['S'] = true;

        SIBILANTS['c'] = true;
        SIBILANTS['C'] = true;

        SIBILANTS['g'] = true;
        SIBILANTS['G'] = true;

        SIBILANTS['z'] = true;
        SIBILANTS['Z'] = true;

        SIBILANTS['x'] = true;
        SIBILANTS['X'] = true;

        SIBILANTS['j'] = true;
        SIBILANTS['J'] = true;
    }

    @Override
    public boolean isMatching(CharSequence cs, int index) {
        char c = cs.charAt(index);

        if (c < 0 || c >= SIBILANTS.length) {
            return false;
        }

        if (SIBILANTS[c]) {
            return true;
        }

        if (index == 0) {
            return false;
        }

        // CH, SH case
        char previous = cs.charAt(index - 1);

        if (previous == 'c' || previous == 'C' || previous == 's' || previous == 'S') {
            return true;
        }

        return false;
    }
}
