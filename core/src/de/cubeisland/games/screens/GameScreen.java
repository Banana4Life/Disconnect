package de.cubeisland.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.World;

public class GameScreen implements Screen {
    private final DisconnectGame game;
    private World world;
    private final FPSLogger logger = new FPSLogger();

    public GameScreen(DisconnectGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        this.world = new World();
    }

    @Override
    public void render(float delta) {
        logger.log();
        this.world.update(this.game, delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.world.render(this.game, delta);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
