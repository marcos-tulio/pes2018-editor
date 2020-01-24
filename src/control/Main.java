package control;

import control.view.CtrlHome;
import java.awt.Font;
import java.util.Map;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import model.Config;

/**
 *
 * @author MrCapybara
 */
public class Main {

    public static void main(String[] arg) {

        // Inicio da configuração do tema
        UIDefaults defaults = UIManager.getDefaults();
        String key;
        Font font;

        for (Map.Entry<Object, Object> entry : defaults.entrySet()) {
            key = entry.getKey().toString().toLowerCase();

            if (key.contains("selectionforeground"))
                defaults.put(entry.getKey(), Config.MOSTLY_WHITE);

            else if (key.contains("selectionbackground"))
                defaults.put(entry.getKey(), Config.LIME_GREEN);

            else if (key.contains("font")) {
                font = defaults.getFont(entry.getKey());
                defaults.put(entry.getKey(), Config.FONT_12.deriveFont(font.getStyle(), font.getSize()));
            }
        }

        UIManager.put("FileView.fileIcon", Config.ICON_FILE);
        UIManager.put("FileView.directoryIcon", Config.ICON_FOLDER);
        UIManager.put("FileChooser.newFolderIcon", Config.ICON_ADD_FOLDER);
        UIManager.put("FileChooser.homeFolderIcon", Config.ICON_HOME);
        UIManager.put("FileChooser.listViewIcon", Config.ICON_MEDIUM);
        UIManager.put("FileChooser.detailsViewIcon", Config.ICON_DETAILS);
        UIManager.put("FileView.hardDriveIcon", Config.ICON_HDD);
        UIManager.put("FileView.computerIcon", Config.ICON_COMPUTER);
        UIManager.put("FileChooser.upFolderIcon", Config.ICON_UP);
        // Fim tema

        new CtrlHome().setVisible(true);
    }
}
