package de.cubeisland.games.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.cubeisland.games.DisconnectGame;

import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled;

public class Player extends Entity {

    @Override
    public void onSpawn() {
        getVelocity().set(10, 10);
    }

    @Override
    public void render(DisconnectGame game, float delta) {
        ShapeRenderer r = game.getShapeRenderer();
        r.begin(Filled);
        r.setColor(Color.RED);
        r.circle(pos.x, pos.y, 8);
        r.end();
    }

    @Override
    public void update(DisconnectGame game, float delta) {
        super.update(game, delta);

        OrthographicCamera camera = game.getCamera();
        camera.position.set(this.pos.x, this.pos.y, 0);
        camera.update();
        game.getShapeRenderer().setProjectionMatrix(camera.combined);
    }
}
