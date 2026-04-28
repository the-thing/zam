package com.github.thething.zam.reciter;

/**
 * {@link ContextMatcher} that matches zero or more consecutive consonants, corresponding to the {@code :} context
 * symbol.
 *
 * <p>Two singleton instances are provided:
 * <ul>
 *   <li>{@link #LEFT_TO_RIGHT_INSTANCE} – scans forward (right context)</li>
 *   <li>{@link #RIGHT_TO_LEFT_INSTANCE} – scans backward (left context)</li>
 * </ul>
 */
public final class AnyConsonantMatcher implements ContextMatcher {

    public static final AnyConsonantMatcher LEFT_TO_RIGHT_INSTANCE = new AnyConsonantMatcher(true);
    public static final AnyConsonantMatcher RIGHT_TO_LEFT_INSTANCE = new AnyConsonantMatcher(false);
    public static final char CHAR = ':';
    public static final String CONTEXT = String.valueOf(CHAR);

    private final boolean leftToRight;

    /**
     * Creates an instance that scans in the specified direction.
     *
     * @param leftToRight {@code true} to scan forwards (right context), {@code false} to scan backwards (left context)
     */
    public AnyConsonantMatcher(boolean leftToRight) {
        this.leftToRight = leftToRight;
    }

    @Override
    public int getMatchingLength(CharSequence cs, int index) {
        if (index < 0 || index >= cs.length()) {
            // matching zero characters is valid
            return 0;
        }

        if (leftToRight) {
            return countRightMatchingConsonants(cs, index);
        } else {
            return countLeftMatchingConsonants(cs, index);
        }
    }

    @Override
    public String getExpression() {
        return CONTEXT;
    }

    private static int countLeftMatchingConsonants(CharSequence cs, int index) {
        int count = 0;

        for (int i = index; i >= 0; i--) {
            char c = cs.charAt(i);

            if (!Characters.isConsonant(c)) {
                break;
            }

            count++;
        }

        return count;
    }

    private static int countRightMatchingConsonants(CharSequence cs, int index) {
        int count = 0;

        for (int i = index; i < cs.length(); i++) {
            char c = cs.charAt(i);

            if (!Characters.isConsonant(c)) {
                break;
            }

            count++;
        }

        return count;
    }
}
