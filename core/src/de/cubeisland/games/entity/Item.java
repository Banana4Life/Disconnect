package de.cubeisland.games.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.World;
import de.cubeisland.games.entity.collision.CollisionBox;

import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled;

public class Item extends Entity {

    private static final float SIZE = 3;

    private Type type = Type.DEFAULT;
    
    public Item(Type type) {
        this.type = type;
    }

    @Override
    public void onSpawn(World world) {
        super.onSpawn(world);
        setCollisionBox(new CollisionBox(SIZE, SIZE));
    }

    @Override
    public void render(DisconnectGame game, float delta) {
        super.render(game, delta);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public static enum Type {
        KEY,
        ENERGY,
        UPGRADE,
        ;

        public static final Type DEFAULT = KEY;
    }
}
