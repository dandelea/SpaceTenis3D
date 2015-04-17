package tenis.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import tenis.Tenis3D;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = Tenis3D.TITLE + " " + Tenis3D.VERSION;
		config.vSyncEnabled = true;
		config.fullscreen = false;
		config.useGL30 = false;
		config.width = 800;
		config.height = 600;
		
		new LwjglApplication(new Tenis3D(), config);
	}
}
