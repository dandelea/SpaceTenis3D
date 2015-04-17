package tenis.gamestates;

import tenis.managers.GameKeys;
import tenis.managers.GameStateManager;
import tenis.managers.State;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class GameOverState extends GameState {

	private SpriteBatch sb;
	private ShapeRenderer sr;

	private char[] newName;
	private int currentChar;

	private BitmapFont gameOverFont;
	private BitmapFont font;

	public GameOverState(GameStateManager gsm) {
		super(gsm);
	}

	public void init() {

		sb = new SpriteBatch();
		sr = new ShapeRenderer();

		newName = new char[] { 'A', 'A', 'A' };
		currentChar = 0;

		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
				Gdx.files.internal("fonts/space age.ttf"));
		gameOverFont = gen.generateFont(32);
		font = gen.generateFont(20);
	}

	public void update(float dt) {
		handleInput();
	}

	public void draw() {

		sb.setProjectionMatrix(Asteroides.cam.combined);

		sb.begin();

		String s;
		float w;

		s = "Game Over";
		w = gameOverFont.getBounds(s).width;
		gameOverFont.draw(sb, s, (Asteroides.WIDTH - w) / 2, 220);

		s = "New High Score: " + Asteroides.score;
		w = font.getBounds(s).width;
		font.draw(sb, s, (Asteroides.WIDTH - w) / 2, 180);

		for (int i = 0; i < newName.length; i++) {
			font.draw(sb, Character.toString(newName[i]), 230 + 14 * i, 120);
		}

		sb.end();

		sr.begin(ShapeType.Line);
		sr.line(230 + 14 * currentChar, 100, 244 + 14 * currentChar, 100);
		sr.end();

	}

	public void handleInput() {

		if (GameKeys.isPressed(GameKeys.ENTER)) {

			SQLiteController.savePoints(new String(newName), Asteroides.score,
					Asteroides.level, Asteroides.bullets, Asteroides.asteroids);

			gsm.setState(State.MENU);
		}

		if (GameKeys.isPressed(GameKeys.UP)) {
			if (newName[currentChar] == ' ') {
				newName[currentChar] = 'Z';
			} else {
				newName[currentChar]--;
				if (newName[currentChar] < 'A') {
					newName[currentChar] = ' ';
				}
			}
		}

		if (GameKeys.isPressed(GameKeys.DOWN)) {
			if (newName[currentChar] == ' ') {
				newName[currentChar] = 'A';
			} else {
				newName[currentChar]++;
				if (newName[currentChar] > 'Z') {
					newName[currentChar] = ' ';
				}
			}
		}

		if (GameKeys.isPressed(GameKeys.RIGHT)) {
			if (currentChar < newName.length - 1) {
				currentChar++;
			}
		}

		if (GameKeys.isPressed(GameKeys.LEFT)) {
			if (currentChar > 0) {
				currentChar--;
			}
		}

	}

	public void dispose() {
		sb.dispose();
		sr.dispose();
		gameOverFont.dispose();
		font.dispose();
	}

}
