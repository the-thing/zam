package com.github.thething.zam.reciter;

import java.util.Arrays;

/**
 * {@link ContextMatcher} that evaluates a sequence of child matchers in order.
 *
 * <p>When scanning a right-context (left-to-right) each matcher is applied starting at the
 * current index and the index advances by the matched length.  For left-context (right-to-left) the matchers are
 * evaluated in reverse order.
 */
public final class CompositeMatcher implements ContextMatcher {

    private final boolean leftToRight;
    private final ContextMatcher[] matchers;
    private final String context;

    /**
     * Creates a composite matcher from an ordered list of child matchers.
     *
     * @param leftToRight {@code true} to evaluate matchers left-to-right (right context), {@code false} to evaluate
     *                    right-to-left (left context)
     * @param matchers    the child matchers; must not be {@code null} or empty
     * @throws IllegalArgumentException if {@code matchers} is {@code null} or empty
     */
    public CompositeMatcher(boolean leftToRight, ContextMatcher... matchers) {
        this.leftToRight = leftToRight;

        if (matchers == null || matchers.length == 0) {
            throw new IllegalArgumentException("At least one matcher is required");
        }

        this.matchers = matchers;
        this.context = buildContext(matchers);
    }

    private String buildContext(ContextMatcher[] matchers) {
        StringBuilder contextBuilder = new StringBuilder(matchers.length);

        for (int i = 0; i < matchers.length; i++) {
            ContextMatcher matcher = matchers[i];
            contextBuilder.append(matcher.getExpression());
        }

        return contextBuilder.toString();
    }

    @Override
    public int getMatchingLength(CharSequence cs, int index) {
        // composite matcher doesn't check the index bounds it is the responsibility of individual context matchers
        if (leftToRight) {
            return countLeftToRight(cs, index);
        } else {
            return countRightToLeft(cs, index);
        }
    }

    @Override
    public String getExpression() {
        return context;
    }

    private int countLeftToRight(CharSequence cs, int index) {
        int currentIndex = index;

        for (int i = 0; i < matchers.length; i++) {
            ContextMatcher matcher = matchers[i];
            int length = matcher.getMatchingLength(cs, currentIndex);

            if (length == -1) {
                return -1;
            }

            currentIndex += length;
        }

        return currentIndex - index;
    }

    private int countRightToLeft(CharSequence cs, int index) {
        int currentIndex = index;

        for (int i = matchers.length - 1; i >= 0; i--) {
            ContextMatcher matcher = matchers[i];
            int length = matcher.getMatchingLength(cs, currentIndex);

            if (length == -1) {
                return -1;
            }

            currentIndex -= length;
        }

        return index - currentIndex;
    }

    @Override
    public String toString() {
        return "CompositeMatcher{" +
                "leftToRight=" + leftToRight +
                ", matchers=" + Arrays.toString(matchers) +
                ", context='" + context + '\'' +
                '}';
    }
}
