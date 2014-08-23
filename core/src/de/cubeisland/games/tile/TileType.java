package de.cubeisland.games.tile;

import com.badlogic.gdx.graphics.Color;

import java.util.HashMap;
import java.util.Map;

public enum TileType {
    FLOOR(0xFCFC00),
    WALL(0x000000),
    ;

    private static final Map<Integer, TileType> BY_COLOR_VALUE;

    private final int colorValue;
    private final Color color;

    TileType(int color) {
        this.colorValue = color;
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
}
