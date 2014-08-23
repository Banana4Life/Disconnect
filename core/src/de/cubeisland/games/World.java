package de.cubeisland.games;

import de.cubeisland.games.entity.Entity;
import de.cubeisland.games.entity.Player;
import de.cubeisland.games.entity.TileEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class World {
    private final List<TileEntity> tileEntities = new ArrayList<>();
    private final List<Entity> entities = new ArrayList<>();

    public World() {
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                spawn(new TileEntity(i,j));
            }
        }

        spawn(new Player());
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
