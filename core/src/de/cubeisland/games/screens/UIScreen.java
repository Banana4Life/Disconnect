package de.cubeisland.games.screens;

import de.cubeisland.games.DisconnectGame;

public abstract class UIScreen extends DisconnectScreen {

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
