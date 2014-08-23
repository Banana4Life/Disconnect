package de.cubeisland.games;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import de.cubeisland.engine.reflect.Reflector;
import de.cubeisland.games.resource.LudumResourcePack;
import de.cubeisland.games.screens.GameScreen;

public class DisconnectGame extends Game {

    private InputMultiplexer inputMultiplexer;
    private LudumResourcePack resourcePack;
    private Reflector reflector;
    private Camera guiCamera;

    @Override
    public void create() {
        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);

        this.reflector = new Reflector();
        this.resourcePack = new LudumResourcePack(Files.FileType.Internal, reflector);
        this.resourcePack.build();

        this.guiCamera = Camera.gui();

        setScreen(new GameScreen(this));
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.guiCamera.resize(width, height);
    }

    public InputMultiplexer getInputMultiplexer() {
        return inputMultiplexer;
    }

    public Camera getGuiCamera() {
        return guiCamera;
    }

    public LudumResourcePack getResourcePack() {
        return resourcePack;
    }
}
