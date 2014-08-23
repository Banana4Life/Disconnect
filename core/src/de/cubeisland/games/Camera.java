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
        return frustum.pointInFrustum(tlPos.x, tlPos.y, 0)
            || frustum.pointInFrustum(tlPos.x + size.x, tlPos.y + size.y, 0)
            || frustum.pointInFrustum(tlPos.x, tlPos.y + size.y, 0)
             || frustum.pointInFrustum(tlPos.x + size.x, tlPos.y, 0);
    }
}
