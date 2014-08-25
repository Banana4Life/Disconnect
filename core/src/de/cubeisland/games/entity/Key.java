package de.cubeisland.games.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.World;
import de.cubeisland.games.entity.collision.CollisionBox;

public class Key extends Item {
    private float statetime = 0f;

    public Key() {
        super(Type.KEY);
    }

    @Override
    public void onSpawn(World world) {
        super.onSpawn(world);
        this.setCollisionBox(new CollisionBox(6, 6, 5, -6));
    }

    @Override
    public void update(DisconnectGame game, float delta) {
        super.update(game, delta);
        this.statetime += delta;
        texReg = game.getResourcePack().animations.key.getKeyFrame(this.statetime, true);
    }

    @Override
    public void render(DisconnectGame game, float delta) {
        SpriteBatch batch = this.getWorld().beginBatch();
        batch.draw(texReg, pos.x, pos.y, 16, 16);
        batch.end();
        super.render(game, delta);
    }
}
