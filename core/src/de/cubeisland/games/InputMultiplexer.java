package de.cubeisland.games;

import com.badlogic.gdx.InputProcessor;

import java.util.LinkedList;

public class InputMultiplexer implements InputProcessor {
    private final LinkedList<InputProcessor> processors = new LinkedList<>();
    private final InputProcessor fallback;

    public InputMultiplexer() {
        this(null);
    }

    public InputMultiplexer(InputProcessor fallback) {
        this.fallback = fallback;
    }

    public InputMultiplexer append(InputProcessor processor) {
        this.processors.addLast(processor);
        return this;
    }

    public InputMultiplexer prepend(InputProcessor processor) {
        this.processors.addLast(processor);
        return this;
    }

    public InputMultiplexer remove(InputProcessor processor) {
        this.processors.remove(processor);
        return this;
    }

    @Override
    public boolean keyDown(int keycode) {
        for (InputProcessor processor : processors) {
            if (processor.keyDown(keycode)) {
                return true;
            }
        }
        return this.fallback != null && this.fallback.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        for (InputProcessor processor : processors) {
            if (processor.keyUp(keycode)) {
                return true;
            }
        }
        return this.fallback != null && this.fallback.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        for (InputProcessor processor : processors) {
            if (processor.keyTyped(character)) {
                return true;
            }
        }
        return this.fallback != null && this.fallback.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        for (InputProcessor processor : processors) {
            if (processor.touchDown(screenX, screenY, pointer, button)) {
                return true;
            }
        }
        return this.fallback != null && this.fallback.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        for (InputProcessor processor : processors) {
            if (processor.touchUp(screenX, screenY, pointer, button)) {
                return true;
            }
        }
        return this.fallback != null && this.fallback.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        for (InputProcessor processor : processors) {
            if (processor.touchDragged(screenX, screenY, pointer)) {
                return true;
            }
        }
        return this.fallback != null && this.fallback.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        for (InputProcessor processor : processors) {
            if (processor.mouseMoved(screenX, screenY)) {
                return true;
            }
        }
        return this.fallback != null && this.fallback.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        for (InputProcessor processor : processors) {
            if (processor.scrolled(amount)) {
                return true;
            }
        }
        return this.fallback != null && this.fallback.scrolled(amount);
    }
}
