package de.cubeisland.games.entity;

import de.cubeisland.games.tile.TileType;

public class Door extends AbstractDoor {
    public Door(int x, int y, TileType type) {
        super(x, y, type);
    }

    @Override
    public void interact(Item carriedItem, Player player) {
        if (carriedItem instanceof Key) {
            player.useItem();
            this.open();
        }
    }

    @Override
    public void open() {
        super.open();
        this.type = TileType.DOOR;
    }

    @Override
    public void close() {
        super.close();
        this.type = TileType.DOOR;
    }
}
