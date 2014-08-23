package de.cubeisland.games.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.entity.collision.CollisionBox;

public class Player extends Entity {
    private float statetime = 0f;
    private TextureRegion currentKeyFrame;

    public Player() {
        setCollisionBox(new CollisionBox(6, 1, 5, 0));
    }

    @Override
    public void render(DisconnectGame game, float delta) {
        SpriteBatch batch = this.getWorld().getCamera().getSpriteBatch();

        this.statetime += delta;

        if (velocity.y < 0) {
            currentKeyFrame = game.getResourcePack().animations.characterfront.getKeyFrame(this.statetime, true);
        } else if (velocity.y > 0) {
            currentKeyFrame = game.getResourcePack().animations.characterback.getKeyFrame(this.statetime, true);
        } else if (currentKeyFrame == null) {
            currentKeyFrame = game.getResourcePack().animations.characterfront.getKeyFrame(this.statetime, true);
        }

        batch.begin();
        batch.draw(currentKeyFrame, pos.x, pos.y, 16, 16);
        batch.end();

        super.render(game, delta);
    }

    @Override
    public void onCollide(Entity other, Vector2 mtv) {
        die();
    }

    @Override
    public void update(DisconnectGame game, float delta) {
        super.update(game, delta);

        this.getWorld().getCamera().position.set(this.pos.x, this.pos.y, 0);
    }
}
