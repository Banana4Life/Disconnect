package de.cubeisland.games.entity;

import de.cubeisland.games.tile.TileType;

public class ActivatedDoor extends AbstractDoor {
    public ActivatedDoor(int x, int y, TileType type) {
        super(x, y, type);
    }

    @Override
    public void open() {
        super.open();
        this.type = TileType.AUTO_DOOR;
    }

    @Override
    public void close() {
        super.close();
        this.type = TileType.AUTO_DOOR;
    }
}
