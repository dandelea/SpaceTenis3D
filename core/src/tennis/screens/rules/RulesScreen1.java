package tennis.screens.rules;

import tennis.SpaceTennis3D;
import tennis.managers.Assets;
import tennis.managers.Soundbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class RulesScreen1 implements Screen {
	private Assets assets;
	private Skin skin;
	private BitmapFont titleFont;

	private Stage stage;
	private Table table;

	private Label heading, rules1;
	private Image image1;
	private TextButton btnExit;

	private static final String RULES1 = "Para jugar a este juego\nnecesitar�s un dispositivo\nAndroid con Bluetooth.";
	private static final String RULES2 = "\nEmpareja tu m�vil con tu\nordenador y sigue las\ninstrucciones de la aplicaci�n\nde m�vil para conectarte.";

	@SuppressWarnings("deprecation")
	@Override
	public void show() {
		assets = new Assets();
		assets.loadScreen(Assets.RULES_SCREEN);

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		skin = Assets.skin;
		titleFont = Assets.titleGenerator.generateFont(50);

		table = new Table(skin);
		table.setFillParent(true);

		heading = new Label("Bluetooth", skin);
		heading.setStyle(new LabelStyle(titleFont, Color.WHITE));

		rules1 = new Label(RULES1 + "\n" + RULES2, skin);

		image1 = new Image(assets.get(Assets.URL_RULES_IMAGE5, Texture.class));

		btnExit = new TextButton("Volver", skin);
		btnExit.pad(20);
		btnExit.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				SpaceTennis3D.goTo(new RulesScreen());
			}

		});

		table.add(heading).spaceBottom(0.03f * SpaceTennis3D.HEIGHT).colspan(2)
				.row();
		table.add(image1).pad(20);
		table.add(rules1).row();
		table.add(btnExit).spaceBottom(0.1f * SpaceTennis3D.HEIGHT).colspan(2)
				.row();
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
			SpaceTennis3D.goTo(new RulesScreen());
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
				|| Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)
				|| Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
			SpaceTennis3D.goTo(new RulesScreen2());
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
		assets.dispose();
		stage.dispose();
		skin.dispose();
		titleFont.dispose();
	}
}
