package de.cubeisland.games.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.entity.collision.CollisionBox;
import de.cubeisland.games.tile.TileType;

import static de.cubeisland.games.tile.Direction.*;
import static de.cubeisland.games.tile.TileType.FLOOR;

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
        if (!game.getCamera1().canBeSeen(this.pos, this.size))
        {
            return;
        }
        


        if (texture == null) {
            switch(type) {
                case FLOOR: this.texture = game.getResourcePack().textures.floor;
                    break;
                case WALL:
                        if (getWorld().hasNeighbour(this, BOTTOM) && getWorld().getNeighbourOf(this, BOTTOM).getType() == FLOOR) {
                            this.texture = game.getResourcePack().textures.walltop;
                        } else if (getWorld().hasNeighbour(this, LEFT) && getWorld().getNeighbourOf(this, LEFT).getType() == FLOOR) {
                            this.texture = game.getResourcePack().textures.wallright;
                        } else if (getWorld().hasNeighbour(this, TOP) && getWorld().getNeighbourOf(this, TOP).getType() == FLOOR) {
                            this.texture = game.getResourcePack().textures.wallbottom;
                        } else if (getWorld().hasNeighbour(this, RIGHT) && getWorld().getNeighbourOf(this, RIGHT).getType() == FLOOR) {
                            this.texture = game.getResourcePack().textures.wallleft;
                        } else {
                            this.texture = game.getResourcePack().textures.wall;
                        }
                    break;
            }
        }

        SpriteBatch batch = game.getCamera2().use().spriteBatch;
        batch.begin();
        batch.draw(this.texture, pos.x, pos.y + SIZE, SIZE, -SIZE);
        batch.end();

        // Clone for other cam
        batch = game.getCamera1().use().spriteBatch;
        batch.begin();
        batch.draw(this.texture, pos.x, pos.y + SIZE, SIZE, -SIZE);
        batch.end();
        super.render(game, delta);
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
