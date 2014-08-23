package de.cubeisland.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;
import de.cubeisland.games.entity.Entity;
import de.cubeisland.games.entity.Player;
import de.cubeisland.games.entity.TileEntity;
import de.cubeisland.games.entity.collision.Collider;
import de.cubeisland.games.tile.Direction;
import de.cubeisland.games.tile.TileType;

import java.util.*;

import static de.cubeisland.games.tile.TileType.SPAWNPOINT;

public class World {
    private final int width;
    private final int height;
    private final TileEntity[][] tileEntities;
    private final Set<TileEntity> blockingTiles;
    private final List<Entity> entities = new ArrayList<>();
    private Vector2 spawnPos;
    private Camera camera;

    public World(Camera camera) {
        this.camera = camera;
        Pixmap pixmap = new Pixmap(Gdx.files.internal("level.png"));
        width = pixmap.getWidth();
        height = pixmap.getHeight();

        this.tileEntities = new TileEntity[width][height];
        this.blockingTiles = new HashSet<>();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                TileEntity tile = new TileEntity(x, y, TileType.getByColor(pixmap.getPixel(x, y)));
                tile.setWorld(this);
                tileEntities[x][y] = tile;
                if (tile.getType() == SPAWNPOINT) {
                    spawnPos = tile.getPos();
                }
            }
        }

        int directions;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                directions = 0;
                for (int m = -1; m < 2; m++) {
                    for (int n = -1; n < 2; n++) {
                        if (tileEntities[x][y].getType().isBlocking()) {
                            if (x + m > -1 && x + m < width && y + n > -1 && y + n < height) {
                                if (tileEntities[x + m][y + n] == null || tileEntities[x + m][y + n].getType() == tileEntities[x][y].getType()) {
                                    directions++;
                                }
                            }
                        }
                    }
                }
                if (directions > 8) {
                    tileEntities[x][y] = null;
                }
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (tileEntities[x][y] != null && tileEntities[x][y].getType().isBlocking()) {
                    blockingTiles.add(tileEntities[x][y]);
                }
            }
        }

        if (spawnPos != null) {
            spawn(new Player(spawnPos));
        }
    }

    public TileEntity getNeighbourOf(TileEntity tile, Direction dir) {
        int x = tile.getTileX() + dir.getX();
        int y = tile.getTileY() - dir.getY();

        if (x < 0 || y < 0) {
            return null;
        }
        if (x >= width || y >= height) {
            return null;
        }
        return this.tileEntities[x][y];
    }

    public boolean hasNeighbour(TileEntity tile, Direction dir) {
        return getNeighbourOf(tile, dir) != null;
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

        Collider.collideEntities(game, this.entities, this.blockingTiles);
    }

    public void render(DisconnectGame game, float delta) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (tileEntities[x][y] != null) {
                    tileEntities[x][y].render(game, delta);
                }
            }
        }

        for (Entity entity : entities) {
            entity.render(game, delta);
        }
    }

    public Camera getCamera() {
        return camera;
    }
}
