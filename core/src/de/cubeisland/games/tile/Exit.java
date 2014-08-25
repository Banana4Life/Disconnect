package de.cubeisland.games.tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.World;
import de.cubeisland.games.entity.Entity;
import de.cubeisland.games.entity.Item;
import de.cubeisland.games.entity.Player;
import de.cubeisland.games.entity.collision.CollisionBox;

public class Exit extends Entity {

    private float statetime;

    @Override
    public void onSpawn(World world) {
        super.onSpawn(world);
        this.setCollisionBox(new CollisionBox(16, 16)); // TODO anpassen an textur
    }

    @Override
    public void onCollide(Entity other, Rectangle collisionBox) {
        super.onCollide(other, collisionBox);
    }

    @Override
    public void render(DisconnectGame game, float delta) {
        super.render(game, delta);
        statetime += delta;
        SpriteBatch batch = this.getWorld().beginBatch();
        batch.draw(game.getResourcePack().animations.exit.getKeyFrame(statetime, true), pos.x, pos.y, 16, 16); // TODO EXIT-TEXTUR
        batch.end();
    }

    @Override
    public void interact(Item carriedItem, Player player) {
        this.getWorld().getGame().win();
    }
}
