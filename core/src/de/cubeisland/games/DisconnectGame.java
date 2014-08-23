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
    private Camera camera1;
    private Camera camera2;
    private LudumResourcePack resourcePack;
    private Reflector reflector;

    @Override
    public void create() {
        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);

        this.reflector = new Reflector();
        this.resourcePack = new LudumResourcePack(Files.FileType.Internal, reflector);
        this.resourcePack.build();

        this.camera1 = Camera.left();
        this.camera2 = Camera.right();

        setScreen(new GameScreen(this));
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.camera1.resize(width, height);
        this.camera2.resize(width, height);

    }

    public InputMultiplexer getInputMultiplexer() {
        return inputMultiplexer;
    }

    public Camera getCamera1() {
        return camera1;
    }


    public Camera getCamera2() {
        return camera2;
    }

    public LudumResourcePack getResourcePack() {
        return resourcePack;
    }
}
