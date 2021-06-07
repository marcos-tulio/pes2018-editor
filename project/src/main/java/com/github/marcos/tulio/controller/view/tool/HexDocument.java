package com.github.marcos.tulio.controller.view.tool;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JTextArea;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import com.github.marcos.tulio.model.ICanUpdate;

/**
 *
 * @author Marcos Santos
 */
public final class HexDocument extends PlainDocument {

    private final String SEPARATOR = " ";
    private final String PATTERN = "^[A-F0-9]+$";
    private final JTextArea FIELD;

    private final List<ICanUpdate> LIST_TO_UPDATE = new ArrayList<>();

    /**
     * Variável para verificação de qual tecla foi pressionada: delete ou
     * backspace.
     */
    private boolean isDeleteKey = false;

    /**
     * Cria um documento para exibir bytes em formato hex.
     *
     * @param textArea JTextArea que receberá esta formatação.
     */
    public HexDocument(JTextArea textArea) {
        this.FIELD = textArea;
        this.FIELD.setDocument(this);
    }

    /**
     * Adiciona um componente que será atualizado quando alguma modificação
     * ocorrer neste documento.
     *
     * @param component componente que herda a interface ICanUpdate
     */
    public void addComponentToUpdateAtModify(ICanUpdate component) {
        if (!LIST_TO_UPDATE.contains(component))
            LIST_TO_UPDATE.add(component);
    }

    /**
     * Remove um componente que é atualizado quando alguma modificação ocorrer
     * neste documento.
     *
     * @param component componente que herda a interface ICanUpdate
     */
    public void removeComponentToUpdateAtModify(ICanUpdate component) {
        LIST_TO_UPDATE.remove(component);
    }

    @Override
    public void remove(int offs, int len) throws BadLocationException {

        // Verificar se tem algum texto selecionado
        if (FIELD.getSelectedText() != null && !FIELD.getSelectedText().isEmpty()) {
            String selection = FIELD.getSelectedText().replaceAll(SEPARATOR, "");
            if (selection.isEmpty() || selection.length() == 1)
                return;
            else
                super.remove(offs, len);
        }

        // Verificar se é o backspace ou o delete
        isDeleteKey = (FIELD.getCaretPosition() == offs);

        // Primeiro byte
        if (((offs == 1 || offs == 2) && !isDeleteKey) || (offs == 0 && isDeleteKey)) {
            // Caso ainda tenha texto após o primeiro byte...
            if (getLength() > 3)
                super.remove(0, 3);
            // Caso só resta o primeiro byte...
            else
                super.remove(0, getLength());

            updateComponents(offs);
            return;
        }

        // backspace pressionado?
        if (!isDeleteKey) {

            // Remove quando não há espaço anteriormente -> 00 11| 22 (piper = cursor)
            if (getText(offs - 2, 1).equals(SEPARATOR) || getText(offs - 1, 1).equals(SEPARATOR)) {

                // Remove quando há um espaço posteriormente -> 00 11 22| 33
                if (getText(offs + 1, 1).equals(SEPARATOR)) {
                    super.remove(offs - 1, 3);
                    FIELD.setCaretPosition(offs - 2);

                } else {  // Remove quando não há mais caracteres posteriormente -> 00 11 22|
                    // 1 nibble apenas -> 00 11 2|
                    if (getText(offs - 1, 1).equals(SEPARATOR))
                        super.remove(offs - 1, 2);

                    // 2 nibbles -> 00 11 22|
                    else
                        super.remove(offs - 2, 3);
                }

            } else if (getText(offs, 1).equals(SEPARATOR))
                // Remove quando há um espaço anteriormente -> 00 11 |22 (piper = cursor)
                super.remove(offs - 3, 3);

        } else {
            // Cursor no final do byte e usando delete para apagar -> 00 11| 22 (piper = cursor)
            if (getText(offs, 1).equals(SEPARATOR))
                super.remove(offs + 1, 3);

            // Cursor no final do byte e usando delete para apagar -> 00 11 |22 (piper = cursor)
            else if (getText(offs - 1, 1).equals(SEPARATOR))
                super.remove(offs, 3);
        }

        updateComponents(offs);

        //System.out.println("CURSOR " + hex.getCaretPosition() + " - OFFSET: " + offs + " - TAMANHO: " + len + " - VALOR: " + getText(offs, len));
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        str = str.toUpperCase().replaceAll("[ ]", "");

        // Apenas caracteres permitidos?
        if (Pattern.matches(PATTERN, str)) {

            while (str.length() > 0) {
                offs = this.insertOneCaracter(offs, str.substring(0, 1), a);    //Atualiza o offset pois espaços alteram-o 
                str = str.substring(1);
            }

            updateComponents(offs);
        }
    }

    /**
     * Insere um caracter no documento e formata-o.
     */
    private int insertOneCaracter(int offs, String str, AttributeSet a) throws BadLocationException {
        super.insertString(offs, str, a);

        // Adicionando o segundo digito?
        if (offs >= 2 && !getText(offs - 2, 1).equals(SEPARATOR) && !getText(offs - 1, 1).equals(SEPARATOR)) {

            // Adiciona o separador e aumenta o offset
            super.insertString(offs++, SEPARATOR, a);

            // Ao adicionar o separador criou novo byte? -> AA| BB CC
            if (offs + 2 < getLength() && getText(offs + 1, 1).equals(SEPARATOR))
                super.remove(offs + 1, 2);

        } else if (offs + 1 < getLength() && !getText(offs, 1).equals(SEPARATOR) && !getText(offs + 1, 1).equals(SEPARATOR)) {
            // Substitui o caracter. 00 |11 22 ou 00 1|1 22
            super.remove(offs + 1, 1);
        }

        // Retorna offset + 1 pois adicionou um novo caracter
        return ++offs;
    }

    private void updateComponents(int offset) {
        LIST_TO_UPDATE.forEach((component) -> {
            component.update(this, offset);
        });
    }
}
