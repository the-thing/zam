package com.github.thething.zam.generator;

import com.github.thething.zam.common.ZamFormats;
import com.github.thething.zam.enricher.PhonemeEnricher;
import com.github.thething.zam.enricher.PhonemeToken;
import com.github.thething.zam.io.Resources;
import com.github.thething.zam.renderer.PhonemeFrame;
import com.github.thething.zam.renderer.PhonemeRenderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class AudioGeneratorBatchTest {

    private AudioGenerator underTest;
    private PhonemeEnricher enricher;
    private PhonemeRenderer renderer;

    @BeforeEach
    void setUp() {
        underTest = new AudioGenerator();
        enricher = new PhonemeEnricher();
        renderer = new PhonemeRenderer();
    }

    @ParameterizedTest
    @MethodSource("phoneticSource")
    void shouldRenderFrames(String resourceName) throws IOException {
        Resources.forEachLine(resourceName, (lineNumber, line) -> {
            if (line.startsWith("#")) {
                return;
            }

            String[] parts = line.split("=");
            String phonetic = parts[0];
            String expectedAudio = parts[1];

            PhonemeToken[] tokens = enricher.enrich(phonetic);
            PhonemeFrame[][] frameChunks = renderer.render(tokens);
            byte[] audio = underTest.generateAudio(frameChunks);

            String actualAudio = ZamFormats.formatAudioAsLine(audio);
            assertThat(actualAudio).isEqualTo(expectedAudio);
        });
    }

    private static Stream<Arguments> phoneticSource() {
        return Stream.of(
                Arguments.of("sam-generator-phonetic-sentences.txt"),
                Arguments.of("sam-generator-phonetic-sentences-with-punctuation.txt")
        );
    }
}
