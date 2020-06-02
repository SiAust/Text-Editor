package io.github.siaust;

import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    public static void main(String[] args) {
        new HelloFrame();
    }


}

class HelloFrame extends JFrame {

    public HelloFrame() {
        // JFrame
        super("Text Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLayout(null);
        setVisible(true);

        // JTextArea
        JTextArea nameTextField = new JTextArea();
        nameTextField.setName("TextArea");
        nameTextField.setBounds(20, 20, 300, 300);
        nameTextField.setVisible(true);
        add(nameTextField);
    }
}