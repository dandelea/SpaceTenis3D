package tennis.screens.rules;

import tennis.SpaceTennis3D;
import tennis.managers.Assets;
import tennis.managers.Soundbox;
import tennis.screens.MainMenuScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class RulesScreen implements Screen {
	private Assets assets;

	private Stage stage;
	private Table table;

	private BitmapFont titleFont;

	private Skin skin;

	private Label heading;
	private TextButton  btnRules1, btnRules2, btnRules3;

	@SuppressWarnings("deprecation")
	@Override
	public void show() {
		assets = new Assets();
		assets.loadScreen(Assets.RULES_SCREEN);

		stage = new Stage();
		stage.setDebugAll(true);
		Gdx.input.setInputProcessor(stage);

		skin = Assets.skin;

		table = new Table(skin);
		table.setFillParent(true);

		// Creating heading
		titleFont = Assets.titleGenerator.generateFont(50);
		heading = new Label("Cómo jugar", skin);
		heading.setStyle(new LabelStyle(titleFont, Color.WHITE));
		
		btnRules1 = new TextButton("Bluetooth", skin);
		btnRules1.pad(20);
		btnRules1.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				Soundbox.play("button");
				SpaceTennis3D.goTo(new RulesScreen1());
			}

		});
		
		btnRules2 = new TextButton("Control", skin);
		btnRules2.pad(20);
		btnRules2.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				Soundbox.play("button");
				SpaceTennis3D.goTo(new RulesScreen2());
			}

		});
		
		btnRules3 = new TextButton("Reglas", skin);
		btnRules3.pad(20);
		btnRules3.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				Soundbox.play("button");
				SpaceTennis3D.goTo(new RulesScreen3());
			}

		});
		
		table.add(heading).spaceBottom(0.07f * SpaceTennis3D.HEIGHT).row();
		table.add(btnRules1).spaceBottom(0.05f * SpaceTennis3D.HEIGHT).row();
		table.add(btnRules2).spaceBottom(0.05f * SpaceTennis3D.HEIGHT).row();
		table.add(btnRules3).spaceBottom(0.05f * SpaceTennis3D.HEIGHT).row();
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
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			Soundbox.play("quit");
			SpaceTennis3D.goTo(new MainMenuScreen());
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) || 
				Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			SpaceTennis3D.goTo(new RulesScreen1());
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
			SpaceTennis3D.goTo(new RulesScreen2());
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
			SpaceTennis3D.goTo(new RulesScreen3());
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
