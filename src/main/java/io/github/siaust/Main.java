package io.github.siaust;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;


public class Main {

    public static void main(String[] args) {
        new TextEditor();
    }

}

class TextEditor extends JFrame {
     static JButton buttonSave;
     static JButton buttonLoad;

    private static final String PATH = "./src/main/java/io/github/siaust/";

    public TextEditor() {
        super("Text Editor");
        prepareGUI();
    }

    private void prepareGUI() {
        // JFrame
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

        // JMenuBar and JMenu
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        fileMenu.setName("MenuFile");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);

        // JMenuItems
        JMenuItem loadMenuItem = new JMenuItem("Load");
        loadMenuItem.setName("MenuLoad");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setName("MenuSave");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setName("MenuExit");

        fileMenu.add(loadMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);


        // JTextField
        JTextField textField = new JTextField("test.txt"); // fixme: test.txt temporary field input for testing, remove
        textField.setName("FilenameField");
        textField.setPreferredSize(new Dimension(200, 27)); // todo: fill space from left?
        textField.setBounds(new Rectangle(200, 100));
        textField.setToolTipText("File must be in local directory");
        textField.setVisible(true);

        //JButton SAVE
        buttonSave = new JButton("Save");
        buttonSave.setName("SaveButton");
        buttonSave.setToolTipText("CTRL+S");
        buttonSave.setVisible(true);

        // JButton LOAD
        buttonLoad = new JButton("Load");
        buttonLoad.setName("LoadButton");
        buttonLoad.setToolTipText("CTRL+ENTER");
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

        // *** LISTENERS ***

        // LOAD button listener
        buttonLoad.addActionListener(e -> Load(textField.getText(), textArea));

        // SAVE button listener
        buttonSave.addActionListener(e -> Save(textField.getText(), textArea.getText()));

        // Load menu item listener
        loadMenuItem.addActionListener(e -> Load(textField.getText(), textArea));

        // Save menu item listener
        saveMenuItem.addActionListener(e -> Save(textField.getText(), textArea.getText()));

        // Exit menu item listener
        exitMenuItem.addActionListener(event -> System.exit(0)); // todo: dispose JFrame object instead?

        // Key listeners
        CustomKeyListener customKeyListener = new CustomKeyListener();
        textField.addKeyListener(customKeyListener);
        textArea.addKeyListener(customKeyListener);
    }

    void Load(String fileName, JTextArea textArea) {
        try {
            String content = new String(Files.readAllBytes(Path.of(PATH
                    + fileName)));
            textArea.setText(content);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            textArea.setText("");
        }
    }

    void Save(String fileName, String content) {
        try {
            Files.write(Path.of(PATH + fileName), content.getBytes());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}

class CustomKeyListener implements KeyListener {

    @Override
    public void keyTyped(KeyEvent e) {
//        System.out.println(e.getKeyCode());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S) {
            System.out.println("CTRL+S pressed");
            TextEditor.buttonSave.doClick();
//            TextEditor.Save();
        }
        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
            System.out.println("CTRL+ENTER pressed");
            TextEditor.buttonLoad.doClick();
//            TextEditor.Load();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
//        System.out.println(e.getKeyCode());
    }
}