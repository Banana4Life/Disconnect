package de.cubeisland.games.entity;

import de.cubeisland.games.World;
import de.cubeisland.games.entity.collision.CollisionBox;
import de.cubeisland.games.tile.Direction;
import de.cubeisland.games.tile.TileType;

public class Activator extends TileEntity {

    public Activator(int x, int y, TileType type) {
        super(x, y, type);
    }

    @Override
    public void onSpawn(World world) {
        super.onSpawn(world);
        this.texture = world.getGame().getResourcePack().textures.activatoroff;
        this.setCollisionBox(new CollisionBox(TileEntity.SIZE, TileEntity.SIZE));
    }

    @Override
    public void interact(Item carriedItem, Player player) {
        getWorld().getGame().getResourcePack().sounds.pressureplate.start(.25f);
        this.texture = this.getWorld().getGame().getResourcePack().textures.activatoron;
        this.setCollisionBox(null);
        for (Direction direction : Direction.values()) {
            TileEntity autoDoor = player.getOtherPlayer().getWorld().getNeighbourOf(this, direction);
            if (autoDoor instanceof ActivatedDoor)
            {
                ((ActivatedDoor)autoDoor).open();
            }
        }
    }
}
