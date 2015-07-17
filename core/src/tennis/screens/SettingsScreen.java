package tennis.screens;

import tennis.SpaceTennis3D;
import tennis.managers.Utils;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SettingsScreen implements Screen {

	private Stage stage;

	private Skin skin;
	private Table table;
	private Label heading;
	
	private CheckBox vSyncCheckBox, fullscreenCheckBox, musicCheckBox, soundCheckBox;
	private SelectBox<String> resolution;
	private SelectBox<String> fov;
	
	private TextButton btnSave, btnExit;

	private TextureAtlas atlas;


	@Override
	public void show() {
		stage = new Stage();
		//stage.setDebugAll(true);
		Gdx.input.setInputProcessor(stage);

		atlas = new TextureAtlas("ui/uiskin.atlas");
		skin = new Skin(Gdx.files.internal("ui/uiskin.json"), atlas);

		table = new Table(skin);
		table.setFillParent(true);
		
		// Creating heading
		heading = new Label("Settings", skin);
		
		vSyncCheckBox = new CheckBox("vSync", skin);
		vSyncCheckBox.setChecked(Utils.vSync());
		vSyncCheckBox.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// save vSync
				Gdx.app.getPreferences(SpaceTennis3D.TITLE).putBoolean("vsync", vSyncCheckBox.isChecked());
				// set vSync
				Gdx.graphics.setVSync(Utils.vSync());
				Gdx.app.log(SpaceTennis3D.TITLE, "vSync " + (Utils.vSync() ? "enabled" : "disabled"));
			}
		});
		
		fullscreenCheckBox = new CheckBox("Full Screen", skin);
		fullscreenCheckBox.setChecked(Utils.fullscreen());
		fullscreenCheckBox.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// save Fullscreen
				Gdx.app.getPreferences(SpaceTennis3D.TITLE).putBoolean("fullscreen", fullscreenCheckBox.isChecked());
				// Set Fullscreen
				Gdx.graphics.setDisplayMode(SpaceTennis3D.WIDTH, SpaceTennis3D.HEIGHT, fullscreenCheckBox.isChecked());
				Gdx.app.log(SpaceTennis3D.TITLE, "fullscreen " + (Utils.fullscreen() ? "enabled" : "disabled"));
			}
		});
		
		musicCheckBox = new CheckBox("Play Music", skin);
		musicCheckBox.setChecked(Utils.music());
		musicCheckBox.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// save Music
				Gdx.app.getPreferences(SpaceTennis3D.TITLE).putBoolean("music", musicCheckBox.isChecked());
				// set Music
				Gdx.graphics.setVSync(Utils.music());
				Gdx.app.log(SpaceTennis3D.TITLE, "Music " + (Utils.music() ? "enabled" : "disabled"));
			}
		});
		
		soundCheckBox = new CheckBox("Play Sounds FX", skin);
		soundCheckBox.setChecked(Utils.sound());
		soundCheckBox.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// save Music
				Gdx.app.getPreferences(SpaceTennis3D.TITLE).putBoolean("sound", musicCheckBox.isChecked());
				// set Music
				Gdx.graphics.setVSync(Utils.sound());
				Gdx.app.log(SpaceTennis3D.TITLE, "Sound " + (Utils.sound() ? "enabled" : "disabled"));
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

		btnSave = new TextButton("Save", skin);
		btnSave.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				
				// CHANGE WIDTH AND HEIGHT
				String[] parts = ((String) resolution.getSelected()).split(" x ");
				int width = Integer.parseInt(parts[0]);
				int height = Integer.parseInt(parts[1]);
				Gdx.graphics.setDisplayMode(width, height, Utils.fullscreen());
				
				// Save FOV
				Gdx.app.getPreferences(SpaceTennis3D.TITLE).putInteger("FOV", new Integer(fov.getSelected()));
				Gdx.app.log(SpaceTennis3D.TITLE, "FOV changed to " + fov.getSelected());
				
				SpaceTennis3D.goTo(new SettingsScreen());
			}
		});
		
		btnExit = new TextButton("Return", skin);
		btnExit.pad(10);
		btnExit.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				SpaceTennis3D.goTo(new MainMenuScreen());
			}
		});
		
		table.add(heading);
		table.getCell(heading).spaceBottom(100);
		//table.getCell(actor);
		table.row();
		table.add(fullscreenCheckBox).row();
		table.add(vSyncCheckBox).row();
		table.add(musicCheckBox).row();
		table.add(soundCheckBox).row();
		table.add(resolution).row();
		table.add(fov).row();
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
		atlas.dispose();
		skin.dispose();
	}

}
