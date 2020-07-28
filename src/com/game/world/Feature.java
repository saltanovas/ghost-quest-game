package com.game.world;

import java.awt.*;

import com.game.framework.resources.Resources;
import com.game.framework.utils.FunctionalInterface;
import com.game.framework.utils.MathHelper;

public class Feature extends Tile {

    private FunctionalInterface action;

    public Feature(byte id, FunctionalInterface action) {
        super(id, MathHelper.randomInt(2, 14), MathHelper.randomInt(2, 7), false);
        this.action = action;
    }

    public Feature(Feature copy){
        this(copy.getID(), copy.action);
    }

    @Override
    public boolean intersects(Rectangle r){
        if (super.intersects(r)){
            this.action.action();
            return true;
        }
        return false;
    }

    public void render(Graphics graphics){
        graphics.drawImage(Resources.TEXTURES.get(super.getID()), super.x, super.y, super.width, super.height, null);
    }
}
