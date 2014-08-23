package de.cubeisland.games.util;

import com.badlogic.gdx.math.Vector2;

public class Node {
    private final Vector2 location;

    public Node(Vector2 location) {
        this.location = location;
    }

    public Vector2 getLocation() {
        return location;
    }
}
