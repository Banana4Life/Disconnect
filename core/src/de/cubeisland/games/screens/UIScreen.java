package de.cubeisland.games.screens;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import de.cubeisland.games.DisconnectGame;

public class UIScreen extends ScreenAdapter {

    protected DisconnectGame game;
    protected UIInput input;

    public UIScreen(DisconnectGame game) {
        this.game = game;
        input = new UIInput(game);
    }

    @Override
    public void show() {
        game.getInputMultiplexer().addProcessor(input);
    }

    @Override
    public void hide() {
        game.getInputMultiplexer().removeProcessor(input);
    }
}
