package tenis.gamestates;

import java.util.ArrayList;

import cbd.asteroides.Asteroides;
import cbd.asteroides.database.SQLiteController;
import cbd.asteroides.managers.GameKeys;
import cbd.asteroides.managers.GameStateManager;
import cbd.asteroides.managers.State;
import cbd.asteroides.objects.Asteroid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

public class MenuState extends GameState {

	private SpriteBatch sb;
	private ShapeRenderer sr;

	private BitmapFont titleFont;
	private BitmapFont font;

	private final String title = "Asteroides";

	private int currentItem;
	private String[] menuItems;

	private ArrayList<Asteroid> asteroids;

	public MenuState(GameStateManager gsm) {
		super(gsm);
	}

	public void init() {
		SQLiteController.setDatabase();

		sb = new SpriteBatch();
		sr = new ShapeRenderer();

		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
				Gdx.files.internal("fonts/space age.ttf"));

		titleFont = gen.generateFont(56);
		titleFont.setColor(Color.WHITE);

		font = gen.generateFont(20);
		menuItems = new String[] { "Play", "Highscores", "Quit" };

		asteroids = new ArrayList<Asteroid>();
		for (int i = 0; i < 6; i++) {
			asteroids.add(new Asteroid(MathUtils.random(Asteroides.WIDTH),
					MathUtils.random(Asteroides.HEIGHT), Asteroid.LARGE));
		}

	}

	public void update(float dt) {

		handleInput();

		for (int i = 0; i < asteroids.size(); i++) {
			asteroids.get(i).update(dt);
		}

	}

	public void draw() {

		sb.setProjectionMatrix(Asteroides.cam.combined);
		sr.setProjectionMatrix(Asteroides.cam.combined);

		// draw asteroids
		for (int i = 0; i < asteroids.size(); i++) {
			asteroids.get(i).draw(sr);
		}

		sb.begin();

		// draw title
		float width = titleFont.getBounds(title).width;
		titleFont.draw(sb, title, (Asteroides.WIDTH - width) / 2, (Asteroides.HEIGHT - Asteroides.HEIGHT/4));

		// draw menu
		for (int i = 0; i < menuItems.length; i++) {
			width = font.getBounds(menuItems[i]).width;
			if (currentItem == i)
				font.setColor(Color.RED);
			else
				font.setColor(Color.WHITE);
			font.draw(sb, menuItems[i], (Asteroides.WIDTH - width) / 2,
					180 - 35 * i);
		}

		sb.end();

	}

	public void handleInput() {

		if (GameKeys.isPressed(GameKeys.UP)) {
			if (currentItem > 0) {
				currentItem--;
			}
		}
		if (GameKeys.isPressed(GameKeys.DOWN)) {
			if (currentItem < menuItems.length - 1) {
				currentItem++;
			}
		}
		if (GameKeys.isPressed(GameKeys.ENTER)) {
			select();
		}

	}

	private void select() {
		// play
		if (currentItem == 0) {
			gsm.setState(State.PLAY);
		}
		// high scores
		else if (currentItem == 1) {
			gsm.setState(State.HIGHSCORE);
		} else if (currentItem == 2) {
			Gdx.app.exit();
		}
	}

	public void dispose() {
		sb.dispose();
		sr.dispose();
		titleFont.dispose();
		font.dispose();
	}

}
