package tennis.screens;

import tennis.SpaceTennis3D;
import tennis.managers.Assets;
import tennis.managers.Tools;
import tennis.references.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SettingsScreen implements Screen {
	private Assets assets;

	private Stage stage;

	private Skin skin;
	private Table table;
	private Label heading;
	
	private BitmapFont titleFont;
	
	private CheckBox vSyncCheckBox, fullscreenCheckBox, musicCheckBox, soundCheckBox;
	private SelectBox<String> resolution, fov, ambient;
	
	private TextButton btnSave, btnExit;


	@Override
	public void show() {
		assets = new Assets();
		assets.loadScreen(Assets.SETTINGS_SCREEN);
		
		stage = new Stage();
		stage.setDebugAll(true);
		Gdx.input.setInputProcessor(stage);

		skin = Assets.skin;
		
		table = new Table(skin);
		table.setFillParent(true);
		
		// Creating heading
		titleFont = new BitmapFont();
		titleFont.setScale(2);
		heading = new Label("Settings", skin);
		heading.setStyle(new LabelStyle(titleFont, Tools.randomColor()));;
		
		vSyncCheckBox = new CheckBox("vSync", skin);
		vSyncCheckBox.setChecked(Tools.vSync());
		vSyncCheckBox.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// save vSync
				Gdx.app.getPreferences(SpaceTennis3D.TITLE).putBoolean("vsync", vSyncCheckBox.isChecked());
				// set vSync
				Gdx.graphics.setVSync(Tools.vSync());
				Gdx.app.log(SpaceTennis3D.TITLE, "vSync " + (Tools.vSync() ? "enabled" : "disabled"));
			}
		});
		
		fullscreenCheckBox = new CheckBox("Full Screen", skin);
		fullscreenCheckBox.setChecked(Tools.fullscreen());
		fullscreenCheckBox.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// save Fullscreen
				Gdx.app.getPreferences(SpaceTennis3D.TITLE).putBoolean("fullscreen", fullscreenCheckBox.isChecked());
				// Set Fullscreen
				Gdx.graphics.setDisplayMode(SpaceTennis3D.WIDTH, SpaceTennis3D.HEIGHT, fullscreenCheckBox.isChecked());
				Gdx.app.log(SpaceTennis3D.TITLE, "fullscreen " + (Tools.fullscreen() ? "enabled" : "disabled"));
			}
		});
		
		musicCheckBox = new CheckBox("Play Music", skin);
		musicCheckBox.setChecked(Tools.music());
		musicCheckBox.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// save Music
				Gdx.app.getPreferences(SpaceTennis3D.TITLE).putBoolean("music", musicCheckBox.isChecked());
				// set Music
				Gdx.graphics.setVSync(Tools.music());
				Gdx.app.log(SpaceTennis3D.TITLE, "Music " + (Tools.music() ? "enabled" : "disabled"));
			}
		});
		
		soundCheckBox = new CheckBox("Play Sounds FX", skin);
		soundCheckBox.setChecked(Tools.sound());
		soundCheckBox.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// save Music
				Gdx.app.getPreferences(SpaceTennis3D.TITLE).putBoolean("sound", musicCheckBox.isChecked());
				// set Music
				Gdx.graphics.setVSync(Tools.sound());
				Gdx.app.log(SpaceTennis3D.TITLE, "Sound " + (Tools.sound() ? "enabled" : "disabled"));
			}
		});
		
		// resolution
		resolution = new SelectBox<String>(skin);
		String[] resolutions = new String[6];
		resolutions[0] = "800 x 600";
		resolutions[1] = "1024 x 768";
		resolutions[2] = "1280 x 800";
		resolutions[3] = "1280 x 1024";
		resolutions[4] = "1366 x 768";
		resolutions[5] = "1920 x 1080";
		resolution.setItems(resolutions);

		// FOV
		fov = new SelectBox<String>(skin);
		String[] fovs = new String[6];
		fovs[0] = "45";
		fovs[1] = "60";
		fovs[2] = "67";
		fovs[3] = "75";
		fovs[4] = "90";
		fovs[5] = "100";
		fov.setItems(fovs);
		
		// AMBIENT
		ambient = new SelectBox<String>(skin);
		String[] ambients = new String[5];
		ambients[0] = "Clean";
		ambients[1] = "Office";
		ambients[2] = "Park";
		ambients[3] = "Space 1";
		ambients[4] = "Space 2";
		ambient.setItems(ambients);

		btnSave = new TextButton("Save", skin);
		btnSave.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				
				// CHANGE WIDTH AND HEIGHT
				String[] parts = resolution.getSelected().split(" x ");
				int width = Integer.parseInt(parts[0]);
				int height = Integer.parseInt(parts[1]);
				Gdx.graphics.setDisplayMode(width, height, Tools.fullscreen());
				
				// Save FOV
				Gdx.app.getPreferences(SpaceTennis3D.TITLE).putInteger("FOV", new Integer(fov.getSelected()));
				Gdx.app.log(SpaceTennis3D.TITLE, "FOV changed to " + fov.getSelected());
				
				// Save ambient
				Models.setAmbient(ambient.getSelected());
				
				SpaceTennis3D.goTo(new SettingsScreen());
			}
		});
		
		btnExit = new TextButton("Return", skin);
		btnExit.pad(10);
		btnExit.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				SpaceTennis3D.goTo(new MainMenuScreen());
			}
		});
		
		table.add(heading).spaceBottom(0.1f * SpaceTennis3D.HEIGHT).row();
		table.add(fullscreenCheckBox).spaceBottom(0.05f * SpaceTennis3D.HEIGHT).row();
		table.add(vSyncCheckBox).spaceBottom(0.05f * SpaceTennis3D.HEIGHT).row();
		table.add(musicCheckBox).spaceBottom(0.05f * SpaceTennis3D.HEIGHT).row();
		table.add(soundCheckBox).spaceBottom(0.05f * SpaceTennis3D.HEIGHT).row();
		table.add(new Label("Resolution:", skin));
		table.add(resolution).spaceBottom(0.05f * SpaceTennis3D.HEIGHT).row();
		table.add(new Label("FOV:", skin));
		table.add(fov).spaceBottom(0.05f * SpaceTennis3D.HEIGHT).row();
		table.add(new Label("Game background:", skin));
		table.add(ambient).spaceBottom(0.05f * SpaceTennis3D.HEIGHT).row();
		table.add(btnSave);
		stage.addActor(table);
		
	}

	@Override
	public void render(float delta) {
		handleInput();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(delta);
		stage.draw();
	}
	
	public void handleInput() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
			SpaceTennis3D.goTo(new MainMenuScreen());
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
		//weird but ok
		dispose();
	}

	@Override
	public void dispose() {
		stage.dispose();
		assets.dispose();
	}

}
