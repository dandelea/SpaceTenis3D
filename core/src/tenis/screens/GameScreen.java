package tenis.screens;

import tenis.objects.GameObject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

public class GameScreen implements Screen {
	private Stage stage;
	private Label label;
	private BitmapFont font;

	private PerspectiveCamera cam;

	private ModelBatch modelBatch;
	private ModelBuilder modelBuilder;
	private AssetManager assets;
	private Array<GameObject> instances = new Array<GameObject>();
	private boolean loading;

	private Environment environment;

	private CameraInputController camController;

	private GameObject table;
	private GameObject ball;
	private Vector3 ballPosition = new Vector3(0, 0, 0);
	private Vector3 ballScale = new Vector3(0.05f, 0.05f, 0.05f);
	private float speed;
	private ModelInstance ambient;

	private boolean collision = false;;

	private StringBuilder stringBuilder;

	@Override
	public void show() {
		modelBuilder = new ModelBuilder();
		stage = new Stage();
		font = new BitmapFont();
		label = new Label(" ", new Label.LabelStyle(font, Color.WHITE));
		stage.addActor(label);
		stringBuilder = new StringBuilder();

		modelBatch = new ModelBatch();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f,
				0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f,
				-0.8f, -0.2f));

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		cam.position.set(1.3f, 1f, 0f);
		cam.lookAt(-0.5f, -0.75f, 0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();

		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);

		speed=4;
		
		assets = new AssetManager();
		assets.load("models/table/table2.g3dj", Model.class);
		assets.load("models/ambient/ambient1.obj", Model.class);
		loading = true;

		

	}

	private void doneLoading() {
		// TABLE
		table = new GameObject("table", assets.get("models/table/table2.g3dj",
				Model.class));
		table.transform.rotate(Vector3.Y, 180);
		table.transform.setToTranslation(new Vector3(0, -table.transform
				.getScaleY(), 0));

		// BALL
		ModelBuilder modelBuilder = new ModelBuilder();
		Model model = modelBuilder.createSphere(2f, 2f, 2f, 20, 20,
				new Material(), Usage.Position | Usage.Normal
						| Usage.TextureCoordinates);
		ball = new GameObject("ball", model);
		Attribute attribute = ColorAttribute.createSpecular(Color.BLUE);
		Attribute attribute2 = ColorAttribute.createDiffuse(Color.RED);
		ball.materials.get(0).set(attribute, attribute2);
		ball.transform.translate(ballPosition).scale(ballScale.x, ballScale.y,
				ballScale.z);

		// AMBIENT
		ambient = new ModelInstance(assets.get("models/ambient/ambient1.obj",
				Model.class));
		ambient.transform.rotate(Vector3.Z, 180);


		// Table
		instances.add(table);

		// Ball
		instances.add(ball);

		loading = false;
	}

	private Vector3 position = new Vector3();

	public boolean isVisible(final Camera cam, final GameObject instance) {
		instance.transform.getTranslation(position);
		position.add(instance.center);
		return cam.frustum.sphereInFrustum(position, instance.radius);
	}

	private int visibleCount;

	@Override
	public void render(float delta) {

		handleInput();

		if (loading && assets.update())
			doneLoading();
		camController.update();

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam);
		visibleCount = 0;
		for (GameObject instance : instances) {
			if (isVisible(cam, instance)) {
				visibleCount++;
				if (instance.name == "ball" && !collision){
					instance.transform.translate(0, -speed * delta, 0);
				}
				modelBatch.render((ModelInstance) instance, environment);
			}
		}
		modelBatch.end();

		if (ambient != null)
			modelBatch.render(ambient);

		stringBuilder.setLength(0);
		stringBuilder.append(" FPS: ")
				.append(Gdx.graphics.getFramesPerSecond());
		stringBuilder.append(" Visible: ").append(visibleCount);
		label.setText(stringBuilder);
		stage.draw();

	}

	private boolean checkCollision() {
		return false;
	}

	public void handleInput() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			((Game) Gdx.app.getApplicationListener())
					.setScreen(new MainMenuScreen());
		}
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
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
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		instances.clear();
		assets.dispose();

	}

}
