package de.cubeisland.games;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import de.cubeisland.games.entity.Player;

public class PlayerInput extends InputAdapter {

    private static final float SPEED = 60;

    private Player left;
    private Player right;

    private Mode mode = Mode.LEFT;

    public PlayerInput(Player left, Player right) {

        this.left = left;
        this.right = right;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.SPACE:
                switch (mode) {
                    case LEFT:
                        mode = Mode.LEFT_SINGLE;
                        left.spawnGhost();
                        right.getVelocity().set(0, 0);
                        break;
                    case RIGHT:
                        mode = Mode.RIGHT_SINGLE;
                        right.spawnGhost();
                        left.getVelocity().set(0, 0);
                        break;
                }
                return true;
            case Input.Keys.CONTROL_LEFT:
                switch (mode) {
                    case LEFT:
                        mode = Mode.RIGHT;
                        break;
                    case RIGHT:
                        mode = Mode.LEFT;
                        break;
                }
                return true;
        }
        boolean l = !mode.left || this.handle(left.getVelocity(), keycode, true);
        boolean r = !mode.right || this.handle(right.getVelocity(), keycode, true);
        return l || r;
    }


    @Override
    public boolean keyUp(int keycode) {
        boolean l = this.handle(left.getVelocity(), keycode, false);
        boolean r = this.handle(right.getVelocity(), keycode, false);
        return l || r;
    }

    public boolean handle(Vector2 velocity, int keycode, boolean isDown) {
        if (isDown) {
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
        } else {
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

    public Mode getMode() {
        if (this.mode == Mode.LEFT || this.mode == Mode.LEFT_SINGLE) {
            return Mode.LEFT;
        } else {
            return Mode.RIGHT;
        }
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Player getCurrentPlayer() {
        if (getMode() == Mode.LEFT) {
            return left;
        } else {
            return right;
        }
    }

    public Player getOtherPlayer() {
        if (getMode() == Mode.LEFT) {
            return right;
        } else {
            return left;
        }
    }

    public enum Mode {
        LEFT, RIGHT, LEFT_SINGLE(true, false), RIGHT_SINGLE(false, true);
        public final boolean right;
        public final boolean left;

        Mode(boolean left, boolean right) {
            this.left = left;
            this.right = right;
        }

        Mode() {
            this(true, true);
        }
    }

    public boolean getDisconnected() {
        if (this.mode == Mode.LEFT_SINGLE || this.mode == Mode.RIGHT_SINGLE) {
            return true;
        }
        return false;
    }
}
