package de.cubeisland.games.tile;

import java.util.Random;

public enum Direction {
    TOP(0, 1),
    BOTTOM(0, -1),
    LEFT(-1, 0),
    RIGHT(1, 0);
    private static final Random rand = new Random();
    private final int x;
    private final int y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Direction random() {
        return values()[rand.nextInt(values().length)];
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
