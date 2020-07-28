package com.game.framework;

import com.game.framework.gamestates.GameStateManager;
import com.game.framework.gui.WindowManager;
import com.game.states.MainMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 */
public class Engine {

    private static GameStateManager gameStateManager;
    private static WindowManager windowManager;
    private static Timer timer;

    /**
     *
     */
    public static void init(){
        gameStateManager = new GameStateManager();
        gameStateManager.stackState(new MainMenu(gameStateManager));
        windowManager = new WindowManager();
        timer = new Timer(20, new MainGameLoop());
    }

    /**
     *
     */
    public static void start(){
        windowManager.addPanel(new GameScreen());
        windowManager.addKeyListener(new Keyboard());
        windowManager.createWindow();
        timer.start();
    }

    /**
     *
     */
    private static class MainGameLoop implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            gameStateManager.loop();
        }
    }

    /**
     *
     */
    public static class GameScreen extends JPanel {

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            gameStateManager.render(g);
            repaint();
        }
    }

    /**
     *
     */
    private static class Keyboard implements KeyListener {

        @Override
        public void keyTyped(KeyEvent key) {
        }

        @Override
        public void keyPressed(KeyEvent key) {
            gameStateManager.keyPressed(key.getKeyCode());
        }

        @Override
        public void keyReleased(KeyEvent key) {
            gameStateManager.keyReleased(key.getKeyCode());
        }
    }
}
