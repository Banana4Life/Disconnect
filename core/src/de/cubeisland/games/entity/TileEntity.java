package de.cubeisland.games.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.cubeisland.games.DisconnectGame;

public class TileEntity extends Entity {

    private static final int size = 16;

    public TileEntity(int x, int y) {
        this.pos.add(x * size, y * size);
    }

    @Override
    public void render(DisconnectGame game, float delta) {
        ShapeRenderer r = game.getShapeRenderer();
        r.begin(ShapeRenderer.ShapeType.Filled);
        r.setColor(Color.GRAY);
        r.rect(pos.x, pos.y, size, size);
        r.end();

        // Debug Lines:
        r.begin(ShapeRenderer.ShapeType.Line);
        r.setColor(Color.GREEN);
        r.rect(pos.x, pos.y, size, size);
        r.end();
    }
}
