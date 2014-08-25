package de.cubeisland.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.cubeisland.games.Camera;
import de.cubeisland.games.DisconnectGame;
import de.cubeisland.games.PlayerInput;
import de.cubeisland.games.World;
import de.cubeisland.games.entity.Player;
import de.cubeisland.games.resource.bag.Animations;
import de.cubeisland.games.util.SoundPlayer;

import static de.cubeisland.games.Camera.CameraType;
import static de.cubeisland.games.PlayerInput.Mode.LEFT;

public class GameScreen extends DisconnectScreen {
    private final DisconnectGame game;
    private PlayerInput playerInput;
    private World worldLeft;
    private World worldRight;

    private float disconnectTime;
    private float maxdisconnectTime = 5;
    private SoundPlayer timerSound;
    private SoundPlayer.SoundInstance timerSoundInstance;
    private SoundPlayer.SoundInstance alarmSound;

    public GameScreen(DisconnectGame game) {
        this.game = game;
        this.timerSound = game.getResourcePack().sounds.timer;
    }

    @Override
    public void show() {
        Animations animations = game.getResourcePack().animations;
        this.worldLeft = new World(game, Camera.create(CameraType.LEFT), new Player(animations.characterleftfront, animations.characterleftside, animations.characterleftback), "LevelL1");
        this.worldRight = new World(game, Camera.create(CameraType.RIGHT), new Player(animations.characterrightfront, animations.characterrightside, animations.characterrightback), "LevelR1");
        this.playerInput = new PlayerInput(this, this.worldLeft.getPlayer(), this.worldRight.getPlayer());
        this.worldLeft.getPlayer().setOtherPlayer(this.worldRight.getPlayer());
        this.worldRight.getPlayer().setOtherPlayer(this.worldLeft.getPlayer());
        game.getInputMultiplexer().prepend(playerInput);
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
            divider = game.getResourcePack().textures.dividerleft.getTexture();
        } else {
            divider = game.getResourcePack().textures.dividerright.getTexture();
        }
        int width = Gdx.graphics.getWidth();
        int mid = width / 8;
        divider.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        batch.draw(divider, mid - 4, 0, divider.getWidth(), Gdx.graphics.getHeight(),
                0, 0, 1, Gdx.graphics.getHeight() / divider.getHeight());

        batch.draw(game.getResourcePack().textures.iteminhand, mid - 13, 0, 16, 16);
        if (worldLeft.getPlayer().getItemInHandTex() != null) {
            batch.draw(worldLeft.getPlayer().getItemInHandTex(), mid - 13, 0, 16, 16);
        }
        batch.draw(game.getResourcePack().textures.iteminhand, mid - 3, 0, 16, 16);
        if (worldRight.getPlayer().getItemInHandTex() != null) {
            batch.draw(worldRight.getPlayer().getItemInHandTex(), mid - 3, 0, 16, 16);
        }

        if (this.playerInput.getDisconnected()) {
            disconnectTime += delta;
        } else {
            disconnectTime -= delta * 4;
        }

        if (disconnectTime > maxdisconnectTime) {
            disconnectTime = maxdisconnectTime;
            // this.game.lose();
        } else if (disconnectTime < 0) {
            disconnectTime = 0;
        }

        TextureRegion energybar = game.getResourcePack().animations.energybar.getKeyFrames()[(int) ((game.getResourcePack().animations.energybar.getKeyFrames().length - 1) * disconnectTime / maxdisconnectTime)];
        batch.draw(energybar, mid - 16, 0, 32, 16);
        batch.end();
    }

    @Override
    public void hide() {
        super.hide();
        game.getInputMultiplexer().remove(this.playerInput);
        this.stopTimeSound();
        if (this.alarmSound != null) {
            this.alarmSound.stop();
            this.alarmSound = null;
        }
        dispose();
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
        super.dispose();
    }

    public PlayerInput getPlayerInput() {
        return playerInput;
    }

    public void startTimerSound() {
        if (timerSoundInstance == null) {
            this.timerSoundInstance = timerSound.start(.15f).looping(true);
        }
    }

    public void stopTimeSound() {
        if (timerSoundInstance != null) {
            this.timerSoundInstance.stop();
            this.timerSoundInstance = null;
        }
    }

    public void startAlarm() {
        if (this.alarmSound == null) {
            this.alarmSound = game.getResourcePack().sounds.alarm.start(.5f);
        }
    }

}
