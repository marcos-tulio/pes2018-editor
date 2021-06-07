package com.github.marcos.tulio.view.fragment;

import javax.swing.Icon;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.github.marcos.tulio.controller.util.Util;
import com.github.marcos.tulio.model.Config;

/**
 *
 * @author Marcos Santos
 */
public final class TreeCellRenderer extends DefaultTreeCellRenderer {

    public TreeCellRenderer() {
        super.setClosedIcon(Config.ICON_FOLDER);
        super.setOpenIcon(Config.ICON_FOLDER_OPEN);
        super.setLeafIcon(Config.ICON_FILE);
        super.setBackgroundNonSelectionColor(Config.MOSTLY_WHITE);
    }

    @Override
    public Icon getIcon() {
        String name = getText();

        if (name != null && !name.isEmpty()) {
            name = name.toLowerCase();

            if (name.endsWith(".png"))
                return Config.ICON_IMAGE;

            if (name.endsWith(".cpk"))
                return Config.ICON_CPK;

            if (name.endsWith(".bin"))
                return Config.ICON_BIN;

            if (!name.contains(".") && super.getIcon() == Config.ICON_FILE)
                return Config.ICON_FOLDER;
        }

        return super.getIcon();
    }

    @Override
    public String getText() {
        return Util.getTextPreFlag(super.getText(), Config.ID_FLAG);
    }
}
