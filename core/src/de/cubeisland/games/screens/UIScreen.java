package de.cubeisland.games.screens;

import com.badlogic.gdx.graphics.Color;
import de.cubeisland.games.Camera;
import de.cubeisland.games.DisconnectGame;

import static de.cubeisland.games.Camera.CameraType.UI;

public abstract class UIScreen extends DisconnectScreen {

    protected DisconnectGame game;
    private UIInput input;
    private Camera camera;

    public UIScreen(DisconnectGame game) {
        this.game = game;
        this.input = new UIInput(game);
        this.camera = Camera.create(UI);

    }

    @Override
    public void show() {
        setClearColor(Color.BLACK);
        game.getInputMultiplexer().prepend(input);
    }

    @Override
    public void hide() {
        game.getInputMultiplexer().remove(input);
    }

    public UIInput getInput() {
        return input;
    }

    public Camera getCamera() {
        return camera;
    }
}
