package com.github.thething.zam.example;

import com.github.thething.zam.synthesizer.SpeechSynthesizer;
import com.github.thething.zam.synthesizer.Theme;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class ZamGenericExample {

    public static void main(String[] args) throws LineUnavailableException, IOException {
        // create a default synthesizer instance
        SpeechSynthesizer synthesizer = SpeechSynthesizer.newInstance();

        // simply speak plain English text
        synthesizer.say("Hello. I am a speech synthesizer! I can sound different.");

        // speak with custom themes
        synthesizer.say("Oh no. It is Monday again. I did not sign up for this.", Theme.LITTLE_ROBOT);
        synthesizer.say("I told you to read the documentation. Nobody ever reads the documentation.", Theme.LITTLE_OLD_LADY);
        synthesizer.say("You can do anything. Anything at all. The only limit is yourself.", Theme.EXTRA_TERRESTRIAL);

        // speak with custom parameters
        synthesizer.say("Did someone say cheese? I love cheese!", 60, 200, 200, 50, false);

        // you can also generate phonetic equivalents directly
        synthesizer.sayPhonetic("PIY4TER PAY3PER- PIH4KT AH PEH4K AHV PIH4KULD PEH4PERZ.");

        // the robot sings "la la la"
        synthesizer.say("I can also sing. Look.");
        synthesizer.sayPhonetic("laeaeaeaeaeaeaeaeae laeaeaeaeaeaeaeaeae laeaeaeaeaeaeaeaeae", Theme.SAM, true);

        // generate audio bytes instead of playing
        byte[] audio = synthesizer.generateAudio("This audio could be saved to a file.", Theme.SAM);

        // save audio to waveform
        try (AudioInputStream in = new AudioInputStream(new ByteArrayInputStream(audio), SpeechSynthesizer.AUDIO_FORMAT, audio.length)) {
            AudioSystem.write(in, AudioFileFormat.Type.WAVE, new File("speech.wav"));
        }
    }
}
