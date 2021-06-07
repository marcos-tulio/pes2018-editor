package com.github.marcos.tulio.view.fragment;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JTree;

import java.awt.event.MouseEvent;

import com.github.marcos.tulio.model.Config;

/**
 *
 * @author MrCapybara
 */
public final class Tree extends JTree {

    public Tree() {
        setCellRenderer(new TreeCellRenderer());
        setModel(null);
        setBorder(BorderFactory.createEmptyBorder(3, 3, 0, 0));
        setBackground(Config.MOSTLY_WHITE);
        setForeground(Color.red);
    }

    public void selectItemByMouse(MouseEvent e){
        clearSelection();
        setSelectionRow(getRowForLocation(e.getX(), e.getY()));
    }

    public String getNameSelectedItem(){
        if (getSelectionPath() == null) return "";
        return getSelectionPath().getLastPathComponent().toString();
    }
}
