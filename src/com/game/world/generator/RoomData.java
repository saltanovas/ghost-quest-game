package com.game.world.generator;

import com.game.framework.resources.Resources;
import com.game.framework.utils.MathHelper;
import com.game.world.Tile;

import java.awt.*;
import java.util.HashSet;

public class RoomData {

    private Tile[][] tilesData;

    private HashSet<MathHelper.Direction> exits;

    public RoomData(byte[][] tilesData, MathHelper.Direction... exits) {
        this.tilesData = new Tile[tilesData.length][tilesData[0].length];
        for (int i = 0; i < this.tilesData.length; i++) {
            for (int j = 0; j < this.tilesData[i].length; j++) {
                this.tilesData[i][j] = new Tile(tilesData[i][j], j, i, tilesData[i][j] == 1);
            }
        }
        this.exits = new HashSet<>();
        for(MathHelper.Direction direction : exits){
            this.exits.add(direction);
        }
    }

    public void render(Graphics graphics) {
        for (int i = 0; i < this.tilesData.length; i++) {
            for (int j = 0; j < tilesData[i].length; j++) {
                graphics.drawImage(Resources.TEXTURES.get(this.tilesData[i][j].getID()), j * Tile.SIZE, i * Tile.SIZE, Tile.SIZE, Tile.SIZE, null);
               // if (this.tilesData[i][j].getID() == 0)
                  //  graphics.setColor(new Color(70, 70, 70));
               // else {graphics.setColor(new Color(30, 30, 30)); graphics.fillRect(j * Tile.SIZE, i * Tile.SIZE, Tile.SIZE, Tile.SIZE);}

               // graphics.fillRect(j, i, Tile.SIZE, Tile.SIZE);
            }
        }
    }

    public HashSet<MathHelper.Direction> getExits(){
        return exits;
    }

    public Tile getTileAt(int x, int y){
        return tilesData[y][x];
    }

    public int getSizeY(){
        return tilesData.length;
    }

    public int getSizeX(){
        return tilesData[0].length;
    }
}
