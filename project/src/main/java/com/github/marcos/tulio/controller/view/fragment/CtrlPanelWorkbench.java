package com.github.marcos.tulio.controller.view.fragment;

import java.io.File;

import com.github.marcos.tulio.controller.view.tool.CtrlHexEditor;
import com.github.marcos.tulio.controller.view.tool.CtrlTextEditor;
import com.github.marcos.tulio.model.Config;
import org.json.simple.JSONObject;
import com.github.marcos.tulio.view.fragment.PanelWorkbench;
import com.github.marcos.tulio.view.fragment.ProgressBar;

/**
 *
 * @author Marcos Santos
 */
public final class CtrlPanelWorkbench extends PanelWorkbench {

    /**
     * Construtor.
     */
    public CtrlPanelWorkbench() {}

    /**
     * Carrega um arquivo para a workbench.
     *
     * @param object
     */
    public void openFile(JSONObject object, ProgressBar pBar, boolean openInTextEditor) {
        String tabName = object.get("name") + "(" + object.get("id") + ")";
        if (openInTextEditor) tabName = "[TEXT]" + tabName;
        else tabName = "[HEX]" + tabName;

        // Já contém uma tab com esse nome?
        for (int i = 0; i < getTabCount(); i++) {
            if (getTitleAt(i).equals(tabName))
                return;
        }

        String path = Config.PROJECT_CPKS_FOLDER + File.separator + object.get("cpk") + File.separator + object.get("path");

        if (openInTextEditor)
            addTab(tabName, new CtrlTextEditor(path));
        else
            addTab(tabName, new CtrlHexEditor(path, pBar));
    }

    //public void openFile(JSONObject object) { openFile(object, null); }

    public void openFileInHexEditor(JSONObject object, ProgressBar pBar) {
        openFile(object, pBar, false);
    }

    public void openFileInTextEditor(JSONObject object, ProgressBar pBar) {
        openFile(object, pBar, true);
    }
}