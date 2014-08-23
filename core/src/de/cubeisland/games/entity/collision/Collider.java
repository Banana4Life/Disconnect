package de.cubeisland.games.entity.collision;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.entity.Entity;
import de.cubeisland.games.entity.TileEntity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.badlogic.gdx.math.Intersector.MinimumTranslationVector;

public class Collider {
    public static void collideEntities(DisconnectGame game, Collection<Entity> entities, Set<TileEntity> blockingTiles) {
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

                Vector2 mtv = intersect(game, first, second);
                if (mtv != null) {
                    first.onCollide(second, mtv.cpy());
                    second.onCollide(first, mtv.cpy());
                }
            }

            for (TileEntity tile : blockingTiles) {
                Vector2 mtv = intersect(game, first, tile);
                if (mtv != null) {
                    first.onTileCollide(tile, mtv.scl(1.001f));
                }
            }
        }
    }

    private static Polygon p1 = new Polygon();
    private static float[] vertices1 = new float[8];
    private static Polygon p2 = new Polygon();
    private static float[] vertices2 = new float[8];

    private static MinimumTranslationVector mtv = new MinimumTranslationVector();

    private static Vector2 intersect(DisconnectGame game, Entity first, Entity second) {

        final CollisionBox firstBox = first.getCollisionBox();
        final CollisionBox secondBox = second.getCollisionBox();

        final float firstX = first.getPos().x + firstBox.getOffsetX();
        final float firstY = first.getPos().y - firstBox.getOffsetY();
        final float secondX = second.getPos().x + secondBox.getOffsetX();
        final float secondY = second.getPos().y - secondBox.getOffsetY();

        vertices1[0] = firstX;
        vertices1[1] = firstY;
        vertices1[2] = firstX + firstBox.getWidth();
        vertices1[3] = firstY;
        vertices1[4] = firstX + firstBox.getWidth();
        vertices1[5] = firstY + firstBox.getHeight();
        vertices1[6] = firstX;
        vertices1[7] = firstY + firstBox.getHeight();

        vertices2[0] = secondX;
        vertices2[1] = secondY;
        vertices2[2] = secondX + secondBox.getWidth();
        vertices2[3] = secondY;
        vertices2[4] = secondX + secondBox.getWidth();
        vertices2[5] = secondY + secondBox.getHeight();
        vertices2[6] = secondX;
        vertices2[7] = secondY + secondBox.getHeight();

        p1.setVertices(vertices1);
        p2.setVertices(vertices2);

        if (Intersector.overlapConvexPolygons(p1, p2, mtv)) {
            return mtv.normal.scl(mtv.depth);
        }
        return null;
    }
}
