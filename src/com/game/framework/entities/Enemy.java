package com.game.framework.entities;

import com.game.framework.resources.Resources;
import com.game.framework.utils.MathHelper;
import com.game.world.Feature;
import com.game.world.Tile;

import java.awt.*;

/**
 * defines enemy
 */
public class Enemy extends Entity {

    private Player target;

    private int hp;

    /**
     * sets random position for enemy;
     * assigns variables
     */
    public Enemy(byte id, int health, Player target) {
        super(id, MathHelper.randomInt(2, 14), MathHelper.randomInt(2, 7));
        this.target = target;
        this.hp = health;
    }

    /**
     * copy of enemy's parameters
     * @param copy
     */
    public Enemy(Enemy copy) {
        this(copy.getID(), copy.hp, copy.target);
    }

    /**
     * defines enemy movement
     */
    @Override
    public void move() {
        if(!this.target.intersects(super.x, super.y, super.width, super.height))
            super.moveEnemy();

        setFacing();
        float angCoeff = ((float) this.target.y - (float) super.y) / ((float) this.target.x - (float) super.x);
        if (angCoeff < 1 && angCoeff > -1) {
            if (this.target.x < super.x) {
                super.up = false;
                super.down = false;
                super.left = true;
                super.right = false;
            } else {
                super.up = false;
                super.down = false;
                super.left = false;
                super.right = true;
            }
        } else if (angCoeff > 1 || angCoeff < -1) {
            if (this.target.y < super.y) {
                super.up = true;
                super.down = false;
                super.left = false;
                super.right = false;
            } else {
                super.up = false;
                super.down = true;
                super.left = false;
                super.right = false;
            }
        } else if (angCoeff == 1 || angCoeff == -1) {
            if (this.target.x < super.x) {
                super.left = true;
                super.right = false;
            } else {
                super.left = false;
                super.right = true;
            }
        }
    }

    /**
     * enemy's health getter
     * @return hp
     */
    public int getHp(){
        return hp;
    }

    /**
     * takes enemy's health
     * @param amount - amount of damage
     */
    public void damage(int amount){
        this.hp -= amount;
    }

    /**
     * sets enemy's facing
     */
    public void setFacing(){
        switch (super.facing) {
            case NORTH:
                super.entityID = Resources.ENEMY_BACK;
                break;
            case SOUTH:
                super.entityID = Resources.ENEMY;
                break;
            case WEST:
                super.entityID = Resources.ENEMY_LEFT;
                break;
            case EAST:
                super.entityID = Resources.ENEMY_RIGHT;
                break;
        }
    }
}
