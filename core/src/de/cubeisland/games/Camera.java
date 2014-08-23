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

    private boolean right;

    private Camera(boolean right) {
        this.right = right;
        this.zoom = 0.25f;
        this.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
    }

    public void resize(int width, int height) {
        this.setToOrtho(true, width / 2, height);
    }

    public Camera use()
    {
        int height = Gdx.graphics.getHeight();
        int width = Gdx.graphics.getWidth();

        Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
        Gdx.gl.glScissor(right ? 0 : width / 2, 0 , width / 2, height);
        Gdx.gl.glViewport(right ? 0 : width / 2, 0, width / 2, height);

        this.update();

        this.spriteBatch.setProjectionMatrix(this.combined);
        this.shapeRenderer.setProjectionMatrix(this.combined);

        return this;
    }

    public boolean canBeSeen(Vector2 tlPos, Vector2 size) {
        float hx = size.x / 2f;
        float hy = size.y / 2f;
        return frustum.boundsInFrustum(tlPos.x + hx, tlPos.y - hy, 0, hx, hy, 0);
    }

    public static Camera left()
    {
        return new Camera(true);
    }

    public static Camera right()
    {
        return new Camera(false);
    }


}
