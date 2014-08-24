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
import de.cubeisland.games.resource.bag.Animations;

import static de.cubeisland.games.PlayerInput.Mode.LEFT;

public class GameScreen extends DisconnectScreen {
    private final DisconnectGame game;
    private PlayerInput playerInput;
    private World worldLeft;
    private World worldRight;

    private float disconnectTime;
    private float maxdisconnectTime = 5;

    public GameScreen(DisconnectGame game) {
        this.game = game;
    }

    @Override
    public void show() {

        setClearColor(Color.MAGENTA);

        Animations animations = game.getResourcePack().animations;
        this.worldLeft = new World(game, Camera.left(), new Player(animations.characterleftfront, animations.characterleftside, animations.characterleftback), "LevelL1");
        this.worldRight = new World(game, Camera.right(), new Player(animations.characterrightfront, animations.characterrightside, animations.characterrightback), "LevelR1");
        this.playerInput = new PlayerInput(this.worldLeft.getPlayer(), this.worldRight.getPlayer());
        game.getInputMultiplexer().addProcessor(playerInput);

        game.getResourcePack().musics.maintheme.setLooping(true);
        game.getResourcePack().musics.maintheme.setVolume(0.25f);
        game.getResourcePack().musics.maintheme.play();
    }

    @Override
    public void update(float delta) {
        this.worldLeft.update(this.game, delta);
        this.worldRight.update(this.game, delta);
    }

    @Override
    public void draw(float delta) {
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
        batch.draw(divider, Gdx.graphics.getWidth() / 8 - 4, 0, divider.getWidth(), Gdx.graphics.getHeight(),
                0, 0, 1, Gdx.graphics.getHeight() / divider.getHeight());

        batch.draw(game.getResourcePack().textures.iteminhand, Gdx.graphics.getWidth() / 8 - 13, 0, 16, 16);
        batch.draw(game.getResourcePack().textures.iteminhand, Gdx.graphics.getWidth() / 8 - 3, 0, 16, 16);

        if (this.playerInput.getDisconnected()) {
            disconnectTime += delta;
        } else {
            disconnectTime -= delta * 4;
        }

        if (disconnectTime > maxdisconnectTime) {
            disconnectTime = maxdisconnectTime;
        } else if (disconnectTime < 0) {
            disconnectTime = 0;
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
