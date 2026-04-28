package com.github.impleo.zam.sam;

//      #    A vowel (A, E, I, O, U, Y)
class IsVowel implements ContextPredicate {

    private static final boolean[] VOWELS;

    static {
        VOWELS = new boolean[256];

        VOWELS['a'] = true;
        VOWELS['A'] = true;

        VOWELS['e'] = true;
        VOWELS['E'] = true;

        VOWELS['i'] = true;
        VOWELS['I'] = true;

        VOWELS['o'] = true;
        VOWELS['O'] = true;

        VOWELS['u'] = true;
        VOWELS['U'] = true;
    }

    @Override
    public boolean isMatching(CharSequence cs, int index) {
        char c = cs.charAt(index);

        if (c < 0 || c >= VOWELS.length) {
            return false;
        }

        return VOWELS[c];
    }
}
