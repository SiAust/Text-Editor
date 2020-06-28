package io.github.siaust;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

class EditorGUI extends JFrame {

    static JButton saveButton;
    static JButton loadButton;
    static JButton searchButton;
    static JButton nextButton;
    static JButton previousButton;

    private final JFileChooser jFileChooser = new JFileChooser(SRC_PATH);
    private static JTextArea jTextArea;

    private static final String SRC_PATH = "./src/main/java/io/github/siaust/";
    private String filePath;
    private static Deque<int[]> searchResults = new ArrayDeque<>();

    private JCheckBox checkBox = new JCheckBox("Use regex");
    private boolean regex = false;


    public EditorGUI() {
        super("Text Editor");
        prepareGUI();
    }

    private void prepareGUI() {
        // JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
        setLocation(1500, 300); // where the JFrame appears on the desktop
        setSize(600, 400);
        setMinimumSize(new Dimension(600, 400));

        // Setting the filename in the centre of the title bar.


        //JPanel top level container
        JPanel topContainer = new JPanel();
        BorderLayout topBorderLayout = new BorderLayout();
        topContainer.setBorder(new EmptyBorder(20, 20, 20, 20));
        topBorderLayout.setVgap(10);
        topContainer.setLayout(topBorderLayout);
        topContainer.setSize(200, 200);

        // JLabel for about info?
        JLabel aboutLabel = new JLabel("Images used under free license from IconFinder.com");
        aboutLabel.setSize(new Dimension(200, 100));
        aboutLabel.setBorder(new BevelBorder(BevelBorder.RAISED));
        aboutLabel.setVisible(false);

        // JMenuBar and JMenu
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        fileMenu.setName("MenuFile");
        fileMenu.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenu searchMenu = new JMenu("Search");
        searchMenu.setName("MenuSearch");
        searchMenu.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        searchMenu.setMnemonic(KeyEvent.VK_S);

        JMenu aboutMenu = new JMenu("About");
        aboutMenu.setMnemonic(KeyEvent.VK_A);
        aboutMenu.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));

        menuBar.add(fileMenu);
        menuBar.add(searchMenu);
        menuBar.add(aboutMenu);

        // JMenuItems File menu
        JMenuItem loadMenuItem = new JMenuItem("Open");
        loadMenuItem.setName("MenuOpen");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setName("MenuSave");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setName("MenuExit");

        fileMenu.add(loadMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);

        // JMenuItems Search menu
        JMenuItem startSearchItem = new JMenuItem("Start search");
        startSearchItem.setName("MenuStartSearch");
        JMenuItem previousItem = new JMenuItem("Previous search");
        previousItem.setName("MenuPreviousMatch");
        JMenuItem nextItem = new JMenuItem("Next match");
        nextItem.setName("MenuNextMatch");
        JMenuItem regexItem = new JMenuItem("Use regular expressions");
        regexItem.setName("MenuUseRegExp");

        searchMenu.add(startSearchItem);
        searchMenu.add(previousItem);
        searchMenu.add(nextItem);
        searchMenu.add(regexItem);

        // JMenuItems About menu
        JMenuItem aboutItem = new JMenuItem("About");

        aboutMenu.add(aboutItem);


        // *** NORTH ITEMS ***

        // JTextField
        JTextField textField = new JTextField("\\d{3}"); // fixme: test.txt temporary field input for testing, remove
        textField.setName("FilenameField");
        textField.setPreferredSize(new Dimension(200, 35));
        textField.setToolTipText("Enter a word or regexp");

        //JButton SAVE
        saveButton = new JButton(
                new ImageIcon(this.getClass().getResource("/save.png")));
        saveButton.setName("SaveButton");
        saveButton.setPreferredSize(new Dimension(34, 34));
        saveButton.setToolTipText("CTRL+S");

        // JButton LOAD
        loadButton = new JButton(
                new ImageIcon(this.getClass().getResource("/load.png")));
        loadButton.setName("LoadButton");
        loadButton.setPreferredSize(new Dimension(34, 34));
        loadButton.setToolTipText("CTRL+ENTER");

        // JButton Search
        searchButton = new JButton(
                new ImageIcon(this.getClass().getResource("/search.png")));
        searchButton.setName("StartSearchButton");
        searchButton.setPreferredSize(new Dimension(34, 34));
        searchButton.setToolTipText("CTRL+F");

        // JButton Previous
        previousButton = new JButton(
                new ImageIcon(this.getClass().getResource("/previous.png")));
        previousButton.setPreferredSize(new Dimension(34, 34));
        previousButton.setToolTipText("CTRL+?"); //todo: control + left arrow

        // JButton Next
        nextButton = new JButton(); // todo: Change the others!?
        nextButton.setIcon(new ImageIcon(this.getClass().getResource("/forward.png")));
        nextButton.setPreferredSize(new Dimension(34, 34));
        nextButton.setToolTipText("CTRL+?"); // todo: control + right arrow

        // JCheckbox
        checkBox = new JCheckBox("Use regex");
        checkBox.setName("UseRegExCheckbox");
        checkBox.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));

        java.util.List<? extends JComponent> buttons = List.of(saveButton, loadButton, textField, searchButton,
                previousButton, nextButton, checkBox);

        // JTextArea
        jTextArea = new JTextArea();
        jTextArea.setName("TextArea");

        // JScrollPane
        JScrollPane jScrollPane = new JScrollPane(jTextArea);
        jScrollPane.setName("ScrollPane");
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // JPanel NORTH
        JPanel northPanel = new JPanel();
        northPanel.setName("northPanel");
        northPanel.setLayout(new FlowLayout(FlowLayout.LEADING));

        for (JComponent item : buttons) {
            northPanel.add(item);
        }

        topContainer.add(northPanel, BorderLayout.NORTH);
        topContainer.add(jScrollPane, BorderLayout.CENTER);

        // Adding components to JFrame
        jFileChooser.setVisible(false);
        add(jFileChooser);
        add(topContainer, BorderLayout.CENTER);
        setVisible(true);

        // *** LISTENERS ***

        // Button load listener
        loadButton.addActionListener(e -> showFChooserLoad(jTextArea));

        // Button save listener
        saveButton.addActionListener(e -> showFChooserSave(jTextArea.getText()));

        // Button search listener
        searchButton.addActionListener(e -> new SearchWorker(jTextArea.getText(),
                textField.getText(), regex).execute());

        // Button next listener
        nextButton.addActionListener(e -> searchNextItem());

        // Button previous listener
        previousButton.addActionListener(e -> searchPreviousItem());

        // Checkbox listener
        checkBox.addItemListener(e -> {
            if (e.getStateChange() == 1) {
                regex = true;
            } else {
                regex = false;
            }
        });

        // Menu load item listener
        loadMenuItem.addActionListener(e -> showFChooserLoad(jTextArea));

        // Menu save item listener
        saveMenuItem.addActionListener(e -> showFChooserSave(jTextArea.getText()));

        // Menu exit item listener
        exitMenuItem.addActionListener(event -> System.exit(0)); // todo: dispose JFrame object instead?

        // Menu search item listener
        startSearchItem.addActionListener(e -> new SearchWorker(jTextArea.getText(),
                textField.getText(), regex).execute());

        // Menu next item listener
        nextItem.addActionListener(e -> searchNextItem());

        // Menu previous item listener
        previousItem.addActionListener(e -> searchPreviousItem());

        // Menu regular expressions checkbox listener
        regexItem.addActionListener(e -> {
            if (checkBox.isSelected()) {
                checkBox.setSelected(false);
                regex = false;
            } else {
                checkBox.setSelected(true);
                regex = true;
            }
        });

        // Menu about item listener
        aboutItem.addActionListener(event -> showAboutDialog());

        // Key listeners
        CustomKeyListener customKeyListener = new CustomKeyListener();
        textField.addKeyListener(customKeyListener);
        jTextArea.addKeyListener(customKeyListener);
    }

    void showFChooserLoad(JTextArea textArea) {
        jFileChooser.setVisible(true);
        jFileChooser.setDialogTitle("Select a .txt file");
        jFileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT files", "txt");
        jFileChooser.addChoosableFileFilter(filter);

        // Opens the JFileChooser dialog and receives the result of user action
        int result = jFileChooser.showOpenDialog(null);

        // Check the result code, if it is 1 user selected a .txt file. Open the .txt file and show content.
        if (result == JFileChooser.APPROVE_OPTION) {
            filePath = jFileChooser.getSelectedFile().getAbsolutePath();
            try {
                String content = new String(Files.readAllBytes(Path.of(filePath)));
                textArea.setText(content);
            } catch (IOException ioException) {
                ioException.printStackTrace();
                textArea.setText("");
            }
        }
    }

    void showFChooserSave(String content) {
        if (filePath == null) {
            jFileChooser.setDialogTitle("Save file");

            int result = jFileChooser.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                filePath = jFileChooser.getSelectedFile().getAbsolutePath() + ".txt";
                try {
                    Files.write(Path.of(filePath), content.getBytes());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        } else {
            try {
                Files.write(Path.of(filePath), content.getBytes());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    void showAboutDialog() {
        // To keep consistent look, attributes are copied from JLabel and added to the HTML style tag
        JLabel label = new JLabel();
        Font font = label.getFont();

        // CSS string
        StringBuffer style = new StringBuffer("font-family:" + font.getFamily() + ";");
        style.append("font-weight:" + (font.isBold() ? "bold" : "normal") + ";");
        style.append("font-size:" + font.getSize() + "pt;");

        // JEditorPane to create a clickable link using HTML tags
        JEditorPane ep = new JEditorPane("text/html", "<html><body style=\"" + style + "\">"
                + "Author: <a href=\"https://www.github.com/siaust\">Simon Aust</a> (2020)<br><hr>"
                + "Icons provided under the CC BY-NC 3.0 NL License.<br>"
                + "<a href=\"https://creativecommons.org/licenses/by-nc/3.0/nl/deed.en_GB\">Creative Commons" +
                " CC BY-NC 3.0 NL</a><br>"
                + "Icons author: <a href=\"http://tango.freedesktop.org/Tango_Icon_Library\">Tango</a><br>"
                + "Icons available at <a href=\"https://www.iconfinder.com/iconsets/tango-icon-library\">www.iconfinder.com</a>"
                + "</body></html>");

        // Listener to handle the link click
        ep.addHyperlinkListener(e -> {
            if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
                try {
                    Desktop.getDesktop().browse(URI.create(e.getURL().toString())); // roll your own link launcher or use Desktop if J6+
                    // todo: Close on click?
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        ep.setEditable(false);
        ep.setBackground(label.getBackground());

        // Display the dialog
        JOptionPane.showMessageDialog(null, ep, "About", JOptionPane.PLAIN_MESSAGE);

    }

    public static void updateFocus(Deque<int[]> resultsList) {
        searchResults = resultsList;

        jTextArea.grabFocus();

        if (searchResults.size() > 0) {
            // todo: May need to remove this for the tests.
            nextButton.setEnabled(true);
            nextButton.setToolTipText("CTRL+PERIOD");
            previousButton.setEnabled(true);
            previousButton.setToolTipText("CTRL+COMMA");

            int[] initialResult = searchResults.poll();
            searchResults.offerLast(initialResult);
            jTextArea.select(initialResult[0], initialResult[1]);
            jTextArea.moveCaretPosition(initialResult[1]);

            searchResults.forEach(o -> System.out.print(Arrays.toString(o) + " "));
            System.out.println();
        } else { // fixme: selecting text from previous search despite searchResults == 0 (fixed?)
            jTextArea.select(jTextArea.getSelectionEnd(), jTextArea.getSelectionEnd());

            // todo: May need to remove this for the tests.
            nextButton.setEnabled(false);
            nextButton.setToolTipText("No next item");
            previousButton.setEnabled(false);
            previousButton.setToolTipText("No previous item");
        }
    }

    private void searchNextItem() {
        jTextArea.grabFocus();
        if (searchResults.size() != 0) {
            int[] nextResult = new int[2];
            if (Arrays.equals((searchResults.peek()),
                    new int[]{jTextArea.getSelectionStart(), jTextArea.getSelectionEnd()})) {
                for (int i = 0; i < 2; i++) {
                    nextResult = searchResults.poll();
                    searchResults.offerLast(nextResult);
                }
            } else {
                nextResult = searchResults.poll();
                searchResults.offerLast(nextResult);
            }
            jTextArea.select(nextResult[0], nextResult[1]);
            jTextArea.moveCaretPosition(nextResult[1]);

            searchResults.forEach(o -> System.out.print(Arrays.toString(o) + " "));
            System.out.println();
        }
    }

    private void searchPreviousItem() {
        jTextArea.grabFocus();
        if (searchResults.size() != 0) {
            int[] previousResult = new int[2];
            if (Arrays.equals((searchResults.peekLast()),
                    new int[]{jTextArea.getSelectionStart(), jTextArea.getSelectionEnd()})) {
                for (int i = 0; i < 2; i++) {
                    previousResult = searchResults.pollLast();
                    searchResults.offerFirst(previousResult);
                }
            } else {
                previousResult = searchResults.pollLast();
                searchResults.offerFirst(previousResult);
            }
            jTextArea.select(previousResult[0], previousResult[1]);
            jTextArea.moveCaretPosition(previousResult[1]);

            searchResults.forEach(o -> System.out.print(Arrays.toString(o) + " "));
            System.out.println();
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
            EditorGUI.saveButton.doClick();
//            TextEditor.Save();
        }
        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
            System.out.println("CTRL+ENTER pressed");
            EditorGUI.loadButton.doClick();
//            TextEditor.Load();
        }
        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_F) {
            EditorGUI.searchButton.doClick();
//            System.out.println("CTRL+F pressed");
        }
        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_PERIOD) {
            EditorGUI.nextButton.doClick();
//            System.out.println("CTRL+RIGHT pressed");
        }
        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_COMMA) {
            EditorGUI.previousButton.doClick();
//            System.out.println("CTRL+LEFT pressed");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
//        System.out.println(e.getKeyCode());
    }
}