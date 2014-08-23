package de.cubeisland.games.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.tile.Direction;

import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled;

public class Enemy extends Entity {

    public static final int SPEED = 30;

    @Override
    public void onSpawn() {
        Direction dir = Direction.random();
        getVelocity().set(SPEED * dir.getX(), SPEED * dir.getY());
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
        r.circle(pos.x, pos.y, 15);
        r.end();
    }
}
