package tenis.screens;

import cbd.asteroides.managers.GameKeys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SettingsScreen implements Screen {
	// Resources
	private String backgroundDir = "tennis-court.jpg";

	private Stage stage;

	private Skin skin;
	private Table table;
	private BitmapFont font;
	private Label heading;
	
	private SelectBox<Object> sb;
	
	private TextButton btnSave;

	private TextureAtlas atlas;


	@Override
	public void show() {
		stage = new Stage();
		//stage.setDebugAll(true);
		Gdx.input.setInputProcessor(stage);

		atlas = new TextureAtlas("ui/uiskin.atlas");
		skin = new Skin(Gdx.files.internal("ui/uiskin.json"), atlas);

		table = new Table(skin);
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		
		// Creating heading
		heading = new Label("Settings", skin);
		
		// resolution
		sb = new SelectBox<Object>(skin);
		Object[] a = new Object[6];
		a[0] = "800 x 600";
		a[1] = "1024 x 768";
		a[2] = "1280 x 800";
		a[3] = "1280 x 1024";
		a[4] = "1366 x 768";
		a[5] = "1920 x 1080";
		sb.setItems(a);
		

		btnSave = new TextButton("Save", skin);
		btnSave.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				String[] parts = ((String) sb.getSelected()).split(" x ");
				int width = Integer.parseInt(parts[0]);
				int height = Integer.parseInt(parts[1]);
				Gdx.graphics.setDisplayMode(width, height, true);
			}
		});
		
		table.add(heading);
		table.getCell(heading).spaceBottom(100);
		//table.getCell(actor);
		table.row();
		table.add(sb);
		table.row();
		table.add(btnSave);
		stage.addActor(table);
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(delta);
		stage.draw();
		
		handleInput();
	}

	private void handleInput() {
		if (GameKeys.isPressed(GameKeys.UP)) {
			if (currentItem > 0) {
				currentItem--;
			}
		}
		if (GameKeys.isPressed(GameKeys.DOWN)) {
			if (currentItem < menuItems.length - 1) {
				currentItem++;
			}
		}
		if (GameKeys.isPressed(GameKeys.ENTER)) {
			select();
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
		font.dispose();
	}

}
