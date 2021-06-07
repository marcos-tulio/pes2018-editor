package com.github.marcos.tulio.model;

/**
 *
 * @author Marcos Santos
 */
public final class BeanTreeItem {

    private String title;
    private String id;

    public BeanTreeItem(String id, String title) {
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
