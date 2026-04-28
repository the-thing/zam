package com.github.thething.zam.renderer;

import com.github.thething.zam.common.ZamFormats;
import com.github.thething.zam.enricher.PhonemeEnricher;
import com.github.thething.zam.enricher.PhonemeToken;
import com.github.thething.zam.io.Resources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class PhonemeRendererBatchTest {

    private PhonemeRenderer underTest;
    private PhonemeEnricher phonemeEnricher;

    @BeforeEach
    void setUp() {
        underTest = new PhonemeRenderer();
        phonemeEnricher = new PhonemeEnricher();
    }

    @ParameterizedTest
    @MethodSource("tokenSource")
    void shouldRenderFrames(String resourceName, int pitch, int mouth, int throat, PhonemeRenderer.Stage stage) throws IOException {
        Resources.forEachLine(resourceName, (lineNumber, line) -> {
            if (line.startsWith("#")) {
                return;
            }

            String[] parts = line.split("=");
            String phonetic = parts[0];
            String expectedFrames = parts[1];

            PhonemeToken[] tokens = phonemeEnricher.enrich(phonetic);
            PhonemeFrame[][] frameChunks = underTest.render(tokens, pitch, mouth, throat, false, stage);

            String actualFrames = ZamFormats.formatFramesAsLine(frameChunks);
            assertThat(actualFrames).isEqualTo(expectedFrames);
        });
    }

    private static Stream<Arguments> tokenSource() {
        return Stream.of(
                Arguments.of("sam-renderer-create-frames.txt", 64, 128, 128, PhonemeRenderer.Stage.CREATE_FRAMES),
                Arguments.of("sam-renderer-create-frames-with-punctuation.txt", 64, 128, 128, PhonemeRenderer.Stage.CREATE_FRAMES),
                Arguments.of("sam-renderer-create-frames-mouth-throat.txt", 5, 230, 240, PhonemeRenderer.Stage.CREATE_FRAMES),

                Arguments.of("sam-renderer-create-transitions.txt", 64, 128, 128, PhonemeRenderer.Stage.CREATE_TRANSITIONS),
                Arguments.of("sam-renderer-create-transitions-with-punctuation.txt", 64, 128, 128, PhonemeRenderer.Stage.CREATE_TRANSITIONS),

                Arguments.of("sam-renderer-assign-pitch-contour.txt", 64, 128, 128, PhonemeRenderer.Stage.ASSIGN_PITCH_CONTOUR),
                Arguments.of("sam-renderer-assign-pitch-contour-with-punctuation.txt", 64, 128, 128, PhonemeRenderer.Stage.ASSIGN_PITCH_CONTOUR),

                Arguments.of("sam-renderer-rescale-amplitude.txt", 64, 128, 128, PhonemeRenderer.Stage.RESCALE_AMPLITUDE),
                Arguments.of("sam-renderer-rescale-amplitude-with-punctuation.txt", 64, 128, 128, PhonemeRenderer.Stage.RESCALE_AMPLITUDE),
                Arguments.of("sam-renderer-rescale-amplitude-custom.txt", 64, 128, 128, PhonemeRenderer.Stage.RESCALE_AMPLITUDE),

                Arguments.of("sam-renderer-rescale-amplitude-mouth-throat.txt", 5, 230, 240, PhonemeRenderer.Stage.CREATE_TRANSITIONS)
        );
    }
}
