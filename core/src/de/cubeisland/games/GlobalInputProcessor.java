package de.cubeisland.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import java.awt.*;

import static com.badlogic.gdx.Input.Keys.*;

public class GlobalInputProcessor implements InputProcessor {

    private boolean altDown = false;

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case ALT_LEFT:
            case ALT_RIGHT:
                this.altDown = true;
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case ESCAPE:
                Gdx.app.exit();
                return true;
            case ENTER:
                if (altDown) {
                    Gdx.graphics.setDisplayMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), !Gdx.graphics.isFullscreen());
                    return true;
                }
                return false;
            case ALT_LEFT:
            case ALT_RIGHT:
                this.altDown = false;
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}