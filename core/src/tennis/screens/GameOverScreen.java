package tennis.screens;

import tennis.SpaceTennis3D;
import tennis.managers.Assets;
import tennis.objects.Scoreboard;
import tennis.screens.scenes3d.GameScreen3;
import tennis.tween.ActorAccessor;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GameOverScreen implements Screen {
	private Scoreboard scoreBoard;
	
	private Stage stage;

	private Skin skin;

	private Table table;
	private BitmapFont titleFont;
	private Label heading;

	private Assets assets;

	private TweenManager tweenManager;

	private TextButton btnExit, btnPlay;

	@SuppressWarnings("deprecation")
	@Override
	public void show() {
		assets = new Assets();
		assets.loadScreen(Assets.GAME_OVER_SCREEN);
		
		scoreBoard = SpaceTennis3D.lastScoreboard;

		stage = new Stage();
		stage.setDebugAll(true);
		Gdx.input.setInputProcessor(stage);

		skin = Assets.skin;

		table = new Table(skin);
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		String btnPlayText = "";
		String headingText = "";
		if (scoreBoard.isFinished()) {
			headingText = scoreBoard.getWinner()==1 ? "Congratulations!" : "Ooohh! You lost";
			btnPlayText = scoreBoard.getWinner()==1 ? "Play again" : "Try again";
		} else {
			SpaceTennis3D.goTo(new MainMenuScreen());
		}
		
		btnPlay = new TextButton(btnPlayText, skin);
		btnPlay.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SpaceTennis3D.goTo(new GameScreen3());
			}
		});
		btnPlay.pad(20);
		
		btnExit = new TextButton("Exit", skin);
		btnExit.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				SpaceTennis3D.goTo(new MainMenuScreen());
			}

		});
		btnExit.pad(15);

		// Creating heading
		titleFont = Assets.titleGenerator.generateFont(50);

		heading = new Label(headingText, skin);
		heading.setStyle(new LabelStyle(titleFont, Color.WHITE));

		table.add(heading).spaceBottom(100).row();
		table.add(btnPlay).spaceBottom(0.05f * SpaceTennis3D.HEIGHT).row();
		table.add(btnExit);
		stage.addActor(table);

		// Creating animations
		tweenManager = new TweenManager();
		Tween.registerAccessor(Actor.class, new ActorAccessor());

		// Animation Fade-In.

		// Heading color animation
		Timeline.createSequence()
				.beginSequence()
				.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0, 0, 1))
				.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0, 1, 0))
				.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1, 0, 0))
				.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1, 1, 0))
				.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0, 1, 1))
				.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1, 0, 1))
				.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1, 1, 1))
				.end().repeat(Tween.INFINITY, 0).start(tweenManager);

		// Heading and buttons fade-in
		Timeline.createSequence()
				.beginSequence()
				.push(Tween.set(btnPlay, ActorAccessor.ALPHA).target(0))
				.push(Tween.set(btnExit, ActorAccessor.ALPHA).target(0))
				.push(Tween.from(heading, ActorAccessor.ALPHA, .25f).target(0))
				.push(Tween.to(btnPlay, ActorAccessor.ALPHA, .25f).target(1))
				.push(Tween.to(btnExit, ActorAccessor.ALPHA, .25f).target(1))
				.end().start(tweenManager);

		// table fade-in
		Tween.from(table, ActorAccessor.ALPHA, .75f).target(0)
				.start(tweenManager);
		Tween.from(table, ActorAccessor.Y, .75f)
				.target(Gdx.graphics.getHeight() / 8).start(tweenManager);

		tweenManager.update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void render(float delta) {
		handleInput();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();

		tweenManager.update(delta);
	}
	
	public void handleInput() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			SpaceTennis3D.goTo(new MainMenuScreen());
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		stage.dispose();
		assets.dispose();
	}

}
