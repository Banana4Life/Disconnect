package de.cubeisland.games.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.entity.collision.CollisionBox;
import de.cubeisland.games.resource.bag.Animations;
import de.cubeisland.games.tile.Direction;
import de.cubeisland.games.tile.TileType;

import static de.cubeisland.games.tile.TileType.*;

public abstract class AbstractDoor extends TileEntity {
    protected float statetime = 0f;
    protected Animation animation;
    protected DoorState state = DoorState.CLOSED;

    protected AbstractDoor(int x, int y, TileType type) {
        super(x, y, type);
    }

    public void close() {
        if (state == DoorState.OPENED) {
            this.state = DoorState.CLOSING;
            setCollisionBox(new CollisionBox(SIZE, SIZE));
        }
    }

    public void open() {
        if (state == DoorState.CLOSED) {
            this.state = DoorState.OPENING;
            this.texture = this.getWorld().getGame().getResourcePack().textures.floor;
            getWorld().getGame().getResourcePack().sounds.door.start(0.20f);
        }
    }

    public boolean isHorizontal() {
        return WALL.isType(getWorld().getNeighbourOf(this, Direction.LEFT))
                && WALL.isType(getWorld().getNeighbourOf(this, Direction.RIGHT));
    }

    @Override
    public void render(DisconnectGame game, float delta) {
        if (!this.getWorld().getCamera().canBeSeen(this.pos, this.size)) {
            return;
        }

        boolean horizontal = this.isHorizontal();
        Animations animations = this.getWorld().getGame().getResourcePack().animations;
        this.animation = horizontal ? animations.doorhorizontal : animations.doorvertical;
        this.overlayOffset = horizontal ? new Vector2(0, 0) : new Vector2(0, 13);

        if (this.state == DoorState.CLOSED) {
            if (horizontal) {
                this.texture = animation.getKeyFrames()[0];
            } else {
                this.texture = this.getWorld().getGame().getResourcePack().textures.floor;
                this.overlay = animation.getKeyFrames()[0];
            }
        }

        if (this.state == DoorState.OPENING) {
            statetime += delta;
            this.overlay = this.animation.getKeyFrame(statetime);
            if (animation.getKeyFrameIndex(statetime) == animation.getKeyFrames().length - 1) {
                this.state = DoorState.OPENED;
                if (this.type == AUTO_DOOR) {
                    this.type = AUTO_DOOR_OPEN;
                } else if (this.type == DOOR) {
                    this.type = DOOR_OPEN;
                }
                this.setCollisionBox(null);
            }
        }

        if (this.state == DoorState.CLOSING) {
            statetime += delta;
            TextureRegion[] keyFrames = this.animation.getKeyFrames();
            this.overlay = keyFrames[keyFrames.length - this.animation.getKeyFrameIndex(statetime)];
            if (animation.getKeyFrameIndex(statetime) == animation.getKeyFrames().length - 1) // TODO or 0 ?
            {
                this.state = DoorState.CLOSED;
            }
        }

        SpriteBatch batch = this.getWorld().beginBatch();
        batch.draw(this.texture, pos.x, pos.y, SIZE, SIZE);
        batch.end();
    }

    @Override
    public boolean isBlocking() {
        switch (state) {
            case OPENED:
                return false;
            case OPENING:
            case CLOSING:
            case CLOSED:
                return true;
        }
        return true;
    }

    protected enum DoorState {
        OPENED, OPENING, CLOSING, CLOSED
    }
}
