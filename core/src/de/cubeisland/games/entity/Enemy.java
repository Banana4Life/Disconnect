package de.cubeisland.games.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.World;
import de.cubeisland.games.entity.collision.CollisionBox;
import de.cubeisland.games.resource.bag.Animations;
import de.cubeisland.games.screens.GameScreen;
import de.cubeisland.games.tile.Direction;
import de.cubeisland.games.util.SoundPlayer;

import static de.cubeisland.games.entity.collision.Collider.isLineOfSightClear;

public class Enemy extends Entity {

    public static final int SPEED = 20;
    public static final float RANGE = 50;
    public static final float FOV = 50;
    private static final int SIZE = 5;

    private Animation characterFront;
    private Animation characterBack;
    private Animation characterLeft;
    private Animation characterRight;

    private SoundPlayer.SoundInstance step;

    private TextureRegion currentKeyFrame = null;
    private float statetime = 0f;

    private boolean aggro = false;

    @Override
    public void onSpawn(World world) {
        super.onSpawn(world);
        Direction dir = Direction.random();
        getVelocity().set(SPEED * dir.getX(), SPEED * dir.getY());
        setCollisionBox(new CollisionBox(6, 4, 5, 0));

        Animations animations =  getWorld().getGame().getResourcePack().animations;

        this.characterFront = animations.securityfront;
        TextureRegion[] tmp = new TextureRegion[animations.securityside.getKeyFrames().length];
        int i = 0;
        for (TextureRegion keyFrame : animations.securityside.getKeyFrames()) {
            tmp[i] = new TextureRegion(keyFrame);
            tmp[i++].flip(true, false);
        }
        this.characterLeft = new Animation(animations.securityside.getFrameDuration(), tmp);
        this.characterRight = animations.securityside;
        this.characterBack = animations.securityback;

        this.step = getWorld().getGame().getResourcePack().sounds.step.start(.0020f).pause().looping(true);
    }

    @Override
    public void onTileCollide(TileEntity tile, Rectangle helper3) {
        velocity.scl(-1);
        super.onTileCollide(tile, helper3);
    }

    @Override
    public void update(DisconnectGame game, float delta) {
        Player player = getWorld().getPlayer();
        CollisionBox playerCB = player.getCollisionBox();
        playerDistance
                .set(player.getPos())
                .add(playerCB.getOffsetX() + playerCB.getWidth() / 2f, playerCB.getOffsetY() + playerCB.getHeight() / 2f)
                .sub(pos);
        if (aggro) {
            velocity.set(playerDistance.cpy().nor().scl(SPEED));
        }
        super.update(game, delta);
    }

    private Vector2 playerDistance = new Vector2(0, 0);

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
            currentKeyFrame = animation.getKeyFrame(this.statetime, true);
            if (getWorld().getCamera().canBeSeen(pos, new Vector2(currentKeyFrame.getRegionWidth(), currentKeyFrame.getRegionWidth()))) {
                this.step.resume();
            }
        } else {
            this.step.pause();
        }

        batch.begin();
        batch.draw(currentKeyFrame, pos.x, pos.y, 16, 16);
        batch.end();

        float viewAngle = velocity.angle();
        float playerAngle = playerDistance.angle();
        float diffAngle = Math.abs(viewAngle - playerAngle);

        ShapeRenderer r = getWorld().getCamera().getShapeRenderer();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Vector2 center = pos.cpy().add(SIZE, SIZE);
        r.setColor(0,1,0,0.6f);
        if (playerDistance.len2() <= RANGE * RANGE && diffAngle <= (FOV / 2f) && isLineOfSightClear(getWorld(), pos, pos.cpy().add(playerDistance))) {
            r.setColor(1, 0, 0, 0.6f);
            aggro = true;
            Screen screen = getWorld().getGame().getScreen();
            if (screen instanceof GameScreen) {
                ((GameScreen) screen).startAlarm();
            }
            for (Door door : getWorld().findAllDoors()) {
                door.close();
            }
        }
        aggro = false;
        r.begin(ShapeRenderer.ShapeType.Filled);
        r.arc(center.x, center.y, RANGE, velocity.angle() - FOV / 2f, FOV);
        r.end();
    }

    @Override
    public void onDeath() {
        step.stop();
    }

    @Override
    public void interact(Item carriedItem, Player player) {
        player.die();
    }
}
