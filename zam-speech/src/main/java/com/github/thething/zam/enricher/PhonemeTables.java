package com.github.thething.zam.enricher;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * Static look-up tables for all SAM phonemes and their acoustic parameters.
 *
 * <p>Phonemes are indexed by their two-character pattern (e.g. {@code "IY"}, {@code "R*"}).
 * Frequency values are further adjusted at render time by the mouth/throat parameters.
 */
public final class PhonemeTables {

    private static final String[] PATTERNS = new String[]{
            " *", ".*", "?*", ",*", "-*", "IY", "IH", "EH",
            "AE", "AA", "AH", "AO", "UH", "AX", "IX", "ER",
            "UX", "OH", "RX", "LX", "WX", "YX", "WH", "R*",
            "L*", "W*", "Y*", "M*", "N*", "NX", "DX", "Q*",
            "S*", "SH", "F*", "TH", "/H", "/X", "Z*", "ZH",
            "V*", "DH", "CH", "**", "J*", "**", "**", "**",
            "EY", "AY", "OY", "AW", "OW", "UW", "B*", "**",
            "**", "D*", "**", "**", "G*", "**", "**", "GX",
            "**", "**", "P*", "**", "**", "T*", "**", "**",
            "K*", "**", "**", "KX", "**", "**", "UL", "UM",
            "UN"
    };

    private static final int[] UNSTRESSED_LENGTH = new int[]{
            0x00, 0x12, 0x12, 0x12, 0x08, 0x08, 0x08, 0x08,
            0x08, 0x0B, 0x06, 0x0C, 0x0A, 0x05, 0x05, 0x0B,
            0x0A, 0x0A, 0x0A, 0x09, 0x08, 0x07, 0x09, 0x07,
            0x06, 0x08, 0x06, 0x07, 0x07, 0x07, 0x02, 0x05,
            0x02, 0x02, 0x02, 0x02, 0x02, 0x02, 0x06, 0x06,
            0x07, 0x06, 0x06, 0x02, 0x08, 0x03, 0x01, 0x1E,
            0x0D, 0x0C, 0x0C, 0x0C, 0x0E, 0x09, 0x06, 0x01,
            0x02, 0x05, 0x01, 0x01, 0x06, 0x01, 0x02, 0x06,
            0x01, 0x02, 0x08, 0x02, 0x02, 0x04, 0x02, 0x02,
            0x06, 0x01, 0x04, 0x06, 0x01, 0x04, 0xC7, 0xFF,
            0x00
    };

    private static final int[] STRESSED_LENGTH = new int[]{
            0x00, 0x12, 0x12, 0x12, 0x08, 0x0B, 0x09, 0x0B,
            0x0E, 0x0F, 0x0B, 0x10, 0x0C, 0x06, 0x06, 0x0E,
            0x0C, 0x0E, 0x0C, 0x0B, 0x08, 0x08, 0x0B, 0x0A,
            0x09, 0x08, 0x08, 0x08, 0x08, 0x08, 0x03, 0x05,
            0x02, 0x02, 0x02, 0x02, 0x02, 0x02, 0x06, 0x06,
            0x08, 0x06, 0x06, 0x02, 0x09, 0x04, 0x02, 0x01,
            0x0E, 0x0F, 0x0F, 0x0F, 0x0E, 0x0E, 0x08, 0x02,
            0x02, 0x07, 0x02, 0x01, 0x07, 0x02, 0x02, 0x07,
            0x02, 0x02, 0x08, 0x02, 0x02, 0x06, 0x02, 0x02,
            0x07, 0x02, 0x04, 0x07, 0x01, 0x04, 0x05, 0x05,
            0x00
    };

    private static final int[] FREQUENCY1 = new int[]{
            0x00, 0x13, 0x13, 0x13, 0x13, 0x0A, 0x0E, 0x12,
            0x18, 0x1A, 0x16, 0x14, 0x10, 0x14, 0x0E, 0x12,
            0x0E, 0x12, 0x12, 0x10, 0x0C, 0x0E, 0x0A, 0x12,
            0x0E, 0x0A, 0x08, 0x06, 0x06, 0x06, 0x06, 0x11,
            0x06, 0x06, 0x06, 0x06, 0x0E, 0x10, 0x09, 0x0A,
            0x08, 0x0A, 0x06, 0x06, 0x06, 0x05, 0x06, 0x00,
            0x12, 0x1A, 0x14, 0x1A, 0x12, 0x0C, 0x06, 0x06,
            0x06, 0x06, 0x06, 0x06, 0x06, 0x06, 0x06, 0x06,
            0x06, 0x06, 0x06, 0x06, 0x06, 0x06, 0x06, 0x06,
            0x06, 0x0A, 0x0A, 0x06, 0x06, 0x06, 0x2C, 0x13,
            0x00
    };

    private static final int[] FREQUENCY2 = new int[]{
            0x00, 0x43, 0x43, 0x43, 0x43, 0x54, 0x48, 0x42,
            0x3E, 0x28, 0x2C, 0x1E, 0x24, 0x2C, 0x48, 0x30,
            0x24, 0x1E, 0x32, 0x24, 0x1C, 0x44, 0x18, 0x32,
            0x1E, 0x18, 0x52, 0x2E, 0x36, 0x56, 0x36, 0x43,
            0x49, 0x4F, 0x1A, 0x42, 0x49, 0x25, 0x33, 0x42,
            0x28, 0x2F, 0x4F, 0x4F, 0x42, 0x4F, 0x6E, 0x00,
            0x48, 0x26, 0x1E, 0x2A, 0x1E, 0x22, 0x1A, 0x1A,
            0x1A, 0x42, 0x42, 0x42, 0x6E, 0x6E, 0x6E, 0x54,
            0x54, 0x54, 0x1A, 0x1A, 0x1A, 0x42, 0x42, 0x42,
            0x6D, 0x56, 0x6D, 0x54, 0x54, 0x54, 0x7F, 0x7F,
            0x00
    };

    private static final int[] FREQUENCY3 = new int[]{
            0x00, 0x5B, 0x5B, 0x5B, 0x5B, 0x6E, 0x5D, 0x5B,
            0x58, 0x59, 0x57, 0x58, 0x52, 0x59, 0x5D, 0x3E,
            0x52, 0x58, 0x3E, 0x6E, 0x50, 0x5D, 0x5A, 0x3C,
            0x6E, 0x5A, 0x6E, 0x51, 0x79, 0x65, 0x79, 0x5B,
            0x63, 0x6A, 0x51, 0x79, 0x5D, 0x52, 0x5D, 0x67,
            0x4C, 0x5D, 0x65, 0x65, 0x79, 0x65, 0x79, 0x00,
            0x5A, 0x58, 0x58, 0x58, 0x58, 0x52, 0x51, 0x51,
            0x51, 0x79, 0x79, 0x79, 0x70, 0x6E, 0x6E, 0x5E,
            0x5E, 0x5E, 0x51, 0x51, 0x51, 0x79, 0x79, 0x79,
            0x65, 0x65, 0x70, 0x5E, 0x5E, 0x5E, 0x08, 0x01,
            0x00
    };

    private static final int[] AMPLITUDE1 = new int[]{
            0x00, 0x00, 0x00, 0x00, 0x00, 0x0D, 0x0D, 0x0E,
            0x0F, 0x0F, 0x0F, 0x0F, 0x0F, 0x0C, 0x0D, 0x0C,
            0x0F, 0x0F, 0x0D, 0x0D, 0x0D, 0x0E, 0x0D, 0x0C,
            0x0D, 0x0D, 0x0D, 0x0C, 0x09, 0x09, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0B, 0x0B,
            0x0B, 0x0B, 0x00, 0x00, 0x01, 0x0B, 0x00, 0x02,
            0x0E, 0x0F, 0x0F, 0x0F, 0x0F, 0x0D, 0x02, 0x04,
            0x00, 0x02, 0x04, 0x00, 0x01, 0x04, 0x00, 0x01,
            0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x0C, 0x00, 0x00, 0x00, 0x00, 0x0F, 0x0F,
            0x00
    };

    private static final int[] AMPLITUDE2 = new int[]{
            0x00, 0x00, 0x00, 0x00, 0x00, 0x0A, 0x0B, 0x0D,
            0x0E, 0x0D, 0x0C, 0x0C, 0x0B, 0x09, 0x0B, 0x0B,
            0x0C, 0x0C, 0x0C, 0x08, 0x08, 0x0C, 0x08, 0x0A,
            0x08, 0x08, 0x0A, 0x03, 0x09, 0x06, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x05,
            0x03, 0x04, 0x00, 0x00, 0x00, 0x05, 0x0A, 0x02,
            0x0E, 0x0D, 0x0C, 0x0D, 0x0C, 0x08, 0x00, 0x01,
            0x00, 0x00, 0x01, 0x00, 0x00, 0x01, 0x00, 0x00,
            0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x0A, 0x00, 0x00, 0x0A, 0x00, 0x00, 0x00,
            0x00
    };

    private static final int[] AMPLITUDE3 = new int[]{
            0x00, 0x00, 0x00, 0x00, 0x00, 0x08, 0x07, 0x08,
            0x08, 0x01, 0x01, 0x00, 0x01, 0x00, 0x07, 0x05,
            0x01, 0x00, 0x06, 0x01, 0x00, 0x07, 0x00, 0x05,
            0x01, 0x00, 0x08, 0x00, 0x00, 0x03, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x0E, 0x01,
            0x09, 0x01, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x07, 0x00, 0x00, 0x05, 0x00, 0x13, 0x10,
            0x00
    };

    private static final int[] SAMPLED_FLAGS = new int[]{
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0xF1, 0xE2, 0xD3, 0xBB, 0x7C, 0x95, 0x01, 0x02,
            0x03, 0x03, 0x00, 0x72, 0x00, 0x02, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x1B, 0x00, 0x00, 0x19, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00
    };

    private static final int[] MOUTH_FORMANTS_5_29 = new int[]{
            0x0A, 0x0E, 0x13, 0x18, 0x1B, 0x17, 0x15, 0x10,
            0x14, 0x0E, 0x12, 0x0E, 0x12, 0x12, 0x10, 0x0D,
            0x0F, 0x0B, 0x12, 0x0E, 0x0B, 0x09, 0x06, 0x06,
            0x06
    };

    private static final int[] MOUTH_FORMANTS_48_53 = new int[]{
            0x13, 0x1B, 0x15, 0x1B, 0x12, 0x0D
    };

    private static final int[] THROAT_FORMANTS_5_29 = new int[]{
            0x54, 0x49, 0x43, 0x3F, 0x28, 0x2C, 0x1F, 0x25,
            0x2D, 0x49, 0x31, 0x24, 0x1E, 0x33, 0x25, 0x1D,
            0x45, 0x18, 0x32, 0x1E, 0x18, 0x53, 0x2E, 0x36,
            0x56
    };

    private static final int[] THROAT_FORMANTS_48_53 = new int[]{
            0x48, 0x27, 0x1F, 0x2B, 0x1E, 0x22
    };

    private static final int[] FLAGS = new int[]{
            Phoneme.PAUSE_FLAG,
            Phoneme.PUNCTUATION_FLAG,
            Phoneme.PUNCTUATION_FLAG,
            Phoneme.PUNCTUATION_FLAG,
            Phoneme.PUNCTUATION_FLAG,
            Phoneme.VOWEL_FLAG | Phoneme.DIPHTHONG_YX_FLAG | Phoneme.VOICED_FLAG,
            Phoneme.VOWEL_FLAG | Phoneme.DIPHTHONG_YX_FLAG | Phoneme.VOICED_FLAG,
            Phoneme.VOWEL_FLAG | Phoneme.DIPHTHONG_YX_FLAG | Phoneme.VOICED_FLAG,
            Phoneme.VOWEL_FLAG | Phoneme.DIPHTHONG_YX_FLAG | Phoneme.VOICED_FLAG,
            Phoneme.VOWEL_FLAG | Phoneme.DIPHTHONG_YX_FLAG | Phoneme.VOICED_FLAG,
            Phoneme.VOWEL_FLAG | Phoneme.DIPHTHONG_YX_FLAG | Phoneme.VOICED_FLAG,
            Phoneme.VOWEL_FLAG | Phoneme.VOICED_FLAG,
            Phoneme.VOWEL_FLAG | Phoneme.VOICED_FLAG,
            Phoneme.VOWEL_FLAG | Phoneme.DIPHTHONG_YX_FLAG | Phoneme.VOICED_FLAG,
            Phoneme.VOWEL_FLAG | Phoneme.DIPHTHONG_YX_FLAG | Phoneme.VOICED_FLAG,
            Phoneme.VOWEL_FLAG | Phoneme.VOICED_FLAG,
            Phoneme.VOWEL_FLAG | Phoneme.VOICED_FLAG,
            Phoneme.VOWEL_FLAG | Phoneme.VOICED_FLAG,
            Phoneme.VOWEL_FLAG | Phoneme.VOICED_FLAG,
            Phoneme.VOWEL_FLAG | Phoneme.VOICED_FLAG,
            Phoneme.VOWEL_FLAG | Phoneme.VOICED_FLAG,
            Phoneme.VOWEL_FLAG | Phoneme.VOICED_FLAG,
            Phoneme.VOICED_FLAG | Phoneme.CONSONANT_FLAG,
            Phoneme.VOICED_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.LIQUID_FLAG,
            Phoneme.VOICED_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.LIQUID_FLAG,
            Phoneme.VOICED_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.LIQUID_FLAG,
            Phoneme.VOICED_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.LIQUID_FLAG,
            Phoneme.VOICED_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.NASAL_FLAG | Phoneme.SONORANT_FLAG,
            Phoneme.ALVEOLAR_FLAG | Phoneme.VOICED_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.NASAL_FLAG | Phoneme.SONORANT_FLAG,
            Phoneme.VOICED_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.NASAL_FLAG | Phoneme.SONORANT_FLAG,
            Phoneme.ALVEOLAR_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.SONORANT_FLAG,
            Phoneme.VOICED_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.SONORANT_FLAG,
            Phoneme.ALVEOLAR_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.FRICATIVE_FLAG,
            Phoneme.CONSONANT_FLAG | Phoneme.FRICATIVE_FLAG,
            Phoneme.CONSONANT_FLAG | Phoneme.FRICATIVE_FLAG,
            Phoneme.ALVEOLAR_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.FRICATIVE_FLAG,
            Phoneme.CONSONANT_FLAG,
            Phoneme.CONSONANT_FLAG,
            Phoneme.ALVEOLAR_FLAG | Phoneme.VOICED_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.FRICATIVE_FLAG,
            Phoneme.VOICED_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.FRICATIVE_FLAG,
            Phoneme.VOICED_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.FRICATIVE_FLAG,
            Phoneme.ALVEOLAR_FLAG | Phoneme.VOICED_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.FRICATIVE_FLAG,
            Phoneme.CONSONANT_FLAG | Phoneme.FRICATIVE_FLAG | Phoneme.SONORANT_FLAG,
            Phoneme.CONSONANT_FLAG | Phoneme.FRICATIVE_FLAG,
            Phoneme.VOICED_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.SONORANT_FLAG,
            Phoneme.VOICED_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.FRICATIVE_FLAG,
            0,
            0,
            Phoneme.VOWEL_FLAG | Phoneme.DIPHTHONG_FLAG | Phoneme.DIPHTHONG_YX_FLAG | Phoneme.VOICED_FLAG,
            Phoneme.VOWEL_FLAG | Phoneme.DIPHTHONG_FLAG | Phoneme.DIPHTHONG_YX_FLAG | Phoneme.VOICED_FLAG,
            Phoneme.VOWEL_FLAG | Phoneme.DIPHTHONG_FLAG | Phoneme.DIPHTHONG_YX_FLAG | Phoneme.VOICED_FLAG,
            Phoneme.VOWEL_FLAG | Phoneme.DIPHTHONG_FLAG | Phoneme.VOICED_FLAG,
            Phoneme.VOWEL_FLAG | Phoneme.DIPHTHONG_FLAG | Phoneme.VOICED_FLAG,
            Phoneme.VOWEL_FLAG | Phoneme.DIPHTHONG_FLAG | Phoneme.VOICED_FLAG,
            Phoneme.VOICED_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.STOP_CONSONANT_FLAG | Phoneme.SONORANT_FLAG,
            Phoneme.VOICED_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.STOP_CONSONANT_FLAG | Phoneme.SONORANT_FLAG,
            Phoneme.VOICED_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.STOP_CONSONANT_FLAG | Phoneme.SONORANT_FLAG,
            Phoneme.ALVEOLAR_FLAG | Phoneme.VOICED_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.STOP_CONSONANT_FLAG | Phoneme.SONORANT_FLAG,
            Phoneme.ALVEOLAR_FLAG | Phoneme.VOICED_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.STOP_CONSONANT_FLAG | Phoneme.SONORANT_FLAG,
            Phoneme.ALVEOLAR_FLAG | Phoneme.VOICED_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.STOP_CONSONANT_FLAG | Phoneme.SONORANT_FLAG,
            Phoneme.VOICED_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.STOP_CONSONANT_FLAG | Phoneme.SONORANT_FLAG,
            Phoneme.VOICED_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.STOP_CONSONANT_FLAG | Phoneme.SONORANT_FLAG,
            Phoneme.VOICED_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.STOP_CONSONANT_FLAG | Phoneme.SONORANT_FLAG,
            Phoneme.VOICED_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.STOP_CONSONANT_FLAG | Phoneme.SONORANT_FLAG,
            Phoneme.VOICED_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.STOP_CONSONANT_FLAG | Phoneme.SONORANT_FLAG,
            Phoneme.VOICED_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.STOP_CONSONANT_FLAG | Phoneme.SONORANT_FLAG,
            Phoneme.CONSONANT_FLAG | Phoneme.UNVOICED_STOP_CONSONANT_FLAG | Phoneme.STOP_CONSONANT_FLAG | Phoneme.SONORANT_FLAG,
            Phoneme.CONSONANT_FLAG | Phoneme.UNVOICED_STOP_CONSONANT_FLAG | Phoneme.STOP_CONSONANT_FLAG | Phoneme.SONORANT_FLAG,
            Phoneme.CONSONANT_FLAG | Phoneme.UNVOICED_STOP_CONSONANT_FLAG | Phoneme.STOP_CONSONANT_FLAG | Phoneme.SONORANT_FLAG,
            Phoneme.ALVEOLAR_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.UNVOICED_STOP_CONSONANT_FLAG | Phoneme.STOP_CONSONANT_FLAG | Phoneme.SONORANT_FLAG,
            Phoneme.ALVEOLAR_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.UNVOICED_STOP_CONSONANT_FLAG | Phoneme.STOP_CONSONANT_FLAG | Phoneme.SONORANT_FLAG,
            Phoneme.ALVEOLAR_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.UNVOICED_STOP_CONSONANT_FLAG | Phoneme.STOP_CONSONANT_FLAG | Phoneme.SONORANT_FLAG,
            Phoneme.CONSONANT_FLAG | Phoneme.UNVOICED_STOP_CONSONANT_FLAG | Phoneme.STOP_CONSONANT_FLAG | Phoneme.SONORANT_FLAG,
            Phoneme.CONSONANT_FLAG | Phoneme.UNVOICED_STOP_CONSONANT_FLAG | Phoneme.STOP_CONSONANT_FLAG | Phoneme.SONORANT_FLAG,
            Phoneme.CONSONANT_FLAG | Phoneme.UNVOICED_STOP_CONSONANT_FLAG | Phoneme.STOP_CONSONANT_FLAG | Phoneme.SONORANT_FLAG,
            Phoneme.CONSONANT_FLAG | Phoneme.UNVOICED_STOP_CONSONANT_FLAG | Phoneme.STOP_CONSONANT_FLAG | Phoneme.SONORANT_FLAG,
            Phoneme.CONSONANT_FLAG | Phoneme.UNVOICED_STOP_CONSONANT_FLAG | Phoneme.STOP_CONSONANT_FLAG | Phoneme.SONORANT_FLAG,
            Phoneme.CONSONANT_FLAG | Phoneme.UNVOICED_STOP_CONSONANT_FLAG | Phoneme.STOP_CONSONANT_FLAG | Phoneme.SONORANT_FLAG,
            Phoneme.VOWEL_FLAG,
            Phoneme.VOWEL_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.UNVOICED_STOP_CONSONANT_FLAG,
            Phoneme.VOWEL_FLAG | Phoneme.CONSONANT_FLAG | Phoneme.UNVOICED_STOP_CONSONANT_FLAG
    };

    private static final char WILDCARD = '*';
    private static final int INDEX_SHIFT = 7;
    private static final Phoneme[] PHONEMES;
    private static final int[] INDEX_BY_PHONEME;
    private static final int[][] FREQUENCIES_BY_MOUTH;
    private static final int[][] FREQUENCIES_BY_THROAT;

    public static final int UNVOICED_SAMPLED_PHONEME_FLAG = 0xF8;

    static {
        PHONEMES = buildPhonemes();
        INDEX_BY_PHONEME = buildPhonemeIndex();
        FREQUENCIES_BY_MOUTH = buildMouthFrequencies();
        FREQUENCIES_BY_THROAT = buildThroatFrequencies();
    }

    private static Phoneme[] buildPhonemes() {
        Phoneme[] phonemes = new Phoneme[81];

        for (int i = 0; i < phonemes.length; i++) {
            phonemes[i] = new Phoneme(i, PATTERNS[i],
                    FLAGS[i], SAMPLED_FLAGS[i],
                    UNSTRESSED_LENGTH[i], STRESSED_LENGTH[i],
                    FREQUENCY1[i], FREQUENCY2[i], FREQUENCY3[i],
                    AMPLITUDE1[i], AMPLITUDE2[i], AMPLITUDE3[i]);
        }

        return phonemes;
    }

    private static int[] buildPhonemeIndex() {
        int[] indexByPhoneme = new int[128 * 128];
        Arrays.fill(indexByPhoneme, -1);

        for (int i = 0; i < PHONEMES.length; i++) {
            String pattern = PHONEMES[i].pattern();
            int index = getIndex(pattern.charAt(0), pattern.charAt(1));
            indexByPhoneme[index] = i;
        }

        return indexByPhoneme;
    }

    // precompute all FREQUENCY1 for mouth parameter
    private static int[][] buildMouthFrequencies() {
        int[][] frequenciesByMouth = new int[256][];

        for (int mouth = 0; mouth < frequenciesByMouth.length; mouth++) {
            frequenciesByMouth[mouth] = Arrays.copyOf(FREQUENCY1, FREQUENCY1.length);

            // only selected phonemes [5, 29]
            for (int phonemeIndex = 5; phonemeIndex <= 29; phonemeIndex++) {
                int frequency = MOUTH_FORMANTS_5_29[phonemeIndex - 5];
                int newFrequency = translate(mouth, frequency);
                frequenciesByMouth[mouth][phonemeIndex] = newFrequency;
            }

            // only selected phonemes [48, 53]
            for (int phonemeIndex = 48; phonemeIndex <= 53; phonemeIndex++) {
                int frequency = MOUTH_FORMANTS_48_53[phonemeIndex - 48];
                int newFrequency = translate(mouth, frequency);
                frequenciesByMouth[mouth][phonemeIndex] = newFrequency;
            }
        }

        return frequenciesByMouth;
    }

    // precompute all FREQUENCY1 for THROAT parameter
    private static int[][] buildThroatFrequencies() {
        int[][] frequenciesByThroat = new int[256][];

        for (int throat = 0; throat < frequenciesByThroat.length; throat++) {
            frequenciesByThroat[throat] = Arrays.copyOf(FREQUENCY2, FREQUENCY2.length);

            // only selected phonemes [5, 29]
            for (int phonemeIndex = 5; phonemeIndex <= 29; phonemeIndex++) {
                int frequency = THROAT_FORMANTS_5_29[phonemeIndex - 5];
                int newFrequency = translate(throat, frequency);
                frequenciesByThroat[throat][phonemeIndex] = newFrequency;
            }

            // only selected phonemes [48, 53]
            for (int phonemeIndex = 48; phonemeIndex <= 53; phonemeIndex++) {
                int frequency = THROAT_FORMANTS_48_53[phonemeIndex - 48];
                int newFrequency = translate(throat, frequency);
                frequenciesByThroat[throat][phonemeIndex] = newFrequency;
            }
        }

        return frequenciesByThroat;
    }

    private PhonemeTables() {
    }

    private static int getIndex(char c1, char c2) {
        return (c1 << INDEX_SHIFT) + c2;
    }

    private static int translate(int multiplier, int value) {
        boolean carry;
        int tmp;

        int result = 0;
        int bitCount = 8;

        do {
            carry = (multiplier & 1) != 0;
            multiplier = multiplier >> 1;

            if (carry) {
                carry = false;
                tmp = result + value;
                result = result + value;

                if (tmp > 255) {
                    carry = true;
                }
            }

            result = (result >> 1) | (carry ? 128 : 0);
            bitCount--;
        } while (bitCount != 0);

        result = result << 1;

        return result;
    }

    /**
     * Returns the {@link Phoneme} for the given single character using a wildcard second character.
     *
     * @param c1 the first (and only meaningful) pattern character
     * @return the matching {@link Phoneme}
     * @throws java.util.NoSuchElementException if no phoneme is registered for {@code c1}
     */
    public static Phoneme getPhoneme(char c1) {
        return getPhoneme(c1, WILDCARD);
    }

    /**
     * Returns the {@link Phoneme} for the given two-character pattern.
     *
     * @param c1 the first pattern character
     * @param c2 the second pattern character
     * @return the matching {@link Phoneme}
     * @throws java.util.NoSuchElementException if no phoneme is registered for the pattern {@code c1c2}
     */
    public static Phoneme getPhoneme(char c1, char c2) {
        int index = getIndex(c1, c2);

        if (index < 0 || index >= INDEX_BY_PHONEME.length) {
            throw new NoSuchElementException("No phoneme: " + c1 + c2);
        }

        index = INDEX_BY_PHONEME[index];

        if (index == -1) {
            throw new NoSuchElementException("No phoneme: " + c1 + c2);
        }

        return PHONEMES[index];
    }

    /**
     * Returns the {@link Phoneme} at the given table index.
     *
     * @param index the phoneme table index
     * @return the {@link Phoneme} at {@code index}
     * @throws java.util.NoSuchElementException if {@code index} is out of range
     */
    public static Phoneme getPhoneme(int index) {
        if (index < 0 || index >= PHONEMES.length) {
            throw new NoSuchElementException();
        }

        return PHONEMES[index];
    }

    /**
     * Returns {@code true} if a phoneme is defined for the given single character (plus wildcard).
     *
     * @param c1 the character to check
     * @return {@code true} if the phoneme is defined
     */
    public static boolean isDefined(char c1) {
        return isDefined(c1, WILDCARD);
    }

    /**
     * Returns {@code true} if a phoneme is defined for the given two-character pattern.
     *
     * @param c1 the first pattern character
     * @param c2 the second pattern character
     * @return {@code true} if the phoneme is defined
     */
    public static boolean isDefined(char c1, char c2) {
        int index = getIndex(c1, c2);

        if (index < 0 || index >= INDEX_BY_PHONEME.length) {
            return false;
        }

        return INDEX_BY_PHONEME[index] != -1;
    }

    /**
     * Returns the mouth-adjusted first formant frequency for the given phoneme and mouth parameter.
     *
     * @param phonemeIndex the phoneme index
     * @param mouth        the mouth parameter (0 – 255)
     * @return the adjusted frequency value
     * @throws IllegalArgumentException if {@code phonemeIndex} or {@code mouth} is out of range
     */
    public static int getMouthFrequency(int phonemeIndex, int mouth) {
        if (phonemeIndex < 0 || phonemeIndex >= PHONEMES.length) {
            throw new IllegalArgumentException("Invalid phonemeIndex: " + phonemeIndex);
        }

        if (mouth < 0 || mouth > 255) {
            throw new IllegalArgumentException("Invalid mouth: " + mouth);
        }

        return FREQUENCIES_BY_MOUTH[mouth][phonemeIndex];
    }

    /**
     * Returns the throat-adjusted second formant frequency for the given phoneme and throat parameter.
     *
     * @param phonemeIndex the phoneme index
     * @param throat       the throat parameter (0 – 255)
     * @return the adjusted frequency value
     * @throws java.util.NoSuchElementException if {@code phonemeIndex} is out of range
     * @throws IllegalArgumentException         if {@code throat} is out of range
     */
    public static int getThroatFrequency(int phonemeIndex, int throat) {
        if (phonemeIndex < 0 || phonemeIndex >= PHONEMES.length) {
            throw new NoSuchElementException("No phoneme: " + phonemeIndex);
        }

        if (throat < 0 || throat > 255) {
            throw new IllegalArgumentException("Invalid throat: " + throat);
        }

        return FREQUENCIES_BY_THROAT[throat][phonemeIndex];
    }
}
