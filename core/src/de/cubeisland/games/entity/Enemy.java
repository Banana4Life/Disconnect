package de.cubeisland.games.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.entity.collision.CollisionBox;
import de.cubeisland.games.tile.Direction;

import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled;

public class Enemy extends Entity {

    public static final int SPEED = 1;
    public static final float RANGE = 4;
    public static final float FOV = 50;
    private static final int SIZE = 5;

    @Override
    public void onSpawn() {
        Direction dir = Direction.random();
        getVelocity().set(SPEED * dir.getX(), SPEED * dir.getY());
        setCollisionBox(new CollisionBox(SIZE * 2f, SIZE * 2f, -SIZE, SIZE));
    }

    @Override
    public void onTileCollide(TileEntity tile, Vector2 mtv) {
        super.onTileCollide(tile, mtv);
        velocity.scl(-1);
    }

    @Override
    public void render(DisconnectGame game, float delta) {
        ShapeRenderer r = this.getWorld().getCamera().getShapeRenderer();

        r.begin(Filled);
        r.setColor(Color.RED);
        r.circle(pos.x, pos.y, SIZE);
        r.end();

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
