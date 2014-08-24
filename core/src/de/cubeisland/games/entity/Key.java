package de.cubeisland.games.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.cubeisland.games.DisconnectGame;

public class Key extends Entity {
    private float statetime = 0f;

    @Override
    public void render(DisconnectGame game, float delta) {
        SpriteBatch batch = this.getWorld().getCamera().getSpriteBatch();

        this.statetime += delta;

        batch.begin();
        batch.draw(game.getResourcePack().animations.key.getKeyFrame(this.statetime, true), pos.x, pos.y, 16, 16);
        batch.end();

        super.render(game, delta);
    }
}
