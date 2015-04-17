package tenis;

import tenis.screens.MainMenuScreen;

import com.badlogic.gdx.Game;

public class Tenis3D extends Game {
	// Public parameters
	public static final String TITLE = "Tenis 3D";
	public static final String VERSION = "ver 0.1";

	@Override
	public void create() {
		//setScreen(new SplashUsScreen());
		setScreen(new MainMenuScreen());
	}

	public void dispose() {
		super.dispose();
	}

	public void render() {
		/*
		 * Gdx.gl.glClearColor(0, 0, 0, 0);
		 * Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		 */

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