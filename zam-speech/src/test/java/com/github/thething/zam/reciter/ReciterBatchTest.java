package com.github.thething.zam.reciter;

import com.github.thething.zam.io.Resources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class ReciterBatchTest {

    private Reciter underTest;

    @BeforeEach
    void setUp() throws IOException {
        ReciterRuleRegistry rules = ReciterRuleRegistry.load("rules/reciter-rules.txt");
        underTest = new Reciter(rules);
    }

    @ParameterizedTest
    @MethodSource("textSource")
    void shouldRenderFrames(String resourceName) throws IOException {
        Resources.forEachLine(resourceName, (lineNumber, line) -> {
            if (line.startsWith("#")) {
                return;
            }

            String[] parts = line.split("=");
            String text = parts[0];
            String expectedText = parts[1];

            String actualText = underTest.recite(text);
            assertThat(actualText).isEqualTo(expectedText);
        });
    }

    private static Stream<Arguments> textSource() {
        return Stream.of(
                Arguments.of("sam-reciter-words.txt"),
                Arguments.of("sam-reciter-words-special.txt"),
                Arguments.of("sam-reciter-days.txt"),
                Arguments.of("sam-reciter-months.txt"),
                Arguments.of("sam-reciter-numbers.txt"),
                Arguments.of("sam-reciter-states.txt"),
                Arguments.of("sam-reciter-units.txt"),
                Arguments.of("sam-reciter-sentences.txt"),
                Arguments.of("sam-reciter-sentences-with-punctuation.txt")
        );
    }
}
