package com.github.marcos.tulio.view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolTip;
import javax.swing.JTree;
import javax.swing.text.DefaultCaret;

import com.github.marcos.tulio.view.fragment.ProgressBar;
import com.github.marcos.tulio.view.fragment.Tree;
import com.github.marcos.tulio.controller.view.fragment.CtrlPanelWorkbench;
import com.github.marcos.tulio.controller.view.tool.CtrlHexEditor;
import com.github.marcos.tulio.model.Config;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Marcos Santos
 */
public class ViewHome extends JFrame {

    private final Dimension SIZE = new Dimension(1024, 600);

    protected final JMenuItem fExit = new JMenuItem("Exit");
    protected final JMenuItem fNewProject = new JMenuItem("New Project...");

    // Painel da esquerda
    protected Tree treeOriginal = new Tree();
    protected Tree treeProject = new Tree();
    protected JTextArea txtInfo = new JTextArea("...");

    protected JPopupMenu popProject = new JPopupMenu();
    protected JMenu      pmiInternal  = new JMenu("Open with internal...");
    protected JMenu      pmiExternal  = new JMenu("Open with external...");

    protected JMenuItem  pmiOpenIHex  = new JMenuItem("Hex editor");
    protected JMenuItem  pmiOpenIText = new JMenuItem("Text editor");

    // Painel da direita
    protected ProgressBar progressBar = new ProgressBar();
    protected CtrlPanelWorkbench panelWorkbench = new CtrlPanelWorkbench();
    
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
        splitPane.add(panelLeft());
        splitPane.add(panelRight());
        splitPane.setDividerLocation(300);
        splitPane.setDividerSize(5);

        add(splitPane);

        pmiInternal.add(pmiOpenIHex);
        pmiInternal.add(pmiOpenIText);

        popProject.add(pmiInternal);
        //popProject.add(pmiExternal);
    }

    private JTabbedPane panelLeft() {
        final JTabbedPane tabbedPane = new JTabbedPane();

        final JScrollPane scrollOriginal = new JScrollPane(treeOriginal);
        scrollOriginal.setBorder(BorderFactory.createEmptyBorder());

        final JScrollPane scrollProject = new JScrollPane(treeProject);
        scrollProject.setBorder(BorderFactory.createEmptyBorder());

        final DefaultCaret caret = (DefaultCaret) txtInfo.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

        txtInfo.setEditable(false);
        txtInfo.setBackground(new Color(238, 238, 238));

        final JPanel panel = new JPanel(new MigLayout("ins 0"));
        panel.add(scrollProject, "pushy, growy, growx, pushx, wrap");
        panel.add(new JScrollPane(txtInfo), "pushx, growx, h 105:105:105");

        tabbedPane.addTab("Original", scrollOriginal);
        tabbedPane.addTab("Project", panel);

        return tabbedPane;
    }

    private JPanel panelRight() {
        final JPanel panel = new JPanel(new MigLayout("ins 0"));

        panel.add(panelWorkbench, "w 100%, h 100%, wrap");
        //panel.add(new CtrlTextEditor("C:\\Users\\MrCapybara\\Desktop\\Nova pasta\\settings.xml"),
        //         "w 100%, h 100%, wrap");
        
        panelWorkbench.addTab("HEX", new CtrlHexEditor());
        panel.add(progressBar, "w 100%, bottom, pushy, gap 5 5 0 5");

        return panel;
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
