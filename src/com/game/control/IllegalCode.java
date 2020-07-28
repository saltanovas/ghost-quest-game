package com.game.control;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;

public class IllegalCode {

    public IllegalCode (String text){
      //  UIManager.put("OptionPane.background", DARK_BLUE);
      //  UIManager.put("OptionPane.messageForeground", WHITE);
      //  UIManager.put("Panel.background", DARK_BLUE);
      //  UIManager.put("Button.background", ORANGE);
      //  UIManager.put("Button.focus", false);
      //  UIManager.put("Button.border", Color.WHITE);
      //  UIManager.put("OptionPane.okButtonText", "   GERAI   ");
      //  UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("SansSerif", Font.PLAIN, 15)));
      //  ImageIcon img = new ImageIcon(getClass().getResource("fileExceptionIcon.png"));
        JOptionPane.showMessageDialog(null, "Kompiliavimo klaida: " + text, "ERROR!", JOptionPane.INFORMATION_MESSAGE);
    }
}
