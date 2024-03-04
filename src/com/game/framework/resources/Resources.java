package com.game.framework.resources;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class Resources {
    public static final byte TILE = 0;
    public static final byte WALL = 1;
    public static final byte PLAYER = 2;
    public static final byte PLAYER_FRONT1 = 3;
    public static final byte PLAYER_FRONT2 = 4;
    public static final byte PLAYER_RIGHT = 5;
    public static final byte PLAYER_RIGHT1 = 6;
    public static final byte PLAYER_RIGHT2 = 7;
    public static final byte PLAYER_LEFT = 8;
    public static final byte PLAYER_LEFT1 = 9;
    public static final byte PLAYER_LEFT2 = 10;
    public static final byte PLAYER_BACK = 11;
    public static final byte PLAYER_BACK1 = 12;
    public static final byte PLAYER_BACK2 = 13;
    public static final byte STAIRS = 14;
    public static final byte CHEST = 15;
    public static final byte ENEMY = 16;
    public static final int ENEMY_FRONT1 = 17;
    public static final int ENEMY_FRONT2 = 18;
    public static final int ENEMY_LEFT = 19;
    public static final int ENEMY_LEFT1 = 20;
    public static final int ENEMY_LEFT2 = 21;
    public static final int ENEMY_RIGHT = 22;
    public static final int ENEMY_RIGHT1 = 23;
    public static final int ENEMY_RIGHT2 = 24;
    public static final int ENEMY_BACK = 25;
    public static final int ENEMY_BACK1 = 26;
    public static final int ENEMY_BACK2 = 27;
    public static final HashMap<String, BufferedImage> ROOMS = new HashMap<>();
    public static final ArrayList<BufferedImage> TEXTURES = new ArrayList<>();
}
