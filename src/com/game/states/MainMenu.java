package com.game.states;

import com.game.framework.gamestates.GameState;
import com.game.framework.gamestates.GameStateManager;
import com.game.framework.gui.WindowManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MainMenu extends GameState {
    private static final String START_GAME = "Start Game!";
    private static final String QUIT_GAME = "Quit Game";
    private final String[] optionsMenu;
    private int selected;

    public MainMenu(GameStateManager manager) {
        super(manager);
        this.optionsMenu = new String[]{START_GAME, QUIT_GAME};
        this.selected = 0;
    }

    @Override
    public void loop() {
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(Color.DARK_GRAY);
        graphics.fillRect(0, 0, WindowManager.WIDTH, WindowManager.HEIGHT);

        graphics.setFont(new Font("Arial", Font.PLAIN, 25));
        for (int i = 0; i < this.optionsMenu.length; i++) {
            if (i == this.selected) {
                graphics.setColor(Color.GREEN);
            } else {
                graphics.setColor(Color.WHITE);
            }
            graphics.drawString(this.optionsMenu[i], 20, 50 + i * 40);
        }
    }

    @Override
    public void keyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
                if (this.selected > 0) this.selected--;
                break;
            case KeyEvent.VK_DOWN:
                if (this.selected < this.optionsMenu.length - 1) this.selected++;
                break;
            case KeyEvent.VK_ENTER:
                switch (this.optionsMenu[selected]) {
                    case START_GAME:
                        PlayingState playingState = new PlayingState(gameStateManager);
                        gameStateManager.stackState(playingState);
                        SwingUtilities.invokeLater(() -> new com.game.control.GameControl(playingState));
                        break;
                    case QUIT_GAME:
                        System.exit(-1);
                        break;
                }
                break;
        }
    }

    @Override
    public void keyReleased(int keyCode) {

    }
}
