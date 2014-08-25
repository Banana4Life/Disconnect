package de.cubeisland.games.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.World;
import de.cubeisland.games.entity.collision.CollisionBox;

public class Item extends Entity {

    private static final float SIZE = 3;
    protected TextureRegion texReg = null;
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

    public TextureRegion getTex() {
        return texReg;
    }

    @Override
    public void interact(Item carriedItem, Player player) {

        if (carriedItem != null) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                this.getWorld().spawn(carriedItem.getClass(), this.getPos());
            } else {
                return;
            }
        }
        player.setItem(this);
        this.setCollisionBox(null);
        this.die();
        getWorld().getGame().getResourcePack().sounds.pickup.start(.25f);
    }

    public static enum Type {
        KEY,
        ENERGY,
        UPGRADE,;

        public static final Type DEFAULT = KEY;
    }
}
