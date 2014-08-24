package de.cubeisland.games.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.World;
import de.cubeisland.games.entity.collision.CollisionBox;
import de.cubeisland.games.tile.Direction;

import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled;
import static de.cubeisland.games.entity.collision.Collider.isLineOfSightClear;

public class Enemy extends Entity {

    public static final int SPEED = 20;
    public static final float RANGE = 50;
    public static final float FOV = 50;
    private static final int SIZE = 5;

    private boolean aggro = false;

    @Override
    public void onSpawn(World world) {
        super.onSpawn(world);
        Direction dir = Direction.random();
        getVelocity().set(SPEED * dir.getX(), SPEED * dir.getY());
        setCollisionBox(new CollisionBox(SIZE * 2f, SIZE * 2f, 0, 0));
    }

    @Override
    public void onTileCollide(TileEntity tile, Rectangle helper3) {
        velocity.scl(-1);
        super.onTileCollide(tile, helper3);
    }

    @Override
    public void onCollide(Entity other, Rectangle collisionBox) {
        die();
    }

    @Override
    public void update(DisconnectGame game, float delta) {
        Player player = getWorld().getPlayer();
        CollisionBox playerCB = player.getCollisionBox();
        playerDistance
                .set(player.getPos())
                .add(playerCB.getOffsetX() + playerCB.getWidth() / 2f, playerCB.getOffsetY() + playerCB.getHeight() / 2f)
                .sub(pos);
        if (aggro) {
            velocity.set(playerDistance.cpy().nor().scl(SPEED));
        }
        super.update(game, delta);
    }

    private Vector2 playerDistance = new Vector2(0, 0);

    @Override
    public void render(DisconnectGame game, float delta) {
        ShapeRenderer r = this.getWorld().getCamera().getShapeRenderer();

        r.begin(Filled);
        r.setColor(Color.RED);
        r.circle(pos.x + SIZE, pos.y + SIZE, SIZE);
        r.end();

        float viewAngle = velocity.angle();
        float playerAngle = playerDistance.angle();
        float diffAngle = Math.abs(viewAngle - playerAngle);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Vector2 center = pos.cpy().add(SIZE, SIZE);
        r.setColor(0,1,0,0.6f);
        if (playerDistance.len2() <= RANGE * RANGE && diffAngle <= (FOV / 2f) && isLineOfSightClear(getWorld(), pos, pos.cpy().add(playerDistance))) {
            r.setColor(1, 0, 0, 0.6f);
            aggro = true;
            for (Door door : getWorld().findAllDoors()) {
                // TODO close
            }
        }
        aggro = false;
        r.begin(ShapeRenderer.ShapeType.Filled);
        r.arc(center.x, center.y, RANGE, velocity.angle() - FOV / 2f, FOV);
        r.end();
    }
}
