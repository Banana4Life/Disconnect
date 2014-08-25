package de.cubeisland.games.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.cubeisland.games.DisconnectGame;

public abstract class AnimatedEntity extends Entity {
    protected float statetime = 0f;
    protected TextureRegion currentKeyFrame;

    @Override
    public void update(DisconnectGame game, float delta) {
        super.update(game, delta);
        this.statetime += delta;
        this.updateAnimation();
    }

    public abstract void updateAnimation();
}
