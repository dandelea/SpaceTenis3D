package tenis.managers;

import tenis.gamestates.GameOverState;
import tenis.gamestates.GameState;
import tenis.gamestates.HighScoreState;
import tenis.gamestates.MenuState;
import tenis.gamestates.PlayState;
import tenis.gamestates.ProfileState;

public class GameStateManager {
	
	// current game state
	private GameState gameState;
	
	
	public GameStateManager() {
		setState(State.MENU);
	}
	
	public void setState(State state) {
		if(gameState != null) gameState.dispose();
		if(state == State.MENU) {
			gameState = new MenuState(this);
		}
		if(state == State.PLAY) {
			gameState = new PlayState(this);
		}
		if(state == State.HIGHSCORE) {
			gameState = new HighScoreState(this);
		}
		if(state == State.GAMEOVER) {
			gameState = new GameOverState(this);
		}
		if(state == State.PROFILE) {
			gameState = new ProfileState(this);
		}
	}
	
	public void update(float dt) {
		gameState.update(dt);
	}
	
	public void draw() {
		gameState.draw();
	}
	
}











