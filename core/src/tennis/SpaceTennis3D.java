package tennis;

import tennis.managers.bluetooth.BluetoothServer;
import tennis.screens.MainMenuScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class SpaceTennis3D extends Game {
	public static final String TITLE = "Space Tennis 3D";
	public static final String VERSION = "ver 0.1";
	public static int WIDTH;
	public static int HEIGHT;

	@Override
	public void create() {
		BluetoothServer server = new BluetoothServer();
		Thread serverThread = new Thread(server);
		serverThread.start();
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		setScreen(new MainMenuScreen());
	}

	public void dispose() {
		super.dispose();
	}

	public void render() {
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		super.render();
	}

	public void resize(int witdh, int height) {
		super.resize(witdh, height);
	}

	public void pause() {
		super.pause();
	}

	public void resume() {
		super.resume();
	}
	
	

}