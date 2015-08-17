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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class RulesScreen2 implements Screen {
	private Assets assets;
	private Skin skin;
	private BitmapFont titleFont;

	private Stage stage;
	private Table table;

	private Label heading, rules1, rules2, rules3;
	private Image image1, image2, image3, image4;
	private TextButton btnExit;

	private static final String RULES1 = "Este juego te enfrentará a la máquina\nen una partida de tenis. Para golpear\nla pelota usa tu teléfono como raqueta.";
	private static final String RULES2 = "Golpea la bola cuando\naparezca en color rojo y\nesté en tu lado del campo";
	private static final String RULES3 = "Para pausar el juego bloquea la pantalla\nde tu móvil o presiona la tecla ENTER,\nESPACIO o ESCAPE";

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

		heading = new Label("Cómo jugar", skin);
		heading.setStyle(new LabelStyle(titleFont, Color.WHITE));

		rules1 = new Label(RULES1, skin);
		rules2 = new Label(RULES2, skin);
		rules3 = new Label(RULES3, skin);

		image1 = new Image(assets.get(Assets.URL_RULES_IMAGE1, Texture.class));
		image2 = new Image(assets.get(Assets.URL_RULES_IMAGE2, Texture.class));
		image3 = new Image(assets.get(Assets.URL_RULES_IMAGE3, Texture.class));
		image4 = new Image(assets.get(Assets.URL_RULES_IMAGE4, Texture.class));
		
		btnExit = new TextButton("Volver", skin);
		btnExit.pad(20);
		btnExit.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				SpaceTennis3D.goTo(new RulesScreen());
			}

		});
		table.add(heading).spaceBottom(0.03f * SpaceTennis3D.HEIGHT).colspan(3)
				.row();
		table.add(image1).pad(10);
		table.add(image2).pad(10);
		table.add(rules1).row();
		table.add(rules2).colspan(2);
		table.add(image3).pad(10).row();
		table.add(image4).pad(10);
		table.add(rules3).colspan(2).row();
		table.add(btnExit).colspan(3).spaceBottom(0.05f * SpaceTennis3D.HEIGHT)
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

		if (Gdx.input.isKeyJustPressed(Input.Keys.UP)
				|| Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
			SpaceTennis3D.goTo(new RulesScreen1());
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
				|| Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)
				|| Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
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
