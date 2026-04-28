package com.github.thething.zam.enricher;

import static com.github.thething.zam.common.Requirements.requireInRange;
import static java.util.Objects.requireNonNull;

public record Phoneme(int index, String pattern,
                      int flags, int sampleFlags,
                      int unstressedLength, int stressedLength,
                      int frequency1, int frequency2, int frequency3,
                      int amplitude1, int amplitude2, int amplitude3) {

    public static final int VOWEL_FLAG = 1;
    public static final int DIPHTHONG_FLAG = 2;
    public static final int ALVEOLAR_FLAG = 4;
    public static final int DIPHTHONG_YX_FLAG = 8;
    public static final int VOICED_FLAG = 16;
    public static final int CONSONANT_FLAG = 32;
    public static final int PUNCTUATION_FLAG = 64;
    public static final int FRICATIVE_FLAG = 128;
    public static final int UNVOICED_STOP_CONSONANT_FLAG = 256;
    public static final int NASAL_FLAG = 512;
    public static final int STOP_CONSONANT_FLAG = 1024;
    public static final int LIQUID_FLAG = 2048;
    public static final int SONORANT_FLAG = 4096;
    public static final int PAUSE_FLAG = 8192;

    public Phoneme(int index, String pattern) {
        this(index, pattern, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    public Phoneme(
            int index, String pattern,
            int flags, int sampleFlags,
            int unstressedLength, int stressedLength,
            int frequency1, int frequency2, int frequency3,
            int amplitude1, int amplitude2, int amplitude3) {

        if (index < 0) {
            throw new IllegalArgumentException("index < 0: " + index);
        }

        this.index = index;
        this.pattern = requireNonNull(pattern);

        if (pattern.length() != 2) {
            throw new IllegalArgumentException("Pattern length != 2: '" + pattern + "'");
        }

        this.flags = flags;
        this.sampleFlags = requireInRange(sampleFlags, 0, 255);
        this.unstressedLength = requireInRange(unstressedLength, 0, 255);
        this.stressedLength = requireInRange(stressedLength, 0, 255);
        this.frequency1 = requireInRange(frequency1, 0, 255);
        this.frequency2 = requireInRange(frequency2, 0, 255);
        this.frequency3 = requireInRange(frequency3, 0, 255);
        this.amplitude1 = requireInRange(amplitude1, 0, 255);
        this.amplitude2 = requireInRange(amplitude2, 0, 255);
        this.amplitude3 = requireInRange(amplitude3, 0, 255);
    }

    private boolean isFlagSet(int flag) {
        return (flags & flag) == flag;
    }

    public boolean isQuestionMark() {
        return pattern.charAt(0) == '?';
    }

    public boolean isPeriod() {
        return pattern.charAt(0) == '.';
    }

    public boolean isVowel() {
        return isFlagSet(VOWEL_FLAG);
    }

    public boolean isDiphthong() {
        return isFlagSet(DIPHTHONG_FLAG);
    }

    public boolean isAlveolar() {
        return isFlagSet(ALVEOLAR_FLAG);
    }

    public boolean isDiphthongYX() {
        return isFlagSet(DIPHTHONG_YX_FLAG);
    }

    public boolean isVoiced() {
        return isFlagSet(VOICED_FLAG);
    }

    public boolean isConsonant() {
        return isFlagSet(CONSONANT_FLAG);
    }

    public boolean isPunctuation() {
        return isFlagSet(PUNCTUATION_FLAG);
    }

    public boolean isFricative() {
        return isFlagSet(FRICATIVE_FLAG);
    }

    public boolean isUnvoicedStopConsonant() {
        return isFlagSet(UNVOICED_STOP_CONSONANT_FLAG);
    }

    public boolean isNasal() {
        return isFlagSet(NASAL_FLAG);
    }

    public boolean isStopConsonant() {
        return isFlagSet(STOP_CONSONANT_FLAG);
    }

    public boolean isLiquid() {
        return isFlagSet(LIQUID_FLAG);
    }

    public boolean isVcSonorant() {
        return isFlagSet(SONORANT_FLAG);
    }

    public boolean isPause() {
        return isFlagSet(PAUSE_FLAG);
    }
}
