package view;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import model.Config;

/**
 *
 * @author MrCapybara
 */
public final class ViewTree extends JTree {

    public ViewTree() {
        setCellRenderer(createTreeRenderer());
        setModel(null);
        setBorder(BorderFactory.createEmptyBorder(3, 3, 0, 0));
        setBackground(Config.MOSTLY_WHITE);
        setForeground(Color.red);
    }

    private DefaultTreeCellRenderer createTreeRenderer() {
        return new DefaultTreeCellRenderer() {

            {
                super.setClosedIcon(Config.ICON_FOLDER);
                super.setOpenIcon(Config.ICON_FOLDER_OPEN);
                super.setLeafIcon(Config.ICON_FILE);
                super.setBackgroundNonSelectionColor(Config.MOSTLY_WHITE);
            }

            @Override
            public Icon getIcon() {
                String name = super.getText();

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

                return super.getIcon(); //To change body of generated methods, choose Tools | Templates.
            }

        };
    }

}
