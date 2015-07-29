package tennis.screens;

import tennis.SpaceTennis3D;
import tennis.managers.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class RulesScreen implements Screen {
	private Assets assets;

	private Stage stage;
	private Table table;
	
	private Skin skin;

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
		dispose();
	}

	@Override
	public void dispose() {
		stage.dispose();
		assets.dispose();
	}

}
