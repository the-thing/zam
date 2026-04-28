package com.github.thething.zam.generator;

import com.github.thething.zam.enricher.PhonemeEnricher;
import com.github.thething.zam.enricher.PhonemeToken;
import com.github.thething.zam.renderer.PhonemeFrame;
import com.github.thething.zam.renderer.PhonemeRenderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AudioGeneratorTest {

    private AudioGenerator underTest;
    private PhonemeEnricher enricher;
    private PhonemeRenderer renderer;

    @BeforeEach
    void setUp() {
        underTest = new AudioGenerator();
        enricher = new PhonemeEnricher();
        renderer = new PhonemeRenderer();
    }

    @Test
    void shouldGenerateAudio() {
        PhonemeToken[] tokens;
        PhonemeFrame[][] frameChunks;
        byte[] sound;

        tokens = enricher.enrich("AH");
        frameChunks = renderer.render(tokens);
        sound = underTest.generateAudio(frameChunks);

        assertThat(sound.length).isEqualTo(1399);
    }

    @Test
    void shouldGenerateAudioWithVoicedSample() {
        PhonemeToken[] tokens;
        PhonemeFrame[][] frameChunks;
        byte[] sound;

        tokens = enricher.enrich("DHAEN");
        frameChunks = renderer.render(tokens);
        sound = underTest.generateAudio(frameChunks);

        assertThat(sound.length).isEqualTo(5930);
    }

    @Test
    void shouldGenerateAudioWithUnvoicedSample() {
        PhonemeToken[] tokens;
        PhonemeFrame[][] frameChunks;
        byte[] sound;

        tokens = enricher.enrich("AH BIY4 SIY4");
        frameChunks = renderer.render(tokens);
        sound = underTest.generateAudio(frameChunks);

        assertThat(sound.length).isEqualTo(11409);
    }
}