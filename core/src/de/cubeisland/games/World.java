package de.cubeisland.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import de.cubeisland.games.entity.Entity;
import de.cubeisland.games.entity.Player;
import de.cubeisland.games.entity.TileEntity;
import de.cubeisland.games.tile.TileType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class World {
    private final List<TileEntity> tileEntities = new ArrayList<>();
    private final List<Entity> entities = new ArrayList<>();

    public World() {
        Pixmap pixmap = new Pixmap(Gdx.files.internal("level.bmp"));
        final int width = pixmap.getWidth();
        final int height = pixmap.getWidth();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                addTile(new TileEntity(x, y, TileType.getByColor(pixmap.getPixel(x, y))));
            }
        }

        spawn(new Player());
    }

    protected void addTile(TileEntity tile) {
        this.tileEntities.add(tile);
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
        for (TileEntity tileEntity : tileEntities) {
            tileEntity.render(game, delta);
        }

        for (Entity entity : entities) {
            entity.render(game, delta);
        }
    }
}
