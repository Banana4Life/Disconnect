package de.cubeisland.games.entity.collision;

public class CollisionBox {
    private final float width;
    private final float height;
    private final float offsetX;
    private final float offsetY;

    public CollisionBox(float width, float height) {
        this(width, height, 0, 0);
    }

    public CollisionBox(float width, float height, float offsetX, float offsetY) {
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }
}
