package control;

import control.tool.Logger;
import control.tool.Util;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JProgressBar;
import model.Config;
import org.json.simple.JSONObject;

/**
 *
 * @author MrCapybara
 */
public class Project {

    public static void create(JProgressBar bar) throws InterruptedException, IOException {
        /* Validando e criando as pastas */
        File file;
        final String[] folders = {
            Config.PATH_PES, Config.PATH_PROJECT, Config.PATH_PROJECT_CPKS, Config.PATH_PROJECT_JSON
        };

        // Mostrar feedback em uma barra de progresso
        if (bar != null) {
            bar.setString("Checking folders...");
            bar.setValue(bar.getValue() + 1);
        }
        // Fim feedback

        for (String folder : folders) {
            file = new File(folder);
            file.mkdir();

            if (!file.exists() || !file.isDirectory()) {
                Logger.showError("\"" + folder + " \"is not defined correctly!");
                throw new IOException();
            }
        }
        /* Fim da validação das pastas. *///

        /* Tratando os CPKS */
        final ArrayList<File> listCPKs = new ArrayList<>();     // Criar lista
        Util.getPathCPKs(listCPKs, new File(Config.PATH_PES));  // Popular lista 

        // Extrair CPK's
        Runtime runtime = Runtime.getRuntime();
        Process process;
        String outputFolder;
        JSONObject jMapper;

        // Incrementar a barra de acordo com a quantidade de cpks
        float porc = (float) (50f - folders.length) / listCPKs.size(), increment = 0;
        int actualFile = 1;

        // Percorrer a lista de paths
        for (File cpk : listCPKs) {
            // Mostrar feedback em uma barra de progresso
            if (bar != null) {
                bar.setString("[" + actualFile + "/" + listCPKs.size() + "] Extracting \"" + cpk.getName() + "\"...");

                if ((int) increment > 0) {
                    bar.setValue(bar.getValue() + (int) increment);
                    increment -= (int) increment;
                }

                increment += porc;
            }
            // Fim feedback

            // Extrair CPK
            outputFolder = Config.PATH_PROJECT_CPKS + File.separator + Util.removeExtension(cpk.getName());

            process = runtime.exec(Config.PATH_CPKMAKEC + " -extract=\"" + outputFolder + "\" \"" + cpk.getPath() + "\"");
            process.waitFor();
            process.destroy();

            // Salvar mapper
            jMapper = new JSONObject();
            jMapper.put("original_name", cpk.getName());
            jMapper.put("original_size", cpk.length());
            jMapper.put("original_path", cpk.getPath());
            jMapper.put("project_path", outputFolder);

            outputFolder = Config.PATH_PROJECT_JSON + File.separator + Util.removeExtension(cpk.getName()) + ".json";
            try (FileWriter writer = new FileWriter(outputFolder)) {
                writer.write(jMapper.toJSONString());
            }

            actualFile++;
            // Fim Extrair CPK
        }
        /* Fim do tratamento dos CPKS */
    }
}
