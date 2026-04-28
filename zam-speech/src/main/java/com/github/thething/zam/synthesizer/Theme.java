package com.github.thething.zam.synthesizer;

import static com.github.thething.zam.common.Requirements.requireInRange;

/**
 * Voice theme that bundles the four acoustic parameters used by the synthesizer: speed, pitch, mouth, and throat.
 *
 * <p>Several preset themes are provided as constants (e.g. {@link #SAM}, {@link #ELF}).
 *
 * @param speed  speaking rate (1 – 255; higher values are faster)
 * @param pitch  base voice pitch (0 – 255)
 * @param mouth  mouth formant adjustment (0 – 255)
 * @param throat throat formant adjustment (0 – 255)
 */
public record Theme(int speed, int pitch, int mouth, int throat) {

    /**
     * Default SAM voice: neutral, balanced parameters.
     */
    public static final Theme SAM = new Theme(72, 64, 128, 128);

    /**
     * Elf voice: higher throat, producing a lighter, ethereal quality.
     */
    public static final Theme ELF = new Theme(72, 64, 110, 160);

    /**
     * Little Robot voice: fast, high mouth and throat, robotic character.
     */
    public static final Theme LITTLE_ROBOT = new Theme(92, 60, 190, 190);

    /**
     * Stuffy Guy voice: slightly slower, nasal-sounding character.
     */
    public static final Theme STUFFY_GUY = new Theme(82, 72, 110, 105);

    /**
     * Little Old Lady voice: slow, low pitch, warm character.
     */
    public static final Theme LITTLE_OLD_LADY = new Theme(82, 32, 145, 145);

    /**
     * Extra-Terrestrial voice: fast, high mouth and throat, alien character.
     */
    public static final Theme EXTRA_TERRESTRIAL = new Theme(100, 64, 150, 200);

    /**
     * Creates a custom theme with validated parameters.
     *
     * @param speed  speaking rate; must be in [1, 255]
     * @param pitch  base voice pitch; must be in [0, 255]
     * @param mouth  mouth formant adjustment; must be in [0, 255]
     * @param throat throat formant adjustment; must be in [0, 255]
     * @throws IllegalArgumentException if any parameter is out of range
     */
    public Theme(int speed, int pitch, int mouth, int throat) {
        this.speed = requireInRange(speed, 1, 255);
        this.pitch = requireInRange(pitch, 0, 255);
        this.mouth = requireInRange(mouth, 0, 255);
        this.throat = requireInRange(throat, 0, 255);
    }
}
