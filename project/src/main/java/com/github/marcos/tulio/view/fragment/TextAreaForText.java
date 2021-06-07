package com.github.marcos.tulio.view.fragment;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.github.marcos.tulio.model.ICanUpdate;

/**
 *
 * @author Marcos Santos
 */
public class TextAreaForText extends JTextArea implements ICanUpdate {

    @Override
    public void update(Document document, int offset) {
        try {
            String text = document.getText(0, document.getLength());   
            //System.out.println(text);


        } catch (BadLocationException ex) {
            Logger.getLogger(TextAreaForText.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
