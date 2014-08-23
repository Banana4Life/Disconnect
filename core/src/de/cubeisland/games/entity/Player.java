package de.cubeisland.games.entity;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.entity.collision.CollisionBox;

import static com.badlogic.gdx.Input.Keys;

public class Player extends Entity {

    private static final float SPEED = 60;
    private float statetime = 0f;

    public Player() {
        setCollisionBox(new CollisionBox(6, 1, 16 - 1, 0));
    }

    @Override
    public void render(DisconnectGame game, float delta) {
        SpriteBatch batch = game.getCameraBot().use().spriteBatch;

        batch.begin();
        this.statetime += delta;
        batch.draw(game.getResourcePack().animations.character.getKeyFrame(this.statetime, true), pos.x, pos.y, 16, -16);
        batch.end();

        game.getInputMultiplexer().addProcessor(new Input());
    }

    @Override
    public void update(DisconnectGame game, float delta) {
        super.update(game, delta);

        game.getCameraBot().position.set(this.pos.x, this.pos.y, 0);
        game.getCameraTop().position.set(this.pos.x, this.pos.y, 0);
    }

    @Override
    public void onCollide(Entity other, Vector2 mtv) {

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
                    velocity.set(velocity.x, -SPEED);
                    return true;
                case Keys.DOWN:
                    velocity.set(velocity.x, SPEED);
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
            if (keycode == Keys.UP && velocity.y < 0) {
                velocity.y = 0;
                return true;
            }
            if (keycode == Keys.DOWN && velocity.y > 0) {
                velocity.y = 0;
                return true;
            }
            return false;
        }
    }
}
