package view;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.plaf.basic.BasicProgressBarUI;
import net.miginfocom.swing.MigLayout;
import model.Config;

/**
 *
 * @author MrCapybara
 */
public final class ViewRightPanel extends JPanel {

    private final JProgressBar progressBar = new JProgressBar();
    private Thread tLoading = null;

    public ViewRightPanel() {
        initProperties();
        initComponents();
    }

    private void initProperties() {
        setLayout(new MigLayout());
    }

    private void initComponents() {
        progressBar.setStringPainted(true);
        progressBar.setString(Config.TITLE);
        progressBar.setForeground(Config.LIME_GREEN);
        progressBar.setBackground(Config.MOSTLY_WHITE);
        progressBar.setBorder(null);
        progressBar.setFocusable(false);
        //progressBar.setFont(Config.FONT_12_BOLD);
        
        progressBar.setUI(new BasicProgressBarUI() {
            @Override
            protected Color getSelectionBackground() {
                return Color.DARK_GRAY;
            }

            @Override
            protected Color getSelectionForeground() {
                return Config.MOSTLY_WHITE;
            }
        });

        add(progressBar, "w 100%, bottom, pushy");
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public void initProgress() {
        if (tLoading == null || !tLoading.isAlive()) {
            tLoading = new Thread(() -> {
                while (progressBar.getValue() != progressBar.getMaximum()) {
                    try {
                        Thread.sleep(750);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ViewRightPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                try {
                    Thread.sleep(2000);
                    progressBar.setValue(0);
                    progressBar.setString(Config.TITLE);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ViewRightPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            tLoading.start();
        }
    }

    public void initProgress(String txt) {
        initProgress();
        progressBar.setString(txt);
    }

    public void endProgress() {
        if (tLoading != null && tLoading.isAlive())
            progressBar.setValue(progressBar.getMaximum());
    }

    public void endProgress(String txt) {
        endProgress();
        progressBar.setString(txt);
    }
}
