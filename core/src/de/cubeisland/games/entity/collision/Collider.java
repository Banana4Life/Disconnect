package de.cubeisland.games.entity.collision;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import de.cubeisland.games.World;
import de.cubeisland.games.entity.Entity;
import de.cubeisland.games.entity.TileEntity;

import java.util.List;

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

    private static final Polygon polyHelper = new Polygon();
    private static final float[] vertexHelper = new float[8];

    public static boolean isLineOfSightClear(World world, Vector2 begin, Vector2 end) {
        float x1;
        float x2;
        float y1;
        float y2;

        if (begin.x < end.x) {
            x1 = begin.x;
            x2 = end.x;
        } else {
            x1 = end.x;
            x2 = begin.x;
        }

        if (begin.y > end.y) {
            y1 = begin.y;
            y2 = end.y;
        } else {
            y1 = end.y;
            y2 = begin.y;
        }

        x1 -= TileEntity.SIZE;
        y1 += TileEntity.SIZE;
        x2 += TileEntity.SIZE;
        y2 -= TileEntity.SIZE;

        List<TileEntity> tiles = world.getTilesWithin(x1, y1, x2, y2);
        for (TileEntity tile : tiles) {
            if (!tile.getType().isBlocking()) {
                continue;
            }
            Vector2 pos = tile.getPos();
            vertexHelper[0] = pos.x;
            vertexHelper[1] = pos.y;
            vertexHelper[2] = pos.x + TileEntity.SIZE;
            vertexHelper[3] = pos.y;
            vertexHelper[4] = pos.x + TileEntity.SIZE;
            vertexHelper[5] = pos.y + TileEntity.SIZE;
            vertexHelper[6] = pos.x;
            vertexHelper[7] = pos.y + TileEntity.SIZE;
            polyHelper.setVertices(vertexHelper);
            if (Intersector.intersectLinePolygon(begin, end, polyHelper)) {
                return false;
            }
        }
        return true;
    }
}
