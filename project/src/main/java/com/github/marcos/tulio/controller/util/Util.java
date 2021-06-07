package com.github.marcos.tulio.controller.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

import javax.swing.ImageIcon;

import com.github.marcos.tulio.controller.view.CtrlHome;
import com.github.marcos.tulio.model.SortedTreeNode;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Marcos Santos
 */
public class Util {

    private static final JSONParser parser = new JSONParser();

    public static final String ANSI
            = "................................ !\"#$%&'()*+,-./0123456789:;<=>?"
            + "@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~"
            + "€.‚ƒ„…†‡ˆ‰Š‹Œ.Ž..‘’“”•–—˜™š›œ.žŸ ¡¢£¤¥¦§¨©ª«¬­®¯°±²³´µ¶·¸¹º»¼½¾¿ÀÁÂ"
            + "ÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿ";

    /**
     * Retorna um ImageIcon de uma image no pacote utils.
     *
     * @param nameIcon Nome do ícone a ser retornado (sem .png)
     * @return ImageIcon da imagem solicitada
     */
    public static ImageIcon getImageIcon(String nameIcon) {
        return new ImageIcon(
            Util.class.getResource("/com/github/marcos/tulio/resources/" + nameIcon + ".png")
        );
    }

    /**
     * Retorna um ImageIcon, de uma imagem no pacote utils, com o tamanho
     * desejado.
     *
     * @param nameIcon Nome do ícone a ser retornado (sem .png)
     * @param width Largura da imagem
     * @param height Altura da imagem
     * @return ImageIcon da imagem solicitada
     */
    public static ImageIcon getImageIcon(String nameIcon, int width, int height) {
        ImageIcon imageIcon = getImageIcon(nameIcon);
        imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(width, height, 100));
        return imageIcon;
    }

    public static SortedTreeNode addTreeNodes(SortedTreeNode curTop, File dir) {
        if (!dir.exists())
            return null;

        String curPath = dir.getPath();
        SortedTreeNode curDir = new SortedTreeNode(dir.getName());

        if (curTop != null)
            curTop.add(curDir);

        File f;
        ArrayList<String> folders = new ArrayList(Arrays.asList(dir.list()));
        ArrayList<String> files = new ArrayList();

        for (int i = 0; i < folders.size(); i++) {
            String thisObject = folders.get(i);
            String newPath;

            if (curPath.equals("."))
                newPath = thisObject;
            else
                newPath = curPath + File.separator + thisObject;

            if ((f = new File(newPath)).isDirectory())
                addTreeNodes(curDir, f);
            else
                files.add(thisObject);
        }

        files.forEach((file) -> {
            curDir.add(new SortedTreeNode(file));
        });

        return curDir;
    }

    /**
     * Adiciona na lista recebida como argumento todos os paths dos cpks do
     * diretório e subdiretório.
     *
     * @param list
     * @param dir
     */
    public static void getPathCPKs(ArrayList<File> list, File dir) {
        for (File child : dir.listFiles()) {
            // É um arquivo CPK?
            if (child.getName().toLowerCase().endsWith(".cpk"))
                list.add(child);

            // É um diretório?
            else if (child.isDirectory())
                getPathCPKs(list, child);
        }
    }

    public static String removeExtension(String fName) {
        if (fName.contains("."))
            return fName.substring(0, fName.lastIndexOf("."));

        return fName;
    }

    public static String getTextPostFlag(String text, String flag) {

        if (text.contains(flag))
            return text.substring(text.indexOf(flag) + flag.length());

        return text;
    }

    public static String getTextPreFlag(String text, String flag) {

        if (text.contains(flag))
            return text.substring(0, text.indexOf(flag));

        return text;
    }

    /**
     * Retorna um JSON do campo setado.
     *
     * @param field
     * @param value
     * @param path
     * @return
     */
    public static JSONObject getJSONObject(String field, String value, String path) {
        JSONObject obj = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                obj = (JSONObject) parser.parse(line);

                if (obj.get(field).toString().equalsIgnoreCase(value))
                    break;

                obj.clear();
            }
        } catch (ParseException | IOException ex) {
            java.util.logging.Logger.getLogger(CtrlHome.class.getName()).log(Level.SEVERE, null, ex);
        }

        return obj;
    }

    public static String getCharANSI(char c) {
        c = (char) (c & 0xFF);
        return Util.ANSI.substring(c, c + 1);
    }
}
