package de.cubeisland.games.tile;

import com.badlogic.gdx.graphics.Color;
import de.cubeisland.games.entity.Enemy;
import de.cubeisland.games.entity.Entity;
import de.cubeisland.games.entity.Key;
import de.cubeisland.games.entity.TileEntity;

import java.util.HashMap;
import java.util.Map;

public enum TileType {
    WALL            (0x000000FF, true),
    DOOR            (0x9F9F9FFF, true),
    DOOR_OPEN       (0x9F9FAFFF, false),
    FLOOR           (0xFFFFFFFF, false),
    FLOOR_PLAYER    (0xFFF000FF, false, FLOOR),
    FLOOR_ENEMY     (0xFF0000FF, false, FLOOR, Enemy.class),
    FLOOR_ENERGY    (0x1200FFFF, false, FLOOR),
    FLOOR_UPGRADE   (0x0066FFFF, false, FLOOR),
    FLOOR_KEY       (0x00AAFFFF, false, FLOOR, Key.class),
    ;

    private static final Map<Integer, TileType> BY_COLOR_VALUE;

    private final int colorValue;
    private final boolean blocking;
    private Class<? extends Entity> entityClass;
    private final Color color;

    private TileType type;

    TileType(int color, boolean blocking) {
       this(color, blocking, null);
    }

    TileType(int color, boolean blocking, TileType baseType) {
        this(color, blocking, baseType, null);
    }

    TileType(int color, boolean blocking, TileType baseType, Class<? extends Entity> entityClass) {
        this.colorValue = color;
        this.blocking = blocking;
        this.entityClass = entityClass;
        this.color = new Color();
        this.type = baseType == null ? this : baseType;
        Color.rgba8888ToColor(this.color, color);
    }

    public static TileType getByColor(int colorValue) {
        TileType tileType = BY_COLOR_VALUE.get(colorValue);
        if (tileType == null){
            throw new IllegalArgumentException(Integer.toHexString(colorValue));
        }
        return tileType;
    }
    static {
        BY_COLOR_VALUE = new HashMap<>();

        for (TileType t : values()) {
            BY_COLOR_VALUE.put(t.colorValue, t);
        }
    }

    public Color getColor() {
        return color.cpy();
    }

    public boolean isBlocking() {
        return blocking;
    }

    public TileType get()
    {
        return this.type;
    }

    public Class<? extends Entity> getEntityClass() {
        return entityClass;
    }

    public boolean isType(TileEntity tile) {
        return tile != null && tile.getType() == this;
    }
}
