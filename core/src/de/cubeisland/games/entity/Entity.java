package de.cubeisland.games.entity;

import com.badlogic.gdx.math.Vector2;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.World;
import de.cubeisland.games.entity.collision.CollisionBox;

public abstract class Entity {
    private World world;

    protected Vector2 pos = Vector2.Zero.cpy();
    protected Vector2 velocity = Vector2.Zero.cpy();
    protected Vector2 size = Vector2.Zero.cpy();

    private int depth = 1;
    private boolean alive = true;
    private CollisionBox collisionBox;

    public void update(DisconnectGame game, float delta) {
        this.pos.add(this.velocity.cpy().scl(delta));
    }

    public void render(DisconnectGame game, float delta) {
//        if (this.collisionBox != null) {
//            ShapeRenderer r = game.getCamera2().use().shapeRenderer;
//
//            final float firstX = pos.x + collisionBox.getOffsetX();
//            final float firstY = pos.y - collisionBox.getOffsetY();
//
//            r.begin(ShapeRenderer.ShapeType.Filled);
//            r.setColor(this instanceof TileEntity ? Color.BLUE : Color.RED);
//            r.rect(firstX, firstY, collisionBox.getWidth(), collisionBox.getHeight());
//            r.end();
//        }
    }

    public void onSpawn() {
    }

    public void onDeath() {
    }

    public void onCollide(Entity other, Vector2 mtv) {
    }

    public void onTileCollide(TileEntity tile, Vector2 mtv) {
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

    public CollisionBox getCollisionBox() {
        return collisionBox;
    }

    public void setCollisionBox(CollisionBox collisionBox) {
        this.collisionBox = collisionBox;
    }
}
