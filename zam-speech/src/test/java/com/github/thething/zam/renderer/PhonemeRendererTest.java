package com.github.thething.zam.renderer;

import com.github.thething.zam.enricher.PhonemeEnricher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class PhonemeRendererTest {

    private PhonemeRenderer underTest;
    private PhonemeEnricher phonemeEnricher;

    @BeforeEach
    void setUp() throws IOException {
        underTest = new PhonemeRenderer();
        phonemeEnricher = new PhonemeEnricher();
    }

    @Test
    void shouldReturnCreatedFrames() {
        PhonemeFrame[][] frameChunks;

        // sun
        frameChunks = underTest.render(phonemeEnricher.enrich("SAHN"), PhonemeRenderer.Stage.CREATE_FRAMES);
        assertThat(frameChunks.length).isEqualTo(1);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency1)).containsExactly(6, 6, 22, 22, 22, 22, 22, 22, 22, 22, 6, 6, 6, 6, 6, 6, 6);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency2)).containsExactly(73, 73, 44, 44, 44, 44, 44, 44, 44, 44, 54, 54, 54, 54, 54, 54, 54);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency3)).containsExactly(99, 99, 87, 87, 87, 87, 87, 87, 87, 87, 121, 121, 121, 121, 121, 121, 121);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude1)).containsExactly(0, 0, 15, 15, 15, 15, 15, 15, 15, 15, 9, 9, 9, 9, 9, 9, 9);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude2)).containsExactly(0, 0, 12, 12, 12, 12, 12, 12, 12, 12, 9, 9, 9, 9, 9, 9, 9);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude3)).containsExactly(0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::pitch)).containsExactly(64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::sampleFlags)).containsExactly(241, 241, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

        // nus
        frameChunks = underTest.render(phonemeEnricher.enrich("NAHS"), PhonemeRenderer.Stage.CREATE_FRAMES);
        assertThat(frameChunks.length).isEqualTo(1);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency1)).containsExactly(6, 6, 6, 6, 6, 6, 6, 22, 22, 22, 22, 22, 22, 6, 6);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency2)).containsExactly(54, 54, 54, 54, 54, 54, 54, 44, 44, 44, 44, 44, 44, 73, 73);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency3)).containsExactly(121, 121, 121, 121, 121, 121, 121, 87, 87, 87, 87, 87, 87, 99, 99);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude1)).containsExactly(9, 9, 9, 9, 9, 9, 9, 15, 15, 15, 15, 15, 15, 0, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude2)).containsExactly(9, 9, 9, 9, 9, 9, 9, 12, 12, 12, 12, 12, 12, 0, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude3)).containsExactly(0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::pitch)).containsExactly(64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::sampleFlags)).containsExactly(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 241, 241);

        // mix
        frameChunks = underTest.render(phonemeEnricher.enrich("MIHKS"), PhonemeRenderer.Stage.CREATE_FRAMES);
        assertThat(frameChunks.length).isEqualTo(1);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency1)).containsExactly(6, 6, 6, 6, 6, 6, 6, 14, 14, 14, 14, 14, 14, 14, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency2)).containsExactly(46, 46, 46, 46, 46, 46, 46, 72, 72, 72, 72, 72, 72, 72, 84, 84, 84, 84, 84, 84, 84, 84, 84, 84, 84, 73, 73);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency3)).containsExactly(81, 81, 81, 81, 81, 81, 81, 93, 93, 93, 93, 93, 93, 93, 94, 94, 94, 94, 94, 94, 94, 94, 94, 94, 94, 99, 99);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude1)).containsExactly(12, 12, 12, 12, 12, 12, 12, 13, 13, 13, 13, 13, 13, 13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude2)).containsExactly(3, 3, 3, 3, 3, 3, 3, 11, 11, 11, 11, 11, 11, 11, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude3)).containsExactly(0, 0, 0, 0, 0, 0, 0, 7, 7, 7, 7, 7, 7, 7, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::pitch)).containsExactly(64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::sampleFlags)).containsExactly(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 241, 241);

        // six
        frameChunks = underTest.render(phonemeEnricher.enrich("SIHKS"), PhonemeRenderer.Stage.CREATE_FRAMES);
        assertThat(frameChunks.length).isEqualTo(1);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency1)).containsExactly(6, 6, 14, 14, 14, 14, 14, 14, 14, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency2)).containsExactly(73, 73, 72, 72, 72, 72, 72, 72, 72, 84, 84, 84, 84, 84, 84, 84, 84, 84, 84, 84, 73, 73);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency3)).containsExactly(99, 99, 93, 93, 93, 93, 93, 93, 93, 94, 94, 94, 94, 94, 94, 94, 94, 94, 94, 94, 99, 99);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude1)).containsExactly(0, 0, 13, 13, 13, 13, 13, 13, 13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude2)).containsExactly(0, 0, 11, 11, 11, 11, 11, 11, 11, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude3)).containsExactly(0, 0, 7, 7, 7, 7, 7, 7, 7, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::pitch)).containsExactly(64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::sampleFlags)).containsExactly(241, 241, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 241, 241);
    }

    @Test
    void shouldReturnFramesWithTransitions() {
        PhonemeFrame[][] frameChunks;

        // sun
        frameChunks = underTest.render(phonemeEnricher.enrich("SAHN"), PhonemeRenderer.Stage.CREATE_TRANSITIONS);
        assertThat(frameChunks.length).isEqualTo(1);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency1)).containsExactly(6, 6, 10, 14, 18, 22, 22, 22, 22, 17, 12, 6, 6, 6, 6, 6, 6);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency2)).containsExactly(73, 73, 66, 59, 52, 44, 44, 44, 44, 47, 50, 54, 54, 54, 54, 54, 54);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency3)).containsExactly(99, 99, 96, 93, 90, 87, 87, 87, 87, 98, 109, 121, 121, 121, 121, 121, 121);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude1)).containsExactly(0, 0, 3, 7, 11, 15, 15, 15, 15, 13, 11, 9, 9, 9, 9, 9, 9);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude2)).containsExactly(0, 0, 3, 6, 9, 12, 12, 12, 12, 11, 10, 9, 9, 9, 9, 9, 9);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude3)).containsExactly(0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::pitch)).containsExactly(64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::sampleFlags)).containsExactly(241, 241, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

        // nus
        frameChunks = underTest.render(phonemeEnricher.enrich("NAHS"), PhonemeRenderer.Stage.CREATE_TRANSITIONS);
        assertThat(frameChunks.length).isEqualTo(1);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency1)).containsExactly(6, 6, 6, 6, 6, 6, 6, 11, 16, 22, 22, 18, 14, 10, 6);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency2)).containsExactly(54, 54, 54, 54, 54, 54, 54, 51, 48, 44, 44, 51, 58, 65, 73);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency3)).containsExactly(121, 121, 121, 121, 121, 121, 121, 110, 99, 87, 87, 90, 93, 96, 99);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude1)).containsExactly(9, 9, 9, 9, 9, 9, 9, 11, 13, 15, 15, 12, 8, 4, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude2)).containsExactly(9, 9, 9, 9, 9, 9, 9, 10, 11, 12, 12, 9, 6, 3, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude3)).containsExactly(0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::pitch)).containsExactly(64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::sampleFlags)).containsExactly(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 241, 241);

        // mix
        frameChunks = underTest.render(phonemeEnricher.enrich("MIHKS"), PhonemeRenderer.Stage.CREATE_TRANSITIONS);
        assertThat(frameChunks.length).isEqualTo(1);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency1)).containsExactly(6, 6, 6, 6, 6, 6, 6, 10, 14, 14, 14, 14, 13, 12, 10, 9, 8, 6, 6, 6, 6, 6, 6, 6, 5, 4, 3);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency2)).containsExactly(46, 46, 46, 46, 46, 46, 46, 59, 72, 72, 72, 72, 74, 76, 78, 80, 82, 84, 84, 84, 84, 84, 84, 84, 68, 51, 34);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency3)).containsExactly(81, 81, 81, 81, 81, 81, 81, 87, 93, 93, 93, 93, 93, 93, 93, 93, 93, 94, 94, 94, 94, 94, 94, 94, 76, 57, 38);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude1)).containsExactly(12, 12, 12, 12, 12, 12, 12, 12, 13, 13, 13, 13, 11, 9, 7, 5, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude2)).containsExactly(3, 3, 3, 3, 3, 3, 3, 7, 11, 11, 11, 11, 10, 8, 6, 4, 2, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude3)).containsExactly(0, 0, 0, 0, 0, 0, 0, 3, 7, 7, 7, 7, 6, 5, 4, 3, 2, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::pitch)).containsExactly(64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::sampleFlags)).containsExactly(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 241, 241);

        // six
        frameChunks = underTest.render(phonemeEnricher.enrich("SIHKS"), PhonemeRenderer.Stage.CREATE_TRANSITIONS);
        assertThat(frameChunks.length).isEqualTo(1);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency1)).containsExactly(6, 6, 8, 10, 12, 14, 14, 13, 12, 10, 9, 8, 6, 6, 6, 6, 6, 6, 6, 5, 4, 3);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency2)).containsExactly(73, 73, 73, 73, 73, 72, 72, 74, 76, 78, 80, 82, 84, 84, 84, 84, 84, 84, 84, 68, 51, 34);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency3)).containsExactly(99, 99, 98, 96, 95, 93, 93, 93, 93, 93, 93, 93, 94, 94, 94, 94, 94, 94, 94, 76, 57, 38);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude1)).containsExactly(0, 0, 3, 6, 9, 13, 13, 11, 9, 7, 5, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude2)).containsExactly(0, 0, 2, 5, 8, 11, 11, 10, 8, 6, 4, 2, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude3)).containsExactly(0, 0, 1, 3, 5, 7, 7, 6, 5, 4, 3, 2, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::pitch)).containsExactly(64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::sampleFlags)).containsExactly(241, 241, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 241, 241);
    }

    @Test
    void shouldReturnFramesWithPitchContour() {
        PhonemeFrame[][] frameChunks;

        // sun
        frameChunks = underTest.render(phonemeEnricher.enrich("SAHN"), PhonemeRenderer.Stage.ASSIGN_PITCH_CONTOUR);
        assertThat(frameChunks.length).isEqualTo(1);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency1)).containsExactly(6, 6, 10, 14, 18, 22, 22, 22, 22, 17, 12, 6, 6, 6, 6, 6, 6);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency2)).containsExactly(73, 73, 66, 59, 52, 44, 44, 44, 44, 47, 50, 54, 54, 54, 54, 54, 54);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency3)).containsExactly(99, 99, 96, 93, 90, 87, 87, 87, 87, 98, 109, 121, 121, 121, 121, 121, 121);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude1)).containsExactly(0, 0, 3, 7, 11, 15, 15, 15, 15, 13, 11, 9, 9, 9, 9, 9, 9);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude2)).containsExactly(0, 0, 3, 6, 9, 12, 12, 12, 12, 11, 10, 9, 9, 9, 9, 9, 9);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude3)).containsExactly(0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::pitch)).containsExactly(61, 61, 59, 57, 55, 53, 53, 53, 53, 56, 58, 61, 61, 61, 61, 61, 61);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::sampleFlags)).containsExactly(241, 241, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

        // nus
        frameChunks = underTest.render(phonemeEnricher.enrich("NAHS"), PhonemeRenderer.Stage.ASSIGN_PITCH_CONTOUR);
        assertThat(frameChunks.length).isEqualTo(1);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency1)).containsExactly(6, 6, 6, 6, 6, 6, 6, 11, 16, 22, 22, 18, 14, 10, 6);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency2)).containsExactly(54, 54, 54, 54, 54, 54, 54, 51, 48, 44, 44, 51, 58, 65, 73);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency3)).containsExactly(121, 121, 121, 121, 121, 121, 121, 110, 99, 87, 87, 90, 93, 96, 99);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude1)).containsExactly(9, 9, 9, 9, 9, 9, 9, 11, 13, 15, 15, 12, 8, 4, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude2)).containsExactly(9, 9, 9, 9, 9, 9, 9, 10, 11, 12, 12, 9, 6, 3, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude3)).containsExactly(0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::pitch)).containsExactly(61, 61, 61, 61, 61, 61, 61, 59, 56, 53, 53, 55, 57, 59, 61);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::sampleFlags)).containsExactly(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 241, 241);

        // mix
        frameChunks = underTest.render(phonemeEnricher.enrich("MIHKS"), PhonemeRenderer.Stage.ASSIGN_PITCH_CONTOUR);
        assertThat(frameChunks.length).isEqualTo(1);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency1)).containsExactly(6, 6, 6, 6, 6, 6, 6, 10, 14, 14, 14, 14, 13, 12, 10, 9, 8, 6, 6, 6, 6, 6, 6, 6, 5, 4, 3);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency2)).containsExactly(46, 46, 46, 46, 46, 46, 46, 59, 72, 72, 72, 72, 74, 76, 78, 80, 82, 84, 84, 84, 84, 84, 84, 84, 68, 51, 34);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency3)).containsExactly(81, 81, 81, 81, 81, 81, 81, 87, 93, 93, 93, 93, 93, 93, 93, 93, 93, 94, 94, 94, 94, 94, 94, 94, 76, 57, 38);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude1)).containsExactly(12, 12, 12, 12, 12, 12, 12, 12, 13, 13, 13, 13, 11, 9, 7, 5, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude2)).containsExactly(3, 3, 3, 3, 3, 3, 3, 7, 11, 11, 11, 11, 10, 8, 6, 4, 2, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude3)).containsExactly(0, 0, 0, 0, 0, 0, 0, 3, 7, 7, 7, 7, 6, 5, 4, 3, 2, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::pitch)).containsExactly(61, 61, 61, 61, 61, 61, 61, 59, 57, 57, 57, 57, 58, 58, 59, 60, 60, 61, 61, 61, 61, 61, 61, 61, 62, 62, 63);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::sampleFlags)).containsExactly(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 241, 241);

        // six
        frameChunks = underTest.render(phonemeEnricher.enrich("SIHKS"), PhonemeRenderer.Stage.ASSIGN_PITCH_CONTOUR);
        assertThat(frameChunks.length).isEqualTo(1);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency1)).containsExactly(6, 6, 8, 10, 12, 14, 14, 13, 12, 10, 9, 8, 6, 6, 6, 6, 6, 6, 6, 5, 4, 3);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency2)).containsExactly(73, 73, 73, 73, 73, 72, 72, 74, 76, 78, 80, 82, 84, 84, 84, 84, 84, 84, 84, 68, 51, 34);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency3)).containsExactly(99, 99, 98, 96, 95, 93, 93, 93, 93, 93, 93, 93, 94, 94, 94, 94, 94, 94, 94, 76, 57, 38);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude1)).containsExactly(0, 0, 3, 6, 9, 13, 13, 11, 9, 7, 5, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude2)).containsExactly(0, 0, 2, 5, 8, 11, 11, 10, 8, 6, 4, 2, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude3)).containsExactly(0, 0, 1, 3, 5, 7, 7, 6, 5, 4, 3, 2, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::pitch)).containsExactly(61, 61, 60, 59, 58, 57, 57, 58, 58, 59, 60, 60, 61, 61, 61, 61, 61, 61, 61, 62, 62, 63);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::sampleFlags)).containsExactly(241, 241, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 241, 241);
    }

    @Test
    void shouldReturnFramesWithRescaledAmplitude() {
        PhonemeFrame[][] frameChunks;

        // sun
        frameChunks = underTest.render(phonemeEnricher.enrich("SAHN"), PhonemeRenderer.Stage.RESCALE_AMPLITUDE);
        assertThat(frameChunks.length).isEqualTo(1);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency1)).containsExactly(6, 6, 10, 14, 18, 22, 22, 22, 22, 17, 12, 6, 6, 6, 6, 6, 6);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency2)).containsExactly(73, 73, 66, 59, 52, 44, 44, 44, 44, 47, 50, 54, 54, 54, 54, 54, 54);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency3)).containsExactly(99, 99, 96, 93, 90, 87, 87, 87, 87, 98, 109, 121, 121, 121, 121, 121, 121);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude1)).containsExactly(0, 0, 2, 4, 8, 15, 15, 15, 15, 11, 8, 5, 5, 5, 5, 5, 5);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude2)).containsExactly(0, 0, 2, 3, 5, 9, 9, 9, 9, 8, 6, 5, 5, 5, 5, 5, 5);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude3)).containsExactly(0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::pitch)).containsExactly(61, 61, 59, 57, 55, 53, 53, 53, 53, 56, 58, 61, 61, 61, 61, 61, 61);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::sampleFlags)).containsExactly(241, 241, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

        // nus
        frameChunks = underTest.render(phonemeEnricher.enrich("NAHS"), PhonemeRenderer.Stage.RESCALE_AMPLITUDE);
        assertThat(frameChunks.length).isEqualTo(1);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency1)).containsExactly(6, 6, 6, 6, 6, 6, 6, 11, 16, 22, 22, 18, 14, 10, 6);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency2)).containsExactly(54, 54, 54, 54, 54, 54, 54, 51, 48, 44, 44, 51, 58, 65, 73);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency3)).containsExactly(121, 121, 121, 121, 121, 121, 121, 110, 99, 87, 87, 90, 93, 96, 99);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude1)).containsExactly(5, 5, 5, 5, 5, 5, 5, 8, 11, 15, 15, 9, 4, 2, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude2)).containsExactly(5, 5, 5, 5, 5, 5, 5, 6, 8, 9, 9, 5, 3, 2, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude3)).containsExactly(0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::pitch)).containsExactly(61, 61, 61, 61, 61, 61, 61, 59, 56, 53, 53, 55, 57, 59, 61);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::sampleFlags)).containsExactly(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 241, 241);

        // mix
        frameChunks = underTest.render(phonemeEnricher.enrich("MIHKS"), PhonemeRenderer.Stage.RESCALE_AMPLITUDE);
        assertThat(frameChunks.length).isEqualTo(1);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency1)).containsExactly(6, 6, 6, 6, 6, 6, 6, 10, 14, 14, 14, 14, 13, 12, 10, 9, 8, 6, 6, 6, 6, 6, 6, 6, 5, 4, 3);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency2)).containsExactly(46, 46, 46, 46, 46, 46, 46, 59, 72, 72, 72, 72, 74, 76, 78, 80, 82, 84, 84, 84, 84, 84, 84, 84, 68, 51, 34);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency3)).containsExactly(81, 81, 81, 81, 81, 81, 81, 87, 93, 93, 93, 93, 93, 93, 93, 93, 93, 94, 94, 94, 94, 94, 94, 94, 76, 57, 38);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude1)).containsExactly(9, 9, 9, 9, 9, 9, 9, 9, 11, 11, 11, 11, 8, 5, 4, 3, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude2)).containsExactly(2, 2, 2, 2, 2, 2, 2, 4, 8, 8, 8, 8, 6, 4, 3, 2, 2, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude3)).containsExactly(0, 0, 0, 0, 0, 0, 0, 2, 4, 4, 4, 4, 3, 3, 2, 2, 2, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::pitch)).containsExactly(61, 61, 61, 61, 61, 61, 61, 59, 57, 57, 57, 57, 58, 58, 59, 60, 60, 61, 61, 61, 61, 61, 61, 61, 62, 62, 63);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::sampleFlags)).containsExactly(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 241, 241);

        // six
        frameChunks = underTest.render(phonemeEnricher.enrich("SIHKS"), PhonemeRenderer.Stage.RESCALE_AMPLITUDE);
        assertThat(frameChunks.length).isEqualTo(1);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency1)).containsExactly(6, 6, 8, 10, 12, 14, 14, 13, 12, 10, 9, 8, 6, 6, 6, 6, 6, 6, 6, 5, 4, 3);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency2)).containsExactly(73, 73, 73, 73, 73, 72, 72, 74, 76, 78, 80, 82, 84, 84, 84, 84, 84, 84, 84, 68, 51, 34);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::frequency3)).containsExactly(99, 99, 98, 96, 95, 93, 93, 93, 93, 93, 93, 93, 94, 94, 94, 94, 94, 94, 94, 76, 57, 38);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude1)).containsExactly(0, 0, 2, 3, 5, 11, 11, 8, 5, 4, 3, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude2)).containsExactly(0, 0, 2, 3, 4, 8, 8, 6, 4, 3, 2, 2, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::amplitude3)).containsExactly(0, 0, 1, 2, 3, 4, 4, 3, 3, 2, 2, 2, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::pitch)).containsExactly(61, 61, 60, 59, 58, 57, 57, 58, 58, 59, 60, 60, 61, 61, 61, 61, 61, 61, 61, 62, 62, 63);
        assertThat(Arrays.stream(frameChunks[0]).map(PhonemeFrame::sampleFlags)).containsExactly(241, 241, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 241, 241);
    }
}
