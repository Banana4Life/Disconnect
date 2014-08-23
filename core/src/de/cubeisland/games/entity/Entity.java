package de.cubeisland.games.entity;

import de.cubeisland.games.Disconnect;
import de.cubeisland.games.World;

public abstract class Entity {
    private World world;

    public abstract void render(Disconnect game, float delta);

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }
}
