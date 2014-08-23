package de.cubeisland.games;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.cubeisland.engine.reflect.Reflector;
import de.cubeisland.games.resource.LudumResourcePack;
import de.cubeisland.games.screens.GameScreen;

public class DisconnectGame extends Game {

    private InputMultiplexer inputMultiplexer;
    private Camera camera;
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;
    private LudumResourcePack resourcePack;
    private Reflector reflector;

    @Override
	public void create () {
        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);

        this.reflector = new Reflector();
        this.resourcePack = new LudumResourcePack(Files.FileType.Internal, reflector);
        this.resourcePack.build();
        
        this.spriteBatch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();

        this.camera = new Camera(spriteBatch, shapeRenderer);
        this.camera.setToOrtho(true);
        this.camera.zoom = (float) 0.5;

        setScreen(new GameScreen(this));
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.camera.setToOrtho(true, width, height);
    }

    public InputMultiplexer getInputMultiplexer() {
        return inputMultiplexer;
    }

    public Camera getCamera() {
        return camera;
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }
}
