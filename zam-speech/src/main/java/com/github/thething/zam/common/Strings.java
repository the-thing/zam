package com.github.thething.zam.common;

import com.github.thething.zam.reciter.Characters;

import static java.util.Objects.checkFromToIndex;
import static java.util.Objects.checkIndex;

/**
 * General-purpose string utility methods.
 */
public final class Strings {

    private Strings() {
    }

    /**
     * Returns a copy of {@code text} with all occurrences of {@code c} removed, unless the character is preceded by a
     * backslash escape ({@code \}). Escape characters themselves are also removed from the result.
     *
     * @param text the input string
     * @param c    the character to strip
     * @return a new string with the character removed
     */
    public static String strip(String text, char c) {
        StringBuilder builder = new StringBuilder(text);
        strip(builder, c);
        return builder.toString();
    }

    /**
     * Strips all occurrences of {@code c} from {@code text} in-place. Characters escaped with a preceding {@code \} are
     * kept (the backslash is removed).
     *
     * @param text the builder to modify in-place
     * @param c    the character to strip
     */
    public static void strip(StringBuilder text, char c) {
        int insertIndex = 0;
        char previous = '\0';

        for (int i = 0; i < text.length(); i++) {
            char current = text.charAt(i);

            if (current == c) {
                if (previous != '\\') {
                    // character is not escaped, remove
                    previous = current;
                    continue;
                } else {
                    // character is escaped, do not remove, but we have to remove the escape character
                    insertIndex--;
                }
            }

            previous = current;
            text.setCharAt(insertIndex, current);
            insertIndex++;
        }

        text.setLength(insertIndex);
    }

    /**
     * Returns the index of the first occurrence of {@code c} in {@code text}, or {@code -1}.
     *
     * @param text the character sequence to search
     * @param c    the character to find
     * @return the zero-based index of the first match, or {@code -1} if not found
     */
    public static int indexOf(CharSequence text, char c) {
        return indexOf(text, c, 0);
    }

    /**
     * Returns the index of the first occurrence of {@code c} in {@code text} at or after {@code fromIndex}, or
     * {@code -1}.
     *
     * @param text      the character sequence to search
     * @param c         the character to find
     * @param fromIndex the index to start searching from (inclusive)
     * @return the zero-based index of the first match, or {@code -1} if not found
     * @throws IndexOutOfBoundsException if {@code fromIndex} is out of bounds
     */
    public static int indexOf(CharSequence text, char c, int fromIndex) {
        checkIndex(fromIndex, text.length());

        for (int i = fromIndex; i < text.length(); i++) {
            if (text.charAt(i) == c) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Returns the index of the last occurrence of {@code c} in {@code text}, or {@code -1}.
     *
     * @param text the character sequence to search
     * @param c    the character to find
     * @return the zero-based index of the last match, or {@code -1} if not found
     */
    public static int lastIndexOf(CharSequence text, char c) {
        for (int i = text.length() - 1; i >= 0; i--) {
            if (text.charAt(i) == c) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Returns {@code true} if the substring of {@code text1} from {@code fromIndex1} to {@code toIndex1} equals the
     * substring of {@code text2} from {@code fromIndex2} to {@code toIndex2}, character by character.
     *
     * @param text1      first character sequence
     * @param fromIndex1 start of the first substring (inclusive)
     * @param toIndex1   end of the first substring (exclusive)
     * @param text2      second character sequence
     * @param fromIndex2 start of the second substring (inclusive)
     * @param toIndex2   end of the second substring (exclusive)
     * @return {@code true} if the two substrings are equal
     * @throws IndexOutOfBoundsException if any index is out of bounds for its sequence
     */
    public static boolean contentEquals(
            CharSequence text1,
            int fromIndex1,
            int toIndex1,
            CharSequence text2,
            int fromIndex2,
            int toIndex2) {
        checkFromToIndex(fromIndex1, toIndex1, text1.length());
        checkFromToIndex(fromIndex2, toIndex2, text2.length());

        int length1 = toIndex1 - fromIndex1;
        int length2 = toIndex2 - fromIndex2;

        if (length1 != length2) {
            return false;
        }

        for (int i = 0; i < length1; i++) {
            if (text1.charAt(fromIndex1 + i) != text2.charAt(fromIndex2 + i)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Removes trailing whitespace characters from {@code text} in-place.
     *
     * @param text the builder to trim
     */
    public static void trimTail(StringBuilder text) {
        int index = text.length() - 1;

        while (index >= 0) {
            char c = text.charAt(index);

            if (!Characters.isWhitespace(c)) {
                break;
            }

            index--;
        }

        text.setLength(index + 1);
    }

    /**
     * Returns a string produced by left-padding {@code value} to at least {@code length} characters using
     * {@code paddingCharacter}.  If {@code value} is already at least {@code length} characters long it is returned
     * as-is.
     *
     * @param value            the string to pad
     * @param length           the minimum desired length
     * @param paddingCharacter the character to use for padding
     * @return the padded string
     */
    public static String padLeft(CharSequence value, int length, char paddingCharacter) {
        StringBuilder out = new StringBuilder(Math.max(length, value.length()));
        padLeft(out, value, length, paddingCharacter);
        return out.toString();
    }

    /**
     * Appends {@code value} left-padded to at least {@code length} characters to {@code out}.
     *
     * @param out              the builder to append to
     * @param value            the string to pad
     * @param length           the minimum desired length
     * @param paddingCharacter the character to use for padding
     */
    public static void padLeft(StringBuilder out, CharSequence value, int length, char paddingCharacter) {
        int diff = length - value.length();

        if (diff < 0) {
            out.append(value);
            return;
        }

        for (int i = 0; i < diff; i++) {
            out.append(paddingCharacter);
        }

        out.append(value);
    }
}
