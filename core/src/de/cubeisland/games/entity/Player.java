package de.cubeisland.games.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.cubeisland.games.Disconnect;

import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled;

public class Player extends Entity {
    @Override
    public void render(Disconnect game, float delta) {
        ShapeRenderer r = game.getShapeRenderer();
        r.begin(Filled);
        r.setColor(Color.RED);
        r.circle(30, 30, 20);
        r.end();
    }
}
