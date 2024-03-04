package com.game.framework.resources;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Loader {
    public static void load() {
        try {
            File texturesFolder = new File("res/textures");
            for (File imgFile : Objects.requireNonNull(texturesFolder.listFiles()))
                Resources.ROOMS.put(imgFile.getName(), ImageIO.read(imgFile));

            Resources.TEXTURES.add(Resources.TILE, ImageIO.read(new File("res/textures/tile.png")));
            Resources.TEXTURES.add(Resources.WALL, ImageIO.read(new File("res/textures/wall.png")));
            Resources.TEXTURES.add(Resources.PLAYER, ImageIO.read(new File("res/textures/player.png")));
            Resources.TEXTURES.add(Resources.PLAYER_FRONT1, ImageIO.read(new File("res/textures/player_front1.png")));
            Resources.TEXTURES.add(Resources.PLAYER_FRONT2, ImageIO.read(new File("res/textures/player_front2.png")));
            Resources.TEXTURES.add(Resources.PLAYER_RIGHT, ImageIO.read(new File("res/textures/player_right.png")));
            Resources.TEXTURES.add(Resources.PLAYER_RIGHT1, ImageIO.read(new File("res/textures/player_right1.png")));
            Resources.TEXTURES.add(Resources.PLAYER_RIGHT2, ImageIO.read(new File("res/textures/player_right2.png")));
            Resources.TEXTURES.add(Resources.PLAYER_LEFT, ImageIO.read(new File("res/textures/player_left.png")));
            Resources.TEXTURES.add(Resources.PLAYER_LEFT1, ImageIO.read(new File("res/textures/player_left1.png")));
            Resources.TEXTURES.add(Resources.PLAYER_LEFT2, ImageIO.read(new File("res/textures/player_left2.png")));
            Resources.TEXTURES.add(Resources.PLAYER_BACK, ImageIO.read(new File("res/textures/player_back.png")));
            Resources.TEXTURES.add(Resources.PLAYER_BACK1, ImageIO.read(new File("res/textures/player_back1.png")));
            Resources.TEXTURES.add(Resources.PLAYER_BACK2, ImageIO.read(new File("res/textures/player_back2.png")));
            Resources.TEXTURES.add(Resources.STAIRS, ImageIO.read(new File("res/textures/stairs.png")));
            Resources.TEXTURES.add(Resources.CHEST, ImageIO.read(new File("res/textures/chest.png")));
            Resources.TEXTURES.add(Resources.ENEMY, ImageIO.read(new File("res/textures/enemy.png")));
            Resources.TEXTURES.add(Resources.ENEMY_FRONT1, ImageIO.read(new File("res/textures/enemy_front1.png")));
            Resources.TEXTURES.add(Resources.ENEMY_FRONT2, ImageIO.read(new File("res/textures/enemy_front2.png")));
            Resources.TEXTURES.add(Resources.ENEMY_LEFT, ImageIO.read(new File("res/textures/enemy_left.png")));
            Resources.TEXTURES.add(Resources.ENEMY_LEFT1, ImageIO.read(new File("res/textures/enemy_left1.png")));
            Resources.TEXTURES.add(Resources.ENEMY_LEFT2, ImageIO.read(new File("res/textures/enemy_left2.png")));
            Resources.TEXTURES.add(Resources.ENEMY_RIGHT, ImageIO.read(new File("res/textures/enemy_right.png")));
            Resources.TEXTURES.add(Resources.ENEMY_RIGHT1, ImageIO.read(new File("res/textures/enemy_right1.png")));
            Resources.TEXTURES.add(Resources.ENEMY_RIGHT2, ImageIO.read(new File("res/textures/enemy_right2.png")));
            Resources.TEXTURES.add(Resources.ENEMY_BACK, ImageIO.read(new File("res/textures/enemy_back.png")));
            Resources.TEXTURES.add(Resources.ENEMY_BACK1, ImageIO.read(new File("res/textures/enemy_back1.png")));
            Resources.TEXTURES.add(Resources.ENEMY_BACK2, ImageIO.read(new File("res/textures/enemy_back2.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
