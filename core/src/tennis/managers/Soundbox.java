package tennis.managers;

import java.util.HashMap;

import tennis.SpaceTennis3D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * Manage the sounds, to be easily called from the game.
 * 
 * @author Daniel de los Reyes Leal
 * @version 1
 */
public class Soundbox {

	private static HashMap<String, Sound> sounds;

	static {
		sounds = new HashMap<String, Sound>();
	}

	/**
	 * Loads a certain sound from the assets folder. Used in #
	 * {@link tennis.managers.Assets#loadSounds()}
	 * 
	 * @param path
	 *            Relative path to the sound asset
	 * @param name
	 *            Name of the sound
	 */
	protected static void load(String path, String name) {
		Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
		sounds.put(name, sound);
	}

	/**
	 * Plays a sound
	 * 
	 * @param name
	 *            Name of the sound
	 */
	public static void play(String name) {
		boolean on = Gdx.app.getPreferences(SpaceTennis3D.TITLE).getBoolean(
				"sound");
		if (on) {
			sounds.get(name).play();
		}
	}

	/**
	 * Loops a sound
	 * 
	 * @param name
	 *            Name of the sound
	 */
	public static void loop(String name) {
		boolean on = Gdx.app.getPreferences(SpaceTennis3D.TITLE).getBoolean(
				"sound");
		if (on) {
			sounds.get(name).loop();
		}
	}
}
