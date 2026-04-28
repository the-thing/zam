package com.github.thething.zam.renderer;

import com.github.thething.zam.common.VisibleForTesting;
import com.github.thething.zam.enricher.Phoneme;
import com.github.thething.zam.enricher.PhonemeTables;
import com.github.thething.zam.enricher.PhonemeToken;

import java.util.ArrayList;
import java.util.List;

import static com.github.thething.zam.common.Requirements.requireInRange;

/**
 * Converts an array of {@link PhonemeToken}s into {@link PhonemeFrame} chunks ready for audio generation.
 *
 * <p>Processing is split into four internal stages (see {@link Stage}):
 * <ol>
 *   <li><b>CREATE_FRAMES</b> – assigns raw frequency, amplitude, and pitch values to each frame</li>
 *   <li><b>CREATE_TRANSITIONS</b> – interpolates parameter values across phoneme boundaries</li>
 *   <li><b>ASSIGN_PITCH_CONTOUR</b> – applies a natural intonation curve (skipped in sing mode)</li>
 *   <li><b>RESCALE_AMPLITUDE</b> – maps raw amplitude values to the final output scale</li>
 * </ol>
 *
 * <p>Input tokens are first split into breath groups at {@link PhonemeToken#BREATH} markers;
 * each group is rendered independently and returned as one element of the output array.
 */
public final class PhonemeRenderer {

    public static final int DEFAULT_PITCH = 64;
    public static final int DEFAULT_MOUTH = 128;
    public static final int DEFAULT_THROAT = 128;
    public static final boolean DEFAULT_SING = false;

    private static final int FREQUENCIES1_INDEX = 0;
    private static final int FREQUENCIES2_INDEX = 1;
    private static final int FREQUENCIES3_INDEX = 2;
    private static final int AMPLITUDES1_INDEX = 3;
    private static final int AMPLITUDES2_INDEX = 4;
    private static final int AMPLITUDES3_INDEX = 5;
    private static final int SAMPLE_FLAGS_INDEX = 6;
    private static final int PITCHES_INDEX = 7;

    /**
     * Renders phoneme tokens using default pitch, mouth, throat, and sing-mode values.
     *
     * @param tokens the enriched phoneme tokens to render
     * @return an array of {@link PhonemeFrame} arrays, one per breath group
     */
    public PhonemeFrame[][] render(PhonemeToken[] tokens) {
        return render(tokens, DEFAULT_PITCH, DEFAULT_MOUTH, DEFAULT_THROAT, DEFAULT_SING);
    }

    /**
     * Renders phoneme tokens with the given acoustic parameters.
     *
     * @param tokens the enriched phoneme tokens to render
     * @param pitch  base pitch (0 – 255)
     * @param mouth  mouth formant parameter (0 – 255)
     * @param throat throat formant parameter (0 – 255)
     * @param sing   {@code true} to skip the natural pitch contour assignment
     * @return an array of {@link PhonemeFrame} arrays, one per breath group
     * @throws IllegalArgumentException if any parameter is out of range
     */
    public PhonemeFrame[][] render(PhonemeToken[] tokens, int pitch, int mouth, int throat, boolean sing) {
        requireInRange(pitch, 0, 255);
        requireInRange(mouth, 0, 255);
        requireInRange(throat, 0, 255);

        return render(tokens, pitch, mouth, throat, sing, Stage.RESCALE_AMPLITUDE);
    }

    @VisibleForTesting
    PhonemeFrame[][] render(PhonemeToken[] tokens, Stage stage) {
        return render(tokens, DEFAULT_PITCH, DEFAULT_MOUTH, DEFAULT_THROAT, DEFAULT_SING, stage);
    }

    @VisibleForTesting
    PhonemeFrame[][] render(PhonemeToken[] tokens, int pitch, int mouth, int throat, boolean sign, Stage stage) {
        PhonemeToken[][] tokenChunks = split(tokens);

        switch (stage) {

            case CREATE_FRAMES -> {
                int[][][] frameChunks = createFrameChunks(tokenChunks, pitch, mouth, throat);
                return convertToObjects(frameChunks);
            }

            case CREATE_TRANSITIONS -> {
                int[][][] frameChunks = createFrameChunks(tokenChunks, pitch, mouth, throat);
                createTransitions(tokenChunks, frameChunks);
                return convertToObjects(frameChunks);
            }

            case ASSIGN_PITCH_CONTOUR -> {
                int[][][] frameChunks = createFrameChunks(tokenChunks, pitch, mouth, throat);
                createTransitions(tokenChunks, frameChunks);

                if (!sign) {
                    assignPitchContour(frameChunks);
                }

                return convertToObjects(frameChunks);
            }

            case RESCALE_AMPLITUDE -> {
                int[][][] frameChunks = createFrameChunks(tokenChunks, pitch, mouth, throat);
                createTransitions(tokenChunks, frameChunks);

                if (!sign) {
                    assignPitchContour(frameChunks);
                }

                rescaleAmplitude(frameChunks);

                return convertToObjects(frameChunks);
            }

            default -> throw new IllegalArgumentException("Unknown stage: " + stage);
        }
    }

    private PhonemeFrame[][] convertToObjects(int[][][] frameChunks) {
        PhonemeFrame[][] newFrameChunks = new PhonemeFrame[frameChunks.length][];

        for (int i = 0; i < frameChunks.length; i++) {
            int[] frequencies1 = frameChunks[i][FREQUENCIES1_INDEX];
            int[] frequencies2 = frameChunks[i][FREQUENCIES2_INDEX];
            int[] frequencies3 = frameChunks[i][FREQUENCIES3_INDEX];
            int[] amplitudes1 = frameChunks[i][AMPLITUDES1_INDEX];
            int[] amplitudes2 = frameChunks[i][AMPLITUDES2_INDEX];
            int[] amplitudes3 = frameChunks[i][AMPLITUDES3_INDEX];
            int[] sampleFlags = frameChunks[i][SAMPLE_FLAGS_INDEX];
            int[] pitches = frameChunks[i][PITCHES_INDEX];

            newFrameChunks[i] = new PhonemeFrame[frequencies1.length];

            for (int j = 0; j < frequencies1.length; j++) {
                newFrameChunks[i][j] = new PhonemeFrame(
                        frequencies1[j], frequencies2[j], frequencies3[j],
                        amplitudes1[j], amplitudes2[j], amplitudes3[j],
                        sampleFlags[j], pitches[j]);
            }
        }

        return newFrameChunks;
    }

    /**
     * Splits a flat list of phoneme tokens into chunks, using breath pauses ({@link PhonemeToken#BREATH}) as
     * delimiters. Each chunk represents a continuous segment of speech between breaths. Pause phonemes within a chunk
     * are discarded.
     */
    private static PhonemeToken[][] split(PhonemeToken[] tokens) {
        List<List<PhonemeToken>> chunks = new ArrayList<>();
        List<PhonemeToken> currentTokens = new ArrayList<>();

        for (PhonemeToken token : tokens) {
            if (token.equals(PhonemeToken.BREATH)) {
                if (!currentTokens.isEmpty()) {
                    chunks.add(currentTokens);
                    currentTokens = new ArrayList<>();
                }

                continue;
            }

            if (token.phoneme().isPause()) {
                continue;
            }

            currentTokens.add(token);
        }

        if (!currentTokens.isEmpty()) {
            chunks.add(currentTokens);
        }

        return chunks.stream()
                .map(part -> part.toArray(PhonemeToken[]::new))
                .toArray(PhonemeToken[][]::new);
    }

    private static int[][][] createFrameChunks(PhonemeToken[][] tokenChunks, int pitch, int mouth, int throat) {
        int[][][] frameChunks = new int[tokenChunks.length][][];

        for (int i = 0; i < tokenChunks.length; i++) {
            PhonemeToken[] tokens = tokenChunks[i];
            frameChunks[i] = createFrameChunks(tokens, pitch, mouth, throat);
        }

        return frameChunks;
    }

    private static int[][] createFrameChunks(PhonemeToken[] tokens, int pitch, int mouth, int throat) {
        int length = getTotalLength(tokens);

        int[][] frames = new int[8][length];

        int insertIndex = 0;

        for (PhonemeToken token : tokens) {
            Phoneme phoneme = token.phoneme();

            if (phoneme.isPeriod()) {
                addInflection(insertIndex, 1, frames[PITCHES_INDEX]);
            } else if (phoneme.isQuestionMark()) {
                addInflection(insertIndex, -1, frames[PITCHES_INDEX]);
            }

            int frequency1 = PhonemeTables.getMouthFrequency(phoneme.index(), mouth);
            int frequency2 = PhonemeTables.getThroatFrequency(phoneme.index(), throat);
            int frequency3 = phoneme.frequency3();
            int amplitude1 = phoneme.amplitude1();
            int amplitude2 = phoneme.amplitude2();
            int amplitude3 = phoneme.amplitude3();
            int sampleFlags = phoneme.sampleFlags();
            int newPitch = (pitch + RendererTables.getPitch(token.stress())) & 0xFF;

            for (int j = 0; j < token.length(); j++) {
                frames[FREQUENCIES1_INDEX][insertIndex] = frequency1;
                frames[FREQUENCIES2_INDEX][insertIndex] = frequency2;
                frames[FREQUENCIES3_INDEX][insertIndex] = frequency3;
                frames[AMPLITUDES1_INDEX][insertIndex] = amplitude1;
                frames[AMPLITUDES2_INDEX][insertIndex] = amplitude2;
                frames[AMPLITUDES3_INDEX][insertIndex] = amplitude3;
                frames[SAMPLE_FLAGS_INDEX][insertIndex] = sampleFlags;
                frames[PITCHES_INDEX][insertIndex] = newPitch;
                insertIndex++;
            }
        }

        return frames;
    }

    private static void createTransitions(PhonemeToken[][] tokenChunks, int[][][] frameChunks) {
        for (int i = 0; i < tokenChunks.length; i++) {
            int[] frequencies1 = frameChunks[i][FREQUENCIES1_INDEX];
            int[] frequencies2 = frameChunks[i][FREQUENCIES2_INDEX];
            int[] frequencies3 = frameChunks[i][FREQUENCIES3_INDEX];
            int[] amplitudes1 = frameChunks[i][AMPLITUDES1_INDEX];
            int[] amplitudes2 = frameChunks[i][AMPLITUDES2_INDEX];
            int[] amplitudes3 = frameChunks[i][AMPLITUDES3_INDEX];
            int[] pitches = frameChunks[i][PITCHES_INDEX];
            createTransitions(tokenChunks[i], frequencies1, frequencies2, frequencies3,
                    amplitudes1, amplitudes2, amplitudes3, pitches);
        }
    }

    private static void createTransitions(
            PhonemeToken[] tokens,
            int[] frequencies1, int[] frequencies2, int[] frequencies3,
            int[] amplitudes1, int[] amplitudes2, int[] amplitudes3,
            int[] pitches) {

        int cumulativeLength = 0;

        for (int i = 0; i < tokens.length - 1; i++) {
            Phoneme currentPhoneme = tokens[i].phoneme();
            Phoneme nextPhoneme = tokens[i + 1].phoneme();

            int currentRank = RendererTables.getBlendRank(currentPhoneme.index());
            int nextRank = RendererTables.getBlendRank(nextPhoneme.index());

            int preBlendLength;
            int postBlendLength;

            if (currentRank == nextRank) {
                // same rank - each phoneme contributes its own out-blend length
                preBlendLength = RendererTables.getOutBlendLength(currentPhoneme.index());
                postBlendLength = RendererTables.getOutBlendLength(nextPhoneme.index());
            } else if (currentRank < nextRank) {
                // current phoneme is stronger - use second phoneme's in/out blend lengths
                preBlendLength = RendererTables.getInBlendLength(nextPhoneme.index());
                postBlendLength = RendererTables.getOutBlendLength(nextPhoneme.index());
            } else {
                // next phoneme is stronger - use current phoneme's blend lengths (in/out swapped)
                preBlendLength = RendererTables.getOutBlendLength(currentPhoneme.index());
                postBlendLength = RendererTables.getInBlendLength(currentPhoneme.index());
            }

            cumulativeLength += tokens[i].length();

            // transition window
            int blendStartFrame = cumulativeLength - preBlendLength;
            int blendEndFrame = cumulativeLength + postBlendLength;

            // total transition length
            int transitionLength = preBlendLength + postBlendLength;

            if (transitionLength < 2) {
                // not need to crate transition between short phonemes
                continue;
            }

            int freq1Delta;
            int freq2Delta;
            int freq3Delta;
            int ampl1Delta;
            int ampl2Delta;
            int ampl3Delta;

            // this part is not in the original code, but it is possible that blendEndFrame is outside of frames' length
            // the original code is protected by using larger buffers, but in some cases it will read garbage from previous chunks
            // this makes certain tests give different results than original
            if (blendEndFrame >= frequencies1.length) {
                freq1Delta = -frequencies1[blendStartFrame];
                freq2Delta = -frequencies2[blendStartFrame];
                freq3Delta = -frequencies3[blendStartFrame];
                ampl1Delta = -amplitudes1[blendStartFrame];
                ampl2Delta = -amplitudes2[blendStartFrame];
                ampl3Delta = -amplitudes3[blendStartFrame];
            } else {
                freq1Delta = frequencies1[blendEndFrame] - frequencies1[blendStartFrame];
                freq2Delta = frequencies2[blendEndFrame] - frequencies2[blendStartFrame];
                freq3Delta = frequencies3[blendEndFrame] - frequencies3[blendStartFrame];
                ampl1Delta = amplitudes1[blendEndFrame] - amplitudes1[blendStartFrame];
                ampl2Delta = amplitudes2[blendEndFrame] - amplitudes2[blendStartFrame];
                ampl3Delta = amplitudes3[blendEndFrame] - amplitudes3[blendStartFrame];
            }

            interpolate(frequencies1, freq1Delta, blendStartFrame, transitionLength);
            interpolate(frequencies2, freq2Delta, blendStartFrame, transitionLength);
            interpolate(frequencies3, freq3Delta, blendStartFrame, transitionLength);
            interpolate(amplitudes1, ampl1Delta, blendStartFrame, transitionLength);
            interpolate(amplitudes2, ampl2Delta, blendStartFrame, transitionLength);
            interpolate(amplitudes3, ampl3Delta, blendStartFrame, transitionLength);

            // pitch interpolates between the midpoints of adjacent phonemes
            int currentHalfLength = tokens[i].length() >> 1;
            int nextHalfLength = tokens[i + 1].length() >> 1;
            int pitchStartFrame = cumulativeLength - currentHalfLength;
            int pitchEndFrame = cumulativeLength + nextHalfLength;
            int pitchDelta = pitches[pitchEndFrame] - pitches[pitchStartFrame];

            // override range especially for pitch calculation
            transitionLength = currentHalfLength + nextHalfLength;
            interpolate(pitches, pitchDelta, blendStartFrame, transitionLength);
        }
    }

    private static void interpolate(int[] values, int delta, int startFrame, int interpolationLength) {
        byte deltaByte = (byte) delta;
        int deltaSign = delta & 0x80;
        int absDelta = Math.abs(deltaByte);
        int deltaFraction = absDelta % interpolationLength;
        int deltaStep = (deltaByte / interpolationLength) & 0xFF;

        int frameCount = interpolationLength;
        int frameIndex = startFrame;
        int fracAccumulator = 0;

        while (true) {
            int frameValue = (values[frameIndex] + deltaStep) & 0xFF;
            frameIndex++;
            frameCount--;

            if (frameCount == 0) {
                break;
            }

            if (frameIndex == values.length) {
                // original code allocates fixed 256 byte arrays for freq / ampl / pitch so it can write outside
                break;
            }

            fracAccumulator = (fracAccumulator + deltaFraction) & 0xFF;

            if (fracAccumulator >= interpolationLength) {
                fracAccumulator = (fracAccumulator - interpolationLength) & 0xFF;

                if (deltaSign == 0) {
                    if (frameValue != 0) {
                        frameValue = (frameValue + 1) & 0xFF;
                    }
                } else {
                    frameValue = (frameValue - 1) & 0xFF;
                }
            }

            values[frameIndex] = frameValue;
        }
    }

    private static void assignPitchContour(int[][][] frameChunks) {
        for (int[][] frameChunk : frameChunks) {
            assignPitchContour(frameChunk);
        }
    }

    private static void assignPitchContour(int[][] frames) {
        for (int i = 0; i < frames[0].length; i++) {
            int pitch = frames[PITCHES_INDEX][i];
            int frequency1 = frames[FREQUENCIES1_INDEX][i];
            int newPitch = (pitch - (frequency1 >> 1)) & 0xFF;
            frames[PITCHES_INDEX][i] = newPitch;
        }
    }

    private static void rescaleAmplitude(int[][][] frameChunks) {
        for (int[][] frameChunk : frameChunks) {
            rescaleAmplitude(frameChunk);
        }
    }

    private static void rescaleAmplitude(int[][] frames) {
        for (int i = 0; i < frames[0].length; i++) {
            int newAmplitude1 = RendererTables.getAmplitudeRescale(frames[AMPLITUDES1_INDEX][i]);
            int newAmplitude2 = RendererTables.getAmplitudeRescale(frames[AMPLITUDES2_INDEX][i]);
            int newAmplitude3 = RendererTables.getAmplitudeRescale(frames[AMPLITUDES3_INDEX][i]);

            frames[AMPLITUDES1_INDEX][i] = newAmplitude1;
            frames[AMPLITUDES2_INDEX][i] = newAmplitude2;
            frames[AMPLITUDES3_INDEX][i] = newAmplitude3;
        }
    }

    private static void addInflection(int index, int delta, int[] pitches) {
        int left = Math.max(0, index - 30);

        if (left == index) {
            return;
        }

        // TODO I was not able to find a case with invalid pitch values that require skipping
        // ----- original code -----
        // skip over frames with an invalid pitch value (127)
        // while (pitches[frameIndex] == 127) frameIndex++;

        for (int i = left; i < index; i++) {
            if (pitches[i] == 0x7F) {
                throw new RuntimeException("Silent pitch == 127 for frame i: " + i + ": " + pitches[i]);
            }
        }

        int currentPitch = pitches[left];

        for (int i = left; i < index; i++) {
            if (pitches[i] == 0xFF) {
                // skip max pitches (see original code below)
                continue;
            }

            currentPitch += delta;
            int newPitch = currentPitch & 0xFF;
            pitches[i] = newPitch;

            // ----- original code -----
            // skip over silent/stop frames (pitch == 255) without modifying them
            // while (frameIndex != punctuationPosition && pitches[frameIndex] == 255) frameIndex++;
        }
    }

    private static int getTotalLength(PhonemeToken[] tokens) {
        int length = 0;

        for (PhonemeToken token : tokens) {
            length += token.length();
        }

        return length;
    }

    enum Stage {

        // assigns raw frequency, amplitude, pitch, and sample-flags values to each frame
        CREATE_FRAMES,
        // interpolates parameter values across phoneme boundaries to create smooth transitions
        CREATE_TRANSITIONS,
        // applies a natural pitch contour based on first-formant frequency
        ASSIGN_PITCH_CONTOUR,
        // rescales raw amplitude values to the final output scale
        RESCALE_AMPLITUDE
    }
}
