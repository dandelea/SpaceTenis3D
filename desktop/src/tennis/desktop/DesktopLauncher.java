package tennis.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import tennis.SpaceTennis3D;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.vSyncEnabled = true;
		config.width = 800;
		config.height = 600;
		config.addIcon("img/icon.png", Files.FileType.Internal);
		new LwjglApplication(new SpaceTennis3D(), config);
	}
}
