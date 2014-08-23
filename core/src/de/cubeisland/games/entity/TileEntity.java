package de.cubeisland.games.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.cubeisland.games.DisconnectGame;

import java.util.Random;

public class TileEntity extends Entity {

    private static final int size = 16;

    // to remove
    Random random = new Random();
    private Color color = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), random.nextFloat());
    // to remove

    public TileEntity(int x, int y) {
        this.pos.add(x * size, y * size);
    }

    @Override
    public void render(DisconnectGame game, float delta) {
        //SpriteBatch batch = game.getSpriteBatch();
        //batch.begin();
        //batch.draw(tex, pos.x, pos.y, size, size);
        if (!game.getCamera().canBeSeen(this.pos))
        {
            return;
        }

        ShapeRenderer r = game.getShapeRenderer();
        r.begin(ShapeRenderer.ShapeType.Filled);
        r.setColor(color);
        r.rect(pos.x, pos.y, size, size);
        r.end();
        SpriteBatch batch = game.getSpriteBatch();

        batch.begin();
        batch.draw(game.getResourcePack().textures.tilemap, pos.x, pos.y, 0, 0, size, size);
        batch.end();
    }
}
