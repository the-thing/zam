package com.github.thething.zam.demo;

import com.github.thething.zam.generator.AudioGenerator;
import com.github.thething.zam.renderer.PhonemeRenderer;
import com.github.thething.zam.synthesizer.SpeechSynthesizer;
import com.github.thething.zam.synthesizer.Theme;
import net.miginfocom.swing.MigLayout;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileFilter;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;

import static java.util.Objects.requireNonNull;

public final class DemoFrame extends JFrame {

    private final SpeechSynthesizer speechSynthesizer;
    private final JFileChooser fileChooser;
    private final Clip clip;

    private JTextArea plainTextArea;
    private JSpinner speedSpinner;
    private JSlider speedSlider;
    private JSpinner pitchSpinner;
    private JSlider pitchSlider;
    private JSpinner mouthSpinner;
    private JSlider mouthSlider;
    private JSpinner throatSpinner;
    private JSlider throatSlider;
    private JButton stopButton;

    private VersionLineListener lineListener;
    private int clipVersion;
    private int speed;
    private int pitch;
    private int mouth;
    private int throat;

    public DemoFrame(SpeechSynthesizer speechSynthesizer) {
        this.speechSynthesizer = requireNonNull(speechSynthesizer);
        this.fileChooser = new JFileChooser();

        try {
            this.clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            throw new RuntimeException("Failed to initialize audio", e);
        }

        this.lineListener = new VersionLineListener(0);
        this.fileChooser.setFileFilter(new WaveformFileFilter());
        this.fileChooser.setAcceptAllFileFilterUsed(false);
        this.fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        this.fileChooser.setMultiSelectionEnabled(false);
        this.fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        this.speed = AudioGenerator.DEFAULT_SPEED;
        this.pitch = PhonemeRenderer.DEFAULT_PITCH;
        this.mouth = PhonemeRenderer.DEFAULT_MOUTH;
        this.throat = PhonemeRenderer.DEFAULT_THROAT;
        initComponents();
    }

    private void initComponents() {
        initMenuBar();
        initContent();
        initFrame();
    }

    private void initMenuBar() {
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(this::actionExit);

        JMenu fileMenu = new JMenu("File");
        fileMenu.add(exitMenuItem);

        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(this::actionAbout);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.add(aboutMenuItem);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void initContent() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new MigLayout("fill,nogrid"));

        plainTextArea = new JTextArea(8, 8);
        plainTextArea.setText("Hello, my name is Zam. What is your name?");
        JScrollPane plainTextScrollPane = new JScrollPane(plainTextArea);
        plainTextScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        plainTextScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        contentPanel.add(plainTextScrollPane, "growx,wrap");

        JButton clearPlainTextButton = new JButton("Clear");
        clearPlainTextButton.addActionListener(this::actionClearTextArea);
        contentPanel.add(clearPlainTextButton, "wrap");

        contentPanel.add(new JSeparator(JSeparator.HORIZONTAL), "growx,wrap");

        JLabel speedLabel = new JLabel("Speed");
        contentPanel.add(speedLabel, "width 80");

        speedSpinner = new JSpinner(new SpinnerNumberModel(speed, 1, 255, 1));
        ((JSpinner.DefaultEditor) speedSpinner.getEditor()).getTextField().setEnabled(false);
        speedSpinner.addChangeListener(this::actionChangeSpinnerSpeed);
        contentPanel.add(speedSpinner, "width 20");

        speedSlider = new JSlider(1, 255, speed);
        speedSlider.addChangeListener(this::actionChangeSpeed);
        contentPanel.add(speedSlider, "growx,wrap");

        JLabel pitchLabel = new JLabel("Pitch");
        contentPanel.add(pitchLabel, "width 80");

        pitchSpinner = new JSpinner(new SpinnerNumberModel(pitch, 0, 255, 1));
        ((JSpinner.DefaultEditor) pitchSpinner.getEditor()).getTextField().setEnabled(false);
        pitchSpinner.addChangeListener(this::actionChangeSpinnerPitch);
        contentPanel.add(pitchSpinner, "width 20");

        pitchSlider = new JSlider(0, 255, pitch);
        pitchSlider.addChangeListener(this::actionChangePitch);
        contentPanel.add(pitchSlider, "growx,wrap");

        JLabel mouthLabel = new JLabel("Mouth");
        contentPanel.add(mouthLabel, "width 80");

        mouthSpinner = new JSpinner(new SpinnerNumberModel(mouth, 0, 255, 1));
        ((JSpinner.DefaultEditor) mouthSpinner.getEditor()).getTextField().setEnabled(false);
        mouthSpinner.addChangeListener(this::actionChangeSpinnerMouth);
        contentPanel.add(mouthSpinner, "width 20");

        mouthSlider = new JSlider(0, 255, mouth);
        mouthSlider.addChangeListener(this::actionChangeMouth);
        contentPanel.add(mouthSlider, "growx,wrap");

        JLabel throatLabel = new JLabel("Throat");
        contentPanel.add(throatLabel, "width 80");

        throatSpinner = new JSpinner(new SpinnerNumberModel(throat, 0, 255, 1));
        ((JSpinner.DefaultEditor) throatSpinner.getEditor()).getTextField().setEnabled(false);
        throatSpinner.addChangeListener(this::actionChangeSpinnerThroat);
        contentPanel.add(throatSpinner, "width 20");

        throatSlider = new JSlider(0, 255, throat);
        throatSlider.addChangeListener(this::actionChangeThroat);
        contentPanel.add(throatSlider, "growx,wrap");

        JButton samButton = new JButton("SAM");
        samButton.addActionListener(this::actionSam);
        contentPanel.add(samButton, "width 120");

        JButton elfButton = new JButton("Elf");
        elfButton.addActionListener(this::actionElf);
        contentPanel.add(elfButton, "width 120");

        JButton littleRobotButton = new JButton("Little Robot");
        littleRobotButton.addActionListener(this::actionLittleRobot);
        contentPanel.add(littleRobotButton, "width 120,wrap");

        JButton stuffyGuyButton = new JButton("Stuffy Guy");
        stuffyGuyButton.addActionListener(this::actionStuffyGuy);
        contentPanel.add(stuffyGuyButton, "width 120");

        JButton littleOldLadyButton = new JButton("Little Old Lady");
        littleOldLadyButton.addActionListener(this::actionLittleOldLady);
        contentPanel.add(littleOldLadyButton, "width 120");

        JButton extraTerrestrialButton = new JButton("Extra Terrestrial");
        extraTerrestrialButton.addActionListener(this::actionExtraTerrestrial);
        contentPanel.add(extraTerrestrialButton, "width 120,wrap");

        contentPanel.add(new JSeparator(JSeparator.HORIZONTAL), "growx,wrap");

        JButton sayButton = new JButton("Say");
        sayButton.addActionListener(this::actionSay);
        contentPanel.add(sayButton, "width 50%,height 40");

        stopButton = new JButton("Stop");
        stopButton.setEnabled(false);
        stopButton.addActionListener(this::actionStop);
        contentPanel.add(stopButton, "width 50%,height 40");

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(this::actionSave);
        contentPanel.add(saveButton, "width 50%,height 40");

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(contentPanel, BorderLayout.CENTER);
    }

    private void initFrame() {
        setTitle("Zam Demo");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        pack();
        setLocationRelativeTo(null);
    }

    private void actionClearTextArea(ActionEvent ignore) {
        plainTextArea.setText("");
    }

    private void actionChangeSpeed(ChangeEvent ignore) {
        speed = speedSlider.getValue();
        speedSpinner.setValue(speed);
    }

    private void actionChangePitch(ChangeEvent ignore) {
        pitch = pitchSlider.getValue();
        pitchSpinner.setValue(pitch);
    }

    private void actionChangeMouth(ChangeEvent ignore) {
        mouth = mouthSlider.getValue();
        mouthSpinner.setValue(mouth);
    }

    private void actionChangeThroat(ChangeEvent ignore) {
        throat = throatSlider.getValue();
        throatSpinner.setValue(throat);
    }

    private void actionChangeSpinnerSpeed(ChangeEvent ignore) {
        speed = (int) speedSpinner.getValue();
        speedSlider.setValue(speed);
    }

    private void actionChangeSpinnerPitch(ChangeEvent ignore) {
        pitch = (int) pitchSpinner.getValue();
        pitchSlider.setValue(pitch);
    }

    private void actionChangeSpinnerMouth(ChangeEvent ignore) {
        mouth = (int) mouthSpinner.getValue();
        mouthSlider.setValue(mouth);
    }

    private void actionChangeSpinnerThroat(ChangeEvent ignore) {
        throat = (int) throatSpinner.getValue();
        throatSlider.setValue(throat);
    }

    private void actionSam(ActionEvent ignore) {
        speedSlider.setValue(Theme.SAM.speed());
        pitchSlider.setValue(Theme.SAM.pitch());
        mouthSlider.setValue(Theme.SAM.mouth());
        throatSlider.setValue(Theme.SAM.throat());
    }

    private void actionElf(ActionEvent ignore) {
        speedSlider.setValue(Theme.ELF.speed());
        pitchSlider.setValue(Theme.ELF.pitch());
        mouthSlider.setValue(Theme.ELF.mouth());
        throatSlider.setValue(Theme.ELF.throat());
    }

    private void actionLittleRobot(ActionEvent ignore) {
        speedSlider.setValue(Theme.LITTLE_ROBOT.speed());
        pitchSlider.setValue(Theme.LITTLE_ROBOT.pitch());
        mouthSlider.setValue(Theme.LITTLE_ROBOT.mouth());
        throatSlider.setValue(Theme.LITTLE_ROBOT.throat());
    }

    private void actionStuffyGuy(ActionEvent ignore) {
        speedSlider.setValue(Theme.STUFFY_GUY.speed());
        pitchSlider.setValue(Theme.STUFFY_GUY.pitch());
        mouthSlider.setValue(Theme.STUFFY_GUY.mouth());
        throatSlider.setValue(Theme.STUFFY_GUY.throat());
    }

    private void actionLittleOldLady(ActionEvent ignore) {
        speedSlider.setValue(Theme.LITTLE_OLD_LADY.speed());
        pitchSlider.setValue(Theme.LITTLE_OLD_LADY.pitch());
        mouthSlider.setValue(Theme.LITTLE_OLD_LADY.mouth());
        throatSlider.setValue(Theme.LITTLE_OLD_LADY.throat());
    }

    private void actionExtraTerrestrial(ActionEvent ignore) {
        speedSlider.setValue(Theme.EXTRA_TERRESTRIAL.speed());
        pitchSlider.setValue(Theme.EXTRA_TERRESTRIAL.pitch());
        mouthSlider.setValue(Theme.EXTRA_TERRESTRIAL.mouth());
        throatSlider.setValue(Theme.EXTRA_TERRESTRIAL.throat());
    }

    private void actionExit(ActionEvent ignore) {
        dispose();
    }

    private void actionAbout(ActionEvent ignore) {
        int startYear = 2026;
        int currentYear = LocalDate.now().getYear();
        String year;

        if (startYear < currentYear) {
            year = startYear + "-" + currentYear;
        } else {
            year = String.valueOf(startYear);
        }

        String message = "Zam Demo\nCopyright © " + year + " the-thing";

        JOptionPane.showMessageDialog(this, message, "About", JOptionPane.INFORMATION_MESSAGE);
    }

    private void actionSay(ActionEvent ignore) {
        clip.stop();
        clip.drain();
        clip.close();
        clip.removeLineListener(lineListener);

        String text = plainTextArea.getText();
        byte[] audio;

        try {
            audio = speechSynthesizer.generateAudio(text, speed, pitch, mouth, throat, false);
        } catch (RuntimeException e) {
            stopButton.setEnabled(false);
            JOptionPane.showMessageDialog(this, "Failed to generate audio: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        clipVersion++;
        lineListener = new VersionLineListener(clipVersion);
        clip.addLineListener(lineListener);

        try {
            clip.open(SpeechSynthesizer.AUDIO_FORMAT, audio, 0, audio.length);
        } catch (LineUnavailableException e) {
            JOptionPane.showMessageDialog(this, "Failed to play audio: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        clip.start();
        stopButton.setEnabled(true);
    }

    private void actionStop(ActionEvent ignore) {
        stopButton.setEnabled(false);
        clip.stop();
        clip.drain();
        clip.close();
    }

    private void actionSave(ActionEvent ignore) {
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String name = selectedFile.getName();

            if (!name.endsWith(".wav") && !name.endsWith(".wave")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".wav");
            }

            saveWaveform(selectedFile);
        } else if (result == JFileChooser.ERROR_OPTION) {
            JOptionPane.showMessageDialog(this, "Error when selecting file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveWaveform(File destination) {
        byte[] audio = speechSynthesizer.generateAudio(plainTextArea.getText(), speed, pitch, mouth, throat, false);

        try (ByteArrayInputStream bais = new ByteArrayInputStream(audio);
             AudioInputStream audioStream = new AudioInputStream(bais, SpeechSynthesizer.AUDIO_FORMAT, audio.length / SpeechSynthesizer.AUDIO_FORMAT.getFrameSize())) {

            AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, destination);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "File not found", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static final class WaveformFileFilter extends FileFilter {

        @Override
        public boolean accept(File pathname) {
            if (pathname.isDirectory()) {
                return true;
            }

            File file = pathname.getAbsoluteFile();
            String name = file.getName();

            return name.endsWith(".wav") || name.endsWith(".wave");
        }

        @Override
        public String getDescription() {
            return "Waveform Audio File (.wav, .wave)";
        }
    }

    private final class VersionLineListener implements LineListener {

        private final int expectedClipVersion;

        private VersionLineListener(int expectedClipVersion) {
            this.expectedClipVersion = expectedClipVersion;
        }

        @Override
        public void update(LineEvent event) {
            if (event.getType() == LineEvent.Type.STOP) {
                // line event is delivered by sound dispatcher thread so we have to disable stop button via AWT thread
                SwingUtilities.invokeLater(() -> {
                    // disable stop button only when the actually played clip is matching
                    // it is possible that a new clip has started playing
                    if (clipVersion == expectedClipVersion) {
                        stopButton.setEnabled(false);
                    }
                });
            }
        }
    }
}
