package de.cubeisland.games.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.tile.TileType;
import static de.cubeisland.games.tile.TileType.*;

public class Door extends TileEntity {
    public Door(int x, int y) {
        super(x, y, TileType.DOOR);
    }

    @Override
    public void render(DisconnectGame game, float delta) {
        if (!this.getWorld().getCamera().canBeSeen(this.pos, this.size)) {
            return;
        }

        if (this.texture == null) {
            this.texture = this.getWorld().getGame().getResourcePack().animations.doorhorizontal.getKeyFrames()[0];
        }

        SpriteBatch batch = this.getWorld().getCamera().getSpriteBatch();
        batch.begin();
        batch.draw(this.texture, pos.x, pos.y, SIZE, SIZE);
        batch.end();
    }

    public void interact(Item carriedItem) {
        if (carriedItem instanceof Key) {
            this.texture = this.getWorld().getGame().getResourcePack().textures.floor;
            this.overlay = this.getWorld().getGame().getResourcePack().animations.doorhorizontal.getKeyFrames()[this.getWorld().getGame().getResourcePack().animations.doorhorizontal.getKeyFrames().length - 1];
            this.overlayOffset = new Vector2(0, 0);
            this.type = DOOR_OPEN;
        }
    }
}
