package com.game.framework.entities;

import com.game.framework.resources.Resources;
import com.game.framework.utils.MathHelper;
import com.game.world.Tile;

import java.awt.*;

public class Entity extends Rectangle {

    protected byte entityID;

    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;

    protected int speed;

    protected MathHelper.Direction facing;

    protected int animationFrame;
    protected int animationDelay;

    public Entity(byte id, int posXInRoom, int posYInRoom) {
        super(posXInRoom * Tile.SIZE, posYInRoom * Tile.SIZE, Tile.SIZE, Tile.SIZE);
        this.entityID = id;
        this.up = false;
        this.down = false;
        this.left = false;
        this.right = false;
        this.speed = 5;
        this.facing = MathHelper.Direction.SOUTH;
        this.animationFrame = 0;
    }

    public byte getID() {
        return entityID;
    }

    public synchronized void move() {
        if (up){
            super.y -= this.speed;
            this.facing = MathHelper.Direction.NORTH;
            animationFrame = 0;
        }
        if (down){
            super.y += this.speed;
            this.facing = MathHelper.Direction.SOUTH;
        }
        if (left){
            super.x -= this.speed;
            this.facing = MathHelper.Direction.WEST;
        }
        if (right){
            super.x += this.speed;
            this.facing = MathHelper.Direction.EAST;
        }
    }

    public void moveEnemy() {
        if (up){
            super.y -= this.speed-3;
            this.facing = MathHelper.Direction.NORTH;
            animationFrame = 0;
        }
        if (down){
            super.y += this.speed-3;
            this.facing = MathHelper.Direction.SOUTH;
        }
        if (left){
            super.x -= this.speed-3;
            this.facing = MathHelper.Direction.WEST;
        }
        if (right){
            super.x += this.speed-3;
            this.facing = MathHelper.Direction.EAST;
        }
    }

    public void setMovingUp(boolean up) {
        this.up = up;
    }

    public void setMovingDown(boolean down) {
        this.down = down;
    }

    public synchronized void setMovingLeft(boolean left) {
        this.left = left;
    }

    public void setMovingRight(boolean right) {
        this.right = right;
    }

    public void setCenterX(int x) {
        super.x = x - super.width / 2;
    }

    public void setCenterY(int y) {
        super.y = y - super.height / 2;
    }

    public void render(Graphics graphics) {
        if(up || down || left || right){
            this.animationDelay++;
            if(this.animationDelay == 70) {
                this.animationFrame = 1;
            }
            if(this.animationDelay == 140) {
                this.animationDelay = 0;
                this.animationFrame = 2;
            }
        }
        //graphics.setColor(Color.BLUE);
        //graphics.drawRect(x, y, width, height);
        graphics.drawImage(Resources.TEXTURES.get(entityID + animationFrame), super.x, super.y, super.width, super.height, null);
    }

    public void handleCollisionWith(Tile tile) {
        Rectangle intersection = this.intersection(tile);
        if (intersection.isEmpty() || !tile.isWall()){
            return;
        }

        if (intersection.width > intersection.height) {
            if (this.y < tile.y) this.y = tile.y - this.height;
            else this.y = tile.y + this.height;
        } else {
            if (this.x < tile.x) this.x = tile.x - this.width;
            else this.x = tile.x + this.width;
        }
    }
}
