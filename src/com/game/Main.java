package com.game;

import com.game.control.GameControl;
import com.game.framework.Engine;
import com.game.framework.resources.Loader;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Aivaras Saltanovas
 * VU MIF PS 4 gr.
 */
public class Main {

    /**
     * main method:
     * 1. loads all resources
     * 2. initialize main window
     * 3. starts (show) main window
     *
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Loader.load();
            Engine.init();
            Engine.start();
        });
    }
}
