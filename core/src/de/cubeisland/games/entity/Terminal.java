package de.cubeisland.games.entity;

import de.cubeisland.games.tile.TileType;

public class Terminal extends UserInteractableTile {
    public Terminal(int x, int y, TileType type) {
        super(x, y, type);
    }

    @Override
    public void userInteract(Player player) {
        player.switchItems();
    }
}
