package com.game.control;

import javax.swing.*;

public class FilePath {

    String file;

    public String getFilePath() {

        JFileChooser jfc = new JFileChooser();
        int option = jfc.showSaveDialog(null);

        if (option == JFileChooser.APPROVE_OPTION) {

            String filename = jfc.getSelectedFile().getName();
            String path = jfc.getSelectedFile().getParentFile().getPath();

            int len = filename.length();
            String ext = "";

            if (len > 4)
                ext = filename.substring(len - 4, len);

            if (ext.equals(".png"))
                file = path + "\\" + filename;
            else
                file = path + "\\" + filename + ".png";
        }

        return file;
    }
}
