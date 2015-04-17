package tenis.screens;

import tenis.Tenis3D;
import tenis.screens.demos.BehindTheScenesTest;
import tenis.screens.demos.CameraDemoModelLoads6x6x6;
import tenis.screens.demos.CameraDemoScene;
import tenis.screens.demos.RayPicking;
import tenis.screens.demos.ShaderDemo;
import tenis.tween.ActorAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenuScreen implements Screen {
	// Resources
	private String backgroundDir = "tennis-court.jpg";

	private Stage stage;

	private Skin skin;
	
	private Table table;
	private BitmapFont font;
	private Label heading;

	private TextureAtlas atlas;
	//private TextureAtlas btnPack;
	
	private TweenManager tweenManager;

	private TextButton btnStart, btnOptions, btnRules, btnExit;
	private TextField txtUsername;

	public void processStartGame() {
		System.out.println("Hi " + txtUsername.getText());
		// game.setScreen(new GameScreen());
	}

	public void processMore() {
		System.out.println("Hi " + txtUsername.getText());
	}

	@Override
	public void show() {
		stage = new Stage();
		//stage.setDebugAll(true);
		Gdx.input.setInputProcessor(stage);

		atlas = new TextureAtlas("ui/uiskin.atlas");
		//btnPack = new TextureAtlas("ui/button.pack");
		skin = new Skin(Gdx.files.internal("ui/uiskin.json"), atlas);
		//skin.addRegions(btnPack);

		table = new Table(skin);
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		btnStart = new TextButton("Start", skin);
		btnStart.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
			}
		});
		btnStart.pad(20);
		btnOptions = new TextButton("Options", skin);
		btnOptions.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				((Game) Gdx.app.getApplicationListener()).setScreen(new SettingsScreen());
			}
		});
		btnOptions.pad(20);
		btnRules = new TextButton("Rules", skin);
		btnRules.pad(20);
		btnExit = new TextButton("Exit", skin);
		btnExit.addListener(new ClickListener(){
			
			public void clicked(InputEvent event, float x, float y){
				Timeline.createParallel().beginParallel()
				.push(Tween.to(table, ActorAccessor.ALPHA, .75f).target(0))
				.push(Tween.to(table, ActorAccessor.Y, .75f).target(table.getY() - 50)
						.setCallback(new TweenCallback() {

							@Override
							public void onEvent(int arg0, BaseTween<?> arg1) {
								Gdx.app.exit();
								
							}
						}))
				.end().start(tweenManager);
			}
			
		});
		btnExit.pad(15);
		
		
		// Creating heading
		heading = new Label(Tenis3D.TITLE, skin);
		
		table.add(heading);
		table.getCell(heading).spaceBottom(100);
		//table.getCell(actor);
		table.row();
		table.add(btnStart);
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
		Timeline.createSequence().beginSequence()
				.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0, 0, 1))
				.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0, 1, 0))
				.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1, 0, 0))
				.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1, 1, 0))
				.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0, 1, 1))
				.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1, 0, 1))
				.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1, 1, 1))
				.end().repeat(Tween.INFINITY, 0).start(tweenManager);

		// Heading and buttons fade-in
		Timeline.createSequence().beginSequence()
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
		
		// table fade-in
		Tween.from(table, ActorAccessor.ALPHA, .75f).target(0).start(tweenManager);
		Tween.from(table, ActorAccessor.Y, .75f).target(Gdx.graphics.getHeight() / 8).start(tweenManager);

		tweenManager.update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(delta);
		stage.draw();
		
		tweenManager.update(delta);
	}

	@Override
	public void resize(int width, int height) {

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
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		stage.dispose();
		atlas.dispose();
		skin.dispose();
		font.dispose();
	}

}
