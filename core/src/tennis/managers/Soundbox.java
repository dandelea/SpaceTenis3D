package tennis.managers;

import java.util.HashMap;

import tennis.SpaceTennis3D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Soundbox {

	private static HashMap<String, Sound> sounds;

	static {
		sounds = new HashMap<String, Sound>();
	}

	public static void load(String path, String name) {
		Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
		sounds.put(name, sound);
	}

	public static void play(String name) {
		boolean on = Gdx.app.getPreferences(SpaceTennis3D.TITLE).getBoolean("sound");
		if (on) {
			sounds.get(name).play();
		}
	}

	public static void loop(String name) {
		boolean on = Gdx.app.getPreferences(SpaceTennis3D.TITLE).getBoolean("sound");
		if (on) {
			sounds.get(name).loop();
		}
	}

	public static void stop(String name) {
		sounds.get(name).stop();
	}

	public static void stopAll() {
		for (Sound s : sounds.values()) {
			s.stop();
		}
	}
}
