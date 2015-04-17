package tenis.gamestates;

import tenis.Tenis3D;
import tenis.managers.GameStateManager;
import tenis.managers.State;
import tenis.tween.ActorAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenuState extends GameState {
	private Stage stage;

	private Skin skin;

	private Table table;
	private Label heading;

	private TextureAtlas atlas;
	// private TextureAtlas btnPack;

	private TweenManager tweenManager;

	private TextButton btnStart, btnDemo, btnOptions, btnRules, btnExit;

	public MainMenuState(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		stage = new Stage();
		// stage.setDebugAll(true);
		Gdx.input.setInputProcessor(stage);

		atlas = new TextureAtlas("ui/uiskin.atlas");
		// btnPack = new TextureAtlas("ui/button.pack");
		skin = new Skin(Gdx.files.internal("ui/uiskin.json"), atlas);
		// skin.addRegions(btnPack);

		table = new Table(skin);
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		btnStart = new TextButton("Start", skin);
		btnStart.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				gsm.setState(State.GAME);
				;
			}
		});
		btnStart.pad(20);
		btnDemo = new TextButton("Start", skin);
		btnDemo.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				gsm.setState(State.GAME);
				;
			}
		});
		btnDemo.pad(20);
		btnOptions = new TextButton("Options", skin);
		btnOptions.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				gsm.setState(State.SETTINGS);
				;
			}
		});
		btnOptions.pad(20);
		btnRules = new TextButton("Rules", skin);
		btnRules.pad(20);
		btnExit = new TextButton("Exit", skin);
		btnExit.addListener(new ClickListener() {

			public void clicked(InputEvent event, float x, float y) {
				exitFadeOut();
			}

		});
		btnExit.pad(15);

		// Creating heading
		heading = new Label(Tenis3D.TITLE, skin);

		table.add(heading);
		table.getCell(heading).spaceBottom(100);
		// table.getCell(actor);
		table.row();
		table.add(btnStart);
		table.row();
		table.add(btnDemo);
		table.row();
		table.add(btnOptions);
		table.row();
		table.add(btnRules);
		table.row();
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
				.push(Tween.set(btnStart, ActorAccessor.ALPHA).target(0))
				.push(Tween.set(btnDemo, ActorAccessor.ALPHA).target(0))
				.push(Tween.set(btnOptions, ActorAccessor.ALPHA).target(0))
				.push(Tween.set(btnRules, ActorAccessor.ALPHA).target(0))
				.push(Tween.set(btnExit, ActorAccessor.ALPHA).target(0))
				.push(Tween.from(heading, ActorAccessor.ALPHA, .25f).target(0))
				.push(Tween.to(btnStart, ActorAccessor.ALPHA, .25f).target(1))
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

	@Override
	public void update(float delta) {
		handleInput();

		stage.act(delta);
		stage.draw();

		tweenManager.update(delta);
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleInput() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			exitFadeOut();
		}
	}

	@Override
	public void dispose() {
		stage.dispose();
		atlas.dispose();
		skin.dispose();
	}

}
