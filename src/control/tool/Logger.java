package control.tool;

import javax.swing.JOptionPane;

/**
 *
 * @author MrCapybara
 */
public class Logger {

    public static void showError(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
