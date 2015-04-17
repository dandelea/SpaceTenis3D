package tenis.managers;

import tenis.gamestates.GameState;
import tenis.gamestates.MainMenuState;
import tenis.gamestates.PlayState;
import tenis.gamestates.SettingsState;
import tenis.gamestates.SplashDevState;
import tenis.gamestates.SplashPublisherState;

public class GameStateManager {
	
	// current game state
	private GameState gameState;
	
	
	public GameStateManager() {
		setState(State.SPLASH_DEV);
	}
	
	public void setState(State state) {
		if(gameState != null) gameState.dispose();
		if(state == State.SPLASH_DEV) {
			gameState = new SplashDevState(this);
		}
		if(state == State.SPLASH_PUBLISHER) {
			gameState = new SplashPublisherState(this);
		}
		if(state == State.MAIN_MENU) {
			gameState = new MainMenuState(this);
		}
		if(state == State.SETTINGS) {
			gameState = new SettingsState(this);
		}
		if(state == State.GAME) {
			gameState = new PlayState(this);
		}
	}
	
	public void update(float dt) {
		gameState.update(dt);
	}
	
	public void draw() {
		gameState.draw();
	}
	
}











