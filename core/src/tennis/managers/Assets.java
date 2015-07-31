package tennis.managers;

import tennis.references.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

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
	
	
	public Array<String> models;
	public static Skin skin;
	public static FreeTypeFontGenerator titleGenerator;
	public static FreeTypeFontGenerator fontGenerator;
	public AssetManager assetManager;

	public Assets() {
		models = Models.MODELS;
		assetManager = new AssetManager();
	}

	public void loadAll() {
		loadModels();

		loadSkin();

		finish();
	}

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
	 * Only loads skin located in assets folder.
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
	private void loadRulesImages(){
		assetManager.load(URL_RULES_IMAGE1, Texture.class);
		assetManager.load(URL_RULES_IMAGE2, Texture.class);
	}
	
	/**
	 * Only loads the music
	 */
	private void loadMusic(){
		Jukebox.load("music/mess.ogg", "game");
		Jukebox.load("music/mess2.ogg", "game2");
	}
	
	/**
	 * Only loads the sounds
	 */
	private void loadSounds(){
		Soundbox.load("sounds/laser.ogg", "laser");
		Soundbox.load("sounds/button.ogg", "button");
		Soundbox.load("sounds/quit.ogg", "quit");
	}

	/**
	 * Get a previously loaded object with {@link Assets}
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

	private void finish() {
		assetManager.finishLoading();
		Log.info("Finished loading assets");
	}

	/**
	 * Dispose objects loaded by the asset Manager.
	 */
	public void dispose() {
		assetManager.dispose();
		if (skin != null)
			skin.dispose();
		if (titleGenerator != null)
			titleGenerator.dispose();
		if (fontGenerator != null)
			fontGenerator.dispose();
	}
}
