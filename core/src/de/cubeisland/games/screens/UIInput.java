package de.cubeisland.games.screens;

import com.badlogic.gdx.InputAdapter;
import de.cubeisland.games.DisconnectGame;

import java.util.HashMap;
import java.util.Map;

public class UIInput extends InputAdapter {
    private final DisconnectGame game;
    private final Map<Integer, Handler> handlers = new HashMap<>();

    public UIInput(DisconnectGame game) {
        this.game = game;
    }

    public UIInput add(int keycode, Handler handler) {
        this.handlers.put(keycode, handler);
        return this;
    }

    public UIInput add(Handler handler, int... keycodes) {
        for (int keycode : keycodes) {
            add(keycode, handler);
        }
        return this;
    }

    @Override
    public boolean keyUp(int keycode) {
        Handler h = this.handlers.get(keycode);
        return h != null && h.handle(game);
    }

    public static interface Handler {
        boolean handle(DisconnectGame game);
    }
}
