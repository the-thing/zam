package com.github.thething.zam.enricher;

import com.github.thething.zam.common.Requirements;

import static java.util.Objects.requireNonNull;

/**
 * Immutable record representing an enriched phoneme token: a {@link Phoneme} combined with a duration (in frames) and a
 * stress level.
 *
 * @param phoneme the phoneme
 * @param length  the duration of this token in audio frames (0 – 255)
 * @param stress  the stress level (0 – 8; 0 means unstressed)
 */
public record PhonemeToken(Phoneme phoneme, int length, int stress) {

    /**
     * Special token used to mark a breath pause between utterance segments.
     */
    public static final PhonemeToken BREATH = new PhonemeToken(new Phoneme(254, "??"), 0, 0);

    public PhonemeToken(Phoneme phoneme, int length, int stress) {
        this.phoneme = requireNonNull(phoneme);
        this.length = Requirements.requireInRange(length, 0, 255);
        this.stress = Requirements.requireInRange(stress, 0, 8);
    }
}
