package de.cubeisland.games.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.PlayerInput;
import de.cubeisland.games.World;
import de.cubeisland.games.entity.collision.CollisionBox;
import de.cubeisland.games.screens.GameScreen;
import de.cubeisland.games.util.SoundPlayer;

public class Player extends Entity {
    private float statetime = 0f;
    private TextureRegion currentKeyFrame;
    private TextureRegion idleFrame;
    private Item carriedItem = null;
    
    private Animation characterFront;
    private Animation characterBack;
    private Animation characterLeft;
    private Animation characterRight;

    private SoundPlayer.SoundInstance step;
    private Player otherPlayer;
    private boolean skipUpdate = false;

    private GhostPlayer ghost = null;

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
    public void onSpawn(World world) {
        super.onSpawn(world);
        this.step = getWorld().getGame().getResourcePack().sounds.step.start(.05f).pause().looping(true);
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
            this.step.resume();
            currentKeyFrame = animation.getKeyFrame(this.statetime, true);
            idleFrame = animation.getKeyFrames()[0];
        } else {
            this.step.pause();
            currentKeyFrame = idleFrame;
        }

        batch.begin();
        batch.draw(currentKeyFrame, pos.x, pos.y, 16, 16);
        batch.end();

        super.render(game, delta);
    }

    public TextureRegion getItemInHandTex() {
        if (carriedItem == null) {
            return null;
        }
        return carriedItem.getTex();
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
            ((GameScreen)getWorld().getGame().getScreen()).stopTimeSound();
        } else if (other instanceof Item) {
            if (this.carriedItem != null)
            {
                this.getWorld().spawn(carriedItem.getClass(), carriedItem.getPos());
            }
            this.carriedItem = (Item) other;
            other.setCollisionBox(null);
            this.carriedItem.die();
        } else {
            die();
        }
    }

    @Override
    public void onTileCollide(TileEntity tile, Rectangle collisionBox) {
        super.onTileCollide(tile, collisionBox);
        if (tile instanceof Door)
        {
            System.out.println("DOOR " + ((Door) tile).state.name());
        }
        tile.interact(carriedItem, this);
        if (this.ghost == null || !this.ghost.isAlive()) {
            this.otherPlayer.getPos().set(this.getPos());
            this.otherPlayer.skipUpdate();
        }
    }

    private void skipUpdate() {
        this.skipUpdate = true;
    }

    @Override
    public void onDeath() {
        getWorld().getGame().lose();
        this.step.stop();
    }

    @Override
    public void update(DisconnectGame game, float delta) {
        this.getWorld().getCamera().position.set(this.pos.x, this.pos.y, 0);
        if (skipUpdate)
        {
            skipUpdate = false;
            return;
        }
        super.update(game, delta);
    }

    public TextureRegion getIdleFrame() {
        return idleFrame;
    }

    public void spawnGhost() {
        this.ghost = new GhostPlayer(this);
        this.getWorld().spawn(ghost);
    }

    public void setOtherPlayer(Player otherPlayer) {
        this.otherPlayer = otherPlayer;
    }

    public Player getOtherPlayer() {
        return otherPlayer;
    }

    public void useItem() {
        this.carriedItem = null;
    }
}
