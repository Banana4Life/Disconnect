package de.cubeisland.games;

import de.cubeisland.games.entity.Entity;
import de.cubeisland.games.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class World {
    private final List<Entity> entities = new ArrayList<Entity>();

    public World() {
        add(new Player());
    }

    public void add(Entity e) {
        e.setWorld(this);
        this.entities.add(e);
    }

    public void render(Disconnect game, float delta) {
        for (Entity e : entities) {
            e.render(game, delta);
        }
    }
}
