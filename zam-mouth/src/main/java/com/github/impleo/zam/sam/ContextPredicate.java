package com.github.impleo.zam.sam;

interface ContextPredicate {

    boolean isMatching(CharSequence cs, int index);
}
