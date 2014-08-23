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

    private static Vector2 intersect(DisconnectGame game, Entity first, Entity second) {

        final CollisionBox firstBox = first.getCollisionBox();
        final CollisionBox secondBox = second.getCollisionBox();

        final float firstX = first.getPos().x + firstBox.getOffsetX();
        final float firstY = first.getPos().y - firstBox.getOffsetY();
        final float secondX = second.getPos().x + secondBox.getOffsetX();
        final float secondY = second.getPos().y - secondBox.getOffsetY();

        Polygon p1 = new Polygon(new float[]{firstX, firstY, firstX + firstBox.getWidth(), firstY, firstX + firstBox.getWidth(), firstY + firstBox.getHeight(), firstX, firstY + firstBox.getHeight()});
        Polygon p2 = new Polygon(new float[]{secondX, secondY, secondX + secondBox.getWidth(), secondY, secondX + secondBox.getWidth(), secondY + secondBox.getHeight(), secondX, secondY + secondBox.getHeight()});

        Intersector.MinimumTranslationVector mtv = new Intersector.MinimumTranslationVector();
        if (Intersector.overlapConvexPolygons(p1, p2, mtv)) {
            return mtv.normal.scl(mtv.depth);
        }
        return null;
    }
}
