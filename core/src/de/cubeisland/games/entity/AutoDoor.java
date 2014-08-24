package de.cubeisland.games.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.resource.bag.Animations;
import de.cubeisland.games.tile.Direction;
import de.cubeisland.games.tile.TileType;

import static de.cubeisland.games.tile.TileType.*;

public class Autodoor extends TileEntity {
    private float statetime = 0f;

    public Autodoor(int x, int y, TileType type) {
        super(x, y, type);
    }

    @Override
    public void render(DisconnectGame game, float delta) {
        if (!this.getWorld().getCamera().canBeSeen(this.pos, this.size)) {
            return;
        }

        Animations animations = this.getWorld().getGame().getResourcePack().animations;

        if (this.texture == null) {
            if (isHorizontal()) {
                this.texture = animations.doorhorizontal.getKeyFrames()[0];
                this.overlayOffset = new Vector2(0, 0);
            } else {
                this.texture = this.getWorld().getGame().getResourcePack().textures.floor;
                this.overlay = animations.doorvertical.getKeyFrames()[0];
                this.overlayOffset = new Vector2(0, 13);
            }
        }

        if (this.type == DOOR_OPENING) {
            statetime += delta;
            if (isHorizontal()) {
                this.overlay = animations.doorhorizontal.getKeyFrame(statetime);
                if (animations.doorhorizontal.getKeyFrameIndex(statetime) == animations.doorhorizontal.getKeyFrames().length) {
                    this.type = DOOR_OPEN;
                }
            } else {
                this.overlay = animations.doorvertical.getKeyFrame(statetime);
                if (animations.doorvertical.getKeyFrameIndex(statetime) == animations.doorvertical.getKeyFrames().length) {
                    this.type = DOOR_OPEN;
                }
            }
        }

        SpriteBatch batch = this.getWorld().getCamera().getSpriteBatch();
        batch.begin();
        batch.draw(this.texture, pos.x, pos.y, SIZE, SIZE);
        batch.end();
    }

    @Override
    public void interact(Item carriedItem, Player player) {

    }

    public boolean isHorizontal() {
        if (getWorld().getNeighbourOf(this, Direction.LEFT).getType() == WALL && getWorld().getNeighbourOf(this, Direction.RIGHT).getType() == WALL) {
            return true;
        }
        return false;
    }

    public void open() {
        this.type = DOOR_OPENING;
        this.texture = this.getWorld().getGame().getResourcePack().textures.floor;
        getWorld().getGame().getResourcePack().sounds.door.start(0.20f);
    }
}
