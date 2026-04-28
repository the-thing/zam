package com.github.thething.zam.reciter;

/**
 * Utility class providing character classification methods used throughout the reciter pipeline.
 */
public final class Characters {

    private Characters() {
    }

    /**
     * Returns {@code true} if {@code c} is a vowel (A, E, I, O, U, Y – both cases).
     *
     * @param c the character to test
     * @return {@code true} if {@code c} is a vowel
     */
    public static boolean isVowel(char c) {
        return switch (c) {
            case 'A', 'a', 'E', 'e', 'I', 'i', 'O', 'o', 'U', 'u', 'Y', 'y' -> true;
            default -> false;
        };
    }

    /**
     * Returns {@code true} if {@code c} is an ASCII letter (a–z or A–Z).
     *
     * @param c the character to test
     * @return {@code true} if {@code c} is a letter
     */
    public static boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    /**
     * Returns {@code true} if {@code c} is an upper-case ASCII letter (A–Z).
     *
     * @param c the character to test
     * @return {@code true} if {@code c} is an upper-case letter
     */
    public static boolean isUpperCaseLetter(char c) {
        return c >= 'A' && c <= 'Z';
    }

    /**
     * Returns {@code true} if {@code c} is an ASCII digit (0–9).
     *
     * @param c the character to test
     * @return {@code true} if {@code c} is a digit
     */
    public static boolean isDigit(char c) {
        return (c >= '0' && c <= '9');
    }

    /**
     * Returns {@code true} if {@code c} is a whitespace character (space, tab, newline, or carriage return).
     *
     * @param c the character to test
     * @return {@code true} if {@code c} is whitespace
     */
    public static boolean isWhitespace(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }

    /**
     * Returns {@code true} if {@code c} is a consonant (a letter that is not a vowel).
     *
     * @param c the character to test
     * @return {@code true} if {@code c} is a consonant
     */
    public static boolean isConsonant(char c) {
        return isLetter(c) && !isVowel(c);
    }

    /**
     * Returns {@code true} if {@code c} is a voiced consonant (B, D, G, J, L, M, N, R, V, W, Z – both cases).
     *
     * @param c the character to test
     * @return {@code true} if {@code c} is a voiced consonant
     */
    public static boolean isVoicedConsonant(char c) {
        return switch (c) {
            case 'B', 'b', 'D', 'd', 'G', 'g', 'J', 'j', 'L', 'l', 'M', 'm', 'N', 'n', 'R', 'r', 'V', 'v', 'W', 'w',
                 'Z', 'z' -> true;
            default -> false;
        };
    }

    /**
     * Returns {@code true} if {@code c} is an alternative voiced consonant (T, S, R, D, L, Z, N, J – both cases),
     * corresponding to the {@code @} context symbol.
     *
     * @param c the character to test
     * @return {@code true} if {@code c} is an alternative voiced consonant
     */
    public static boolean isAltVoicedConsonant(char c) {
        return switch (c) {
            case 'T', 't', 'S', 's', 'R', 'r', 'D', 'd', 'L', 'l', 'Z', 'z', 'N', 'n', 'J', 'j' -> true;
            default -> false;
        };
    }

    /**
     * Returns {@code true} if {@code c} is a single-letter sibilant (C, G, J, S, X, Z – both cases).
     *
     * @param c the character to test
     * @return {@code true} if {@code c} is a single-letter sibilant
     */
    public static boolean isSingleLetterSibilant(char c) {
        return switch (c) {
            case 'C', 'c', 'G', 'g', 'J', 'j', 'S', 's', 'X', 'x', 'Z', 'z' -> true;
            default -> false;
        };
    }

    /**
     * Returns {@code true} if {@code c} is a front vowel (E, I, Y – both cases), corresponding to the {@code +} context
     * symbol.
     *
     * @param c the character to test
     * @return {@code true} if {@code c} is a front vowel
     */
    public static boolean isFrontVowel(char c) {
        return switch (c) {
            case 'E', 'e', 'I', 'i', 'Y', 'y' -> true;
            default -> false;
        };
    }

    /**
     * Converts {@code c} to upper-case if it is a lower-case ASCII letter; otherwise returns {@code c} unchanged.
     *
     * @param c the character to convert
     * @return the upper-case equivalent, or {@code c} if it is not a lower-case letter
     */
    public static char toUpperCase(char c) {
        if (c >= 'a' && c <= 'z') {
            return (char) (c - ('a' - 'A'));
        } else {
            return c;
        }
    }

    /**
     * Converts {@code c} to lower-case if it is an upper-case ASCII letter; otherwise returns {@code c} unchanged.
     *
     * @param c the character to convert
     * @return the lower-case equivalent, or {@code c} if it is not an upper-case letter
     */
    public static char toLowerCase(char c) {
        if (c >= 'A' && c <= 'Z') {
            return (char) (c - ('A' - 'a'));
        } else {
            return c;
        }
    }

    /**
     * Returns {@code true} if {@code c} is valid in a rule context expression (letters, single-quote, space, {@code #},
     * {@code .}, {@code &}, {@code @}, {@code :}, {@code ^}, {@code +}, {@code %}).
     *
     * @param c the character to test
     * @return {@code true} if {@code c} is a valid context character
     */
    public static boolean isContextChar(char c) {
        if (isLetter(c)) {
            return true;
        }

        // single quote or space
        if (c == '\'' || c == ' ') {
            return true;
        }

        return c == '#' || c == '.' || c == '&' || c == '@' || c == ':' || c == '^' || c == '+' || c == '%';
    }

    /**
     * Returns {@code true} if {@code c} is valid in a phoneme output string (letters, digits, {@code /}, {@code .},
     * {@code -}, space, {@code ?}, {@code ,}).
     *
     * @param c the character to test
     * @return {@code true} if {@code c} is a valid phoneme character
     */
    public static boolean isPhonemeChar(char c) {
        return isLetter(c) || isDigit(c) || c == '/' || c == '.' || c == '-' || c == ' ' || c == '?' || c == ',';
    }

    /**
     * Returns {@code true} if {@code c} is valid in a rule pattern (any printable ASCII character, space – tilde).
     *
     * @param c the character to test
     * @return {@code true} if {@code c} is a valid pattern character
     */
    public static boolean isPatternChar(char c) {
        return c >= ' ' && c <= '~';
    }
}
