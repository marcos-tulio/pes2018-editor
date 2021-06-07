package com.github.marcos.tulio.view.tool;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.github.marcos.tulio.model.Config;

import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Marcos Santos
 */
public class ViewTextEditor extends JPanel {

    protected JTextArea text;

    public ViewTextEditor() {
        initProperties();
        initComponents();
    }

    private void initProperties() {
        setLayout(new MigLayout("ins 0"));
        setBackground(Color.DARK_GRAY);
    }

    private void initComponents() {
        text = new JTextArea();
        text.setBackground(Config.MOSTLY_WHITE);
        text.setForeground(Color.BLACK);
        
        add(new JScrollPane(text), "width 100%, height 100%");
    }

}
