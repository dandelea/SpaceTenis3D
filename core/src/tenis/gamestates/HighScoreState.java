package tenis.gamestates;

import cbd.asteroides.Asteroides;
import cbd.asteroides.database.MySQLController;
import cbd.asteroides.database.SQLiteController;
import cbd.asteroides.managers.GameKeys;
import cbd.asteroides.managers.GameStateManager;
import cbd.asteroides.managers.State;
import cbd.asteroides.objects.User;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class HighScoreState extends GameState {
	
	private SpriteBatch sb;

	private BitmapFont font;

	private User[] users;
	
	private int currentItem;

	public HighScoreState(GameStateManager gsm) {
		super(gsm);
	}

	public void init() {

		sb = new SpriteBatch();

		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
				Gdx.files.internal("fonts/space age.ttf"));
		font = gen.generateFont(20);

		MySQLController c = new MySQLController();
		c.connect();
		c.sync();
		users = c.getHighScores(10);
		c.close();

	}

	public void update(float dt) {
		handleInput();
	}

	public void draw() {

		sb.setProjectionMatrix(Asteroides.cam.combined);

		sb.begin();

		String s;
		float w;

		s = "High Scores";
		w = font.getBounds(s).width;
		font.draw(sb, s, (Gdx.graphics.getWidth() - w) / 2, (Gdx.graphics.getHeight() - Gdx.graphics.getHeight()/6));
		for (int i = 0; i < users.length; i++) {
			if (users[i]==null)
				break;
			s = String.format("%2d. %7s %s", i + 1,
					new Long(users[i].getScore()), users[i].getName()
							.toString());
			w = font.getBounds(s).width;
			if (currentItem == i)
				font.setColor(Color.RED);
			else
				font.setColor(Color.WHITE);
			font.draw(sb, s, (Asteroides.WIDTH - w) / 2, 270 - 20 * i);
		}
		
		if (users[0]==null){
			//No highscores
			s = "No highscores yet!!!";
			w = font.getBounds(s).width;
			font.draw(sb, s, (Asteroides.WIDTH - w) / 2, 270);
		}
		
		sb.end();

	}

	public void handleInput() {
		if (GameKeys.isPressed(GameKeys.ESCAPE)) {
			gsm.setState(State.MENU);
		}
		
		if (GameKeys.isPressed(GameKeys.UP)) {
			if (currentItem > 0) {
				currentItem--;
			}
		}
		if (GameKeys.isPressed(GameKeys.DOWN)) {
			if (currentItem < users.length - 1) {
				currentItem++;
			}
		}
		if (GameKeys.isPressed(GameKeys.ENTER) && users[0]!=null) {
			select();
		}
	}
	
	private void select() {
		gsm.setState(State.PROFILE);
	}

	public void dispose() {
		sb.dispose();
		font.dispose();
	}

}
