package com.github.thething.zam.reciter;

/**
 * {@link ContextMatcher} that matches common English suffixes, corresponding to the {@code %} context symbol.
 *
 * <p>Recognised suffixes (case-insensitive):
 * <ul>
 *   <li>E – at end of word or end of text</li>
 *   <li>ED, ER, ES</li>
 *   <li>ELY</li>
 *   <li>EFUL</li>
 *   <li>ING</li>
 * </ul>
 */
public final class SuffixMatcher implements ContextMatcher {

    public static final SuffixMatcher INSTANCE = new SuffixMatcher();
    public static final char CHAR = '%';
    public static final String CONTEXT = String.valueOf(CHAR);

    @Override
    public int getMatchingLength(CharSequence cs, int index) {
        if (index < 0 || index >= cs.length()) {
            return -1;
        }

        //  E (at the word end)
        //  ED, ER, ES, ELY, ING, EFUL (at the current index)

        int remaining = cs.length() - index;
        char c1 = cs.charAt(index);

        if (c1 == 'E' || c1 == 'e') {
            // 'E' - end of text
            if (remaining < 2) {
                return 1;
            }

            char c2 = cs.charAt(index + 1);

            // 'E' - end of word
            if (!Characters.isLetter(c2)) {
                return 1;
            }

            // 'ED'
            if (c2 == 'D' || c2 == 'd') {
                return 2;
            }

            // 'ER'
            if (c2 == 'R' || c2 == 'r') {
                return 2;
            }

            // 'ES'
            if (c2 == 'S' || c2 == 's') {
                return 2;
            }

            if (remaining < 3) {
                return -1;
            }

            // 'ELY'
            if (c2 == 'L' || c2 == 'l') {
                char c3 = cs.charAt(index + 2);

                if (c3 == 'Y' || c3 == 'y') {
                    return 3;
                }

                return -1;
            }

            if (remaining < 4) {
                return -1;
            }

            // 'EFUL'
            if (c2 == 'F' || c2 == 'f') {
                char c3 = cs.charAt(index + 2);
                char c4 = cs.charAt(index + 3);

                if ((c3 == 'U' || c3 == 'u') && (c4 == 'L' || c4 == 'l')) {
                    return 4;
                }

                return -1;
            }
        }

        if (c1 == 'I' || c1 == 'i') {
            if (remaining < 3) {
                return -1;
            }

            char c2 = cs.charAt(index + 1);
            char c3 = cs.charAt(index + 2);

            // 'ING'
            if ((c2 == 'N' || c2 == 'n') && (c3 == 'G' || c3 == 'g')) {
                return 3;
            }

            return -1;
        }

        return -1;
    }

    @Override
    public String getExpression() {
        return CONTEXT;
    }
}
