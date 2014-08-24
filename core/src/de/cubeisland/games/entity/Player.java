package de.cubeisland.games.entity;

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
    
    private Animation characterfront;
    private Animation characterback;
    private Animation characterleft;
    private Animation characterright;

    public Player(Animation characterfront, Animation characterside, Animation characterback) {
        super();

        this.characterfront = characterfront;
        TextureRegion[] tmp = new TextureRegion[characterside.getKeyFrames().length];
        int i = 0;
        for (TextureRegion keyFrame : characterside.getKeyFrames()) {
            tmp[i] = new TextureRegion(keyFrame);
            tmp[i++].flip(true, false);
        }
        this.characterleft = new Animation(characterside.getFrameDuration(), tmp);
        this.characterright = characterside;
        this.characterback = characterback;

        setCollisionBox(new CollisionBox(6, 2, 5, 0));
    }

    @Override
    public void render(DisconnectGame game, float delta) {
        SpriteBatch batch = this.getWorld().getCamera().getSpriteBatch();

        this.statetime += delta;

        if (velocity.y < 0) {
            currentKeyFrame = characterfront.getKeyFrame(this.statetime, true);
            idleFrame = characterfront.getKeyFrames()[0];
        } else if (velocity.y > 0) {
            currentKeyFrame = characterback.getKeyFrame(this.statetime, true);
            idleFrame = characterback.getKeyFrames()[0];
        } else if (velocity.x < 0) {
            currentKeyFrame = characterleft.getKeyFrame(this.statetime, true);
            idleFrame = characterleft.getKeyFrames()[0];
        } else if (velocity.x > 0) {
            currentKeyFrame = characterright.getKeyFrame(this.statetime, true);
            idleFrame = characterright.getKeyFrames()[0];
        }else if (currentKeyFrame == null) {
            currentKeyFrame = characterfront.getKeyFrame(this.statetime, true);
            idleFrame = characterfront.getKeyFrames()[0];
        } else {
            currentKeyFrame = idleFrame;
        }

        batch.begin();
        batch.draw(currentKeyFrame, pos.x, pos.y, 16, 16);
        batch.end();

        super.render(game, delta);
    }

    @Override
    public void onCollide(Entity other, Rectangle collision) {
        super.onCollide(other, collision);
        if (other instanceof TileEntity) {
            return;
        }
        if (other instanceof GhostPlayer) {
            this.getPos().set(other.getPos());
            other.die();
            PlayerInput playerInput = ((GameScreen) this.getWorld().getGame().getScreen()).getPlayerInput();
            playerInput.setMode(playerInput.getMode());
            playerInput.getOtherPlayer().getVelocity().set(this.getVelocity());
            playerInput.getOtherPlayer().getPos().set(this.getPos());
        } else {
            die();
        }
    }

    @Override
    public void onDeath() {
        getWorld().getGame().lose();
    }

    @Override
    public void update(DisconnectGame game, float delta) {
        super.update(game, delta);

        this.getWorld().getCamera().position.set(this.pos.x, this.pos.y, 0);
    }

    public TextureRegion getIdleFrame() {
        return idleFrame;
    }

    public void spawnGhost() {
        this.getWorld().spawn(new GhostPlayer(this));
    }
}
