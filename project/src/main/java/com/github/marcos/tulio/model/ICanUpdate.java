package com.github.marcos.tulio.model;

import javax.swing.text.Document;

/**
 * Classes que herdam esta interface podem ser atualizadas usando a função
 * update;
 *
 * @author Marcos Santos
 */
public interface ICanUpdate {

    void update(Document document, int offset);
}
