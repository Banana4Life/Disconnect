package de.cubeisland.games.screens;

import de.cubeisland.games.DisconnectGame;

import static com.badlogic.gdx.Input.Keys.ENTER;
import static com.badlogic.gdx.Input.Keys.SPACE;
import static de.cubeisland.games.screens.UIInput.Handler;

public class TitleScreen extends UIScreen {

    public TitleScreen(DisconnectGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        input.add(new Handler() {
            @Override
            public boolean handle(DisconnectGame game) {
                game.transition(GameScreen.class);
                return true;
            }
        }, SPACE, ENTER);
    }

    @Override
    public void render(float delta) {

    }
}
