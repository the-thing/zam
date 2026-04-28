package com.github.thething.zam.renderer;

import static com.github.thething.zam.common.Requirements.requireInRange;

/**
 * Immutable record representing one frame of audio synthesis data.
 *
 * <p>Each frame carries three formant frequency values, three corresponding amplitude values,
 * a sample-flags byte (used to select the appropriate waveform table for voiced/unvoiced phonemes), and a pitch value.
 * All values are in the range [0, 255].
 *
 * @param frequency1  first formant frequency (mouth resonance)
 * @param frequency2  second formant frequency (throat resonance)
 * @param frequency3  third formant frequency
 * @param amplitude1  amplitude of the first formant
 * @param amplitude2  amplitude of the second formant
 * @param amplitude3  amplitude of the third formant
 * @param sampleFlags waveform selector flags; non-zero values indicate sampled phoneme data
 * @param pitch       fundamental pitch value for this frame
 */
public record PhonemeFrame(int frequency1, int frequency2, int frequency3,
                           int amplitude1, int amplitude2, int amplitude3,
                           int sampleFlags, int pitch) {

    public PhonemeFrame(
            int frequency1, int frequency2, int frequency3,
            int amplitude1, int amplitude2, int amplitude3,
            int sampleFlags, int pitch) {
        this.frequency1 = requireInRange(frequency1, 0, 255);
        this.frequency2 = requireInRange(frequency2, 0, 255);
        this.frequency3 = requireInRange(frequency3, 0, 255);
        this.amplitude1 = requireInRange(amplitude1, 0, 255);
        this.amplitude2 = requireInRange(amplitude2, 0, 255);
        this.amplitude3 = requireInRange(amplitude3, 0, 255);
        this.sampleFlags = sampleFlags;
        this.pitch = requireInRange(pitch, 0, 255);
    }
}
