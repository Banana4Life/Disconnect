package de.cubeisland.games.screens;

import de.cubeisland.games.DisconnectGame;

import static com.badlogic.gdx.Input.Keys.ESCAPE;
import static de.cubeisland.games.screens.UIInput.Handler;

public class LoseScreen extends UIScreen {

    public LoseScreen(DisconnectGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        input.add(ESCAPE, new Handler() {
            @Override
            public boolean handle(DisconnectGame game) {
                game.exit();
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {

    }
}
