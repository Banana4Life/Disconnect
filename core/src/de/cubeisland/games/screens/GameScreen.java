package de.cubeisland.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import de.cubeisland.games.Disconnect;
import de.cubeisland.games.World;

public class GameScreen implements Screen {
    private final Disconnect game;
    private World world;

    public GameScreen(Disconnect game) {
        this.game = game;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.game.getSpriteBatch().begin();
        this.world.render(this.game, delta);
        this.game.getSpriteBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        this.game.getCamera().update();
    }

    @Override
    public void show() {
        this.world = new World();
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
