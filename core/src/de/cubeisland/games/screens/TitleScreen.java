package de.cubeisland.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.cubeisland.games.Camera;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.World;
import de.cubeisland.games.entity.Player;
import de.cubeisland.games.resource.bag.Animations;

import static com.badlogic.gdx.Input.Keys.*;
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
        }, SPACE, ENTER).add(new Handler() {
            @Override
            public boolean handle(DisconnectGame game) {
                game.exit();
                return true;
            }
        }, ESCAPE).add(new Handler() {
            @Override
            public boolean handle(DisconnectGame game) {
                game.transition(CreditsScreen.class);
                return true;
            }
        }, C);
    }

    @Override
    public void draw(float delta) {
        TextureRegion logo = game.getResourcePack().textures.logo;
        TextureRegion exittipp = game.getResourcePack().textures.exittipp;
        this.titleWorld.render(this.game, delta);
        Camera camera = this.titleWorld.getCamera();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        SpriteBatch batch = this.titleWorld.beginBatch();
        batch.draw(logo, 0, Gdx.graphics.getHeight() / 4 - logo.getRegionHeight(), logo.getRegionWidth(), logo.getRegionHeight());
        batch.draw(exittipp, 0, 0, exittipp.getRegionWidth(), exittipp.getRegionHeight());
        batch.end();
    }
}
