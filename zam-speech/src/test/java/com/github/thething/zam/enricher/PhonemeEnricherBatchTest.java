package com.github.thething.zam.enricher;

import com.github.thething.zam.common.ZamFormats;
import com.github.thething.zam.io.Resources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class PhonemeEnricherBatchTest {

    private PhonemeEnricher underTest;

    @BeforeEach
    void setUp() {
        underTest = new PhonemeEnricher();
    }

    @ParameterizedTest
    @MethodSource("phoneticSource")
    void shouldRenderFrames(String resourceName, PhonemeEnricher.Stage stage) throws IOException {
        Resources.forEachLine(resourceName, (lineNumber, line) -> {
            if (line.startsWith("#")) {
                return;
            }

            String[] parts = line.split("=");
            String phonetic = parts[0];
            String expectedTokens = parts[1];

            PhonemeToken[] tokens = underTest.enrich(phonetic, stage);

            String actualTokens = ZamFormats.formatTokensAsLine(tokens);
            assertThat(actualTokens).isEqualTo(expectedTokens);
        });
    }

    private static Stream<Arguments> phoneticSource() {
        return Stream.of(
                Arguments.of("sam-rewrite-phonetic-pairs.txt", PhonemeEnricher.Stage.REWRITE),
                Arguments.of("sam-rewrite-phonetic-pairs-with-space.txt", PhonemeEnricher.Stage.REWRITE),
                Arguments.of("sam-rewrite-phoentic-sentences.txt", PhonemeEnricher.Stage.REWRITE),

                Arguments.of("sam-add-stress-phonetic-sentences.txt", PhonemeEnricher.Stage.ADD_STRESS),

                Arguments.of("sam-set-length-phonetic-sentences.txt", PhonemeEnricher.Stage.SET_LENGTH),

                Arguments.of("sam-adjust-length-phonetic-sentences.txt", PhonemeEnricher.Stage.ADJUST_LENGTH),
                Arguments.of("sam-adjust-length-phonetic-sentences-with-punctuation.txt", PhonemeEnricher.Stage.ADJUST_LENGTH),

                Arguments.of("sam-expand-phonetic-sentences.txt", PhonemeEnricher.Stage.EXPAND),
                Arguments.of("sam-expand-phonetic-sentences-with-punctuation.txt", PhonemeEnricher.Stage.EXPAND),

                Arguments.of("sam-insert-breath-phonetic-sentences.txt", PhonemeEnricher.Stage.INSERT_BREATH),
                Arguments.of("sam-insert-breath-phonetic-sentences-with-punctuation.txt", PhonemeEnricher.Stage.INSERT_BREATH)
        );
    }
}
