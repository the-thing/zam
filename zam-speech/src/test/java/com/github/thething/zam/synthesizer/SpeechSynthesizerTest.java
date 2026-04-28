package com.github.thething.zam.synthesizer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.LineUnavailableException;

import static org.assertj.core.api.Assertions.assertThat;

class SpeechSynthesizerTest {

    private SpeechSynthesizer underTest;

    @BeforeEach
    void setUp() {
        underTest = SpeechSynthesizer.newInstance();
    }

    @Test
    void shouldReturnAudio() {
        byte[] audio;

        audio = underTest.generateAudio("a");
        assertThat(audio.length).isEqualTo(1399);

        audio = underTest.generateAudio("a", Theme.LITTLE_ROBOT);
        assertThat(audio.length).isEqualTo(1788);
    }

    @Test
    void shouldReturnAudioFromPhonetic() {
        byte[] audio;

        audio = underTest.generateAudioPhonetic("AE4KTIHV");
        assertThat(audio.length).isEqualTo(9979);

        audio = underTest.generateAudioPhonetic("AE4KTIHV", Theme.LITTLE_ROBOT);
        assertThat(audio.length).isEqualTo(12719);
    }
}