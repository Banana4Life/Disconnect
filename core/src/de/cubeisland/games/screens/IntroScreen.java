package de.cubeisland.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.cubeisland.games.DisconnectGame;

import static com.badlogic.gdx.Input.Keys.ENTER;
import static com.badlogic.gdx.Input.Keys.SPACE;

public class IntroScreen extends UIScreen {

    private float time;

    public IntroScreen(DisconnectGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        getInput().add(new UIInput.Handler() {
            @Override
            public boolean handle(DisconnectGame game) {
                game.setScreen(new GameScreen(game));
                return true;
            }
        }, ENTER, SPACE);
    }

    @Override
    public void draw(float delta) {
        SpriteBatch batch = game.getGuiCamera().getSpriteBatch();
        TextureRegion introscreen = game.getResourcePack().textures.introscreen;
        TextureRegion pressenter = game.getResourcePack().textures.pressenter;
        TextureRegion overlay = game.getResourcePack().textures.wallbottom;

        time += delta * 10;

        if (time > introscreen.getRegionHeight() - Gdx.graphics.getHeight() / 4 + pressenter.getRegionHeight()) {
            time = introscreen.getRegionHeight() - Gdx.graphics.getHeight() / 4 + pressenter.getRegionHeight();
        }

        batch.begin();
        batch.draw(introscreen,
                Gdx.graphics.getWidth() / 8 - introscreen.getRegionWidth() / 2,
                Gdx.graphics.getHeight() / 4 - introscreen.getRegionHeight() + (int) time,
                introscreen.getRegionWidth(),
                introscreen.getRegionHeight());
        batch.draw(pressenter,
                0,
                0,
                pressenter.getRegionWidth(),
                pressenter.getRegionHeight());
        for (int i = 0; pressenter.getRegionWidth() + i * overlay.getRegionWidth() < Gdx.graphics.getWidth() / 4; i++) {
            batch.draw(overlay,
                    pressenter.getRegionWidth() + i * overlay.getRegionWidth(),
                    0,
                    overlay.getRegionWidth(),
                    overlay.getRegionHeight());
        }
        batch.end();
    }
}
