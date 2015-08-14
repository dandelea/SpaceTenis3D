package tennis.managers;

import java.util.HashMap;

import tennis.SpaceTennis3D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * Manage the music, to be easily called from the game.
 * @author Daniel de los Reyes Leal
 * @version 1
 */
public class Jukebox {

	private static HashMap<String, Music> musics;

	static {
		musics = new HashMap<String, Music>();
	}

	/**
	 * Loads a certain song from the assets folder. Used in #
	 * {@link tennis.managers.Assets#loadMusic()}
	 * 
	 * @param path
	 *            Relative path to the song asset
	 * @param name
	 *            Name of the song
	 */
	protected static void load(String path, String name) {
		Music music = Gdx.audio.newMusic(Gdx.files.internal(path));
		musics.put(name, music);
	}

	/**
	 * @param name
	 *            Name of the song
	 * @return if the song is being played
	 */
	public static boolean isPlaying(String name) {
		return musics.get("game").isPlaying();
	}

	/**
	 * Plays a song
	 * 
	 * @param name
	 *            Name of the song
	 */
	public static void play(String name) {
		boolean on = Gdx.app.getPreferences(SpaceTennis3D.TITLE).getBoolean(
				"music");
		if (on) {
			musics.get(name).play();
		}
	}

	/**
	 * Loops a song
	 * 
	 * @param name
	 *            Name of the song
	 */
	public static void loop(String name) {
		boolean on = Gdx.app.getPreferences(SpaceTennis3D.TITLE).getBoolean(
				"music");
		if (on) {
			musics.get(name).setLooping(true);
			musics.get(name).play();
		}
	}

	/**
	 * Stops playing a song
	 * 
	 * @param name
	 *            Name of the song
	 */
	public static void stop(String name) {
		musics.get(name).stop();
	}

	/**
	 * Stops playing all the music
	 */
	public static void stopAll() {
		for (Music m : musics.values()) {
			m.stop();
		}
	}

	/**
	 * Pauses a song
	 * 
	 * @param name
	 *            Name of the song
	 */
	public static void pause(String name) {
		musics.get(name).pause();
	}

}
