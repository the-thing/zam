package com.github.thething.zam.reciter;

/**
 * {@link ContextMatcher} that always succeeds and consumes zero characters.
 *
 * <p>Used as a placeholder when a rule has no left or right context requirement.
 */
public final class EmptyMatcher implements ContextMatcher {

    public static final EmptyMatcher INSTANCE = new EmptyMatcher();

    @Override
    public int getMatchingLength(CharSequence cs, int index) {
        // always match zero characters
        return 0;
    }

    @Override
    public String getExpression() {
        return "";
    }
}
