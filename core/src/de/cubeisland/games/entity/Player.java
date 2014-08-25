package de.cubeisland.games.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.World;
import de.cubeisland.games.entity.collision.CollisionBox;
import de.cubeisland.games.util.SoundPlayer;

public class Player extends AnimatedEntity {
    private float statetime = 0f;

    private TextureRegion idleFrame;
    private Item carriedItem = null;
    
    private Animation characterFront;
    private Animation characterBack;
    private Animation characterLeft;
    private Animation characterRight;

    private SoundPlayer.SoundInstance step;
    private Player otherPlayer;

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
        SpriteBatch batch = this.getWorld().beginBatch();
        batch.draw(currentKeyFrame, pos.x, pos.y, 16, 16);
        batch.end();
        super.render(game, delta);

        if (currentKeyFrame == idleFrame)
        {
            this.step.pause();
        }
        else
        {
            this.step.resume();
        }
    }

    public TextureRegion getItemInHandTex() {
        if (carriedItem == null) {
            return null;
        }
        return carriedItem.getTex();
    }

    @Override
    public void updateAnimation() {
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
            currentKeyFrame = animation.getKeyFrame(this.statetime, true);
            idleFrame = animation.getKeyFrames()[0];
        } else {
            currentKeyFrame = idleFrame;
        }
    }

    @Override
    public void onCollide(Entity other, Rectangle collision) {
        other.interact(carriedItem, this);
    }

    @Override
    public void onTileCollide(TileEntity tile, Rectangle collisionBox) {
        super.onTileCollide(tile, collisionBox);
        tile.interact(carriedItem, this);
        if (this.ghost == null || !this.ghost.isAlive()) {
            this.otherPlayer.getPos().set(this.getPos());
            this.otherPlayer.skipMovement();
        }
    }

    @Override
    public void onDeath() {
        getWorld().getGame().lose();
        this.step.stop();
    }

    @Override
    public void update(DisconnectGame game, float delta) {
        this.getWorld().getCamera().position.set(this.pos.x, this.pos.y, 0);
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

    public void switchItems() {
        if (this.carriedItem == null)
        {
            return;
        }
        this.otherPlayer.getWorld().spawn(this.carriedItem.getClass(), this.getPos());
        this.carriedItem = null;
    }

    public void setItem(Item item) {
        this.carriedItem = item;
    }
}
