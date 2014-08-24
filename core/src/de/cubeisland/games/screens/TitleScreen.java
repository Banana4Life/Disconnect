package de.cubeisland.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.cubeisland.games.Camera;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.World;
import de.cubeisland.games.entity.Player;
import de.cubeisland.games.resource.bag.Animations;

import static com.badlogic.gdx.Input.Keys.ENTER;
import static com.badlogic.gdx.Input.Keys.SPACE;
import static de.cubeisland.games.screens.UIInput.Handler;

public class TitleScreen extends UIScreen {

    private World titleWorld;

    public TitleScreen(DisconnectGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        Animations animations = game.getResourcePack().animations;
        this.titleWorld = new World(game, game.getGuiCamera(), new Player(animations.characterleftfront, animations.characterleftside, animations.characterleftback), "TitleWorld");

        getInput().add(new Handler() {
            @Override
            public boolean handle(DisconnectGame game) {
                game.transition(GameScreen.class);
                return true;
            }
        }, SPACE, ENTER);
    }

    @Override
    public void draw(float delta) {
        SpriteBatch batch = game.getGuiCamera().getSpriteBatch();
        Texture logo = game.getResourcePack().textures.logo;
        Texture exittipp = game.getResourcePack().textures.exittipp;

        this.titleWorld.render(this.game, delta);

        batch.begin();
        batch.draw(logo, 0, Gdx.graphics.getHeight() / 4 - logo.getHeight(), logo.getWidth(), logo.getHeight());
        batch.draw(exittipp, 0, 0, exittipp.getWidth(), exittipp.getHeight());
        batch.end();
    }
}
