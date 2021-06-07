package com.github.marcos.tulio.view.fragment;

import javax.swing.JTextArea;
import javax.swing.text.Document;

import com.github.marcos.tulio.model.ICanUpdate;

/**
 *
 * @author Marcos Santos
 */
public class TextAreaForOffset extends JTextArea implements ICanUpdate {

    @Override
    public void update(Document document, int offset) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
