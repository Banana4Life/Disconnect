package de.cubeisland.games;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Camera extends OrthographicCamera {

    private final SpriteBatch spriteBatch;
    private final ShapeRenderer shapeRenderer;

    public Camera(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
        this.spriteBatch = spriteBatch;
        this.shapeRenderer = shapeRenderer;
    }

    @Override
    public void update(boolean updateFrustum) {
        super.update(updateFrustum);
        this.spriteBatch.setProjectionMatrix(this.combined);
        this.shapeRenderer.setProjectionMatrix(this.combined);
    }

    public boolean canBeSeen(Vector2 tlPos, Vector2 size) {
        float hx = size.x / 2f;
        float hy = size.y / 2f;
        return frustum.boundsInFrustum(tlPos.x + hx, tlPos.y + hy, 0, hx, hy, 0);
    }
}
