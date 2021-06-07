package com.github.marcos.tulio.model;

import java.util.Collections;
import java.util.Comparator;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import com.github.marcos.tulio.controller.util.Util;

/**
 *
 * @author MrCapybara
 */
public final class SortedTreeNode extends DefaultMutableTreeNode {

    public SortedTreeNode(String id, String path) {
        super(path + Config.ID_FLAG + id, true);
    }

    public SortedTreeNode(Object userObject) {
        super(userObject, true);
    }

    public String getTitle() {
        return Util.getTextPreFlag(super.userObject.toString(), Config.ID_FLAG);
    }

    public String getId() {
        return Util.getTextPostFlag(super.userObject.toString(), Config.ID_FLAG);
    }

    @Override
    public void insert(MutableTreeNode newChild, int childIndex) {
        super.insert(newChild, childIndex);
        Collections.sort(this.children, nodeComparator);
    }

    protected Comparator nodeComparator = new Comparator() {
        @Override
        public int compare(Object o1, Object o2) {
             if (o1.toString().contains(".") && !o2.toString().contains("."))
                return 1;

            if (!o1.toString().contains(".") && o2.toString().contains("."))
                return -1;
            
            return o1.toString().compareToIgnoreCase(o2.toString());
        }

        @Override
        @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
        public boolean equals(Object obj) {
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            return hash;
        }
    };
}
