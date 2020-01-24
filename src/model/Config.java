package model;

import java.awt.Color;
import java.awt.Font;
import javax.swing.ImageIcon;
import control.tool.Util;
import java.io.File;

/**
 *
 * @author MrCapybara
 */
public class Config {

    public static String PATH_CPKMAKEC = "E:\\PES_EDIT\\PES_TOOLS\\CRIFILESYSTEM\\cpkmakec.exe";
    public static String PATH_PES      = "E:\\PS3\\GAMES\\PRO EVOLUTION SOCCER 2018 [BLUS31612]";
    public static String PATH_PROJECT       = "E:\\PES_PROJECT";
    public static String PATH_PROJECT_CPKS  = Config.PATH_PROJECT + File.separator + "CPK";
    public static String PATH_PROJECT_JSON  = Config.PATH_PROJECT + File.separator + "JSON";
        
    public final static String TITLE = "PES 2018 Editor by MrCapybara";

    public final static Color MOSTLY_WHITE = new Color(250, 250, 250);
    public final static Color LIME_GREEN = new Color(70, 195, 135);

    public final static Font FONT_12 = new Font(Font.MONOSPACED, Font.PLAIN, 12);
    //public final static Font FONT_12_BOLD = new Font(Font.MONOSPACED, Font.BOLD, 12);
    //public final static Font FONT_11 = new Font(Font.MONOSPACED, Font.PLAIN, 11);

    public final static ImageIcon ICON_ADD_FOLDER = Util.getImageIcon("add-folder", 18, 18);
    public final static ImageIcon ICON_FOLDER = Util.getImageIcon("folder", 18, 18);
    public final static ImageIcon ICON_FOLDER_OPEN = Util.getImageIcon("opened-folder", 17, 17);
    public final static ImageIcon ICON_FILE = Util.getImageIcon("file", 17, 17);
    public final static ImageIcon ICON_IMAGE = Util.getImageIcon("picture", 17, 17);
    public final static ImageIcon ICON_CPK = Util.getImageIcon("box", 17, 17);
    public final static ImageIcon ICON_BIN = Util.getImageIcon("binary", 18, 18);
    public final static ImageIcon ICON_HOME = Util.getImageIcon("home", 18, 18);
    public final static ImageIcon ICON_COMPUTER = Util.getImageIcon("computer", 18, 18);
    public final static ImageIcon ICON_HDD = Util.getImageIcon("hdd", 18, 18);
    public final static ImageIcon ICON_DETAILS = Util.getImageIcon("details", 18, 18);
    public final static ImageIcon ICON_MEDIUM = Util.getImageIcon("medium", 18, 18);
    public final static ImageIcon ICON_UP = Util.getImageIcon("up", 16, 16);
}
