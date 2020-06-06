package io.github.siaust;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;


public class Main {

    public static void main(String[] args) {
        new TextEditor();
    }

}

class TextEditor extends JFrame {

    public TextEditor() {
        // JFrame
        super("Text Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
        setLocation(1500,300); // where the JFrame appears on the desktop
        setSize(600, 600);

        //JPanel top level container
        JPanel topContainer = new JPanel();
        BorderLayout topBorderLayout = new BorderLayout();
        topContainer.setBorder(new EmptyBorder(20, 20, 20, 20));
        topBorderLayout.setVgap(10);
        topContainer.setLayout(topBorderLayout);
        topContainer.setSize(200, 200);

        // JTextField
        JTextField textField = new JTextField("test.txt"); // todo: test.txt temporary field input for testing, remove
        textField.setName("FilenameField");
        textField.setPreferredSize(new Dimension(200, 27)); // todo: fill space from left?
        textField.setBounds(new Rectangle(200, 100));
        textField.setToolTipText("File must be in local directory");
        textField.setVisible(true);

        //JButton SAVE
        JButton buttonSave = new JButton("Save");
        buttonSave.setName("SaveButton");
        buttonSave.setVisible(true);

        // JButton LOAD
        JButton buttonLoad = new JButton("Load");
        buttonLoad.setName("LoadButton");
        buttonLoad.setVisible(true);

        // JTextArea
        JTextArea textArea = new JTextArea();
        textArea.setName("TextArea");
        textArea.setBounds(20, 20, 300, 300);
        textArea.setVisible(true);

        // JScrollPane
        JScrollPane jScrollPane = new JScrollPane(textArea);
        jScrollPane.setName("ScrollPane");
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // FlowLayout for northPanel
        FlowLayout northFlow = new FlowLayout();
        northFlow.setAlignment(FlowLayout.CENTER);
        northFlow.preferredLayoutSize(topContainer);

        // JPanel NORTH
        JPanel northPanel = new JPanel();
        northPanel.setName("northPanel");
        northPanel.setLayout(northFlow);

        northPanel.add(textField, FlowLayout.LEFT);
        northPanel.add(buttonLoad, FlowLayout.CENTER);
        northPanel.add(buttonSave, FlowLayout.RIGHT);

        topContainer.add(northPanel, BorderLayout.NORTH);
        topContainer.add(jScrollPane, BorderLayout.CENTER);

        // Adding components to JFrame
        add(topContainer, BorderLayout.CENTER);
        setVisible(true);

        // LOAD button listener
        buttonLoad.addActionListener(e -> {
            try {
                String content = new String(Files.readAllBytes(Path.of("./src/main/java/io/github/siaust/"
                                                            + textField.getText())));
                textArea.setText(content);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        });

        // SAVE button listener
        buttonSave.addActionListener(e -> {
            try {
                Files.write(Path.of("./src/main/java/io/github/siaust/"
                                                    + textField.getText()), textArea.getText().getBytes());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

    }
}