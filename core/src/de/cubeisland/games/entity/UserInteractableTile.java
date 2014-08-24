package de.cubeisland.games.entity;

import de.cubeisland.games.tile.TileType;

public abstract class UserInteractableTile extends TileEntity {
    public UserInteractableTile(int x, int y, TileType type) {
        super(x, y, type);
    }

    public abstract void userInteract(Player player);
}
