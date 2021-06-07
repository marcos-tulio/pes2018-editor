package com.github.marcos.tulio.view.tool;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.github.marcos.tulio.model.Config;
import com.github.marcos.tulio.view.fragment.TextAreaForOffset;
import com.github.marcos.tulio.view.fragment.TextAreaForText;

import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Marcos Santos
 */
public class ViewHexEditor extends JScrollPane {

    protected JTextArea hexArea;
    protected TextAreaForText textArea;
    protected TextAreaForOffset offsetArea;
    protected JLabel lblHex = lblTitle("Hex");

    public ViewHexEditor() {
        initComponents();
    }

    private JLabel lblTitle(String txt) {
        JLabel lbl = new JLabel(txt);
        lbl.setForeground(Config.GREEN);
        return lbl;
    }

    private void initComponents() {
        offsetArea = new TextAreaForOffset();
        offsetArea.setForeground(Config.GREEN);
        offsetArea.setFont(offsetArea.getFont().deriveFont(Font.BOLD));
        offsetArea.setLineWrap(true);
        offsetArea.setWrapStyleWord(true);
        offsetArea.setFocusable(false);
        offsetArea.setEditable(false);

        hexArea = new JTextArea();
        hexArea.setLineWrap(true);
        hexArea.setWrapStyleWord(true);

        textArea = new TextAreaForText();
        textArea.setBackground(Config.MOSTLY_WHITE);
        textArea.setForeground(Color.BLACK);

        final JPanel panel = new JPanel(new MigLayout());
        panel.setBackground(Config.MOSTLY_WHITE);

        panel.add(lblTitle("Offset"), "cell 0 0, ");
        panel.add(lblHex, "cell 1 0, wmin 1px");
        //panel.add(lblTitle("Text"), "cell 2 0, ");
        panel.add(offsetArea, "cell 0 1, wmin 1px, gap 10 20");
        panel.add(hexArea, "cell 1 1, wmin 1px, gap 00 20");
        //panel.add(textArea, "cell 2 1");

        setViewportView(panel);
    }
}
