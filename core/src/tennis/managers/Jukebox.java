package tennis.managers;

import java.util.HashMap;

import tennis.SpaceTennis3D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class Jukebox {
	
	private static HashMap<String, Music> musics;
	
	static {
		musics = new HashMap<String, Music>();
	}
	
	public static void load(String path, String name) {
		Music music = Gdx.audio.newMusic(Gdx.files.internal(path));
		musics.put(name, music);
	}
	
	public static Music get(String name){
		return musics.get(name);		
	}
	
	public static void play(String name) {
		boolean on = Gdx.app.getPreferences(SpaceTennis3D.TITLE).getBoolean("music");
		if (on) {
			musics.get(name).play();
		}
	}
	
	public static void loop(String name) {
		boolean on = Gdx.app.getPreferences(SpaceTennis3D.TITLE).getBoolean("music");
		if (on) {
			musics.get(name).setLooping(true);
			musics.get(name).play();
		}
	}
	
	public static void stop(String name) {
		musics.get(name).stop();
	}
	
	public static void stopAll() {
		for(Music m : musics.values()) {
			m.stop();
		}
	}
	
}













