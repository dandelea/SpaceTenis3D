package tennis.screens;

import tennis.SpaceTennis3D;
import tennis.managers.Assets;
import tennis.managers.Models;
import tennis.managers.Soundbox;
import tennis.managers.Tools;
import tennis.objects.Difficulty;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SettingsScreen implements Screen {
	private Assets assets;
	private Skin skin;
	private BitmapFont titleFont;

	private Stage stage;
	private Table table;

	private Label heading;
	private CheckBox vSyncCheckBox, fullscreenCheckBox, musicCheckBox, soundCheckBox;
	private SelectBox<String> resolution, fov, ambient, difficulty;
	private TextButton btnSave, btnExit;

	@SuppressWarnings("deprecation")
	@Override
	public void show() {
		assets = new Assets();
		assets.loadScreen(Assets.SETTINGS_SCREEN);
		
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		skin = Assets.skin;
		titleFont = Assets.titleGenerator.generateFont(50);
		
		table = new Table(skin);
		table.setFillParent(true);
		
		heading = new Label("Opciones", skin);
		heading.setStyle(new LabelStyle(titleFont, Color.WHITE));
		
		// VSYNC
		vSyncCheckBox = new CheckBox("  Sincronización vertical", skin);
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
		
		// FULL SCREEN
		fullscreenCheckBox = new CheckBox("  Pantalla completa", skin);
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
		
		// PLAY MUSIC
		musicCheckBox = new CheckBox("  Reproducir música", skin);
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
		
		// PLAY SOUND
		soundCheckBox = new CheckBox("  Reproducir sonidos", skin);
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
		
		// RESOLUTION
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
		fovs[0] = "67";
		fovs[1] = "45";
		fovs[2] = "60";
		fovs[3] = "75";
		fovs[4] = "90";
		fovs[5] = "100";
		fov.setItems(fovs);
		
		// AMBIENT
		ambient = new SelectBox<String>(skin);
		String[] ambients = new String[4];
		ambients[0] = "Space 1";
		ambients[1] = "Space 2";
		ambients[2] = "Space 3";
		ambients[3] = "Clean";
		ambient.setItems(ambients);
		
		// DIFFICULTY
		difficulty = new SelectBox<String>(skin);
		String[] difficulties = new String[3];
		difficulties[0] = Difficulty.EASY.toString();
		difficulties[1] = Difficulty.MEDIUM.toString();
		difficulties[2] = Difficulty.HARD.toString();
		difficulty.setItems(difficulties);

		// BUTTON SAVE
		btnSave = new TextButton("Guardar", skin);
		btnSave.pad(10, 20, 10, 20);
		btnSave.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				
				// CHANGE WIDTH AND HEIGHT
				String[] parts = resolution.getSelected().split(" x ");
				int width = Integer.parseInt(parts[0]);
				int height = Integer.parseInt(parts[1]);
				Gdx.graphics.setDisplayMode(width, height, Tools.fullscreen());
				
				// SAVE FOV
				Gdx.app.getPreferences(SpaceTennis3D.TITLE).putInteger("FOV", new Integer(fov.getSelected()));
				Gdx.app.log(SpaceTennis3D.TITLE, "FOV changed to " + fov.getSelected());
				
				// SAVE AMBIENT
				Models.setAmbient(ambient.getSelected());
				
				// SAVE DIFFICULTY
				SpaceTennis3D.difficulty = Enum.valueOf(Difficulty.class, difficulty.getSelected());
				
				if (soundCheckBox.isChecked()){
					Soundbox.play("button");
				}
				
				SpaceTennis3D.goTo(new SettingsScreen());
			}
		});
		
		btnExit = new TextButton("Volver", skin);
		btnExit.pad(10);
		btnExit.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				if (soundCheckBox.isChecked()){
					Soundbox.play("quit");
				}
				SpaceTennis3D.goTo(new MainMenuScreen());
			}
		});
		
		// TABLE
		table.add(heading).spaceBottom(0.07f * SpaceTennis3D.HEIGHT).colspan(2).row();
		table.add(fullscreenCheckBox).spaceBottom(0.03f * SpaceTennis3D.HEIGHT).colspan(2).row();
		table.add(vSyncCheckBox).spaceBottom(0.03f * SpaceTennis3D.HEIGHT).colspan(2).row();
		table.add(musicCheckBox).spaceBottom(0.03f * SpaceTennis3D.HEIGHT).colspan(2).row();
		table.add(soundCheckBox).spaceBottom(0.03f * SpaceTennis3D.HEIGHT).colspan(2).row();
		table.add(new Label("Resolución:", skin)).spaceBottom(0.03f * SpaceTennis3D.HEIGHT);
		table.add(resolution).spaceBottom(0.03f * SpaceTennis3D.HEIGHT).row();
		table.add(new Label("FOV:", skin)).spaceBottom(0.03f * SpaceTennis3D.HEIGHT);
		table.add(fov).spaceBottom(0.03f * SpaceTennis3D.HEIGHT).row();
		table.add(new Label("Fondo de juego:", skin)).spaceBottom(0.03f * SpaceTennis3D.HEIGHT);
		table.add(ambient).spaceBottom(0.03f * SpaceTennis3D.HEIGHT).row();
		table.add(new Label("Dificultad:", skin)).spaceBottom(0.03f * SpaceTennis3D.HEIGHT);
		table.add(difficulty).spaceBottom(0.03f * SpaceTennis3D.HEIGHT).row();
		table.add(btnSave).pad(10);
		table.add(btnExit).pad(10).row();
		table.center();
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
			if (soundCheckBox.isChecked()){
				Soundbox.play("quit");
			}
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
		skin.dispose();
		titleFont.dispose();
	}

}
