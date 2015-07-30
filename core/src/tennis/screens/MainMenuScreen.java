package tennis.screens;

import tennis.SpaceTennis3D;
import tennis.managers.Assets;
import tennis.screens.demos.PathRotationTest;
import tennis.screens.rules.RulesScreen;
import tennis.screens.scenes3d.GameScreen3;
import tennis.tween.ActorAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
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

public class MainMenuScreen implements Screen {
	private Stage stage;

	private Skin skin;

	private Table table;
	private BitmapFont titleFont;
	private Label heading;

	private Assets assets;

	private TweenManager tweenManager;

	private TextButton btnStart, btnDemo, btnOptions, btnRules, btnExit;

	@SuppressWarnings("deprecation")
	@Override
	public void show() {

		assets = new Assets();
		assets.loadScreen(Assets.MAIN_MENU_SCREEN);

		stage = new Stage();
		stage.setDebugAll(true);
		Gdx.input.setInputProcessor(stage);

		skin = Assets.skin;

		table = new Table(skin);
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		btnStart = new TextButton("Start", skin);
		btnStart.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SpaceTennis3D.goTo(new GameScreen3());
			}
		});
		btnStart.pad(20);
		btnDemo = new TextButton("Demo", skin);
		btnDemo.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SpaceTennis3D.goTo(new PathRotationTest());
			}
		});
		btnDemo.pad(20);
		btnOptions = new TextButton("Options", skin);
		btnOptions.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SpaceTennis3D.goTo(new SettingsScreen());
			}
		});
		btnOptions.pad(20);
		btnRules = new TextButton("Rules", skin);
		btnRules.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SpaceTennis3D.goTo(new RulesScreen());
			}
		});
		btnRules.pad(20);
		btnExit = new TextButton("Exit", skin);
		btnExit.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				exitFadeOut();
			}

		});
		btnExit.pad(15);

		// Creating heading
		titleFont = Assets.titleGenerator.generateFont(50);

		heading = new Label(SpaceTennis3D.TITLE, skin);
		heading.setStyle(new LabelStyle(titleFont, Color.WHITE));

		table.add(heading);
		table.getCell(heading).spaceBottom(100).row();
		table.add(btnStart).spaceBottom(0.05f * SpaceTennis3D.HEIGHT).row();
		//table.add(btnDemo).spaceBottom(0.05f * SpaceTennis3D.HEIGHT).row();
		table.add(btnOptions).spaceBottom(0.05f * SpaceTennis3D.HEIGHT).row();
		table.add(btnRules).spaceBottom(0.05f * SpaceTennis3D.HEIGHT).row();
		table.add(btnExit).spaceBottom(0.05f * SpaceTennis3D.HEIGHT).row();
		stage.addActor(table);

		// Creating animations
		tweenManager = new TweenManager();
		Tween.registerAccessor(Actor.class, new ActorAccessor());

		// Animation Fade-In.

		// Heading color animation
		Timeline.createSequence()
				.beginSequence()
				.push(Tween.to(heading, ActorAccessor.RGB, .4f).target(0, 0, 1))
				.push(Tween.to(heading, ActorAccessor.RGB, .4f).target(0, 1, 0))
				.push(Tween.to(heading, ActorAccessor.RGB, .4f).target(1, 0, 0))
				.push(Tween.to(heading, ActorAccessor.RGB, .4f).target(1, 1, 0))
				.push(Tween.to(heading, ActorAccessor.RGB, .4f).target(0, 1, 1))
				.push(Tween.to(heading, ActorAccessor.RGB, .4f).target(1, 0, 1))
				.push(Tween.to(heading, ActorAccessor.RGB, .4f).target(1, 1, 1))
				.end().repeat(Tween.INFINITY, 0).start(tweenManager);

		// Heading and buttons fade-in
		Timeline.createSequence()
				.beginSequence()
				.push(Tween.set(btnStart, ActorAccessor.ALPHA).target(0))
				.push(Tween.set(btnDemo, ActorAccessor.ALPHA).target(0))
				.push(Tween.set(btnOptions, ActorAccessor.ALPHA).target(0))
				.push(Tween.set(btnRules, ActorAccessor.ALPHA).target(0))
				.push(Tween.set(btnExit, ActorAccessor.ALPHA).target(0))
				.push(Tween.from(heading, ActorAccessor.ALPHA, .25f).target(0))
				.push(Tween.to(btnStart, ActorAccessor.ALPHA, .25f).target(1))
				.push(Tween.to(btnDemo, ActorAccessor.ALPHA, .25f).target(1))
				.push(Tween.to(btnOptions, ActorAccessor.ALPHA, .25f).target(1))
				.push(Tween.to(btnRules, ActorAccessor.ALPHA, .25f).target(1))
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

	public void exitFadeOut() {
		Timeline.createParallel()
				.beginParallel()
				.push(Tween.to(table, ActorAccessor.ALPHA, .75f).target(0))
				.push(Tween.to(table, ActorAccessor.Y, .75f)
						.target(table.getY() - 50)
						.setCallback(new TweenCallback() {

							@Override
							public void onEvent(int arg0, BaseTween<?> arg1) {
								Gdx.app.exit();

							}
						})).end().start(tweenManager);
	}

	public void handleInput() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			exitFadeOut();
		}
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		table.invalidateHierarchy();
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
