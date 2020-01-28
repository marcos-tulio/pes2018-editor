package control;

import control.tool.Logger;
import control.tool.Util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JProgressBar;
import model.Config;
import static model.Config.PROJECT_NAME;
import org.json.simple.JSONObject;
import static model.Config.PROJECT_CPKS_FOLDER;
import model.SortedTreeNode;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author MrCapybara
 */
public final class Project {

    int idItem = 0;

    /**
     * Cria todas as dependências iniciais para iniciar um projeto: pastas e
     * etc...
     *
     * @param bar barra para exibir o progresso da criação.
     * @return criada com sucesso?
     */
    public boolean createDependecies(JProgressBar bar) {
        File file;
        final String[] folders = {
            Config.PES_PATH, PROJECT_CPKS_FOLDER, Config.PROJECT_PATH_JSON
        };

        // Mostrar feedback em uma barra de progresso
        if (bar != null) {
            bar.setString("Checking folders...");
            bar.setValue(bar.getValue() + 1);
        }
        // Fim feedback

        for (String folder : folders) {
            file = new File(folder);
            file.mkdirs();

            if (!file.exists() || !file.isDirectory()) {
                Logger.showError("\"" + folder + " \"is not defined correctly!");
                return false;
            }
        }

        return true;
    }

    /**
     * Extrai todos os CPKs da pasta do PES para a pasta do projeto e cria uma
     * pasta para cada CPK.
     *
     * @param bar
     * @throws java.lang.InterruptedException
     * @throws java.io.IOException
     */
    public void extractCPKS(JProgressBar bar) throws InterruptedException, IOException {
        //------------------------ Tratando os CPKS --------------------------//
        final ArrayList<File> listCPKs = new ArrayList<>();     // Criar lista
        Util.getPathCPKs(listCPKs, new File(Config.PES_PATH));  // Popular lista 

        // Extrair CPK's
        Runtime runtime = Runtime.getRuntime();
        Process process;
        String outputFolder;
        JSONObject jMapper;

        // Incrementar a barra de acordo com a quantidade de cpks
        float porc = (float) (45f) / listCPKs.size(), increment = 0;
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
            outputFolder = Config.PROJECT_CPKS_FOLDER + File.separator + Util.removeExtension(cpk.getName());

            process = runtime.exec(Config.CPKMAKEC_PATH + " -extract=\"" + outputFolder + "\" \"" + cpk.getPath() + "\"");
            process.waitFor();
            process.destroy();

            // Salvar mapper
            jMapper = new JSONObject();
            jMapper.put("original_name", cpk.getName());
            jMapper.put("original_size", cpk.length());
            jMapper.put("original_path", cpk.getPath());
            jMapper.put("project_path", outputFolder);

            outputFolder = Config.PROJECT_PATH_JSON + File.separator + Util.removeExtension(cpk.getName()) + ".json";
            try (FileWriter writer = new FileWriter(outputFolder)) {
                writer.write(jMapper.toJSONString());
            }

            actualFile++;
            // Fim Extrair CPK
        }

        // Criar o JSON para mapeamento dos arquivos
        try (FileWriter writer = new FileWriter(Config.PROJECT_JSON_FILES)) {
            idItem = 0;
            createFileMapper(new File(PROJECT_CPKS_FOLDER), writer);
        }
    }

    /**
     * Retorna uma árvore com os itens encontrados dentro da pasta do PES.
     *
     * @return
     */
    public SortedTreeNode getTreeOriginal() {
        return Util.addTreeNodes(null, new File(Config.PES_PATH));
    }

    /**
     * Retorna uma árvore com os itens encontrados dentro do arquivo com os
     * JSON's.
     *
     * @return
     * @throws java.io.IOException
     * @throws org.json.simple.parser.ParseException
     */
    public SortedTreeNode getTreeProject() throws IOException, ParseException {
        // Nome para o projeto foi definido?
        if (PROJECT_NAME == null || PROJECT_NAME.replace(" ", "").isEmpty()) {
            Logger.showError("Project name is not defined correctly!");
            throw new IOException();
        }

        // Arquivo de mapeamento existe?
        final File mapper = new File(Config.PROJECT_JSON_FILES);
        if (!mapper.exists() || mapper.length() == 0) {
            Logger.showError("Mapper file is not created correctly!");
            throw new IOException();
        }

        // Criar árvore
        final SortedTreeNode mainNode = new SortedTreeNode(PROJECT_NAME);

        // Adiciona os itens do JSON à árvore
        try (final BufferedReader reader = new BufferedReader(new FileReader(mapper))) {
            String line;
            JSONParser parser = new JSONParser();

            while ((line = reader.readLine()) != null) {
                addInTree(mainNode, ((JSONObject) parser.parse(line)).get("path").toString());
            }
        }

        return mainNode;
    }

    /**
     * Adiciona para a árvore todos os itens informado de acordo com um path. \n
     * Arquivos repetidos são inseridos dentro de pastas únicas.\n
     *
     * @param mainNode
     * @param path
     */
    private void addInTree(SortedTreeNode mainNode, String path) {
        int indexOf = -1;
        SortedTreeNode curNode;

        while (true) {
            indexOf = path.indexOf(File.separator);

            // Está no último item do path?
            if (indexOf == -1) {
                mainNode.add(new SortedTreeNode(path));
                break;

            } else {
                curNode = new SortedTreeNode(path.substring(0, indexOf));
                boolean contains = false;

                // Contém algum objeto?
                if (mainNode.getChildCount() > 0) {
                    for (int i = 0; i < mainNode.getChildCount(); i++) {
                        if (mainNode.getChildAt(i).toString().equals(curNode.toString())) {
                            mainNode = (SortedTreeNode) mainNode.getChildAt(i);
                            contains = true;
                            break;
                        }
                    }
                }

                // Tentando adicionar repetido?
                if (!contains) {
                    mainNode.add(curNode);
                    mainNode = curNode;
                }
            }

            path = path.substring(indexOf + 1);
        }
    }

    /**
     * Cria um arquivo contendo todos os JSON's dos arquivos extraidos pelos
     * CPK'S e salva-o usando o writer
     */
    private void createFileMapper(File file, FileWriter writer) throws IOException {
        for (File temp : file.listFiles()) {
            if (temp.isDirectory())
                createFileMapper(temp, writer);

            else {
                String path = temp.getAbsolutePath().replace(PROJECT_CPKS_FOLDER + File.separator, "");
                JSONObject jMapper = new JSONObject();

                jMapper.put("id", idItem++);
                jMapper.put("name", temp.getName());
                jMapper.put("cpk", path.subSequence(0, path.indexOf(File.separator)));
                jMapper.put("path", path.subSequence(path.indexOf(File.separator) + 1, path.length()));
                jMapper.put("size", temp.length());
                //jMapper.put("original_path", temp.getAbsolutePath());

                writer.append(jMapper.toJSONString() + "\n");
            }
        }
    }

}
