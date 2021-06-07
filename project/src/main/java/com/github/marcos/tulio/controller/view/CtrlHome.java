package com.github.marcos.tulio.controller.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import java.io.IOException;
import java.util.logging.Level;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.github.marcos.tulio.controller.Project;
import com.github.marcos.tulio.controller.util.Logger;
import com.github.marcos.tulio.controller.util.Util;
import com.github.marcos.tulio.controller.view.fragment.TreeProjectListener;
import com.github.marcos.tulio.model.Config;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import com.github.marcos.tulio.view.ViewHome;

/**
 *
 * @author Marcos Santos
 */
public final class CtrlHome extends ViewHome {

    private TreeProjectListener treeProjectListener = null;

    public CtrlHome() {
        initListeners();

        // debug
        load();
    }

    private void initListeners() {
        // Listener da árvore para quando selecionar um item
        treeProjectListener = new TreeProjectListener() {            
            @Override
            public void showInfo(JSONObject item) {
                txtInfo.setText(""
                        + " File: " + item.get("name") + "\n"
                        + " Size: " + item.get("size") + " bytes\n"
                        + "   ID: " + item.get("id") + "\n"
                        + "  CPK: " + item.get("cpk") + "\n"
                        + "State: Compressed"
                );
            }

            @Override
            public void clearInfo() {
                if (!txtInfo.getText().isEmpty()) txtInfo.setText("Select a file...");
            }

            @Override
            public void loadingInfo() { txtInfo.setText("Loading..."); }
        };
        treeProject.addTreeSelectionListener(treeProjectListener);

        // Listener da árvore para quando clicar no item
        treeProject.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                treeProject.selectItemByMouse(e);
                treeProjectListener.loadingInfo();

                // Carregar arquivo apenas se for clique duplo com o botão esquerdo
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() >= 2){
                    String format = treeProjectListener.getItemSelectedName();

                    if (format.endsWith(".xml") || format.endsWith(".txt")){  
                        pmiOpenIText.doClick();
                    }
                }
                
                // Abre o pop menu de opcoes
                else if (SwingUtilities.isRightMouseButton(e) 
                    && !treeProjectListener.getItemSelectedId().isEmpty()){
                    popProject.show(e.getComponent(), e.getX(), e.getY());
                }                
            }
        });

        // Novo projeto
        fNewProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                try {
                    if (chooser.showDialog(getRootPane(), "Select PES Folder...") == JOptionPane.YES_OPTION) {
                        new Thread(() -> {
                            load();
                        }).start();
                    }
                } catch (NullPointerException ex) {
                    Logger.showError("Cannot select this folder");
                    progressBar.end("Error at load project folder!");
                }
            }
        });
    
        // Abrir com editor hexadecimal
        pmiOpenIHex.addActionListener((e)->{
            new Thread(() -> {
                treeProject.setEnabled(false);

                JSONObject object = Util.getJSONObject("id", treeProjectListener.getItemSelectedId(), Config.PROJECT_JSON_FILES);
                panelWorkbench.openFileInHexEditor(object, progressBar);

                treeProject.setEnabled(true);
            }).start(); 
        });

        // Abrir com editor de texto
        pmiOpenIText.addActionListener((e)->{
            new Thread(() -> {
                treeProject.setEnabled(false);

                JSONObject object = Util.getJSONObject("id", treeProjectListener.getItemSelectedId(), Config.PROJECT_JSON_FILES);
                panelWorkbench.openFileInTextEditor(object, progressBar);

                treeProject.setEnabled(true);
            }).start(); 
        });
    }

    private void load() {
        progressBar.init("Loading project...");

        // Criando o projeto
        Project project = new Project();

        // Criar as dependências
        if (!project.createDependecies(progressBar))
            return;

        //treeOriginal.setModel(new DefaultTreeModel(
        //        Util.addTreeNodes(null, new File(Config.PATH_PES)))
        //);
        //tree.setModel(new DefaultTreeModel(Util.addTreeNodes(null, chooser.getSelectedFile())));
        try {
            // Criar um novo projeto
            //project.extractCPKS(panelWorkbench.getProgressBar());

            // Carregar as árvores
            treeOriginal.setModel(new DefaultTreeModel(project.getTreeOriginal()));
            treeProject.setModel(new DefaultTreeModel(project.getTreeProject()));

        } catch (IOException | ParseException ex) {
            Logger.showError("Error loading project");
            System.out.println(ex.getMessage());
        }

        progressBar.end("Project loaded sucefully!");
    }
}
