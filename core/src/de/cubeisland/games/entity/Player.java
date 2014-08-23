package de.cubeisland.games.entity;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.entity.collision.CollisionBox;

import static com.badlogic.gdx.Input.Keys;

public class Player extends Entity {

    private static final float SPEED = 60;
    private float statetime = 0f;
    private TextureRegion currentKeyFrame;
    private Input input;

    public Player(Vector2 pos) {
        this.pos = new Vector2(pos);
        setCollisionBox(new CollisionBox(6, 1, 5, 0));
    }

    @Override
    public void render(DisconnectGame game, float delta) {
        if (input == null) {
            input = new Input();
            game.getInputMultiplexer().addProcessor(input);
        }

        SpriteBatch batch = this.getWorld().getCamera().getSpriteBatch();

        this.statetime += delta;

        if (velocity.y < 0) {
            currentKeyFrame = game.getResourcePack().animations.characterfront.getKeyFrame(this.statetime, true);
        } else if (velocity.y > 0) {
            currentKeyFrame = game.getResourcePack().animations.characterback.getKeyFrame(this.statetime, true);
        } else if (currentKeyFrame == null) {
            currentKeyFrame = game.getResourcePack().animations.characterfront.getKeyFrame(this.statetime, true);
        }

        batch.begin();
        batch.draw(currentKeyFrame, pos.x, pos.y, 16, 16);
        batch.end();

        super.render(game, delta);
    }

    @Override
    public void update(DisconnectGame game, float delta) {
        super.update(game, delta);

        this.getWorld().getCamera().position.set(this.pos.x, this.pos.y, 0);
    }

    @Override
    public void onCollide(Entity other, Vector2 mtv) {

    }

    @Override
    public void onTileCollide(TileEntity tile, Vector2 mtv) {
        System.out.println(tile.getType());
        System.out.println(mtv);
        getPos().add(mtv);
    }

    private final class Input extends InputAdapter {
        @Override
        public boolean keyDown(int keycode) {
            switch (keycode) {
                case Keys.LEFT:
                    velocity.set(-SPEED, velocity.y);
                    return true;
                case Keys.RIGHT:
                    velocity.set(SPEED, velocity.y);
                    return true;
                case Keys.UP:
                    velocity.set(velocity.x, SPEED);
                    return true;
                case Keys.DOWN:
                    velocity.set(velocity.x, -SPEED);
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public boolean keyUp(int keycode) {
            if (keycode == Keys.LEFT && velocity.x < 0) {
                velocity.x = 0;
                return true;
            }
            if (keycode == Keys.RIGHT && velocity.x > 0) {
                velocity.x = 0;
                return true;
            }
            if (keycode == Keys.UP && velocity.y > 0) {
                velocity.y = 0;
                return true;
            }
            if (keycode == Keys.DOWN && velocity.y < 0) {
                velocity.y = 0;
                return true;
            }
            return false;
        }
    }
}
