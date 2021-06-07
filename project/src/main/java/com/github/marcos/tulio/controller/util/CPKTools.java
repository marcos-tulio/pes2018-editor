package com.github.marcos.tulio.controller.util;

import java.io.IOException;
import java.util.List;

import com.github.marcos.tulio.controller.ReaderText;

/**
 *
 * @author MrCapybara
 */
public class CPKTools {

    private final String osName;
    private List<String> outputFiles, outputError;

    public CPKTools(String osName) {
        this.osName = osName;
    }

    public boolean extract(List<String> params) {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(params.toArray(new String[0]));

            ReaderText outText = new ReaderText(process.getInputStream());
            outputFiles = outText.readToList();

            outText = new ReaderText(process.getErrorStream());
            outputError = outText.readToList();

            //removeListHeader();
            return true;
        } catch (IOException ex) {
            if (outputFiles != null) {
                outputFiles.clear();
            }

            if (outputError != null) {
                outputError.clear();
            }

            return false;
        }
    }

    private void removeListHeader() {
        if (outputFiles != null) {
            if (outputFiles.size() > 3) {
                for (int i = 0; i < 3; i++) {
                    outputFiles.remove(0);
                }
            }
        }
    }

    public List<String> getOutputFiles() {
        return outputFiles;
    }

    public List<String> getOutputError() {
        return outputError;
    }
}
