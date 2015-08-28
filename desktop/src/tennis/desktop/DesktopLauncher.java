package tennis.desktop;

import java.awt.Dimension;
import java.awt.Toolkit;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import tennis.SpaceTennis3D;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		config.vSyncEnabled = true;
		config.width = screenDimension.width;
		config.height = screenDimension.height;
		config.fullscreen = true;
		config.addIcon("img/icon.png", Files.FileType.Internal);
		new LwjglApplication(new SpaceTennis3D(), config);
	}
}
