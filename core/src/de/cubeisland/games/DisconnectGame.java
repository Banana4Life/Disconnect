package de.cubeisland.games;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.cubeisland.games.screens.GameScreen;

public class DisconnectGame extends Game {

    private InputMultiplexer inputMultiplexer;
    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;

    @Override
	public void create () {
        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(true);
        this.camera.zoom = (float) 0.5;
        
        this.spriteBatch = new SpriteBatch();
        this.spriteBatch.setProjectionMatrix(this.camera.combined);
        this.shapeRenderer = new ShapeRenderer();
        this.shapeRenderer.setProjectionMatrix(this.camera.combined);

        setScreen(new GameScreen(this));
    }

    public InputMultiplexer getInputMultiplexer() {
        return inputMultiplexer;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }
}
