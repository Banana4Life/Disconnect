package de.cubeisland.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;

public abstract class DisconnectScreen extends ScreenAdapter {
    private static final FPSLogger FPS = new FPSLogger();

    private final Color clearColor = Color.BLACK.cpy();

    @Override
    public final void render(float delta) {
        FPS.log();
        this.update(delta);
        Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.draw(delta);

    }

    public Color getClearColor() {
        return clearColor;
    }

    public void setClearColor(Color c) {
        this.clearColor.set(c);
    }

    public void update(float delta) {

    }

    public void draw(float delta) {

    }
}
