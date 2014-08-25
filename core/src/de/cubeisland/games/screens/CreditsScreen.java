package de.cubeisland.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.cubeisland.games.DisconnectGame;

import static com.badlogic.gdx.Input.Keys.*;
import static de.cubeisland.games.screens.UIInput.Handler;

public class CreditsScreen extends UIScreen {

    public CreditsScreen(DisconnectGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        getInput().add(new Handler() {
            @Override
            public boolean handle(DisconnectGame game) {
                game.setScreen(new TitleScreen(game));
                return true;
            }
        }, ESCAPE, ENTER, SPACE);
    }

    @Override
    public void draw(float delta) {
        SpriteBatch batch = game.getGuiCamera().getSpriteBatch();
        TextureRegion creditsscreen = game.getResourcePack().textures.creditscreen;

        batch.begin();
        batch.draw(creditsscreen,
                    Gdx.graphics.getWidth() / 8 - creditsscreen.getRegionWidth() / 2,
                    Gdx.graphics.getHeight() / 8 - creditsscreen.getRegionHeight() / 2,
                    creditsscreen.getRegionWidth(),
                    creditsscreen.getRegionHeight());
        batch.end();
    }
}
