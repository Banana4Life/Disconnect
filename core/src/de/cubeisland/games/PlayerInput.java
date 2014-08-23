package de.cubeisland.games;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import de.cubeisland.games.entity.Player;

public class PlayerInput extends InputAdapter {

    private static final float SPEED = 60;

    private Player left;
    private Player right;

    public PlayerInput(Player left, Player right) {

        this.left = left;
        this.right = right;
    }

    @Override
    public boolean keyDown(int keycode) {
        boolean l = this.handle(left.getVelocity(), keycode, true);
        boolean r = this.handle(right.getVelocity(), keycode, true);
        return l || r;
    }


    @Override
    public boolean keyUp(int keycode) {
        boolean l = this.handle(left.getVelocity(), keycode, false);
        boolean r = this.handle(right.getVelocity(), keycode, false);
        return l || r;
    }

    public boolean handle(Vector2 velocity, int keycode, boolean isDown)
    {
        if (isDown)
        {
            switch (keycode) {
                case Input.Keys.LEFT:
                    velocity.set(-SPEED, velocity.y);
                    return true;
                case Input.Keys.RIGHT:
                    velocity.set(SPEED, velocity.y);
                    return true;
                case Input.Keys.UP:
                    velocity.set(velocity.x, SPEED);
                    return true;
                case Input.Keys.DOWN:
                    velocity.set(velocity.x, -SPEED);
                    return true;
            }
        }
        else
        {
            if (keycode == Input.Keys.LEFT && velocity.x < 0) {
                velocity.x = 0;
                return true;
            }
            if (keycode == Input.Keys.RIGHT && velocity.x > 0) {
                velocity.x = 0;
                return true;
            }
            if (keycode == Input.Keys.UP && velocity.y > 0) {
                velocity.y = 0;
                return true;
            }
            if (keycode == Input.Keys.DOWN && velocity.y < 0) {
                velocity.y = 0;
                return true;
            }
        }
        return false;
    }

}
