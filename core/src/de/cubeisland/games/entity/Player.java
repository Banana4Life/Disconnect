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

    public Player() {
        super();

        setCollisionBox(new CollisionBox(6, 2, 5, 0));
    }

    @Override
    public void render(DisconnectGame game, float delta) {
        SpriteBatch batch = this.getWorld().getCamera().getSpriteBatch();

        this.statetime += delta;

        if (velocity.y < 0) {
            currentKeyFrame = game.getResourcePack().animations.characterfront.getKeyFrame(this.statetime, true);
            idleFrame = game.getResourcePack().animations.characterfront.getKeyFrames()[0];
        } else if (velocity.y > 0) {
            currentKeyFrame = game.getResourcePack().animations.characterback.getKeyFrame(this.statetime, true);
            idleFrame = game.getResourcePack().animations.characterback.getKeyFrames()[0];
        } else if (velocity.x < 0) {
            if (game.getResourcePack().animations.characterleft == null) {
                TextureRegion[] tmp = new TextureRegion[game.getResourcePack().animations.characterside.getKeyFrames().length];
                int i = 0;
                for (TextureRegion keyFrame : game.getResourcePack().animations.characterside.getKeyFrames()) {
                    tmp[i] = new TextureRegion(keyFrame);
                    tmp[i++].flip(true, false);
                }
                game.getResourcePack().animations.characterleft = new Animation(game.getResourcePack().animations.characterside.getFrameDuration(), tmp);
            }
            currentKeyFrame = game.getResourcePack().animations.characterleft.getKeyFrame(this.statetime, true);
            idleFrame = game.getResourcePack().animations.characterleft.getKeyFrames()[0];
        } else if (velocity.x > 0) {
            if (game.getResourcePack().animations.characterright == null) {
                game.getResourcePack().animations.characterright = new Animation(game.getResourcePack().animations.characterside.getFrameDuration(), game.getResourcePack().animations.characterside.getKeyFrames().clone());
            }
            currentKeyFrame = game.getResourcePack().animations.characterright.getKeyFrame(this.statetime, true);
            idleFrame = game.getResourcePack().animations.characterright.getKeyFrames()[0];
        }else if (currentKeyFrame == null) {
            currentKeyFrame = game.getResourcePack().animations.characterfront.getKeyFrame(this.statetime, true);
            idleFrame = game.getResourcePack().animations.characterfront.getKeyFrames()[0];
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
