package com.game.world;

import java.awt.*;

public class Tile extends Rectangle {
    public static final int SIZE = 50;
    private final byte tileID;
    private final boolean wall;

    public Tile(byte id, int posXinRoom, int posYinRoom, boolean isWall) {
        super(posXinRoom * SIZE, posYinRoom * SIZE, SIZE, SIZE);
        this.tileID = id;
        this.wall = isWall;
    }

    public byte getID() {
        return tileID;
    }

    public boolean isWall() {
        return wall;
    }
}
