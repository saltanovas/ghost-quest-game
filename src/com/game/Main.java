package com.game;

import com.game.framework.Engine;
import com.game.framework.resources.Loader;

import javax.swing.SwingUtilities;

/**
 * 1. Loads all resources
 * 2. Initializes main window
 * 3. Starts main window
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Loader.load();
            Engine.init();
            Engine.start();
        });
    }
}
