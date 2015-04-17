package tenis;

import tenis.screens.MainMenuScreen;
import tenis.managers.GameInputProcessor;
import tenis.managers.GameStateManager;
import tenis.managers.Jukebox;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class Tenis3D extends Game {
	// Public parameters
	public static final String TITLE = "Tenis 3D";
	public static final String VERSION = "ver 0.1";
	public static int WIDTH;
	public static int HEIGHT;
	
	private GameStateManager gsm;

	@Override
	public void create() {
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		
		Gdx.input.setInputProcessor(
				new GameInputProcessor()
		);
		
		Jukebox.load("sounds/explode.ogg", "explode");
		Jukebox.load("sounds/extralife.ogg", "extralife");
		Jukebox.load("sounds/largesaucer.ogg", "largesaucer");
		Jukebox.load("sounds/pulsehigh.ogg", "pulsehigh");
		Jukebox.load("sounds/pulselow.ogg", "pulselow");
		Jukebox.load("sounds/saucershoot.ogg", "saucershoot");
		Jukebox.load("sounds/shoot.ogg", "shoot");
		Jukebox.load("sounds/smallsaucer.ogg", "smallsaucer");
		Jukebox.load("sounds/thruster.ogg", "thruster");
		
		gsm = new GameStateManager();
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