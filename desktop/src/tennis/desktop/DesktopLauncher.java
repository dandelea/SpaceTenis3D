package tennis.desktop;

import tennis.SpaceTennis3D;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.vSyncEnabled = true;
		config.width = 800;
		config.height = 600;
		new LwjglApplication(new SpaceTennis3D(), config);
	}
}
