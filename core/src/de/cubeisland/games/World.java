package de.cubeisland.games;

import de.cubeisland.games.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class World {
    private final Ludumdare30 game;
    private final List<Entity> entities = new ArrayList<Entity>();

    public World(Ludumdare30 game) {
        this.game = game;
    }

    public void render(Ludumdare30 game, float delta) {
        for (Entity e : entities) {
            e.render(this.game, delta);
        }
    }
}
