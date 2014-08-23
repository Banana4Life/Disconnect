package de.cubeisland.games.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.entity.collision.CollisionBox;
import de.cubeisland.games.tile.TileType;

import static de.cubeisland.games.tile.Direction.*;
import static de.cubeisland.games.tile.TileType.FLOOR;
import static de.cubeisland.games.tile.TileType.WALL;

public class TileEntity extends Entity {

    public static final int SIZE = 16;
    private final TileType type;
    private final int tileX;
    private final int tileY;
    private Texture texture = null;

    public TileEntity(int x, int y, TileType type) {
        this.tileX = x;
        this.tileY = y;
        this.type = type;
        this.size = new Vector2(SIZE, SIZE);
        this.pos.set(x, y).scl(SIZE);
        this.setCollisionBox(new CollisionBox(SIZE, SIZE));
    }

    @Override
    public void render(DisconnectGame game, float delta) {
        if (!this.getWorld().getCamera().canBeSeen(this.pos, this.size))
        {
            return;
        }

        if (texture == null) {
            switch(type) {
                case FLOOR: this.texture = game.getResourcePack().textures.floor;
                    break;
                case WALL:
                        if (getWorld().hasNeighbour(this, TOP)) {
                            TileEntity neighbor = getWorld().getNeighbourOf(this, TOP);
                            if (neighbor.getType() == FLOOR) {
                                if (getWorld().hasNeighbour(this, LEFT) && getWorld().getNeighbourOf(this, LEFT).getType() == FLOOR) {
                                    if (getWorld().hasNeighbour(this, RIGHT) && getWorld().getNeighbourOf(this, RIGHT).getType() == FLOOR) {
                                        this.texture = game.getResourcePack().textures.walltopboth;
                                    } else {
                                        this.texture = game.getResourcePack().textures.walltopleft;
                                    }
                                } else if (getWorld().hasNeighbour(this, RIGHT) && getWorld().getNeighbourOf(this, RIGHT).getType() == FLOOR){
                                    this.texture = game.getResourcePack().textures.walltopright;
                                } else {
                                    this.texture = game.getResourcePack().textures.walltop;
                                }
                            }
                        }
                        if (getWorld().hasNeighbour(this, LEFT) && this.texture == null) {
                            TileEntity neighbor = getWorld().getNeighbourOf(this, LEFT);
                            if (neighbor.getType() == FLOOR) {
                                if (getWorld().hasNeighbour(neighbor, TOP) && getWorld().getNeighbourOf(neighbor, TOP).getType() == WALL) {
                                    this.texture = game.getResourcePack().textures.wallrightbottom;
                                } else {
                                    this.texture = game.getResourcePack().textures.wallright;
                                }
                            } else if (neighbor.getType() == WALL && getWorld().hasNeighbour(neighbor, TOP) && getWorld().getNeighbourOf(neighbor, TOP).getType() == FLOOR) {
                                this.texture = game.getResourcePack().textures.wallrighttop;
                            }
                        } if (getWorld().hasNeighbour(this, RIGHT) && this.texture == null) {
                            TileEntity neighbor = getWorld().getNeighbourOf(this, RIGHT);
                            if (neighbor.getType() == FLOOR) {
                                if (getWorld().hasNeighbour(neighbor, TOP) && getWorld().getNeighbourOf(neighbor, TOP).getType() == WALL) {
                                    this.texture = game.getResourcePack().textures.wallleftbottom;
                                } else {
                                    this.texture = game.getResourcePack().textures.wallleft;
                                }
                            } else if (neighbor.getType() == WALL && getWorld().hasNeighbour(neighbor, TOP) && getWorld().getNeighbourOf(neighbor, TOP).getType() == FLOOR) {
                                this.texture = game.getResourcePack().textures.walllefttop;
                            }
                        }
                        if (this.texture == null) {
                            this.texture = game.getResourcePack().textures.wall;
                        }
                    break;
                case SPAWNPOINT: this.texture = game.getResourcePack().textures.floor;
                    break;
            }
        }

        SpriteBatch batch = this.getWorld().getCamera().getSpriteBatch();
        batch.begin();
        batch.draw(this.texture, pos.x, pos.y, SIZE, SIZE);
        batch.end();

        super.render(game, delta); // TODO why is this here?
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
}
