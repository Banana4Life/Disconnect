package de.cubeisland.games.tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.entity.Entity;

public class Exit extends Entity {
    @Override
    public void onCollide(Entity other, Rectangle collisionBox) {
        super.onCollide(other, collisionBox);
    }

    @Override
    public void render(DisconnectGame game, float delta) {
        super.render(game, delta);

        SpriteBatch batch = this.getWorld().getCamera().getSpriteBatch();
        batch.begin();
        // TODO draw exit item batch.draw(texReg, pos.x, pos.y, 16, 16);
        batch.end();
    }
}
