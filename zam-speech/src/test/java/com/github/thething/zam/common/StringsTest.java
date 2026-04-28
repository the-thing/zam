package com.github.thething.zam.common;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StringsTest {

    @Test
    void shouldReturnStrippedText() {
        assertThat(Strings.strip("a'b'c''d'e''f'''", '\'')).isEqualTo("abcdef");
        assertThat(Strings.strip("\'a'b\\'c''d'e\\''f'''\\'", '\'')).isEqualTo("ab'cde'f'");
        assertThat(Strings.strip("", '\'')).isEqualTo("");
        assertThat(Strings.strip("\'\'\'", '\'')).isEqualTo("");
        assertThat(Strings.strip("\\'a\\'b\\'", '\'')).isEqualTo("'a'b'");
    }

    @Test
    void shouldReturnLeftPaddedString() {
        assertThat(Strings.padLeft("1", 3, ' ')).isEqualTo("  1");
        assertThat(Strings.padLeft("12", 3, ' ')).isEqualTo(" 12");
        assertThat(Strings.padLeft("123", 3, ' ')).isEqualTo("123");
        assertThat(Strings.padLeft("1234", 3, ' ')).isEqualTo("1234");
        assertThat(Strings.padLeft("12345", 10, '.')).isEqualTo(".....12345");
    }
}