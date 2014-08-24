package de.cubeisland.games.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.entity.collision.CollisionBox;

public class GhostPlayer extends Entity {

    private final float time;
    TextureRegion keyFrame;
    private float delta;

    public GhostPlayer(Player player) {
        super();

        delta = 0;
        time = 1;

        this.keyFrame = player.getIdleFrame();
        this.getPos().set(player.getPos());
    }

    @Override
    public void render(DisconnectGame game, float delta) {
        SpriteBatch batch = this.getWorld().getCamera().getSpriteBatch();

        batch.begin();
        Color c = batch.getColor();
        batch.setColor(c.r, c.g, c.b, 0.7f);
        batch.draw(keyFrame, pos.x, pos.y, 16, 16);
        batch.setColor(c.r, c.g, c.b, 1f);
        batch.end();

        super.render(game, delta);
    }

    @Override
    public void update(DisconnectGame game, float delta) {
        super.update(game, delta);

        this.delta += delta;
        if (this.getCollisionBox() == null && this.delta > this.time) {
            this.setCollisionBox(new CollisionBox(2, 1, 7, 0));
        }
    }
}
