package tenis.gamestates;

import cbd.asteroides.Asteroides;
import cbd.asteroides.database.MySQLController;
import cbd.asteroides.managers.GameKeys;
import cbd.asteroides.managers.GameStateManager;
import cbd.asteroides.managers.State;
import cbd.asteroides.objects.User;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class ProfileState extends GameState{
	private int userId = Asteroides.profileViewing;
	private User user;
	
	private SpriteBatch sb;

	private BitmapFont titleFont;
	private BitmapFont font;
	
	public ProfileState(GameStateManager gsm){
		super(gsm);
	}

	@Override
	public void init() {
		user = MySQLController.getUserById(userId);
		
		sb = new SpriteBatch();

		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
				Gdx.files.internal("fonts/space age.ttf"));

		titleFont = gen.generateFont(40);
		titleFont.setColor(Color.WHITE);

		font = gen.generateFont(20);

	}

	@Override
	public void update(float dt) {
		handleInput();
	}

	@Override
	public void draw() {
		sb.setProjectionMatrix(Asteroides.cam.combined);

		sb.begin();

		String s;
		float w;

		s = user.getName();
		w = font.getBounds(s).width;
		titleFont.draw(sb, s, (Gdx.graphics.getWidth() - w) / 2, (Gdx.graphics.getHeight() - Gdx.graphics.getHeight()/6));

		s = String.format("%s %7s", "High score: ", user.getScore());
		w = font.getBounds(s).width;
		font.draw(sb, s, (Gdx.graphics.getWidth() - w) / 2, 270 - 20);
		
		s = String.format("%s %d", "Max level: ", user.getLevel());
		w = font.getBounds(s).width;
		font.draw(sb, s, (Gdx.graphics.getWidth() - w) / 2, 270 - 40);
		
		s = String.format("%s %7s", "Bullets shots: ", user.getBullets());
		w = font.getBounds(s).width;
		font.draw(sb, s, (Gdx.graphics.getWidth() - w) / 2, 270 - 60);
		
		s = String.format("%s %7s", "Asteroids destroyed: ", user.getAsteroids());
		w = font.getBounds(s).width;
		font.draw(sb, s, (Gdx.graphics.getWidth() - w) / 2, 270 - 80);
		
		sb.end();

	}

	@Override
	public void handleInput() {
		if (GameKeys.isPressed(GameKeys.ESCAPE) || GameKeys.isPressed(GameKeys.ESCAPE)) {
			gsm.setState(State.HIGHSCORE);
		}
	}

	@Override
	public void dispose() {
		sb.dispose();
		font.dispose();
		titleFont.dispose();
	}

}
