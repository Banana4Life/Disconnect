package de.cubeisland.games.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.tile.TileType;

public class TileEntity extends Entity {

    private static final int SIZE = 16;
    private final TileType type;
    private final int tileX;
    private final int tileY;

    public TileEntity(int x, int y, TileType type) {
        this.tileX = x;
        this.tileY = y;
        this.type = type;
        this.size = new Vector2(SIZE, SIZE);
        this.pos.set(x, y).scl(SIZE);
    }

    @Override
    public void render(DisconnectGame game, float delta) {
        if (!game.getCamera().canBeSeen(this.pos, this.size))
        {
            return;
        }
        
        SpriteBatch batch = game.getSpriteBatch();
        
        batch.begin();
        batch.draw(game.getResourcePack().textures.tilemap, pos.x, pos.y, 16, 16, SIZE, SIZE);
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
}
