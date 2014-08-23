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
    private Camera cameraTop;
    private Camera cameraBot;
    private LudumResourcePack resourcePack;
    private Reflector reflector;


    @Override
    public void create() {
        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);

        this.reflector = new Reflector();
        this.resourcePack = new LudumResourcePack(Files.FileType.Internal, reflector);
        this.resourcePack.build();

        this.cameraTop = Camera.top();
        this.cameraBot = Camera.bot();

        setScreen(new GameScreen(this));
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    public InputMultiplexer getInputMultiplexer() {
        return inputMultiplexer;
    }

    public Camera getCameraTop() {
        return cameraTop;
    }


    public Camera getCameraBot() {
        return cameraBot;
    }

    public LudumResourcePack getResourcePack() {
        return resourcePack;
    }
}
