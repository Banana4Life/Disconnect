package de.cubeisland.games.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.cubeisland.games.DisconnectGame;

import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled;

public class Enemy extends Entity {
    @Override
    public void render(DisconnectGame game, float delta) {
        ShapeRenderer r = this.getWorld().getCamera().getShapeRenderer();

        r.begin(Filled);
        r.setColor(Color.RED);
        r.circle(pos.x, pos.y, 15);
        r.end();
    }
}
