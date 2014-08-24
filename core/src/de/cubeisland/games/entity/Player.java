package de.cubeisland.games.entity;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.PlayerInput;
import de.cubeisland.games.entity.collision.CollisionBox;
import de.cubeisland.games.screens.GameScreen;

public class Player extends Entity {
    private float statetime = 0f;
    private TextureRegion currentKeyFrame;
    private TextureRegion idleFrame;
    private Item carriedItem = null;
    
    private Animation characterFront;
    private Animation characterBack;
    private Animation characterLeft;
    private Animation characterRight;

    private Sound step;
    private long soundId;
    private Player otherPlayer;
    private boolean skipUpdate = false;

    public Player(Animation characterFront, Animation characterSide, Animation characterBack) {
        super();

        this.characterFront = characterFront;
        TextureRegion[] tmp = new TextureRegion[characterSide.getKeyFrames().length];
        int i = 0;
        for (TextureRegion keyFrame : characterSide.getKeyFrames()) {
            tmp[i] = new TextureRegion(keyFrame);
            tmp[i++].flip(true, false);
        }
        this.characterLeft = new Animation(characterSide.getFrameDuration(), tmp);
        this.characterRight = characterSide;
        this.characterBack = characterBack;

        setCollisionBox(new CollisionBox(6, 2, 5, 0));
    }

    @Override
    public void onSpawn() {
        this.step = getWorld().getGame().getResourcePack().sounds.step;
        this.soundId = this.step.play(.10f);
        this.step.pause(this.soundId);
        this.step.setLooping(this.soundId, true);
    }

    @Override
    public void render(DisconnectGame game, float delta) {
        SpriteBatch batch = this.getWorld().getCamera().getSpriteBatch();

        this.statetime += delta;

        Animation animation = null;
        if (currentKeyFrame == null || velocity.y < 0) {
            animation = characterFront;
        } else if (velocity.y > 0) {
            animation = characterBack;
        } else if (velocity.x < 0) {
            animation = characterLeft;
        } else if (velocity.x > 0) {
            animation = characterRight;
        }

        if (animation != null) {
            this.step.resume(this.soundId);
            currentKeyFrame = animation.getKeyFrame(this.statetime, true);
            idleFrame = animation.getKeyFrames()[0];
        } else {
            this.step.pause(this.soundId);
            currentKeyFrame = idleFrame;
        }

        batch.begin();
        batch.draw(currentKeyFrame, pos.x, pos.y, 16, 16);
        batch.end();

        super.render(game, delta);
    }

    @Override
    public void onCollide(Entity other, Rectangle collision) {
        if (other instanceof GhostPlayer) {
            this.getPos().set(other.getPos());
            other.die();
            PlayerInput playerInput = ((GameScreen) this.getWorld().getGame().getScreen()).getPlayerInput();
            playerInput.setMode(playerInput.getMode());
            playerInput.getOtherPlayer().getVelocity().set(this.getVelocity());
            playerInput.getOtherPlayer().getPos().set(this.getPos());
        } else if (other instanceof Item) {
            other.die();
            this.carriedItem = (Item) other;
        } else {
            die();
        }
    }

    @Override
    public boolean onTileCollide(TileEntity tile, Rectangle collisionBox) {
        super.onTileCollide(tile, collisionBox);
        this.otherPlayer.getPos().set(this.getPos());
        this.otherPlayer.skipUpdate();
        tile.interact(carriedItem);
        return true;
    }

    private void skipUpdate() {
        this.skipUpdate = true;
    }

    @Override
    public void onDeath() {
        getWorld().getGame().lose();
    }

    @Override
    public void update(DisconnectGame game, float delta) {
        if (skipUpdate)
        {
            skipUpdate = false;
            return;
        }
        super.update(game, delta);

        this.getWorld().getCamera().position.set(this.pos.x, this.pos.y, 0);
    }

    public TextureRegion getIdleFrame() {
        return idleFrame;
    }

    public void spawnGhost() {
        this.getWorld().spawn(new GhostPlayer(this));
    }

    public void setOtherPlayer(Player otherPlayer) {
        this.otherPlayer = otherPlayer;
    }
}
