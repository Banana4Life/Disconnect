package de.cubeisland.games.entity;

import com.badlogic.gdx.math.Vector2;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.tile.Direction;
import de.cubeisland.games.tile.TileType;

public abstract class UserInteractableTile extends TileEntity {
    private float statetime = 0f;

    public UserInteractableTile(int x, int y, TileType type) {
        super(x, y, type);

        this.overlayOffset.set(new Vector2(0, 10));
    }

    public abstract void userInteract(Player player);

    @Override
    public void render(DisconnectGame game, float delta) {
        super.render(game, delta);

        statetime += delta;
        if (this.getWorld().getNeighbourOf(this.getWorld().getTileAtPos(this.getWorld().getPlayer().getPos().x + 8, this.getWorld().getPlayer().getPos().y), Direction.BOTTOM) == this) {
            this.overlay = this.getWorld().getGame().getResourcePack().animations.buttonpress.getKeyFrame(statetime, true);
        } else {
            this.overlay = null;
        }
    }
}
