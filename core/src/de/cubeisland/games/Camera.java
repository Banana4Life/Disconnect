package de.cubeisland.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import de.cubeisland.games.entity.TileEntity;

public class Camera extends OrthographicCamera implements Disposable {

    private final SpriteBatch spriteBatch;
    private final ShapeRenderer shapeRenderer;

    private CameraType type;

    private Camera(CameraType type) {
        this.type = type;
        this.zoom = 0.25f;
        this.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
    }

    public static Camera left() {
        return new Camera(CameraType.LEFT);
    }

    public static Camera right() {
        return new Camera(CameraType.RIGHT);
    }

    public static Camera gui() {
        return new Camera(CameraType.GUI);
    }

    public void resize(int width, int height) {
        switch (type) {
            case LEFT:
            case RIGHT:
                this.setToOrtho(false, width / 2, height);
                break;
            case GUI:
                this.setToOrtho(false, width, height);
                break;
        }
    }

    public Camera use() {
        int height = Gdx.graphics.getHeight();
        int width = Gdx.graphics.getWidth();

        int x = 0;
        int y = 0;
        int xSize = width;
        int ySize = height;
        switch (type) {
            case LEFT:
                xSize = width / 2 - 2;
                break;
            case RIGHT:
                xSize = width / 2 - 2;
                x = width / 2 + 2;
                break;
            case GUI:
                break;
            default:
                return this;
        }

        Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
        Gdx.gl.glScissor(x, y, xSize, ySize);
        Gdx.gl.glViewport(x, y, xSize, ySize);

        this.update();

        this.spriteBatch.setProjectionMatrix(this.combined);
        this.shapeRenderer.setProjectionMatrix(this.combined);

        return this;
    }

    public SpriteBatch getSpriteBatch() {
        this.use();
        return spriteBatch;
    }

    public ShapeRenderer getShapeRenderer() {
        this.use();
        return shapeRenderer;
    }

    public boolean canBeSeen(Vector2 tlPos, Vector2 size) {
        float hx = size.x / 2f;
        float hy = (size.y + TileEntity.SIZE) / 2f;
        return frustum.boundsInFrustum(tlPos.x + hx, tlPos.y + hy, 0, hx, hy, 0);
    }

    @Override
    public void dispose() {
        this.shapeRenderer.dispose();
        this.spriteBatch.dispose();
    }

    private enum CameraType {
        LEFT,
        RIGHT,
        GUI,
    }
}
