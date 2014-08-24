package de.cubeisland.games.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.entity.collision.Collider;
import de.cubeisland.games.entity.collision.CollisionBox;
import de.cubeisland.games.tile.Direction;

import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled;
import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Line;
import static de.cubeisland.games.entity.collision.Collider.isLineOfSightClear;

public class Enemy extends Entity {

    public static final int SPEED = 20;
    public static final float RANGE = 50;
    public static final float FOV = 50;
    private static final int SIZE = 5;

    @Override
    public void onSpawn() {
        Direction dir = Direction.random();
        getVelocity().set(SPEED * dir.getX(), SPEED * dir.getY());
        setCollisionBox(new CollisionBox(SIZE * 2f, SIZE * 2f, 0, 0));
    }

    @Override
    public boolean onTileCollide(TileEntity tile, Rectangle helper3) {
        velocity.scl(-1);
        return super.onTileCollide(tile, helper3);
    }

    @Override
    public void onCollide(Entity other, Rectangle collisionBox) {
        die();
    }

    private Vector2 playerDistance = new Vector2(0, 0);

    @Override
    public void render(DisconnectGame game, float delta) {
        ShapeRenderer r = this.getWorld().getCamera().getShapeRenderer();

        r.begin(Filled);
        r.setColor(Color.RED);
        r.circle(pos.x + SIZE, pos.y + SIZE, SIZE);
        r.end();

        Player player = getWorld().getPlayer();
        CollisionBox playerCB = player.getCollisionBox();
        playerDistance
                .set(player.getPos())
                .add(playerCB.getOffsetX() + playerCB.getWidth() / 2f, playerCB.getOffsetY() + playerCB.getHeight() / 2f)
                .sub(pos);
        float viewAngle = velocity.angle();
        float playerAngle = playerDistance.angle();
        float diffAngle = Math.abs(viewAngle - playerAngle);

        if (playerDistance.len2() <= RANGE * RANGE && diffAngle <= (FOV / 2f) && isLineOfSightClear(getWorld(), pos, pos.cpy().add(playerDistance))) {
            r.begin(Line);
            r.setColor(Color.MAGENTA);
            r.line(pos.cpy().add(SIZE, SIZE), playerDistance.add(pos));
            r.end();
            // TODO do something more here
        }

//        r = new ShapeRenderer();
//        r.setProjectionMatrix(getWorld().getCamera().combined);
//        r.begin(Line);
//        r.setColor(Color.GREEN);
//        r.translate(pos.x, pos.y, 0);
//        r.rotate(velocity.x, velocity.y, 0, -(FOV / 2f));
//        Vector2 line = pos.cpy().nor().scl(RANGE);
//        r.line(pos.x, pos.y, line.x, line.y);
//        r.rotate(velocity.x, velocity.y, 0, FOV);
//        line = pos.cpy().nor().scl(RANGE);
//        r.line(pos.x, pos.y, line.x, line.y);
//        r.arc(pos.x, pos.y, RANGE, 0, -FOV);
//        r.end();
    }
}
