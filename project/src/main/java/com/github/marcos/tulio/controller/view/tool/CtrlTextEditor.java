package com.github.marcos.tulio.controller.view.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.marcos.tulio.view.tool.ViewTextEditor;

/**
 *
 * @author Marcos Santos
 */
public final class CtrlTextEditor extends ViewTextEditor {

    public CtrlTextEditor() {

    }

    public CtrlTextEditor(String path) {
        openFile(new File(path));
    }

    private void openFile(File file) {
        StringBuilder content = new StringBuilder();
        super.text.setEditable(false);

        try (FileReader reader = new FileReader(file); BufferedReader buffReader = new BufferedReader(reader)) {
            String line;
            while ((line = buffReader.readLine()) != null) {
                content.append(line);
                content.append("\n");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CtrlTextEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CtrlTextEditor.class.getName()).log(Level.SEVERE, null, ex);
        }

        super.text.setText(content.toString());
        super.text.setEditable(true);
    }
}
