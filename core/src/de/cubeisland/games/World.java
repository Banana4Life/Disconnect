package de.cubeisland.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import de.cubeisland.games.entity.Door;
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
    private List<Entity> spawnedEntities = new ArrayList<>();

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
                TileEntity tile;
                if (tileType.getTileEntityClass() != null)
                {
                    try {
                        tile = tileType.getTileEntityClass().getConstructor(int.class, int.class, TileType.class).newInstance(x, height - y - 1, tileType);
                    } catch (ReflectiveOperationException e) {
                        e.printStackTrace();
                        throw new IllegalArgumentException();
                    }
                }
                else
                {
                    System.out.println("MISSING TILE CLASS!");
                    continue;
                }
                tile.onSpawn(this);
                tileEntities[x][height - y - 1] = tile;
                if (tileType == FLOOR_PLAYER) {
                    spawnPos = tile.getPos();
                }
                if (!tile.getType().isBlocking() && tile.getCollisionBox() != null)
                {
                    this.entities.add(tile);
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

        if (player != null) {
            spawn(player);
        }
    }

    public TileEntity getNeighbourOf(TileEntity tile, Direction dir) {
        if (tile == null)
        {
            return null;
        }
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
            if (clazz !=null)
            {
                E e = clazz.newInstance();
                this.spawn(e);
                e.getPos().set(pos);
                return e;
            }
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException(clazz.getName());
        }
        return null;
    }

    public <E extends Entity> E spawn(E e) {
        if (e instanceof Player && spawnPos != null) {
            e.getPos().set(spawnPos);
        }

        this.spawnedEntities.add(e);
        e.onSpawn(this);
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
        this.entities.addAll(spawnedEntities);
        this.spawnedEntities.clear();
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
                if (e2 instanceof TileEntity)
                {
                    e1.onTileCollide((TileEntity) e2, collision);
                }
                else
                {
                    e1.onCollide(e2, collision);
                }
                count++;
            }
        }
        for (Iterator<TileEntity> iterator = blockingTiles.iterator(); iterator.hasNext(); ) {
            TileEntity e2 = iterator.next();
            if (!e2.getType().isBlocking())
            {
                iterator.remove();
            }
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
        for (Entity entity : this.entities) {
            if (entity.isAlive()) {
                entity.onDeath();
            }
        }
        this.entities.clear();
        this.blockingTiles.clear();
        for (int x = 0; x < this.width; ++x) {
            for (int y = 0; y < this.height; ++y) {
                if (this.tileEntities[x][y] != null) {
                    this.tileEntities[x][y].onDeath();
                }
            }
        }
        this.camera.dispose();
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<TileEntity> getTilesWithin(float x1, float y1, float x2, float y2) {

        List<TileEntity> tiles = new ArrayList<>();
        for (int tileX = 0; tileX < this.width; ++tileX) {
            for (int tileY = 0; tileY < this.height; ++tileY) {
                if (this.tileEntities[tileX][tileY] != null) {
                    TileEntity tile = this.tileEntities[tileX][tileY];
                    float x = tile.getPos().x;
                    float y = tile.getPos().y;
                    if (x >= x1 && x < x2 && y >= y2 && y < y1) {
                        tiles.add(tile);
                    }
                }
            }
        }

        return tiles;
    }

    public List<Door> findAllDoors() {
        List<Door> doors = new ArrayList<>();

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                if (tileEntities[x][y] instanceof Door) {
                    doors.add((Door) tileEntities[x][y]);
                }
            }
        }

        return doors;
    }

    private static final class DepthComparator implements Comparator<Entity> {
        @Override
        public int compare(Entity entity1, Entity entity2) {
            return entity1.getDepth() - entity2.getDepth();
        }
    }
}
