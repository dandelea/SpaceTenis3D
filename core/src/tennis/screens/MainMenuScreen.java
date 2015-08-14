package tennis.screens;

import tennis.SpaceTennis3D;
import tennis.managers.Assets;
import tennis.managers.Soundbox;
import tennis.managers.bluetooth.BluetoothServer;
import tennis.screens.rules.RulesScreen;
import tennis.screens.scenes3d.GameScreen;
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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	private Assets assets;
	private Skin skin;
	private BitmapFont font;
	private BitmapFont titleFont;

	private Stage stage;
	private Table table;
	
	private Label heading;
	private TextButton btnStart, btnOptions, btnRules, btnExit;

	private TweenManager tweenManager;

	private SpriteBatch batch;
	private String device = "Dispositivo conectado";

	@SuppressWarnings("deprecation")
	@Override
	public void show() {
		assets = new Assets();
		assets.loadScreen(Assets.MAIN_MENU_SCREEN);

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		batch = new SpriteBatch();

		skin = Assets.skin;
		font = Assets.fontGenerator.generateFont(14);
		titleFont = Assets.titleGenerator.generateFont(50);

		table = new Table(skin);
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		btnStart = new TextButton("¡Dispositivo no conectado!", skin);
		btnStart.pad(20);
		
		btnOptions = new TextButton("Opciones", skin);
		btnOptions.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Soundbox.play("button");
				SpaceTennis3D.goTo(new SettingsScreen());
			}
		});
		btnOptions.pad(20);
		btnRules = new TextButton("Cómo jugar", skin);
		btnRules.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Soundbox.play("button");
				SpaceTennis3D.goTo(new RulesScreen());
			}
		});
		btnRules.pad(20);
		btnExit = new TextButton("Salir", skin);
		btnExit.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				exitFadeOut();
			}

		});
		btnExit.pad(15);

		heading = new Label(SpaceTennis3D.TITLE, skin);
		heading.setStyle(new LabelStyle(titleFont, Color.WHITE));

		table.add(heading);
		table.getCell(heading).spaceBottom(100).row();
		table.add(btnStart).spaceBottom(0.05f * SpaceTennis3D.HEIGHT).row();
		table.add(btnOptions).spaceBottom(0.05f * SpaceTennis3D.HEIGHT).row();
		table.add(btnRules).spaceBottom(0.05f * SpaceTennis3D.HEIGHT).row();
		table.add(btnExit).spaceBottom(0.05f * SpaceTennis3D.HEIGHT).row();
		stage.addActor(table);

		// TWEEN ANIMATIONS
		tweenManager = new TweenManager();
		Tween.registerAccessor(Actor.class, new ActorAccessor());

		// HEADING COLOR ANIMATION
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

		// HEADING AND BUTTONS FADE IN
		Timeline.createSequence()
				.beginSequence()
				.push(Tween.set(btnStart, ActorAccessor.ALPHA).target(0))
				.push(Tween.set(btnOptions, ActorAccessor.ALPHA).target(0))
				.push(Tween.set(btnRules, ActorAccessor.ALPHA).target(0))
				.push(Tween.set(btnExit, ActorAccessor.ALPHA).target(0))
				.push(Tween.from(heading, ActorAccessor.ALPHA, .25f).target(0))
				.push(Tween.to(btnStart, ActorAccessor.ALPHA, .25f).target(1))
				.push(Tween.to(btnOptions, ActorAccessor.ALPHA, .25f).target(1))
				.push(Tween.to(btnRules, ActorAccessor.ALPHA, .25f).target(1))
				.push(Tween.to(btnExit, ActorAccessor.ALPHA, .25f).target(1))
				.end().start(tweenManager);

		// TABLE FADE IN
		Tween.from(table, ActorAccessor.ALPHA, .75f).target(0)
				.start(tweenManager);
		Tween.from(table, ActorAccessor.Y, .75f)
				.target(Gdx.graphics.getHeight() / 8).start(tweenManager);

		// TWEEN UPDATE
		tweenManager.update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void render(float delta) {
		handleInput();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// UPDATE START BUTTON
		if (BluetoothServer.connected){
			btnStart.setText("Jugar");
			btnStart.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					Soundbox.play("button");
					SpaceTennis3D.goTo(new GameScreen());
				}
			});
		} else {
			btnStart.setText("¡Dispositivo no conectado!");
			btnStart.clearListeners();
		}
				
		stage.act(delta);
		stage.draw();
		
		// DRAW TEXT 'DEVICE CONNECTED'
		batch.begin();
		if (BluetoothServer.connected){
			font.draw(batch, device, 0, font.getBounds(device).height + font.getBounds(device).height / 2);
		}
		batch.end();

		tweenManager.update(delta);
	}

	/**
	 * Smoothly exits
	 */
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
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		stage.dispose();
		assets.dispose();
		font.dispose();
		titleFont.dispose();
		skin.dispose();
		batch.dispose();
	}

}
