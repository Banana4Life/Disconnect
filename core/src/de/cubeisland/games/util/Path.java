package de.cubeisland.games.util;

import java.util.List;

public class Path {
    private final List<Node> nodes;
    private final Node spawn;
    private final Node target;

    public Path(List<Node> nodes) {
        this.nodes = nodes;
        this.spawn = this.nodes.get(0);
        this.target = this.nodes.get(nodes.size() - 1);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public Node getSpawn() {
        return spawn;
    }

    public Node getTarget() {
        return target;
    }
}
