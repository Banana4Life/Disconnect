package de.cubeisland.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.cubeisland.games.Camera;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.PlayerInput;
import de.cubeisland.games.World;
import de.cubeisland.games.entity.Player;

import static de.cubeisland.games.PlayerInput.Mode.LEFT;

public class GameScreen extends ScreenAdapter {
    private final DisconnectGame game;
    private final FPSLogger logger = new FPSLogger();
    private PlayerInput playerInput;
    private World worldLeft;
    private World worldRight;

    private float disconnectTime;
    private float maxdisconnectTime = 10;

    public GameScreen(DisconnectGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        this.worldLeft = new World(game, Camera.left(), new Player());
        this.worldRight = new World(game, Camera.right(), new Player());
        this.playerInput = new PlayerInput(this.worldLeft.getPlayer(), this.worldRight.getPlayer());
        game.getInputMultiplexer().addProcessor(playerInput);

        game.getResourcePack().musics.maintheme.setLooping(true);
        game.getResourcePack().musics.maintheme.setVolume(0.25f);
        game.getResourcePack().musics.maintheme.play();
    }

    @Override
    public void render(float delta) {
        logger.log();
        this.worldLeft.update(this.game, delta);
        this.worldRight.update(this.game, delta);

        Gdx.gl.glClearColor(Color.MAGENTA.r, Color.MAGENTA.g, Color.MAGENTA.b, Color.MAGENTA.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.worldLeft.render(this.game, delta);
        this.worldRight.render(this.game, delta);
        renderGUI(delta);
    }

    private void renderGUI(float delta) {
        SpriteBatch batch = this.game.getGuiCamera().getSpriteBatch();
        batch.begin();
        Texture divider;
        if (playerInput.getMode() == LEFT) {
            divider = game.getResourcePack().textures.dividerleft;
        } else {
            divider = game.getResourcePack().textures.dividerright;
        }
        divider.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        batch.draw(divider, Gdx.graphics.getWidth() / 8 - 4, 16, divider.getWidth(), Gdx.graphics.getHeight(),
                0, 0, 1, Gdx.graphics.getHeight() / divider.getHeight());

        batch.draw(game.getResourcePack().textures.iteminhand, Gdx.graphics.getWidth() / 8 - 8, 0, 16, 16);

        if (this.playerInput.getDisconnected()) {
            disconnectTime += delta;
        } else {
            disconnectTime = 0;
        }

        if (disconnectTime > maxdisconnectTime) {
            disconnectTime = maxdisconnectTime;
        }

        TextureRegion energybar = game.getResourcePack().animations.energybar.getKeyFrames()[(int)((game.getResourcePack().animations.energybar.getKeyFrames().length - 1) * disconnectTime / maxdisconnectTime)];
        batch.draw(energybar, Gdx.graphics.getWidth() / 8 - 16, 0, 32, 16);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        this.worldLeft.getCamera().resize(width, height);
        this.worldRight.getCamera().resize(width, height);
    }

    @Override
    public void dispose() {
        this.worldLeft.dispose();
        this.worldRight.dispose();
        game.getInputMultiplexer().removeProcessor(this.playerInput);
        super.dispose();
    }

    public PlayerInput getPlayerInput() {
        return playerInput;
    }

}
