package com.github.marcos.tulio.controller.view.tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.marcos.tulio.view.fragment.ProgressBar;
import com.github.marcos.tulio.view.tool.ViewHexEditor;

/**
 *
 * @author Marcos Santos
 */
public final class CtrlHexEditor extends ViewHexEditor {

    private ProgressBar pBar = null;

    private final String OFFSET_PRE = "0x";
    private final String OFFSET_POS = "";
    private final int BUFF_SIZE = 1024;

    private int aByte = 16;
    private int hexSize = aByte * 3;

    public CtrlHexEditor() {
        initComponents();
        initListeners();

        // Título do editor hexArea.
        lblHex.setText("");
        for (int i = 0; i < aByte; i++) {
            lblHex.setText(lblHex.getText() + String.format("%02X ", i));
        }

        hexArea.setColumns(hexSize);
    }

    public CtrlHexEditor(String path, ProgressBar pBar) {
        this();
        this.pBar = pBar;
        openFile(new File(path));
    }

    public CtrlHexEditor(String path) {
        this(path, null);
    }

    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    private void initComponents() {
        // Criar o documento para a area de texto hex.
        HexDocument document = new HexDocument(hexArea);
        document.addComponentToUpdateAtModify(textArea);
    }

    private void initListeners() {}

    public void setBytes(char[] bytes) {
        // Adicionar bytes no editor hexArea.
        //textArea.setText("");
        //int nLine = 1;
        for (char b : bytes) {
            hexArea.append(String.format("%02X", (byte) b));
            /*textArea.append(b + "");

            if (nLine == aByte){
                textArea.append("\n");
                nLine = 0;
            }

            nLine++;*/
        }

        // Adicionar o offsetArea
        int maxOffset = hexArea.getDocument().getLength() / hexSize;
        int strLenght = Integer.toHexString(maxOffset * aByte).length();
        offsetArea.setColumns(strLenght + 1 + OFFSET_PRE.length() + OFFSET_POS.length());

        offsetArea.setText("");
        for (int i = 0; i <= maxOffset; i++) {
            offsetArea.append(OFFSET_PRE);
            offsetArea.append(String.format("%0" + strLenght + "X", (i * aByte)));
            offsetArea.append(OFFSET_POS);
            offsetArea.append("\n");
        }
    }

    private void openFile(File file) {
        if (pBar != null){
            pBar.init("Loading " + file.getName() + "...");
            pBar.setMaximum((int) file.length());
            //pBar.setInfinity(true);
        }

        try (FileReader reader = new FileReader(file)) {
            // Leitura dos bytes
            char[] buff = new char[BUFF_SIZE];
            while (reader.read(buff) != -1){
                setBytes(buff);
                pBar.setValue(pBar.getValue() + BUFF_SIZE);
            }

            // Limpar o buffer
            buff = null;
            if (pBar != null) pBar.end("File loaded sucessful!");

        } catch (FileNotFoundException ex) {
            Logger.getLogger(CtrlHexEditor.class.getName()).log(Level.SEVERE, null, ex);
            if (pBar != null) pBar.end("Error: file not found!");

        } catch (IOException ex) {
            Logger.getLogger(CtrlHexEditor.class.getName()).log(Level.SEVERE, null, ex);
            if (pBar != null) pBar.end("Error: file io!");
        }
    }
}
