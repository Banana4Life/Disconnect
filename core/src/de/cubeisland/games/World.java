package de.cubeisland.games;

import de.cubeisland.games.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class World {
    private final List<Entity> entities = new ArrayList<Entity>();

    public void render(float delta) {
        for (Entity e : entities) {
            e.render(delta);
        }
    }
}
