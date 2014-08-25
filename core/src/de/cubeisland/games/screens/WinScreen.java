package de.cubeisland.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.cubeisland.games.DisconnectGame;

import static com.badlogic.gdx.Input.Keys.*;
import static de.cubeisland.games.screens.UIInput.Handler;

public class WinScreen extends UIScreen {

    public WinScreen(DisconnectGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        getInput().add(new Handler() {
            @Override
            public boolean handle(DisconnectGame game) {
                game.transition(TitleScreen.class);
                return true;
            }
        }, ESCAPE, ENTER, SPACE).add(new Handler() {
            @Override
            public boolean handle(DisconnectGame game) {
                game.transition(CreditsScreen.class);
                return true;
            }
        }, C).add(new Handler() {
            @Override
            public boolean handle(DisconnectGame game) {
                game.transition(GameScreen.class);
                return true;
            }
        }, R);
    }

    @Override
    public void draw(float delta) {
        SpriteBatch batch = game.getGuiCamera().getSpriteBatch();
        TextureRegion wonscreen = game.getResourcePack().textures.wonscreen;

        batch.begin();
        batch.draw(wonscreen, Gdx.graphics.getWidth() / 8 - wonscreen.getRegionWidth() / 2, Gdx.graphics.getHeight() / 8 - wonscreen.getRegionHeight() / 2, wonscreen.getRegionWidth(), wonscreen.getRegionHeight());
        batch.end();
    }
}
