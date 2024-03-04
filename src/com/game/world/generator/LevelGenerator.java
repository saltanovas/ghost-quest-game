package com.game.world.generator;

import com.game.framework.utils.MathHelper;

import java.util.HashSet;

public class LevelGenerator {
    public static final int WORLD_SIZE = 5;
    private int posX;
    private int posY;

    private HashSet<MathHelper.Direction>[][] roomsData;
    private boolean[][] generated;

    public void reset() {
        this.roomsData = new HashSet[WORLD_SIZE][WORLD_SIZE];
        this.generated = new boolean[WORLD_SIZE][WORLD_SIZE];
        for (int i = 0; i < this.roomsData.length; i++) {
            for (int j = 0; j < this.roomsData[i].length; j++) {
                this.roomsData[i][j] = new HashSet<>();
                this.generated[i][j] = false;
            }
        }
        this.setRandomPosition();
    }

    public void generate() {
        MathHelper.Direction direction = MathHelper.randomDirection();
        if (this.isValidPosition(posX + direction.dirX, posY + direction.dirY)) {
            if (!this.generated[posX + direction.dirX][posY + direction.dirY]) {
                this.roomsData[posX][posY].add(direction);
                this.roomsData[posX + direction.dirX][posY + direction.dirY].add(direction.opposite);
            }
            posX += direction.dirX;
            posY += direction.dirY;
            generated[posX][posY] = true;
        } else {
            generate();
        }
    }

    public void setRandomPosition() {
        this.posX = MathHelper.randomInt(WORLD_SIZE);
        this.posY = MathHelper.randomInt(WORLD_SIZE);
        this.generated[posX][posY] = true;
    }

    private boolean isValidPosition(int x, int y) {
        return x >= 0 && y >= 0 && x < WORLD_SIZE && y < WORLD_SIZE;
    }

    public boolean finished() {
        for (boolean[] booleans : this.generated) {
            for (boolean aBoolean : booleans) {
                if (!aBoolean) return false;
            }
        }
        return true;
    }

    public HashSet<MathHelper.Direction>[][] getRoomsData() {
        return this.roomsData;
    }
}
