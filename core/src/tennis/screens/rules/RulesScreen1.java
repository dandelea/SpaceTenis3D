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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class RulesScreen1 implements Screen{
	private Assets assets;

	private Stage stage;
	private Table table;
	
	private BitmapFont titleFont;
	private Skin skin;
	
	private Label heading;
	private Label rules1, rules2;
	private Image image1;
	
	private static final String RULES1 = "Para jugar a este juego necesitarás un\ndispositivo Android con Bluetooth.";
	private static final String RULES2 = "Empareja tu móvil con tu ordenador \ny sigue las instrucciones de la aplicación \nde móvil para conectarte.";

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
		heading = new Label("Bluetooth", skin);
		heading.setStyle(new LabelStyle(titleFont, Color.WHITE));

		rules1 = new Label(RULES1, skin);
		rules2 = new Label(RULES2, skin);
		
		image1 = new Image(assets.get(Assets.URL_RULES_IMAGE3, Texture.class));
		
		table.add(heading).spaceBottom(0.07f * SpaceTennis3D.HEIGHT).colspan(2).row();
		table.add(rules1).spaceBottom(0.05f * SpaceTennis3D.HEIGHT).row();
		table.add(rules2).row();
		table.add(image1).pad(10).center();
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
			Soundbox.play("quit");
			SpaceTennis3D.goTo(new RulesScreen());
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || 
				Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) ||
				Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
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
		assets.dispose();
		stage.dispose();
		titleFont.dispose();
	}
}
