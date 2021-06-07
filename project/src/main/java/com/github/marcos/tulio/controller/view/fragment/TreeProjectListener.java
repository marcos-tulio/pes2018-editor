package com.github.marcos.tulio.controller.view.fragment;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.github.marcos.tulio.controller.util.Util;
import com.github.marcos.tulio.model.Config;

import org.json.simple.JSONObject;

public abstract class TreeProjectListener implements TreeSelectionListener{

    private Thread thread = null;
    private String iSelectedName = "";
    private String iSelectedId   = ""; 

    public TreeProjectListener(){ clearInfo(); }

    public String getItemSelectedId(){ return iSelectedId; }
    
    public String getItemSelectedName(){ return iSelectedName; }

    public boolean isItemSelected(){ return !iSelectedId.isBlank(); }

    public abstract void clearInfo();

    public abstract void loadingInfo();

    public abstract void showInfo(JSONObject item);

    /**
     * Cria uma thread para carregar as informações do arquivo
     * selecionado.
     */
    private void loadThread() {
        // Thread rodando?
        if (thread != null && thread.isAlive()) {
            try {
                loadingInfo();
                thread.join();
                loadThread();
            } catch (InterruptedException ex) {
                clearInfo();
                System.err.println("Error in thread");
            }
            
        } else {
            loadingInfo();
            thread = new Thread(handlerLoad());
            thread.start();
        }
    }

    /**
     * Handler de carregamento.
     */
    private Runnable handlerLoad() {
        return () -> {
            JSONObject object = Util.getJSONObject("id", iSelectedId, Config.PROJECT_JSON_FILES);

            if (object != null && !object.isEmpty())
                showInfo(object);
            else 
                clearInfo();
        };
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        // Retorna caso a seleção do item seja nula
        if (e.getNewLeadSelectionPath() == null){
            iSelectedId   = "";
            iSelectedName = "";
            return;
        }

        // Seleciona o id do item que se encontra no final do path
        String path = e.getNewLeadSelectionPath().getLastPathComponent().toString();

        // O id foi encontrado por meio da flag?
        if (path.contains(Config.ID_FLAG)) {
            iSelectedId = path.substring(path.indexOf(Config.ID_FLAG) + Config.ID_FLAG.length());
            iSelectedName = path.substring(0, path.indexOf(Config.ID_FLAG));
            loadThread();

        } else {
            iSelectedId   = "";
            iSelectedName = "";
            clearInfo();
        }
    }
}
