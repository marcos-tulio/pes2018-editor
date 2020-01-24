package view;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author MrCapybara
 */
public final class ViewLeftPanel extends JTabbedPane {

    public ViewLeftPanel() {
        initProperties();
        initComponents();
    }

    private void initProperties() {
        //setLayout(new MigLayout());
    }

    private void initComponents() {
        addTab("Original", new JPanel());
        addTab("Project", new JPanel());
    }
}
