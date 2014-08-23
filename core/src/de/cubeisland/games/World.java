package de.cubeisland.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import de.cubeisland.games.entity.Entity;
import de.cubeisland.games.entity.Player;
import de.cubeisland.games.entity.TileEntity;
import de.cubeisland.games.tile.Direction;
import de.cubeisland.games.tile.TileType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class World {
    private final int width;
    private final int height;
    private final TileEntity[][] tileEntities;
    private final List<Entity> entities = new ArrayList<>();

    public World() {
        Pixmap pixmap = new Pixmap(Gdx.files.internal("level.png"));
        width = pixmap.getWidth();
        height = pixmap.getWidth();

        this.tileEntities = new TileEntity[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tileEntities[x][y] = new TileEntity(x, y, TileType.getByColor(pixmap.getPixel(x, y)));
                tileEntities[x][y].setWorld(this);
            }
        }

        spawn(new Player());
    }

    public TileEntity getNeighbourOf(TileEntity tile, Direction dir) {
        int x = tile.getTileX();
        int y = tile.getTileY();

        switch (dir) {
            case TOP:
                y -= 1;
                break;
            case BOTTOM:
                y += 1;
                break;
            case LEFT:
                x -= 1;
                break;
            case RIGHT:
                x += 1;
                break;
        }

        if (x < 0 || y < 0) {
            return null;
        }
        if (x >= width || y >= height) {
            return null;
        }
        return this.tileEntities[x][y];
    }

    public boolean hasNeighbour(TileEntity tile, Direction dir) {
        int x = tile.getTileX();
        int y = tile.getTileY();

        switch (dir) {
            case TOP:
                y -= 1;
                break;
            case BOTTOM:
                y += 1;
                break;
            case LEFT:
                x -= 1;
                break;
            case RIGHT:
                x += 1;
                break;
        }

        if (x < 0 || y < 0) {
            return false;
        }
        if (x >= width || y >= height) {
            return false;
        }
        if (this.tileEntities[x][y] == null) {
            return false;
        }
        return true;
    }

    public void spawn(Entity e) {
        e.setWorld(this);
        this.entities.add(e);
        e.onSpawn();
        Collections.sort(this.entities, (o1, o2) -> o1.getDepth() - o2.getDepth());
    }

    public void update(DisconnectGame game, float delta) {
        List<Entity> removalQueue = new ArrayList<>();
        for (Entity entity : entities) {
            entity.update(game, delta);
            if (!entity.isAlive()) {
                removalQueue.add(entity);
            }
        }
        for (Entity entity : removalQueue) {
            entity.onDeath();
            this.entities.remove(entity);
        }
    }

    public void render(DisconnectGame game, float delta) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tileEntities[x][y].render(game, delta);
            }
        }

        for (Entity entity : entities) {
            entity.render(game, delta);
        }
    }
}
