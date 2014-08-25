package de.cubeisland.games.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.World;
import de.cubeisland.games.entity.collision.Collider;
import de.cubeisland.games.entity.collision.CollisionBox;
import de.cubeisland.games.tile.Direction;

public abstract class Entity {
    protected Vector2 pos = Vector2.Zero.cpy();
    protected Vector2 velocity = Vector2.Zero.cpy();
    protected Vector2 size = Vector2.Zero.cpy();
    private World world;
    protected Vector2 lastPos = Vector2.Zero.cpy();
    private int depth = 1;
    private boolean alive = true;
    private CollisionBox collisionBox;
    private boolean inWall = false;

    public void update(DisconnectGame game, float delta) {
        this.lastPos.set(pos);
        this.pos.add(this.velocity.cpy().scl(delta));
        this.world.checkCollisions(this);
    }

    public void render(DisconnectGame game, float delta) {
        /* CollisionBoxes
        if (this.collisionBox != null) {
            ShapeRenderer r = getWorld().getCamera().getShapeRenderer();

            final float firstX = pos.x + collisionBox.getOffsetX();
            final float firstY = pos.y - collisionBox.getOffsetY();

            r.begin(ShapeRenderer.ShapeType.Filled);
            r.setColor(this instanceof TileEntity ? Color.BLUE : Color.RED);
            r.rect(firstX, firstY, collisionBox.getWidth(), collisionBox.getHeight());
            r.end();
        }
        //*/
    }

    public void onSpawn(World world) {
        this.world = world;
    }

    public void onDeath() {
    }

    public void onCollide(Entity other, Rectangle collisionBox) {
    }

    public void onTileCollide(TileEntity tile, Rectangle collisionBox) {
        if (!tile.getType().isBlocking())
        {
            return;
        }
        if (Math.abs(lastPos.y - pos.y) > Math.abs((lastPos.x - pos.x)))
        {
            if (onTileCollideY(collisionBox))
            {
                collisionBox = Collider.findCollision(this, tile);
            }
            if (collisionBox != null)
            {
                float posy = pos.y;
                pos.y = lastPos.y;
                onTileColideX(collisionBox);
                collisionBox = Collider.findCollision(this, tile);
                if (collisionBox != null)
                {
                    pos.y = posy;
                    collisionBox = Collider.findCollision(this, tile);
                    if (collisionBox != null)
                    {
                        if (this.inWall)
                        {
                            fixCollision(tile, collisionBox);
                            return;
                        }
                        this.inWall = true;
                        pos.x = lastPos.x;
                        pos.y = lastPos.y;
                    }
                }
                else
                {
                    pos.y = posy;
                }
            }
        }
        else
        {
            if (onTileColideX(collisionBox))
            {
                collisionBox = Collider.findCollision(this, tile);
            }
            if (collisionBox != null)
            {
                float posx = pos.x;
                pos.x = lastPos.x;
                onTileCollideY(collisionBox);
                collisionBox = Collider.findCollision(this, tile);
                if (collisionBox != null)
                {
                    pos.x = posx;
                    collisionBox = Collider.findCollision(this, tile);
                    if (collisionBox != null)
                    {
                        if (inWall)
                        {
                            fixCollision(tile, collisionBox);
                            return;
                        }
                        inWall = true;
                        pos.x = lastPos.x;
                        pos.y = lastPos.y;
                    }
                }
                else
                {
                    pos.x = posx;
                }
            }
        }
    }

    private boolean fixCollision(TileEntity tile, Rectangle collisionBox) {
        for (Direction direction : Direction.values()) {
            TileEntity neighbourOf = this.getWorld().getNeighbourOf(tile, direction);
            if (!neighbourOf.isBlocking())
            {
                pos.set(neighbourOf.getPos());
                inWall = false;
                System.out.println("FIXED COLLISION! " + collisionBox.height + " " + collisionBox.width);
                return true;
            }
        }
        System.out.println("UNRESOLVABLE COLLISION! " + collisionBox.height + " " + collisionBox.width);
        return false;
    }

    private boolean onTileCollideY(Rectangle collisionBox) {
        float newVal;
        if (velocity.y > 0) {
            newVal = collisionBox.y - this.getCollisionBox().getHeight() - this.getCollisionBox().getOffsetY();
            if (newVal >= lastPos.y && newVal < pos.y) {
                pos.y = newVal;
                return true;
            }
            else
            {
                pos.y = lastPos.y;
                return true;
            }
        } else if (velocity.y < 0) {
            newVal = collisionBox.y + collisionBox.height - this.getCollisionBox().getOffsetY();
            if (newVal <= lastPos.y && newVal > pos.y) {
                pos.y = newVal;
                return true;
            }
            else
            {
                pos.y = lastPos.y;
                return true;
            }
        }
        return false;
    }

    private boolean onTileColideX(Rectangle collisionBox) {
        float newVal;
        if (velocity.x > 0) {
            newVal = collisionBox.x - this.getCollisionBox().getWidth() - this.getCollisionBox().getOffsetX();
            if (newVal >= lastPos.x && newVal < pos.x) {
                pos.x = newVal;
                return true;
            }
            else
            {
                pos.x = lastPos.x;
                return true;
            }
        } else if (velocity.x < 0) {
            newVal = collisionBox.x + collisionBox.width - this.getCollisionBox().getOffsetX();
            if (newVal <= lastPos.x && newVal > pos.x) {
                pos.x = newVal;
                return true;
            }
            else
            {
                pos.x = lastPos.x;
                return true;
            }
        }
        return false;
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

    public void die() {
        this.alive = false;
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

    public void interact(Item carriedItem, Player player) {
    }
}
