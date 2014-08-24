package de.cubeisland.games;

import com.badlogic.gdx.*;
import de.cubeisland.engine.reflect.Reflector;
import de.cubeisland.games.resource.LudumResourcePack;
import de.cubeisland.games.screens.*;

import java.lang.reflect.Constructor;

import static de.cubeisland.games.Camera.CameraType.GUI;

public class DisconnectGame extends Game {

    private InputMultiplexer inputMultiplexer;
    private LudumResourcePack resourcePack;
    private Reflector reflector;
    private Camera guiCamera;

    @Override
    public void create() {
        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);
        inputMultiplexer.addProcessor(new GlobalInputProcessor(this));

        this.reflector = new Reflector();
        this.resourcePack = new LudumResourcePack(Files.FileType.Internal, reflector);
        this.resourcePack.build();

        this.guiCamera = Camera.create(GUI);


        resourcePack.songs.maintheme.setLooping(true);
        resourcePack.songs.maintheme.setVolume(0.15f);
        resourcePack.songs.maintheme.play();

        setScreen(new TitleScreen(this));
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

    public void win() {
        transition(WinScreen.class);
    }

    public void lose() {
        transition(LoseScreen.class);
    }

    public void exit() {
        Gdx.app.exit();
    }

    public <T extends Screen> T transition(Class<T> screenClass) {
        try {
            Constructor<T> ctor = screenClass.getConstructor(this.getClass());
            T screen = ctor.newInstance(this);

            setScreen(screen);

            return screen;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to transition to a screen!", e);
        }
    }
}
