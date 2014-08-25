package de.cubeisland.games.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.entity.collision.Collider;
import de.cubeisland.games.entity.collision.CollisionBox;
import de.cubeisland.games.resource.bag.Textures;
import de.cubeisland.games.tile.TileType;

import java.util.List;

import static de.cubeisland.games.tile.Direction.*;
import static de.cubeisland.games.tile.TileType.*;

public class TileEntity extends Entity {

    public static final int SIZE = 16;
    private final int tileX;
    private final int tileY;
    protected TileType type;
    protected TextureRegion texture = null;
    protected TextureRegion overlay = null;
    protected Vector2 overlayOffset;
    private float statetime = 0f;

    public TileEntity(int x, int y, TileType type) {
        this.tileX = x;
        this.tileY = y;
        this.type = type.get();
        this.size = new Vector2(SIZE, SIZE);
        this.pos.set(x, y).scl(SIZE);
        if (this.type.isBlocking()) {
            this.setCollisionBox(new CollisionBox(SIZE, SIZE));
        }
        this.overlayOffset = new Vector2(0, SIZE);
    }

    @Override
    public void render(DisconnectGame game, float delta) {
        if (!this.getWorld().getCamera().canBeSeen(this.pos, this.size)) {
            return;
        }

        if (texture == null) {
            Textures textures = game.getResourcePack().textures;
            switch (type) {
                case FLOOR:
                    this.texture = textures.floor;
                    break;
                case WALL:
                case TERMINAL:
                    TileEntity top = getWorld().getNeighbourOf(this, TOP);
                    TileEntity left = getWorld().getNeighbourOf(this, LEFT);
                    TileEntity right = getWorld().getNeighbourOf(this, RIGHT);
                    TileEntity bottom = getWorld().getNeighbourOf(this, BOTTOM);
                    if (FLOOR.isType(top) || DOOR.isType(top) || AUTO_DOOR.isType(top)) {
                        if (FLOOR.isType(left) || DOOR.isType(left) || AUTO_DOOR.isType(left)) {
                            if (FLOOR.isType(right) || DOOR.isType(right) || AUTO_DOOR.isType(right)) {
                                this.texture = textures.walltopboth;
                            } else {
                                this.texture = textures.walltopleft;
                            }
                        } else if (FLOOR.isType(right) || DOOR.isType(right) || AUTO_DOOR.isType(right)) {
                            this.texture = textures.walltopright;
                        } else {
                            this.texture = textures.walltop;
                        }
                    }
                    TileEntity rightTop = getWorld().getNeighbourOf(right, TOP);
                    if (left != null && this.texture == null) {
                        TileEntity leftTop = getWorld().getNeighbourOf(left, TOP);
                        if (FLOOR.isType(left)) {
                            if (FLOOR.isType(right)) {
                                this.texture = textures.wallboth;
                            } else if (WALL.isType(leftTop)) {
                                this.texture = textures.wallrightbottom;
                            } else {
                                this.texture = textures.wallright;
                            }
                        } else if (WALL.isType(left) && FLOOR.isType(leftTop)) {
                            if (WALL.isType(right) && WALL.isType(left) && FLOOR.isType(rightTop)) {
                                this.texture = textures.wallbothtop;
                            } else {
                                this.texture = textures.wallrighttop;
                            }
                        }
                    }
                    if (right != null && this.texture == null) {
                        if (FLOOR.isType(right)) {
                            if (WALL.isType(rightTop)) {
                                this.texture = textures.wallleftbottom;
                            } else {
                                this.texture = textures.wallleft;
                            }
                        } else if (WALL.isType(right) && FLOOR.isType(rightTop)) {
                            this.texture = textures.walllefttop;
                        }
                    }
                    if (FLOOR.isType(bottom) || DOOR.isType(bottom) || AUTO_DOOR.isType(bottom)) {
                        if (FLOOR.isType(left)) {
                            if (FLOOR.isType(right)) {
                                this.overlay = textures.wallbottomboth;
                            } else {
                                this.overlay = textures.wallbottomleft;
                            }
                        } else if (FLOOR.isType(right)) {
                            this.overlay = textures.wallbottomright;
                        } else {
                            this.overlay = textures.wallbottom;
                        }
                    }
                    if (this.texture == null) {
                        this.texture = textures.wall;
                    }
                    break;
                case FLOOR_PLAYER:
                    this.texture = textures.floor;
                    break;
            }
        }

        statetime += delta;

        SpriteBatch batch = this.getWorld().getCamera().getSpriteBatch();
        batch.begin();
        batch.draw(this.texture, pos.x, pos.y, SIZE, SIZE);
        if (this.type == TERMINAL) {
            batch.draw(getWorld().getGame().getResourcePack().animations.terminal.getKeyFrame(statetime, true), pos.x, pos.y, SIZE, SIZE);
        }
        batch.end();

        super.render(game, delta);
    }

    public void renderOverlay() {
        if (!this.getWorld().getCamera().canBeSeen(this.pos, this.size) || this.overlay == null) {
            return;
        }

        CollisionBox overlayCollisionBox;
        if (this.type == DOOR_OPEN || this.type == AUTO_DOOR_OPEN) {
            overlayCollisionBox = new CollisionBox(SIZE, SIZE, 0, 0);
        } else {
            overlayCollisionBox = new CollisionBox(SIZE, SIZE, 0, -14); // 13 overlay height + 1 to collide sooner
        }

        SpriteBatch batch = this.getWorld().getCamera().getSpriteBatch();

        List<Entity> entities = this.getWorld().getEntities();
        for (Entity entity : entities) {
            if (entity.getCollisionBox() == null || entity instanceof Item) {
                continue;
            }

            if (Collider.findCollision(entity, this, entity.getCollisionBox(), overlayCollisionBox) != null) {
                batch.begin();
                Color c = batch.getColor();
                batch.setColor(c.r, c.g, c.b, 0.7f);
                batch.draw(this.overlay, pos.x + overlayOffset.x, pos.y + overlayOffset.y, SIZE, SIZE);
                batch.setColor(c.r, c.g, c.b, 1f);
                batch.end();
                return;
            }
        }
        batch.begin();
        batch.draw(this.overlay, pos.x + overlayOffset.x, pos.y + overlayOffset.y, SIZE, SIZE);
        batch.end();
    }

    public TileType getType() {
        return type;
    }

    public int getTileX() {
        return tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public boolean isBlocking() {
        return this.getType().isBlocking();
    }
}
