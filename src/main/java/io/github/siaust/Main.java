package io.github.siaust;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        // Starts the program on the Swing Initial Thread
        SwingUtilities.invokeLater(EditorGUI::new);
    }

}
