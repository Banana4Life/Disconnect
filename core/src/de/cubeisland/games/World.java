package de.cubeisland.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import de.cubeisland.games.entity.Entity;
import de.cubeisland.games.entity.Player;
import de.cubeisland.games.entity.TileEntity;
import de.cubeisland.games.entity.collision.Collider;
import de.cubeisland.games.entity.collision.CollisionBox;
import de.cubeisland.games.tile.Direction;
import de.cubeisland.games.tile.TileType;

import java.util.*;

import static de.cubeisland.games.tile.TileType.FLOOR_PLAYER;

public class World implements Disposable {

    private static final Comparator<Entity> BY_DEPTH = new DepthComparator();

    private final Player player;
    private final int width;
    private final int height;
    private final TileEntity[][] tileEntities;
    private final Set<TileEntity> blockingTiles;
    private final List<Entity> entities = new ArrayList<>();
    private DisconnectGame game;
    private Camera camera;
    private Vector2 spawnPos;

    public World(DisconnectGame game, Camera camera, Player player, String levelName) {
        this.game = game;
        this.camera = camera;
        this.player = player;
        Pixmap pixmap = new Pixmap(Gdx.files.internal(levelName + ".png"));
        width = pixmap.getWidth();
        height = pixmap.getHeight();

        this.tileEntities = new TileEntity[width][height];
        this.blockingTiles = new HashSet<>();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                TileType tileType = TileType.getByColor(pixmap.getPixel(x, y));
                TileEntity tile = new TileEntity(x, height - y - 1, tileType);
                tile.setWorld(this);
                tileEntities[x][height - y - 1] = tile;
                if (tileType == FLOOR_PLAYER) {
                    spawnPos = tile.getPos();
                }
                this.spawn(tileType.getEntityClass(), tile.getPos());
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

        spawn(player);
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

    public <E extends Entity> E spawn(Class<E> clazz, Vector2 pos) {
        try {
            E e = clazz.newInstance();
            this.spawn(e);
            e.getPos().set(pos);
            return e;
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException(clazz.getName());
        }
    }

    public <E extends Entity> E spawn(E e) {
        if (e instanceof Player && spawnPos != null) {
            e.getPos().set(spawnPos);
        }
        e.setWorld(this);
        this.entities.add(e);
        e.onSpawn();
        Collections.sort(entities, BY_DEPTH);
        return e;
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
                if (tileEntities[x][y] != null) {
                    tileEntities[x][y].render(game, delta);
                }
            }
        }

        for (Entity entity : entities) {
            entity.render(game, delta);
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (tileEntities[x][y] != null) {
                    tileEntities[x][y].renderOverlay();
                }
            }
        }
    }

    public Camera getCamera() {
        return camera;
    }

    public Player getPlayer() {
        return player;
    }

    public DisconnectGame getGame() {
        return game;
    }

    public int checkCollisions(Entity e1) {
        int count = 0;
        CollisionBox cB = e1.getCollisionBox();
        if (cB == null) {
            return count;
        }
        for (Entity e2 : entities) {
            if (e2 == e1) {
                continue;
            }
            Rectangle collision = Collider.findCollision(e1, e2);
            if (collision != null) {
                e1.onCollide(e2, collision);
                count++;
            }
        }
        for (TileEntity e2 : blockingTiles) {
            Rectangle collision = Collider.findCollision(e1, e2);
            if (collision != null) {
                e1.onTileCollide(e2, collision);
                count++;
            }
        }
        return count;
    }

    @Override
    public void dispose() {
        this.camera.dispose();
    }

    private static final class DepthComparator implements Comparator<Entity> {
        @Override
        public int compare(Entity entity1, Entity entity2) {
            return entity1.getDepth() - entity2.getDepth();
        }
    }
}
