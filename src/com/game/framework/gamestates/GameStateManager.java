package com.game.framework.gamestates;

import java.awt.*;
import java.util.Stack;

public class GameStateManager {

    private Stack<GameState> states;

    public GameStateManager(){
        this.states = new Stack<>();
    }

    public void stackState(GameState state){
        states.add(state);
    }

    public void backToPrevious(){
        states.pop();
    }

    public void clearStack(){
        states.clear();
    }

    public void loop(){
        states.peek().loop();
    }

    public void render(Graphics g){
        states.peek().render(g);
    }

    public void keyPressed(int keyCode){
        states.peek().keyPressed(keyCode);
    }

    public void keyReleased(int keyCode){
        states.peek().keyReleased(keyCode);
    }
}
