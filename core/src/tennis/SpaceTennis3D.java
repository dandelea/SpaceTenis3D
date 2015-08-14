package tennis;

import tennis.managers.Log;
import tennis.managers.bluetooth.BluetoothServer;
import tennis.managers.physics.ParticleController;
import tennis.objects.Difficulty;
import tennis.objects.Scoreboard;
import tennis.screens.splash.SplashDeveloperScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

/**
 * Space Tennis 3D Application Listener
 * 
 * @author Daniel de los Reyes Leal
 * @version 1
 */
public class SpaceTennis3D extends Game {
	public static final String TITLE = "Space Tennis 3D";
	public static final String VERSION = "ver 0.1";
	public static int WIDTH;
	public static int HEIGHT;
	public static Scoreboard lastScoreboard;
	public static Difficulty difficulty;
	public static int games;
	public static ParticleController particleController;

	/**
	 * First method to create the application. Initialize the server and start
	 * it in a new thread. Then initializes the attributes and preferences of
	 * the game. Starts with Splash screens
	 */
	@Override
	public void create() {
		Log.init();

		BluetoothServer server = new BluetoothServer();
		Thread serverThread = new Thread(server);
		serverThread.start();

		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		Gdx.app.getPreferences(TITLE).putInteger("FOV", 67);
		Gdx.app.getPreferences(TITLE).putBoolean("music", true);
		Gdx.app.getPreferences(TITLE).putBoolean("sound", true);
		difficulty = Difficulty.EASY;
		games = 0;

		setScreen(new SplashDeveloperScreen());
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();

		super.render();
	}

	@Override
	public void resize(int witdh, int height) {
		super.resize(witdh, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

	/**
	 * Static version of {@link #setScreen(Screen)} . Easier to call
	 * 
	 * @param screen
	 *            Screen to display
	 */
	public static void goTo(Screen screen) {
		Game game = (Game) Gdx.app.getApplicationListener();
		game.setScreen(screen);
	}

}