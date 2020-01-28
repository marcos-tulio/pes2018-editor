package control.view;

import control.Project;
import control.tool.Logger;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultTreeModel;
import control.tool.Util;
import javax.swing.JProgressBar;
import model.Config;
import view.ViewHome;

/**
 *
 * @author MrCapybara
 */
public final class CtrlHome extends ViewHome {

    public CtrlHome() {
        initListeners();

        // debug
        load();
    }

    private void initListeners() {

        // Novo projeto
        fNewProject.addActionListener((e) -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            try {
                if (chooser.showDialog(this, "Select PES Folder...") == JOptionPane.YES_OPTION) {
                    new Thread(() -> {
                        load();
                    }).start();
                }
            } catch (NullPointerException ex) {
                Logger.showError("Cannot select this folder");
                rightPanel.endProgress("Error at load project folder!");
            }
        });
    }

    private void load() {
        rightPanel.initProgress("Loading project...");

        // Criando o projeto
        Project project = new Project();

        // Criar as dependências
        if (!project.createDependecies(rightPanel.getProgressBar()))
            return;
        
        
        
        //treeOriginal.setModel(new DefaultTreeModel(
        //        Util.addTreeNodes(null, new File(Config.PATH_PES)))
        //);
        //tree.setModel(new DefaultTreeModel(Util.addTreeNodes(null, chooser.getSelectedFile())));
        try {
            // Criar um novo projeto
            //project.extractCPKS(rightPanel.getProgressBar());

            // Carregar as árvores
            treeOriginal.setModel(new DefaultTreeModel(project.getTreeOriginal()));
            treeProject.setModel(new DefaultTreeModel(project.getTreeProject()));

        } catch (Exception ex) {
            Logger.showError("Error loading project");
            System.out.println(ex.getMessage());
        }

        rightPanel.endProgress("Project loaded sucefully!");
    }
}
