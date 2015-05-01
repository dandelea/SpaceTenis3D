package tenis.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

import tenis.references.Models;



public class Assets {
	public Array<String> models;
	public static Skin skin;
	public AssetManager assetManager;
	
	
	public Assets(){
		models = Models.MODELS;
		assetManager = new AssetManager();
	}
	
	public void loadAll(){
		for (String model: models){
			assetManager.load(model, Model.class);
		}
		
		
		assetManager.load("ui/uiskin.json", Skin.class);
		assetManager.finishLoading();
		
		skin = assetManager.get("ui/uiskin.json");
		System.out.println("Finished loading");
	}
	
	public <T> T get(String direction, Class<T> type){
		return assetManager.get(direction, type);
	}
	
	public void dispose(){
		assetManager.dispose();
		skin.dispose();
	}
}
