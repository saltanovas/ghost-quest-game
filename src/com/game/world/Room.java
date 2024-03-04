package com.game.world;

import com.game.framework.entities.Enemy;
import com.game.framework.entities.Player;
import com.game.framework.resources.Resources;
import com.game.world.generator.RoomData;

import java.awt.*;
import java.util.ArrayList;

public class Room {
    public final ArrayList<Feature> features;
    private final RoomData data;
    private final ArrayList<Enemy> enemies;

    public Room(RoomData data) {
        this.data = data;
        this.features = new ArrayList<>();
        this.enemies = new ArrayList<>();
    }

    public RoomData getData() {
        return data;
    }

    public void placeFeature(Feature feature) {
        if (data.getTileAt(feature.x / Tile.SIZE, feature.y / Tile.SIZE).getID() == Resources.TILE)
            this.features.add(feature);
        else
            this.placeFeature(new Feature(feature));
    }

    public void featureInteraction(Player player) {
        for (int i = 0; i < this.features.size(); i++) {
            if (this.features.get(i).intersects(player)) {
                this.features.remove(i);
                World.chestCount--;
            }
        }
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public void spawnEnemy(Enemy enemy) {
        if (data.getTileAt(enemy.x / Tile.SIZE, enemy.y / Tile.SIZE).getID() == Resources.TILE)
            this.enemies.add(enemy);
        else
            this.spawnEnemy(new Enemy(enemy));
    }

    public void render(Graphics graphics) {
        this.data.render(graphics);
        for (Feature feature : features) {
            //graphics.drawImage(Resources.TEXTURES.get(feature.getID()), feature.x, feature.y, feature.width, feature.height, null);
            feature.render(graphics);
        }
        for (Enemy enemy : enemies) {
            //graphics.drawImage(Resources.TEXTURES.get(enemy.getID()), enemy.x, enemy.y, enemy.width, enemy.height, null);
            enemy.render(graphics);
        }
    }
}
