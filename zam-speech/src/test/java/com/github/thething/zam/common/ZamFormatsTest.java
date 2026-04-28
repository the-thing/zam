package com.github.thething.zam.common;

import com.github.thething.zam.enricher.Phoneme;
import com.github.thething.zam.enricher.PhonemeToken;
import com.github.thething.zam.renderer.PhonemeFrame;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ZamFormatsTest {

    @Test
    void shouldReturnFormattedTokensAsTable() {
        PhonemeToken[] tokens = createTokens();

        assertThat(ZamFormats.formatTokensAsTable(tokens)).isEqualTo(
                """
                        index phoneme length stress
                        ---------------------------
                            0      AB      0      0
                            5      CD     99      0
                           20      XY      5      1
                           44      A*    150      8
                        """);

        assertThat(ZamFormats.formatTokensAsTable(new PhonemeToken[0])).isEqualTo(
                """
                        index phoneme length stress
                        ---------------------------
                        """);

        assertThat(ZamFormats.formatTokensAsTable(null)).isEqualTo("null");
    }

    @Test
    void shouldReturnFormattedTokensAsLine() {
        PhonemeToken[] tokens = createTokens();

        assertThat(ZamFormats.formatTokensAsLine(tokens)).isEqualTo("4|0,5,20,44|AB,CD,XY,A*|0,99,5,150|0,0,1,8");
        assertThat(ZamFormats.formatTokensAsLine(new PhonemeToken[0])).isEqualTo("0||||");
        assertThat(ZamFormats.formatTokensAsLine(null)).isEqualTo("null");
    }

    @Test
    void shouldReturnFormattedFramesAsTable() {
        PhonemeFrame[] frames = createFrames();

        assertThat(ZamFormats.formatFramesAsTable(frames)).isEqualTo(
                """
                        seq flags ampl1 freq1 ampl2 freq2 ampl3 freq3 pitch
                        ---------------------------------------------------
                          0     6     3     0     4     1     5     2     7
                          1    71    23    23    55    44    65    56    83
                          2   160   130   100   140   110   150   120   170
                        """
        );

        assertThat(ZamFormats.formatFramesAsTable(new PhonemeFrame[0])).isEqualTo(
                """
                        seq flags ampl1 freq1 ampl2 freq2 ampl3 freq3 pitch
                        ---------------------------------------------------
                        """);

        assertThat(ZamFormats.formatFramesAsTable(null)).isEqualTo("null");
    }

    @Test
    void shouldReturnFormattedFramesAsLine() {
        PhonemeFrame[] frames = createFrames();

        assertThat(ZamFormats.formatFramesAsLine(frames)).isEqualTo("3|0,23,100|1,44,110|2,56,120|3,23,130|4,55,140|5,65,150|6,71,160|7,83,170");
        assertThat(ZamFormats.formatFramesAsLine(new PhonemeFrame[0])).isEqualTo("0||||||||");
        assertThat(ZamFormats.formatFramesAsLine((PhonemeFrame[]) null)).isEqualTo("null");

        PhonemeFrame[][] frameChunks = createFrameChunks();
        assertThat(ZamFormats.formatFramesAsLine(frameChunks)).isEqualTo("3|0,23,100|1,44,110|2,56,120|3,23,130|4,55,140|5,65,150|6,71,160|7,83,1703|0,23,100|1,44,110|2,56,120|3,23,130|4,55,140|5,65,150|6,71,160|7,83,170");
        assertThat(ZamFormats.formatFramesAsLine(new PhonemeFrame[0][0])).isEqualTo("0||||||||");
        assertThat(ZamFormats.formatFramesAsLine((PhonemeFrame[][]) null)).isEqualTo("null");
    }

    @Test
    void shouldReturnFormattedAudioAsLine() {
        assertThat(ZamFormats.formatAudioAsLine(new byte[]{10, 20, 30})).isEqualTo("3|10,20,30");
        assertThat(ZamFormats.formatAudioAsLine(new byte[]{-1})).isEqualTo("1|-1");
        assertThat(ZamFormats.formatAudioAsLine(new byte[0])).isEqualTo("0|");
        assertThat(ZamFormats.formatAudioAsLine(null)).isEqualTo("null");
    }

    private static PhonemeToken[] createTokens() {
        PhonemeToken t1 = new PhonemeToken(new Phoneme(0, "AB"), 0, 0);
        PhonemeToken t2 = new PhonemeToken(new Phoneme(5, "CD"), 99, 0);
        PhonemeToken t3 = new PhonemeToken(new Phoneme(20, "XY"), 5, 1);
        PhonemeToken t4 = new PhonemeToken(new Phoneme(44, "A*"), 150, 8);
        return new PhonemeToken[]{t1, t2, t3, t4};
    }

    private static PhonemeFrame[] createFrames() {
        PhonemeFrame f1 = new PhonemeFrame(0, 1, 2, 3, 4, 5, 6, 7);
        PhonemeFrame f2 = new PhonemeFrame(23, 44, 56, 23, 55, 65, 71, 83);
        PhonemeFrame f3 = new PhonemeFrame(100, 110, 120, 130, 140, 150, 160, 170);
        return new PhonemeFrame[]{f1, f2, f3};
    }

    private static PhonemeFrame[][] createFrameChunks() {
        return new PhonemeFrame[][]{createFrames(), createFrames()};
    }
}