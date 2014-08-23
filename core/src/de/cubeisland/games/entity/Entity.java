package de.cubeisland.games.entity;

import com.badlogic.gdx.math.Vector2;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.World;

public abstract class Entity {
    private World world;

    protected Vector2 pos = Vector2.Zero.cpy();
    protected Vector2 velocity = Vector2.Zero.cpy();
    private int depth = 1;
    private boolean alive = true;

    public void update(DisconnectGame game, float delta) {
        this.pos.add(this.velocity.cpy().scl(delta));
    }

    public void render(DisconnectGame game, float delta) {
    }

    public void onSpawn() {
    }

    public void onDeath() {
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    public Vector2 getPos() {
        return pos;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public boolean isAlive() {
        return alive;
    }
}
