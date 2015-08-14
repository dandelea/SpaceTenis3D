package tennis.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/**
 * Manage all the assets from the game, located in the /assets folder. Provide
 * methods to easily loads the required assets from a certain screen.
 * 
 * @author Daniel de los Reyes Leal
 * @version 1
 */
public class Assets implements Disposable {

	public static final int MAIN_MENU_SCREEN = 1;
	public static final int SPLASH_SCREEN_DEVELOPER = 2;
	public static final int SPLASH_SCREEN_US = 3;
	public static final int SETTINGS_SCREEN = 4;
	public static final int GAME_SCREEN = 5;
	public static final int GAME_OVER_SCREEN = 6;
	public static final int RULES_SCREEN = 7;

	public static final String URL_SKIN = "ui/uiskin.json";

	public static final String URL_FONT1 = "fonts/space_age.ttf";
	public static final String URL_FONT2 = "fonts/Montserrat-Regular.ttf";
	public static final String URL_FONT3 = "fonts/Montserrat-Bold.ttf";

	public static final String URL_SPLASH_DEV = "img/splash_screen/developer.png";
	public static final String URL_SPLASH_US = "img/splash_screen/us.png";

	public static final String URL_RULES_IMAGE1 = "img/rules/image01.png";
	public static final String URL_RULES_IMAGE2 = "img/rules/image02.png";
	public static final String URL_RULES_IMAGE3 = "img/rules/image03.png";
	public static final String URL_RULES_IMAGE4 = "img/rules/image04.png";

	public static final String URL_MUSIC1 = "music/mess.ogg";
	public static final String URL_MUSIC2 = "music/mess2.ogg";

	public static final String URL_SOUND1 = "sounds/laser.ogg";
	public static final String URL_SOUND2 = "sounds/button.ogg";
	public static final String URL_SOUND3 = "sounds/quit.ogg";
	public static final String URL_SOUND4 = "sounds/error.ogg";
	public static final String URL_SOUND5 = "sounds/success.ogg";
	public static final String URL_SOUND6 = "sounds/explosion.ogg";

	public Array<String> models;
	public static Skin skin;
	public static FreeTypeFontGenerator titleGenerator;
	public static FreeTypeFontGenerator fontGenerator;
	public AssetManager assetManager;

	/**
	 * Initializes {@link #Assets()}
	 */
	public Assets() {
		models = Models.MODELS;
		assetManager = new AssetManager();
	}

	/**
	 * Loads the required assets for a specific screen.
	 * 
	 * @param screen
	 *            Screen code for actual screen. See {@link #Assets()}
	 */
	public void loadScreen(int screen) {
		switch (screen) {
		case MAIN_MENU_SCREEN:
			loadSkin();
			loadSounds();
			break;
		case SPLASH_SCREEN_DEVELOPER:
			loadSplash();
			break;
		case SPLASH_SCREEN_US:
			loadSplash();
			break;
		case SETTINGS_SCREEN:
			loadSkin();
			loadSounds();
			break;
		case GAME_SCREEN:
			loadSkin();
			loadModels();
			loadMusic();
			loadSounds();
			break;
		case GAME_OVER_SCREEN:
			loadSkin();
			break;
		case RULES_SCREEN:
			loadSkin();
			loadRulesImages();
			break;
		}
		finish();
	}

	/**
	 * Only loads models located in assets folder.
	 */
	private void loadModels() {
		for (String model : models) {
			assetManager.load(model, Model.class);
		}

	}

	/**
	 * Only loads skin and fonts located in assets folder. Invoke with
	 * {@link #skin}, {@link #titleGenerator} and {@link #fontGenerator}
	 */
	private void loadSkin() {
		skin = new Skin(Gdx.files.internal(URL_SKIN));
		titleGenerator = new FreeTypeFontGenerator(
				Gdx.files.internal(URL_FONT1));
		fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal(URL_FONT2));
	}

	/**
	 * Only loads splash images located in assets folder.
	 */
	private void loadSplash() {
		assetManager.load(URL_SPLASH_DEV, Texture.class);
		assetManager.load(URL_SPLASH_US, Texture.class);
	}

	/**
	 * Only loads the images used in the rules screens
	 */
	private void loadRulesImages() {
		assetManager.load(URL_RULES_IMAGE1, Texture.class);
		assetManager.load(URL_RULES_IMAGE2, Texture.class);
		assetManager.load(URL_RULES_IMAGE3, Texture.class);
		assetManager.load(URL_RULES_IMAGE4, Texture.class);
	}

	/**
	 * Only loads the music
	 */
	private void loadMusic() {
		Jukebox.load(URL_MUSIC1, "game");
		Jukebox.load(URL_MUSIC2, "game2");
	}

	/**
	 * Only loads the sounds
	 */
	private void loadSounds() {
		Soundbox.load(URL_SOUND1, "laser");
		Soundbox.load(URL_SOUND2, "button");
		Soundbox.load(URL_SOUND3, "quit");
		Soundbox.load(URL_SOUND4, "error");
		Soundbox.load(URL_SOUND5, "success");
		Soundbox.load(URL_SOUND6, "explosion");
	}

	/**
	 * Get a previously loaded object with {@link Assets} in the
	 * {@link #assetManager}
	 * 
	 * @param direction
	 *            Related URI of resource in assets folder
	 * @param type
	 *            Class of object loaded
	 * @return Object loaded
	 */
	public <T> T get(String direction, Class<T> type) {
		return assetManager.get(direction, type);
	}

	/**
	 * Finish loading the {@link #assetManager}
	 */
	private void finish() {
		assetManager.finishLoading();
		Log.info("Finished loading assets");
	}

	/**
	 * Dispose objects loaded by the {@link #assetManager}.
	 */
	public void dispose() {
		assetManager.dispose();
		if (skin != null)
			skin.dispose();
		if (titleGenerator != null)
			titleGenerator.dispose();
	}
}
