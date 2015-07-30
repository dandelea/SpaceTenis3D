package tennis.screens.rules;

import tennis.SpaceTennis3D;
import tennis.managers.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class RulesScreen1 implements Screen{
	private Assets assets;

	private Stage stage;
	private Table table;
	
	private BitmapFont titleFont;
	private BitmapFont rulesFont;
	private Skin skin;
	
	private Label heading;
	private Label rules1, rules2;
	
	private static final String RULES1 = "";
	private static final String RULES2 = "";

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
		heading = new Label("Bluetooth", skin);
		heading.setStyle(new LabelStyle(titleFont, Color.WHITE));

		rulesFont = Assets.fontGenerator.generateFont(20);
		rules1 = new Label(RULES1, skin);
		rules1.setStyle(new LabelStyle(rulesFont, Color.WHITE));
		
		rulesFont = Assets.fontGenerator.generateFont(20);
		rules2 = new Label(RULES2, skin);
		rules2.setStyle(new LabelStyle(rulesFont, Color.WHITE));
		
		table.add(heading).spaceBottom(0.07f * SpaceTennis3D.HEIGHT).colspan(2).row();
		table.add(rules1).colspan(2).row();
		table.add(rules2).colspan(2).row();
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
