package de.cubeisland.games;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import de.cubeisland.engine.reflect.Reflector;
import de.cubeisland.games.resource.LudumResourcePack;
import de.cubeisland.games.screens.LoseScreen;
import de.cubeisland.games.screens.TitleScreen;
import de.cubeisland.games.screens.WinScreen;

import java.lang.reflect.Constructor;

import static com.badlogic.gdx.Files.FileType.Internal;
import static de.cubeisland.games.Camera.CameraType.GUI;

public class DisconnectGame extends Game {

    private InputMultiplexer inputMultiplexer;
    private LudumResourcePack resourcePack;
    private Reflector reflector;
    private Camera guiCamera;
    private Screen nextScreen;

    @Override
    public void create() {
        inputMultiplexer = new InputMultiplexer(new GlobalInputProcessor(this));
        Gdx.input.setInputProcessor(inputMultiplexer);

        this.reflector = new Reflector();
        this.resourcePack = new LudumResourcePack(Internal, reflector);
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

            nextScreen = screen;

            return screen;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to transition to a screen!", e);
        }
    }

    @Override
    public void render() {
        if (nextScreen != null) {
            setScreen(nextScreen);
            nextScreen = null;
        }
        super.render();
    }
}
