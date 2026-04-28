package com.github.thething.zam.common;

import com.github.thething.zam.enricher.PhonemeToken;
import com.github.thething.zam.renderer.PhonemeFrame;

/**
 * Formatting utilities for ZAM data types.
 */
public final class ZamFormats {

    private ZamFormats() {
    }

    /**
     * Formats a {@link PhonemeToken} array as a multi-line ASCII table.
     *
     * @param tokens the tokens to format; may be {@code null}
     * @return the formatted table string
     */
    public static String formatTokensAsTable(PhonemeToken[] tokens) {
        StringBuilder out = new StringBuilder();
        formatTokensAsTable(tokens, out);
        return out.toString();
    }

    /**
     * Appends a {@link PhonemeToken} array formatted as a multi-line ASCII table to {@code out}.
     *
     * @param tokens the tokens to format; may be {@code null}
     * @param out    the builder to append to
     */
    public static void formatTokensAsTable(PhonemeToken[] tokens, StringBuilder out) {
        if (tokens == null) {
            out.append("null");
            return;
        }

        // header
        out.append("index phoneme length stress");
        out.append('\n');

        out.append("---------------------------");
        out.append('\n');

        for (PhonemeToken token : tokens) {
            // index
            String index = String.valueOf(token.phoneme().index());
            Strings.padLeft(out, index, 5, ' ');

            // phoneme
            String pattern = token.phoneme().pattern();
            Strings.padLeft(out, pattern, 8, ' ');

            // length
            String length = String.valueOf(token.length());
            Strings.padLeft(out, length, 7, ' ');

            // stress
            String stress = String.valueOf(token.stress());
            Strings.padLeft(out, stress, 7, ' ');

            out.append('\n');
        }
    }

    /**
     * Formats a {@link PhonemeToken} array as a compact pipe-separated single-line string
     * ({@code count|indices|patterns|lengths|stresses}).
     *
     * @param tokens the tokens to format; may be {@code null}
     * @return the formatted line string
     */
    public static String formatTokensAsLine(PhonemeToken[] tokens) {
        StringBuilder out = new StringBuilder();
        formatTokensAsLine(tokens, out);
        return out.toString();
    }

    /**
     * Appends a {@link PhonemeToken} array formatted as a compact pipe-separated single-line string to {@code out}.
     *
     * @param tokens the tokens to format; may be {@code null}
     * @param out    the builder to append to
     */
    public static void formatTokensAsLine(PhonemeToken[] tokens, StringBuilder out) {
        if (tokens == null) {
            out.append("null");
            return;
        }

        out.append(tokens.length);
        out.append('|');

        // index
        for (int i = 0; i < tokens.length; i++) {
            PhonemeToken token = tokens[i];

            if (i != 0) {
                out.append(',');
            }

            out.append(token.phoneme().index());
        }

        out.append('|');

        // phoneme
        for (int i = 0; i < tokens.length; i++) {
            PhonemeToken token = tokens[i];

            if (i != 0) {
                out.append(',');
            }

            out.append(token.phoneme().pattern());
        }

        out.append('|');

        // length
        for (int i = 0; i < tokens.length; i++) {
            PhonemeToken token = tokens[i];

            if (i != 0) {
                out.append(',');
            }

            out.append(token.length());
        }

        out.append('|');

        // stress
        for (int i = 0; i < tokens.length; i++) {
            PhonemeToken token = tokens[i];

            if (i != 0) {
                out.append(',');
            }

            out.append(token.stress());
        }
    }

    /**
     * Formats a {@link PhonemeFrame} array as a multi-line ASCII table.
     *
     * @param frames the frames to format; may be {@code null}
     * @return the formatted table string
     */
    public static String formatFramesAsTable(PhonemeFrame[] frames) {
        StringBuilder out = new StringBuilder();
        formatFramesAsTable(frames, out);
        return out.toString();
    }

    /**
     * Appends a {@link PhonemeFrame} array formatted as a multi-line ASCII table to {@code out}.
     *
     * @param frames the frames to format; may be {@code null}
     * @param out    the builder to append to
     */
    public static void formatFramesAsTable(PhonemeFrame[] frames, StringBuilder out) {
        if (frames == null) {
            out.append("null");
            return;
        }

        // header
        out.append("seq flags ampl1 freq1 ampl2 freq2 ampl3 freq3 pitch");
        out.append('\n');

        out.append("---------------------------------------------------");
        out.append('\n');

        for (int i = 0; i < frames.length; i++) {
            PhonemeFrame frame = frames[i];

            // sequence
            String sequence = String.valueOf(i);
            Strings.padLeft(out, sequence, 3, ' ');

            String sampleFlags = String.valueOf(frame.sampleFlags());
            Strings.padLeft(out, sampleFlags, 6, ' ');

            String ampl1 = String.valueOf(frame.amplitude1());
            Strings.padLeft(out, ampl1, 6, ' ');

            String freq1 = String.valueOf(frame.frequency1());
            Strings.padLeft(out, freq1, 6, ' ');

            String ampl2 = String.valueOf(frame.amplitude2());
            Strings.padLeft(out, ampl2, 6, ' ');

            String freq2 = String.valueOf(frame.frequency2());
            Strings.padLeft(out, freq2, 6, ' ');

            // ampl3
            String ampl3 = String.valueOf(frame.amplitude3());
            Strings.padLeft(out, ampl3, 6, ' ');

            // freq3
            String freq3 = String.valueOf(frame.frequency3());
            Strings.padLeft(out, freq3, 6, ' ');

            // pitch
            String pitch = String.valueOf(frame.pitch());
            Strings.padLeft(out, pitch, 6, ' ');

            out.append('\n');
        }
    }

    /**
     * Formats a two-dimensional array of {@link PhonemeFrame} chunks as a compact pipe-separated single-line string
     * (all chunks concatenated).
     *
     * @param frameChunks the frame chunks to format; may be {@code null}
     * @return the formatted line string
     */
    public static String formatFramesAsLine(PhonemeFrame[][] frameChunks) {
        StringBuilder out = new StringBuilder();
        formatFramesAsLine(frameChunks, out);
        return out.toString();
    }

    /**
     * Appends a two-dimensional array of {@link PhonemeFrame} chunks formatted as a compact pipe-separated single-line
     * string to {@code out}.
     *
     * @param frameChunks the frame chunks to format; may be {@code null}
     * @param out         the builder to append to
     */
    public static void formatFramesAsLine(PhonemeFrame[][] frameChunks, StringBuilder out) {
        if (frameChunks == null) {
            out.append("null");
            return;
        }

        if (frameChunks.length == 0) {
            out.append("0||||||||");
            return;
        }

        for (PhonemeFrame[] frameChunk : frameChunks) {
            formatFramesAsLine(frameChunk, out);
        }
    }

    /**
     * Formats a single {@link PhonemeFrame} array as a compact pipe-separated single-line string
     * ({@code count|freq1s|freq2s|freq3s|ampl1s|ampl2s|ampl3s|flags|pitches}).
     *
     * @param frames the frames to format; may be {@code null}
     * @return the formatted line string
     */
    public static String formatFramesAsLine(PhonemeFrame[] frames) {
        StringBuilder out = new StringBuilder();
        formatFramesAsLine(frames, out);
        return out.toString();
    }

    /**
     * Appends a single {@link PhonemeFrame} array formatted as a compact pipe-separated single-line string to
     * {@code out}.
     *
     * @param frames the frames to format; may be {@code null}
     * @param out    the builder to append to
     */
    public static void formatFramesAsLine(PhonemeFrame[] frames, StringBuilder out) {
        if (frames == null) {
            out.append("null");
            return;
        }

        out.append(frames.length);

        // freq1
        out.append('|');

        for (int i = 0; i < frames.length; i++) {
            PhonemeFrame frame = frames[i];

            if (i != 0) {
                out.append(',');
            }

            out.append(frame.frequency1());
        }

        // freq2
        out.append('|');

        for (int i = 0; i < frames.length; i++) {
            PhonemeFrame frame = frames[i];

            if (i != 0) {
                out.append(',');
            }

            out.append(frame.frequency2());
        }

        // freq3
        out.append('|');

        for (int i = 0; i < frames.length; i++) {
            PhonemeFrame frame = frames[i];

            if (i != 0) {
                out.append(',');
            }

            out.append(frame.frequency3());
        }

        // ampl1
        out.append('|');

        for (int i = 0; i < frames.length; i++) {
            PhonemeFrame frame = frames[i];

            if (i != 0) {
                out.append(',');
            }

            out.append(frame.amplitude1());
        }

        // ampl2
        out.append('|');

        for (int i = 0; i < frames.length; i++) {
            PhonemeFrame frame = frames[i];

            if (i != 0) {
                out.append(',');
            }

            out.append(frame.amplitude2());
        }

        // ampl3
        out.append('|');

        for (int i = 0; i < frames.length; i++) {
            PhonemeFrame frame = frames[i];

            if (i != 0) {
                out.append(',');
            }

            out.append(frame.amplitude3());
        }

        // flags
        out.append('|');

        for (int i = 0; i < frames.length; i++) {
            PhonemeFrame frame = frames[i];

            if (i != 0) {
                out.append(',');
            }

            out.append(frame.sampleFlags());
        }

        // pitch
        out.append('|');

        for (int i = 0; i < frames.length; i++) {
            PhonemeFrame frame = frames[i];

            if (i != 0) {
                out.append(',');
            }

            out.append(frame.pitch());
        }
    }

    /**
     * Formats a raw PCM audio byte array as a compact pipe-separated single-line string
     * ({@code length|byte0,byte1,...}).
     *
     * @param audio the audio bytes to format; may be {@code null}
     * @return the formatted line string
     */
    public static String formatAudioAsLine(byte[] audio) {
        StringBuilder out = new StringBuilder();
        formatAudioAsLine(audio, out);
        return out.toString();
    }

    /**
     * Appends a raw PCM audio byte array formatted as a compact pipe-separated single-line string to {@code out}.
     *
     * @param audio the audio bytes to format; may be {@code null}
     * @param out   the builder to append to
     */
    public static void formatAudioAsLine(byte[] audio, StringBuilder out) {
        if (audio == null) {
            out.append("null");
            return;
        }

        out.append(audio.length);
        out.append('|');

        for (int i = 0; i < audio.length; i++) {
            if (i != 0) {
                out.append(',');
            }

            out.append(audio[i]);
        }
    }
}
