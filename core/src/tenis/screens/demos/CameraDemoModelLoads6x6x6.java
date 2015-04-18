package tenis.screens.demos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.utils.Array;

public class CameraDemoModelLoads6x6x6 implements Screen {

	private PerspectiveCamera cam;

	private ModelBatch modelBatch;

	private Environment environment;

	private CameraInputController camController;

	public AssetManager assets;
	public Array<ModelInstance> instances = new Array<ModelInstance>();

	public boolean loading;

	@Override
	public void show() {
		modelBatch = new ModelBatch();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f,
				0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f,
				-0.8f, -0.2f));

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		cam.position.set(7f, 7f, 7f);
		cam.lookAt(0, 0, 0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();

		assets = new AssetManager();
		assets.load("models/ship/ship.g3db", Model.class);
		loading = true;

		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);
	}

	public void doneLoading() {
		Model ship = assets.get("models/ship/ship.g3db", Model.class);
		for (float x = -5f; x <= 5f; x += 2f) {
			for (float z = -5f; z <= 5f; z += 2f) {
				for (float y = -5f; y <= 5f; y += 2f) {
					ModelInstance instance = new ModelInstance(ship);
					instance.transform.setToTranslation(x, y, z);
					instances.add(instance);
				}
			}
		}
		loading = false;
	}

	@Override
	public void render(float delta) {
		if (loading && assets.update())
			doneLoading();

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam);
		modelBatch.render(instances, environment);
		modelBatch.end();

		camController.update();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

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
		assets.dispose();
		instances.clear();
	}
}
