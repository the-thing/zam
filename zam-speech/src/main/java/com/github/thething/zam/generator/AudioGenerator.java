package com.github.thething.zam.generator;

import com.github.thething.zam.enricher.PhonemeTables;
import com.github.thething.zam.renderer.PhonemeFrame;

import java.util.Arrays;

import static com.github.thething.zam.common.Requirements.requireInRange;

/**
 * Generates raw PCM audio samples from an array of {@link PhonemeFrame} chunks.
 *
 * <p>The generator simulates three formant oscillators (sine, sine, rectangle) and handles
 * both voiced and unvoiced phoneme samples.  The output is an unsigned 8-bit mono byte array at 22 050 Hz (see
 * {@link com.github.thething.zam.synthesizer.SpeechSynthesizer#AUDIO_FORMAT}).
 */
public final class AudioGenerator {

    /**
     * Default speaking speed used when no explicit value is supplied.
     */
    public static final int DEFAULT_SPEED = 72;

    /**
     * Generates audio from the given frame chunks using the {@link #DEFAULT_SPEED default speed}.
     *
     * @param frameChunks the phoneme frame chunks to synthesize; each inner array is one breath group
     * @return raw PCM audio bytes
     */
    public byte[] generateAudio(PhonemeFrame[][] frameChunks) {
        return generateAudio(frameChunks, DEFAULT_SPEED);
    }

    /**
     * Generates audio from the given frame chunks at the requested speed.
     *
     * @param frameChunks the phoneme frame chunks to synthesize; each inner array is one breath group
     * @param speed       speaking speed; must be in [1, 255]
     * @return raw PCM audio bytes
     * @throws IllegalArgumentException if {@code speed} is out of range
     */
    public byte[] generateAudio(PhonemeFrame[][] frameChunks, int speed) {
        requireInRange(speed, 1, 255);

        SoundBuffer buffer = new SoundBuffer();

        for (PhonemeFrame[] frames : frameChunks) {
            generateAudio(frames, speed, buffer);
        }

        return buffer.toByteArray();
    }

    private void generateAudio(PhonemeFrame[] frames, int speed, SoundBuffer buffer) {
        int frameIndex = 0;
        int phase1 = 0;
        int phase2 = 0;
        int phase3 = 0;
        int sampleIndex = 0;

        int remainingSpeed = speed;

        int glottalPulseRemaining = frames[0].pitch();
        int glottalPulseThreshold = glottalPulseRemaining - (glottalPulseRemaining >> 2);

        while (true) {
            PhonemeFrame frame = frames[frameIndex];
            int sampledFlags = frame.sampleFlags();
            boolean unvoiced = (sampledFlags & PhonemeTables.UNVOICED_SAMPLED_PHONEME_FLAG) != 0;

            if (unvoiced) {
                renderUnvoicedSample(sampledFlags, buffer);
                glottalPulseRemaining = 1;
                frameIndex += 2;
            } else {
                int p1 = Math.multiplyExact(phase1, 256);
                int p2 = Math.multiplyExact(phase2, 256);
                int p3 = Math.multiplyExact(phase3, 256);
                int[] values = new int[5];

                for (int i = 0; i < values.length; i++) {
                    int sp1 = GeneratorTables.getSine((p1 >> 8) & 0xFF);
                    int sp2 = GeneratorTables.getSine((p2 >> 8) & 0xFF);
                    int rp3 = GeneratorTables.getRectangle((p3 >> 8) & 0xFF);
                    int sin1 = sp1 * (frame.amplitude1() & 0x0F);
                    int sin2 = sp2 * (frame.amplitude2() & 0x0F);
                    int rect = rp3 * (frame.amplitude3() & 0x0F);
                    int mix = (sin1 + sin2 + rect) / 32 + 128;
                    values[i] = mix;
                    p1 += Math.multiplyExact(frame.frequency1(), 256) >> 2;
                    p2 += Math.multiplyExact(frame.frequency2(), 256) >> 2;
                    p3 += Math.multiplyExact(frame.frequency3(), 256) >> 2;
                }

                buffer.write(values);
                remainingSpeed--;

                if (remainingSpeed == 0) {
                    frameIndex++;
                    remainingSpeed = speed;
                }
            }

            if (frameIndex == frames.length) {
                break;
            }

            glottalPulseRemaining--;

            if (glottalPulseRemaining == 0) {
                glottalPulseRemaining = frames[frameIndex].pitch();
                glottalPulseThreshold = glottalPulseRemaining - (glottalPulseRemaining >> 2);

                // reset the formant wave generators to keep them in sync with the glottal pulse
                phase1 = 0;
                phase2 = 0;
                phase3 = 0;
                continue;
            }

            glottalPulseThreshold--;

            if (glottalPulseThreshold != 0 || sampledFlags == 0) {
                // advance the phase of the formants
                phase1 += frames[frameIndex].frequency1();
                phase2 += frames[frameIndex].frequency2();
                phase3 += frames[frameIndex].frequency3();
                continue;
            }

            // voiced sampled phonemes interleave the sample with the glottal pulse
            // the sample flag is non-zero, so render the sample for the phoneme.
            sampleIndex = renderVoicedSample(frames, frameIndex, sampleIndex, sampledFlags, buffer);

            // fetch the same glottal pulse length again
            glottalPulseRemaining = frames[frameIndex].pitch();
            glottalPulseThreshold = glottalPulseRemaining - (glottalPulseRemaining >> 2);

            // reset the formant wave generators to keep them in sync with the glottal pulse
            phase1 = 0;
            phase2 = 0;
            phase3 = 0;
        }
    }

    private int renderVoicedSample(PhonemeFrame[] frames, int frameIndex, int sampleIndex, int sampledFlags, SoundBuffer buffer) {
        int waveformIndex = (sampledFlags & 0x07) - 1;
        int glottalCycles = (frames[frameIndex].pitch() >> 4) ^ 0xFF;

        do {
            int bitCounter = 8;
            int sample = GeneratorTables.getSample(waveformIndex * 256 + sampleIndex);

            do {
                // Shift out the high bit
                int highBit = sample & 0x80;
                sample = (sample << 1) & 0xFF;

                if (highBit != 0) {
                    // bit is 1 -> output voiced "on" value (26)
                    buffer.write(3, (byte) ((26 & 0x0F) * 16));
                } else {
                    // bit is 0 -> output voiced "off" value (6)
                    buffer.write(4, (byte) ((6 & 0x0F) * 16));
                }

                bitCounter--;
            } while (bitCounter != 0);

            sampleIndex = (sampleIndex + 1) & 0xFF;
            glottalCycles = (glottalCycles + 1) & 0xFF;
        } while (glottalCycles != 0);

        return sampleIndex;
    }

    private void renderUnvoicedSample(int sampledFlags, SoundBuffer buffer) {
        int waveformIndex = (sampledFlags & 0x07) - 1;
        int offset = GeneratorTables.getWaveformOffset(waveformIndex);
        int sampleIndex = (sampledFlags & 0xF8) ^ 0xFF;

        while (sampleIndex != 0) {
            int bitCounter = 8;
            int sample = GeneratorTables.getSample(waveformIndex * 256 + sampleIndex);

            while (true) {
                int highBit = sample & 0x80;
                sample = (sample << 1) & 0xFF;

                if (highBit == 0) {
                    // bit is 0-> output the silence / conversion byte from the table
                    buffer.write(1, (byte) ((offset & 0x0F) * 16));

                    if (offset != 0) {
                        // non-zero silence byte -> skip the '5' output and continue
                        bitCounter--;

                        if (bitCounter != 0) {
                            continue;
                        } else {
                            break;
                        }
                    }
                }

                // bit is 1 (or silence byte was zero) -> output a '5' (loud click)
                buffer.write(2, (byte) (5 * 16));

                bitCounter--;

                if (bitCounter == 0) {
                    break;
                }
            }

            sampleIndex = (sampleIndex + 1) & 0xFF;
        }
    }

    private static final class SoundBuffer {

        private static final int DEFAULT_BUFFER_CAPACITY = 4096;
        private static final int CYCLES_PER_SAMPLE = 50;

        private byte[] buffer;
        private int position;
        private int previousCycleIndex;

        private SoundBuffer() {
            buffer = new byte[DEFAULT_BUFFER_CAPACITY];
            position = 0;
            previousCycleIndex = 0;
        }

        private void ensureCapacity(int requiredCapacity) {
            if (buffer.length < requiredCapacity) {
                buffer = Arrays.copyOf(buffer, buffer.length << 1);
            }
        }

        private void write(int[] values) {
            if (values.length != 5) {
                throw new IllegalArgumentException("values.length != 5: " + values.length);
            }

            int cycleCount = GeneratorTables.getCycleCount(previousCycleIndex, 0);
            previousCycleIndex = 0;

            position += cycleCount;
            int bufferIndex = position / CYCLES_PER_SAMPLE;
            ensureCapacity(bufferIndex + 6);

            for (int i = 0; i < values.length; i++) {
                buffer[bufferIndex + i] = (byte) values[i];
            }
        }

        private void write(int index, byte value) {
            int cycleCount = GeneratorTables.getCycleCount(previousCycleIndex, index);
            previousCycleIndex = index;

            position += cycleCount;
            int bufferIndex = position / CYCLES_PER_SAMPLE;
            ensureCapacity(bufferIndex + 6);

            buffer[bufferIndex] = value;
            buffer[bufferIndex + 1] = value;
            buffer[bufferIndex + 2] = value;
            buffer[bufferIndex + 3] = value;
            buffer[bufferIndex + 4] = value;
        }

        private byte[] toByteArray() {
            byte[] values = new byte[position / CYCLES_PER_SAMPLE];
            System.arraycopy(buffer, 0, values, 0, values.length);
            return values;
        }
    }
}
