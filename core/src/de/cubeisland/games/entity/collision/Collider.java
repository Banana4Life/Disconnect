package de.cubeisland.games.entity.collision;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import de.cubeisland.games.entity.Entity;
import de.cubeisland.games.entity.TileEntity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.badlogic.gdx.math.Intersector.MinimumTranslationVector;

public class Collider {
    public static void collideEntities(Collection<Entity> entities, Set<TileEntity> blockingTiles) {
        Set<Entity> collidable = new HashSet<>();
        for (Entity e : entities) {
            if (e.getCollisionBox() != null) {
                collidable.add(e);
            }
        }

        Set<Long> checked = new HashSet<>();
        for (Entity first : collidable) {
            for (Entity second : collidable) {
                if (first == second) {
                    continue;
                }

                long signature = first.hashCode() + second.hashCode();
                if (checked.contains(signature)) {
                    continue;
                }
                checked.add(signature);

                Vector2 mtv = intersect(first, second);
                if (mtv != null) {
                    first.onCollide(second, mtv.cpy());
                    second.onCollide(first, mtv.cpy());
                }
            }

            for (TileEntity tile : blockingTiles) {
                Vector2 mtv = intersect(first, tile);
                if (mtv != null) {
                    first.onTileCollide(tile, mtv.scl(1.001f));
                }
            }
        }
    }

    private static Vector2 intersect(Entity entity1, Entity entity2) {
        final CollisionBox box2 = entity2.getCollisionBox();
        final float x2 = entity2.getPos().x + box2.getOffsetX();
        final float y2 = entity2.getPos().y - box2.getOffsetY();

        return intersect(entity1, x2, y2, box2.getWidth(), box2.getHeight());
    }

    public static Vector2 intersect(Entity entity1, float x2, float y2, float width2, float height2) {
        final CollisionBox box1 = entity1.getCollisionBox();
        final float x1 = entity1.getPos().x + box1.getOffsetX();
        final float y1 = entity1.getPos().y - box1.getOffsetY();

        return intersect(x1, y1, box1.getWidth(), box1.getHeight(), x2, y2, width2, height2);
    }

    private static Polygon p1 = new Polygon();
    private static float[] vertices1 = new float[8];
    private static Polygon p2 = new Polygon();
    private static float[] vertices2 = new float[8];

    private static MinimumTranslationVector mtv = new MinimumTranslationVector();

    public static Vector2 intersect(float x1, float y1, float width1, float height1, float x2, float y2, float width2, float height2) {
        vertices1[0] = x1;
        vertices1[1] = y1;
        vertices1[2] = x1 + width1;
        vertices1[3] = y1;
        vertices1[4] = x1 + width1;
        vertices1[5] = y1 + height1;
        vertices1[6] = x1;
        vertices1[7] = y1 + height1;

        vertices2[0] = x2;
        vertices2[1] = y2;
        vertices2[2] = x2 + width2;
        vertices2[3] = y2;
        vertices2[4] = x2 + width2;
        vertices2[5] = y2 + height2;
        vertices2[6] = x2;
        vertices2[7] = y2 + height2;

        p1.setVertices(vertices1);
        p2.setVertices(vertices2);

        if (Intersector.overlapConvexPolygons(p1, p2, mtv)) {
            return mtv.normal.scl(mtv.depth);
        }
        return null;
    }
}
