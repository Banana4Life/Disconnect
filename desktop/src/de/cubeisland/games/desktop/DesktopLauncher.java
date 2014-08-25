package de.cubeisland.games.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.cubeisland.games.DisconnectGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.vSyncEnabled = false;
        config.foregroundFPS = 300;
        config.addIcon("icons/icon16.png", Files.FileType.Internal);
        config.addIcon("icons/icon32.png", Files.FileType.Internal);
        config.title = "Disconnect";
		new LwjglApplication(new DisconnectGame(), config);
	}
}
