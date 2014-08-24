package de.cubeisland.games.entity;

import de.cubeisland.games.World;
import de.cubeisland.games.entity.collision.CollisionBox;
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
    public void interact(Item carriedItem) {
        this.texture = this.getWorld().getGame().getResourcePack().textures.activatoron;
    }
}
