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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class RulesScreen3 implements Screen{
	private Assets assets;

	private Stage stage;
	private Table table;
	
	private BitmapFont titleFont;
	private Skin skin;
	
	private Label heading;
	private Label rules1, rules2;
	private Image image1;
	private TextButton btnExit;
	
	private static final String RULES1 = "Igual que el tenis";
	private static final String RULES2 = "Hemos procurado que las reglas sean las mismas que el tenis tradicional.\nAsi que simplemente divértete.";

	@SuppressWarnings("deprecation")
	@Override
	public void show() {
		assets = new Assets();
		assets.loadScreen(Assets.RULES_SCREEN);
		
		stage = new Stage();
		//stage.setDebugAll(true);
		Gdx.input.setInputProcessor(stage);
		
		skin = Assets.skin;
		
		table = new Table(skin);
		table.setFillParent(true);
		
		// Creating heading
		titleFont = Assets.titleGenerator.generateFont(50);
		heading = new Label("Reglas", skin);
		heading.setStyle(new LabelStyle(titleFont, Color.WHITE));

		rules1 = new Label(RULES1, skin);
		rules2 = new Label(RULES2, skin);

		image1 = new Image(assets.get(Assets.URL_RULES_IMAGE4, Texture.class));
		
		btnExit = new TextButton("Volver", skin);
		btnExit.pad(20);
		btnExit.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				SpaceTennis3D.goTo(new RulesScreen());
			}

		});
		
		table.add(heading).spaceBottom(0.07f * SpaceTennis3D.HEIGHT).colspan(2).row();
		table.add(rules1).spaceBottom(0.05f * SpaceTennis3D.HEIGHT).row();
		table.add(rules2).row();
		table.add(image1).pad(10).center().row();
		table.add(btnExit).spaceBottom(0.05f * SpaceTennis3D.HEIGHT).row();
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
		if (Gdx.input.isKeyJustPressed(Input.Keys.UP) ||
				Gdx.input.isKeyJustPressed(Input.Keys.LEFT)){
			SpaceTennis3D.goTo(new RulesScreen2());
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) ||
				Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
			Soundbox.play("quit");
			SpaceTennis3D.goTo(new RulesScreen());
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
