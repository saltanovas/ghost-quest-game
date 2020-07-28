package com.game.framework.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

public class WindowManager {

    private JFrame frame;
    private JPanel panel;
    public static final int WIDTH = 800;
    public static final int HEIGHT = 450;

    public WindowManager() {
        this.frame = new JFrame("Game");
        this.frame.setBounds(70, 70, WIDTH, HEIGHT);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setResizable(false);
    }

    public void addPanel(JPanel panel) {
        this.panel = panel;
        this.panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.panel.setFocusable(true);
        this.panel.requestFocusInWindow();
    }

    public void addKeyListener(KeyListener listener) {
        this.panel.addKeyListener(listener);
    }

    public void createWindow() {
        this.frame.setContentPane(panel);
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
    }
}
