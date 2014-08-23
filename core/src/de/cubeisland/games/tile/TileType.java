package de.cubeisland.games.tile;

import com.badlogic.gdx.graphics.Color;

import java.util.HashMap;
import java.util.Map;

public enum TileType {
    FLOOR(0xFCFC00FF, false),
    WALL(0xFFFFFFFF, true),
    ;

    private static final Map<Integer, TileType> BY_COLOR_VALUE;

    private final int colorValue;
    private final boolean blocking;
    private final Color color;

    TileType(int color, boolean blocking) {
        this.colorValue = color;
        this.blocking = blocking;
        this.color = new Color();
        Color.rgba8888ToColor(this.color, color);
    }

    public Color getColor() {
        return color.cpy();
    }

    public static TileType getByColor(int colorValue) {
        return BY_COLOR_VALUE.get(colorValue);
    }

    static {
        BY_COLOR_VALUE = new HashMap<>();

        for (TileType t : values()) {
            BY_COLOR_VALUE.put(t.colorValue, t);
        }
    }

    public boolean isBlocking() {
        return blocking;
    }
}
