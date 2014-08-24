package de.cubeisland.games.entity.collision;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import de.cubeisland.games.entity.Entity;

public class Collider {
    private static final Rectangle helper1 = new Rectangle(0, 0, 0, 0);
    private static final Rectangle helper2 = new Rectangle(0, 0, 0, 0);
    private static final Rectangle helper3 = new Rectangle(0, 0, 0, 0);

    public static Rectangle findCollision(Entity e1, Entity e2) {
        CollisionBox cB1 = e1.getCollisionBox();
        CollisionBox cB2 = e2.getCollisionBox();
        if (cB1 == null || cB2 == null) {
            return null;
        }
        return findCollision(e1, e2, cB1, cB2);
    }

    public static Rectangle findCollision(Entity e1, Entity e2, CollisionBox cB1, CollisionBox cB2) {
        helper1.set(e1.getPos().x + cB1.getOffsetX(), e1.getPos().y - cB1.getOffsetY(), cB1.getWidth(), cB1.getHeight());
        helper2.set(e2.getPos().x + cB2.getOffsetX(), e2.getPos().y - cB2.getOffsetY(), cB2.getWidth(), cB2.getHeight());
        if (Intersector.intersectRectangles(helper1, helper2, helper3)) {
            return helper3;
        }
        return null;
    }
}
