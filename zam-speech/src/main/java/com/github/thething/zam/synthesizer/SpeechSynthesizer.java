package com.github.thething.zam.synthesizer;

import com.github.thething.zam.enricher.PhonemeEnricher;
import com.github.thething.zam.enricher.PhonemeToken;
import com.github.thething.zam.generator.AudioGenerator;
import com.github.thething.zam.reciter.Reciter;
import com.github.thething.zam.reciter.ReciterRuleRegistry;
import com.github.thething.zam.renderer.PhonemeFrame;
import com.github.thething.zam.renderer.PhonemeRenderer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.io.IOException;

import static java.util.Objects.requireNonNull;

/**
 * High-level text-to-speech synthesizer that converts English text into raw PCM audio.
 */
public final class SpeechSynthesizer {

    private static final float SAMPLE_RATE = 22050.0f;
    public static final AudioFormat AUDIO_FORMAT = new AudioFormat(SAMPLE_RATE, 8, 1, false, true);

    private final Reciter reciter;
    private final PhonemeEnricher enricher;
    private final PhonemeRenderer renderer;
    private final AudioGenerator generator;

    /**
     * Creates a synthesizer using the supplied {@link Reciter} and default instances of all other components.
     *
     * @param reciter the reciter used to convert English text to phonetic notation; must not be {@code null}
     */
    public SpeechSynthesizer(Reciter reciter) {
        this(reciter, new PhonemeEnricher(), new PhonemeRenderer(), new AudioGenerator());
    }

    /**
     * Creates a synthesizer with full control over every processing component.
     *
     * @param reciter   converts English text to SAM phonetic notation; must not be {@code null}
     * @param enricher  enriches raw phoneme tokens with stress and length; must not be {@code null}
     * @param renderer  renders phoneme tokens into acoustic frames; must not be {@code null}
     * @param generator generates raw PCM audio from acoustic frames; must not be {@code null}
     */
    public SpeechSynthesizer(Reciter reciter, PhonemeEnricher enricher, PhonemeRenderer renderer, AudioGenerator generator) {
        this.reciter = requireNonNull(reciter);
        this.enricher = requireNonNull(enricher);
        this.renderer = requireNonNull(renderer);
        this.generator = requireNonNull(generator);
    }

    /**
     * Factory method that loads the default pronunciation rules and returns a ready-to-use synthesizer.
     *
     * @return a new {@link SpeechSynthesizer} backed by the default {@link ReciterRuleRegistry}
     * @throws RuntimeException if the default reciter rules resource cannot be loaded
     */
    public static SpeechSynthesizer newInstance() {
        ReciterRuleRegistry reciterRuleRegistry;

        try {
            reciterRuleRegistry = ReciterRuleRegistry.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load reciter rules", e);
        }

        Reciter reciter = new Reciter(reciterRuleRegistry);

        return new SpeechSynthesizer(reciter);
    }

    /**
     * Converts English text to raw PCM audio using the default {@link Theme#SAM} voice theme.
     *
     * @param text the English text to synthesize
     * @return raw PCM audio bytes in {@link #AUDIO_FORMAT}
     */
    public byte[] generateAudio(String text) {
        return generateAudio(text, Theme.SAM);
    }

    /**
     * Converts English text to raw PCM audio using the supplied voice theme.
     *
     * @param text  the English text to synthesize
     * @param theme the voice theme to use; must not be {@code null}
     * @return raw PCM audio bytes in {@link #AUDIO_FORMAT}
     */
    public byte[] generateAudio(String text, Theme theme) {
        return generateAudio(text, theme, false);
    }

    /**
     * Converts English text to raw PCM audio using the supplied voice theme and optional sing mode.
     *
     * @param text  the English text to synthesize
     * @param theme the voice theme to use; must not be {@code null}
     * @param sing  {@code true} to disable the natural pitch contour (useful for singing)
     * @return raw PCM audio bytes in {@link #AUDIO_FORMAT}
     */
    public byte[] generateAudio(String text, Theme theme, boolean sing) {
        return generateAudio(text, theme.speed(), theme.pitch(), theme.mouth(), theme.throat(), sing);
    }

    /**
     * Converts English text to raw PCM audio with fine-grained control over all synthesis parameters.
     *
     * @param text   the English text to synthesize
     * @param speed  playback speed (1 – 255; higher values are faster)
     * @param pitch  base pitch (0 – 255)
     * @param mouth  mouth formant parameter (0 – 255)
     * @param throat throat formant parameter (0 – 255)
     * @param sing   {@code true} to disable the natural pitch contour
     * @return raw PCM audio bytes in {@link #AUDIO_FORMAT}
     */
    public byte[] generateAudio(String text, int speed, int pitch, int mouth, int throat, boolean sing) {
        String phonetic = reciter.recite(text);
        PhonemeToken[] tokens = enricher.enrich(phonetic);
        PhonemeFrame[][] frameChunks = renderer.render(tokens, pitch, mouth, throat, sing);
        return generator.generateAudio(frameChunks, speed);
    }

    /**
     * Converts SAM phonetic notation to raw PCM audio using the default {@link Theme#SAM SAM} voice theme.
     *
     * @param phonetic the phonetic input string in SAM notation
     * @return raw PCM audio bytes in {@link #AUDIO_FORMAT}
     */
    public byte[] generateAudioPhonetic(String phonetic) {
        return generateAudioPhonetic(phonetic, Theme.SAM);
    }

    /**
     * Converts SAM phonetic notation to raw PCM audio using the supplied voice theme.
     *
     * @param phonetic the phonetic input string in SAM notation
     * @param theme    the voice theme to use; must not be {@code null}
     * @return raw PCM audio bytes in {@link #AUDIO_FORMAT}
     */
    public byte[] generateAudioPhonetic(String phonetic, Theme theme) {
        return generateAudioPhonetic(phonetic, theme, false);
    }

    /**
     * Converts SAM phonetic notation to raw PCM audio using the supplied voice theme and optional sing mode.
     *
     * @param phonetic the phonetic input string in SAM notation
     * @param theme    the voice theme to use; must not be {@code null}
     * @param sing     {@code true} to disable the natural pitch contour
     * @return raw PCM audio bytes in {@link #AUDIO_FORMAT}
     */
    public byte[] generateAudioPhonetic(String phonetic, Theme theme, boolean sing) {
        return generateAudioPhonetic(phonetic, theme.speed(), theme.pitch(), theme.mouth(), theme.throat(), sing);
    }

    /**
     * Converts SAM phonetic notation to raw PCM audio with fine-grained control over all synthesis parameters.
     *
     * @param phonetic the phonetic input string in SAM notation
     * @param speed    playback speed (1 – 255; higher values are faster)
     * @param pitch    base pitch (0 – 255)
     * @param mouth    mouth formant parameter (0 – 255)
     * @param throat   throat formant parameter (0 – 255)
     * @param sing     {@code true} to disable the natural pitch contour
     * @return raw PCM audio bytes in {@link #AUDIO_FORMAT}
     */
    public byte[] generateAudioPhonetic(String phonetic, int speed, int pitch, int mouth, int throat, boolean sing) {
        PhonemeToken[] tokens = enricher.enrich(phonetic);
        PhonemeFrame[][] frameChunks = renderer.render(tokens, pitch, mouth, throat, sing);
        return generator.generateAudio(frameChunks, speed);
    }

    /**
     * Synthesizes English text and plays it through the default audio output using the {@link Theme#SAM SAM} theme.
     *
     * @param text the English text to speak
     * @throws LineUnavailableException if the audio line cannot be opened
     */
    public void say(String text) throws LineUnavailableException {
        say(text, Theme.SAM);
    }

    /**
     * Synthesizes English text and plays it through the default audio output using the given theme.
     *
     * @param text  the English text to speak
     * @param theme the voice theme to use; must not be {@code null}
     * @throws LineUnavailableException if the audio line cannot be opened
     */
    public void say(String text, Theme theme) throws LineUnavailableException {
        say(text, theme, false);
    }

    /**
     * Synthesizes English text and plays it through the default audio output using the given theme and sing mode.
     *
     * @param text  the English text to speak
     * @param theme the voice theme to use; must not be {@code null}
     * @param sing  {@code true} to disable the natural pitch contour
     * @throws LineUnavailableException if the audio line cannot be opened
     */
    public void say(String text, Theme theme, boolean sing) throws LineUnavailableException {
        say(text, theme.speed(), theme.pitch(), theme.mouth(), theme.throat(), sing);
    }

    /**
     * Synthesizes English text and plays it through the default audio output with full parameter control.
     *
     * @param text   the English text to speak
     * @param speed  playback speed (1 – 255)
     * @param pitch  base pitch (0 – 255)
     * @param mouth  mouth formant parameter (0 – 255)
     * @param throat throat formant parameter (0 – 255)
     * @param sing   {@code true} to disable the natural pitch contour
     * @throws LineUnavailableException if the audio line cannot be opened
     */
    public void say(String text, int speed, int pitch, int mouth, int throat, boolean sing) throws LineUnavailableException {
        byte[] audio = generateAudio(text, speed, pitch, mouth, throat, sing);
        play(audio);
    }

    /**
     * Synthesizes SAM phonetic notation and plays it through the default audio output using the {@link Theme#SAM SAM}
     * theme.
     *
     * @param phonetic the phonetic input string in SAM notation
     * @throws LineUnavailableException if the audio line cannot be opened
     */
    public void sayPhonetic(String phonetic) throws LineUnavailableException {
        sayPhonetic(phonetic, Theme.SAM);
    }

    /**
     * Synthesizes SAM phonetic notation and plays it through the default audio output using the given theme.
     *
     * @param phonetic the phonetic input string in SAM notation
     * @param theme    the voice theme to use; must not be {@code null}
     * @throws LineUnavailableException if the audio line cannot be opened
     */
    public void sayPhonetic(String phonetic, Theme theme) throws LineUnavailableException {
        sayPhonetic(phonetic, theme, false);
    }

    /**
     * Synthesizes SAM phonetic notation and plays it through the default audio output using the given theme and sing
     * mode.
     *
     * @param phonetic the phonetic input string in SAM notation
     * @param theme    the voice theme to use; must not be {@code null}
     * @param sing     {@code true} to disable the natural pitch contour
     * @throws LineUnavailableException if the audio line cannot be opened
     */
    public void sayPhonetic(String phonetic, Theme theme, boolean sing) throws LineUnavailableException {
        sayPhonetic(phonetic, theme.speed(), theme.pitch(), theme.mouth(), theme.throat(), sing);
    }

    /**
     * Synthesizes SAM phonetic notation and plays it through the default audio output with full parameter control.
     *
     * @param phonetic the phonetic input string in SAM notation
     * @param speed    playback speed (1 – 255)
     * @param pitch    base pitch (0 – 255)
     * @param mouth    mouth formant parameter (0 – 255)
     * @param throat   throat formant parameter (0 – 255)
     * @param sing     {@code true} to disable the natural pitch contour
     * @throws LineUnavailableException if the audio line cannot be opened
     */
    public void sayPhonetic(String phonetic, int speed, int pitch, int mouth, int throat, boolean sing) throws LineUnavailableException {
        byte[] audio = generateAudioPhonetic(phonetic, speed, pitch, mouth, throat, sing);
        play(audio);
    }

    private void play(byte[] audio) throws LineUnavailableException {
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, AUDIO_FORMAT);
        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(dataLineInfo);

        line.open();
        line.start();
        line.write(audio, 0, audio.length);
        line.drain();
        line.close();
    }
}
