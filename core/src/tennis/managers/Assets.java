package tennis.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import tennis.references.Models;

public class Assets implements Disposable {
	public static final int MAIN_MENU_SCREEN = 1;
	public static final int SPLASH_SCREEN_DEVELOPER = 2;
	public static final int SPLASH_SCREEN_US = 3;
	public static final int SETTINGS_SCREEN = 4;
	public static final int GAME_SCREEN = 5;
	public Array<String> models;
	public static TextureAtlas atlas;
	public static Skin skin;
	public AssetManager assetManager;

	public Assets() {
		models = Models.MODELS;
		assetManager = new AssetManager();
	}

	public void loadAll() {
		for (String model : models) {
			assetManager.load(model, Model.class);
		}

		assetManager.load("ui/uiskin.atlas", TextureAtlas.class);

		assetManager.load("ui/uiskin.json", Skin.class);
		assetManager.finishLoading();

		atlas = assetManager.get("ui/uiskin.atlas");
		skin = assetManager.get("ui/uiskin.json");
		Log.info("Finished loading assets");
	}

	public void loadScreen(int screen) {
		switch (screen) {
		case MAIN_MENU_SCREEN:
			loadSkin();
			break;
		case SPLASH_SCREEN_DEVELOPER:
			loadTextures();
			break;
		case SPLASH_SCREEN_US:
			loadTextures();
			break;
		case SETTINGS_SCREEN:
			loadSkin();
			break;
		case GAME_SCREEN:
			loadSkin();
			loadModels();
			break;
		}
		finish();
		Log.info("Finished loading assets");
	}
	
	private void loadModels(){
		for (String model : models) {
			assetManager.load(model, Model.class);
		}
	}
	
	private void loadSkin(){
		assetManager.load("ui/uiskin.atlas", TextureAtlas.class);
		assetManager.load("ui/uiskin.json", Skin.class);

		atlas = assetManager.get("ui/uiskin.atlas");
		skin = assetManager.get("ui/uiskin.json");
	}
	
	private void loadTextures(){
		assetManager.load("img/splash_screen/developer.png", Texture.class);
		assetManager.load("img/splash_screen/us.png", Texture.class);
	}

	public <T> T get(String direction, Class<T> type) {
		return assetManager.get(direction, type);
	}
	
	private void finish(){
		assetManager.finishLoading();
	}

	public void dispose() {
		assetManager.dispose();
		if (skin != null)
			skin.dispose();
		if (atlas != null)
			atlas.dispose();
	}
}
