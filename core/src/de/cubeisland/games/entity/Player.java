package de.cubeisland.games.entity;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.cubeisland.games.Camera;
import de.cubeisland.games.DisconnectGame;

import static com.badlogic.gdx.Input.Keys;
import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled;

public class Player extends Entity {

    private static final float SPEED = 60;

    @Override
    public void render(DisconnectGame game, float delta) {
        ShapeRenderer r = game.getShapeRenderer();
        r.begin(Filled);
        r.setColor(Color.RED);
        r.circle(pos.x, pos.y, 8);
        r.end();
        game.getInputMultiplexer().addProcessor(new Input());
    }

    @Override
    public void update(DisconnectGame game, float delta) {
        super.update(game, delta);

        Camera camera = game.getCamera();
        camera.position.set(this.pos.x, this.pos.y, 0);
        camera.update();
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
