package control.tool;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.ImageIcon;
import model.SortedTreeNode;

/**
 *
 * @author MrCapybara
 */
public class Util {

    /**
     * Retorna um ImageIcon de uma image no pacote utils.
     *
     * @param nameIcon Nome do ícone a ser retornado (sem .png)
     * @return ImageIcon da imagem solicitada
     */
    public static ImageIcon getImageIcon(String nameIcon) {
        return new ImageIcon(Util.class.getResource("/resources/" + (nameIcon) + (".png")));
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
}
