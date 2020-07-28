package com.game.states;

import com.game.framework.entities.Enemy;
import com.game.framework.entities.Player;
import com.game.framework.gamestates.GameState;
import com.game.framework.gamestates.GameStateManager;
import com.game.framework.resources.Resources;
import com.game.framework.utils.MathHelper;
import com.game.world.Tile;
import com.game.world.World;
import com.game.world.Feature;
import com.game.world.generator.LevelGenerator;
import com.game.world.generator.RoomData;

import java.awt.*;
import java.awt.event.KeyEvent;

public class PlayingState extends GameState {

    private LevelGenerator generator;
    public World world;
    public Player player;

    public PlayingState(GameStateManager manager) {
        super(manager);
        generator = new LevelGenerator();
        this.player = new Player();
        generateLevel();
    }

    @Override
    public void loop() {
        this.player.move();
        this.world.changeRoom(player);

        this.collisions();

        this.world.getRoom().featureInteraction(player);

        this.player.regenerateHealth();

        this.playerAtacks();

        if(World.chestCount == 0)
            System.exit(-1);
    }

    @Override
    public void render(Graphics graphics) {
        this.world.getRoom().render(graphics);
        this.player.render(graphics);

        graphics.setFont(new Font("TimesRoman", Font.PLAIN, 15));
        graphics.drawString(player.getHp() + " " + player.getArmor() + " " + player.getGold() + " " + World.chestCount, 100, 400);
        graphics.setColor(Color.RED);
        graphics.drawRect(this.player.getAttackBox().x, this.player.getAttackBox().y, this.player.getAttackBox().width, this.player.getAttackBox().height);
    }

    @Override
    public void keyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_W:
                this.player.setMovingUp(true);
                break;
            case KeyEvent.VK_A:
                this.player.setMovingLeft(true);
                break;
            case KeyEvent.VK_S:
                this.player.setMovingDown(true);
                break;
            case KeyEvent.VK_D:
                this.player.setMovingRight(true);
                break;
            case KeyEvent.VK_Q:
                this.player.attack();
                break;
            case KeyEvent.VK_E:
                this.deleteEnemies();
                break;
            case KeyEvent.VK_ENTER:
                this.generateLevel();
                break;
        }
    }

    public void deleteEnemies() {
        this.world.getRoom().getEnemies().clear();
    }

    @Override
    public void keyReleased(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_W:
                this.player.setMovingUp(false);
                break;
            case KeyEvent.VK_A:
                this.player.setMovingLeft(false);
                break;
            case KeyEvent.VK_S:
                this.player.setMovingDown(false);
                break;
            case KeyEvent.VK_D:
                this.player.setMovingRight(false);
                break;
            case KeyEvent.VK_ENTER:
                this.generateLevel();
                break;
        }
    }

    /**
     * 1. generates random map
     * 2. spawn all resources randomly but quantity is determined
     */
    public void generateLevel() {
        this.generator.reset();
        while (!this.generator.finished()) {
            this.generator.generate();
        }
        this.world = new World(this.generator.getRoomsData());

        this.world.getRoomRandom().placeFeature(new Feature(Resources.STAIRS, this::generateLevel));

        for (int i = 1; i <= World.chestCount; i++)
            this.world.getRoomRandom().placeFeature(new Feature(Resources.CHEST, this::givePlayerRandomLoot));

        for (int i = 0; i < 25; i++)
            this.world.getRoomRandom().spawnEnemy(new Enemy(Resources.ENEMY, MathHelper.randomInt(1, 20), this.player));

        this.spawnPlayer();
        this.player.restartPlayer();
        World.chestCount = 12;
    }

    /**
     * respawn player randomly if it is not on the ground
     */
    private void spawnPlayer() {
        if (this.world.getRoom().getData().getTileAt(player.x / Tile.SIZE, player.y / Tile.SIZE).getID() != Resources.TILE) {
            this.player.replaceRandomly();
            this.spawnPlayer();
        }
    }

    public void collisions() {
        RoomData roomIn = this.world.getRoom().getData();
        for (int i = 0; i < roomIn.getSizeX(); i++) {
            for (int j = 0; j < roomIn.getSizeY(); j++) {
                this.player.handleCollisionWith(this.world.getRoom().getData().getTileAt(i, j));
                for (Enemy enemy : this.world.getRoom().getEnemies()) {
                    enemy.handleCollisionWith(roomIn.getTileAt(i, j));
                }
            }
        }
    }

    /**
     * define what can be given when chest is collected
     */
    private void givePlayerRandomLoot() {
        switch (MathHelper.randomInt(3)) {
            case 0:
                this.player.addArmor(MathHelper.randomInt(2, 4));
                break;
            case 1:
                this.player.giveGold(MathHelper.randomInt(3, 7));
                break;
            case 2:
                this.player.instantHeal(MathHelper.randomInt(2, 5));
                break;
        }
    }

    /**
     *
     */
    public void playerAtacks() {
        this.player.decreaseTime();
        for (int i = 0; i < this.world.getRoom().getEnemies().size(); i++) {
            this.world.getRoom().getEnemies().get(i).move();

            if (this.world.getRoom().getEnemies().get(i).intersects(this.player)) {
                if (this.player.getHp() > 0) this.player.damage(5 - this.player.getArmor() / 50);
                else {
                    this.generateLevel();
                    break;
                }
            }

            if (this.world.getRoom().getEnemies().get(i).intersects(this.player.getAttackBox())) {
                this.world.getRoom().getEnemies().get(i).damage(5/*, this.player.getFacing().opposite*/);
                if (this.world.getRoom().getEnemies().get(i).getHp() <= 0)
                    this.world.getRoom().getEnemies().remove(i);
            }
        }
    }
}
