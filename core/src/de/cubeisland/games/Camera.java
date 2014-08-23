package de.cubeisland.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Camera extends OrthographicCamera {

    public final SpriteBatch spriteBatch;
    public final ShapeRenderer shapeRenderer;

    private boolean top;

    private Camera(boolean top) {
        this.top = top;
        this.zoom = 0.25f;
        this.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2);

        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
    }

    public Camera use()
    {
        int height = Gdx.graphics.getHeight();
        int width = Gdx.graphics.getWidth();

        Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
        Gdx.gl.glScissor(0, top ? 0 : height / 2, width, top ? height / 2 : height);
        Gdx.gl.glViewport(0, top ? 0 : height / 2, width, top ? height / 2 : height);

        this.update();

        this.spriteBatch.setProjectionMatrix(this.combined);
        this.shapeRenderer.setProjectionMatrix(this.combined);

        return this;
    }

    public boolean canBeSeen(Vector2 tlPos, Vector2 size) {
        float hx = size.x / 2f;
        float hy = size.y / 2f;
        return frustum.boundsInFrustum(tlPos.x + hx, tlPos.y + hy, 0, hx, hy, 0);
    }

    public static Camera top()
    {
        return new Camera(true);
    }

    public static Camera bot()
    {
        return new Camera(false);
    }
}
