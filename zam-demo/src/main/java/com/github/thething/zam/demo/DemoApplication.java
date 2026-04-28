package com.github.thething.zam.demo;

import com.formdev.flatlaf.FlatLightLaf;
import com.github.thething.zam.reciter.Reciter;
import com.github.thething.zam.reciter.ReciterRuleRegistry;
import com.github.thething.zam.synthesizer.SpeechSynthesizer;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.io.IOException;

public class DemoApplication {

    public static void main(String[] args) {
        FlatLightLaf.installLafInfo();

        ReciterRuleRegistry reciterRuleRegistry;

        try {
            reciterRuleRegistry = ReciterRuleRegistry.load();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to load reciter rules", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Reciter reciter = new Reciter(reciterRuleRegistry);
        SpeechSynthesizer speechSynthesizer = new SpeechSynthesizer(reciter);

        try {
            UIManager.put("ScrollBar.showButtons", true);
            FlatLightLaf flatLightLaf = new FlatLightLaf();
            UIManager.setLookAndFeel(flatLightLaf);
        } catch (UnsupportedLookAndFeelException e) {
            JOptionPane.showMessageDialog(null, "Failed to set Flat Light Look & Feel", "Error", JOptionPane.ERROR_MESSAGE);
        }

        SwingUtilities.invokeLater(() -> {
            try {
                DemoFrame frame = new DemoFrame(speechSynthesizer);
                frame.setVisible(true);
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(null, "Failed to initialize UI", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
