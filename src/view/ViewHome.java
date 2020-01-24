package view;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import model.Config;

/**
 *
 * @author MrCapybara
 */
public class ViewHome extends JFrame {

    private final Dimension SIZE = new Dimension(1024, 600);
    protected final JMenuItem fExit = new JMenuItem("Exit");
    protected final JMenuItem fNewProject = new JMenuItem("New Project...");
    protected JTree treeOriginal = new ViewTree();
    protected JTree treeProject = new ViewTree();
    protected ViewRightPanel rightPanel = new ViewRightPanel();

    public ViewHome() {
        initProperties();
        initComponents();
    }

    private void initProperties() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(SIZE);
        setLocationRelativeTo(null);
        setBackground(new Color(179, 179, 179));
        setTitle(Config.TITLE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //setLayout(new MigLayout());
    }

    private void initComponents() {
        setJMenuBar(menuBar());

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.add(leftPanel());
        splitPane.add(rightPanel);
        splitPane.setDividerLocation(300);
        splitPane.setDividerSize(3);

        add(splitPane);
    }

    private JTabbedPane leftPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        
        JScrollPane scrollOriginal = new JScrollPane(treeOriginal);
        scrollOriginal.setBorder(BorderFactory.createEmptyBorder());
        
        JScrollPane scrollProject = new JScrollPane(treeProject);
        scrollProject.setBorder(BorderFactory.createEmptyBorder());
        
        tabbedPane.addTab("Original", scrollOriginal);
        tabbedPane.addTab("Project", scrollProject);

        return tabbedPane;
    }

    private JMenuBar menuBar() {
        JMenu mFile = new JMenu("File");
        mFile.add(fNewProject);
        mFile.add(new JSeparator());
        mFile.add(fExit);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);
        //menuBar.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(245, 245, 245)));
        menuBar.add(mFile);
        return menuBar;
    }
}
