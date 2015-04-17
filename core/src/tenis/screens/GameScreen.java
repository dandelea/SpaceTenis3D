package tenis.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

public class GameScreen implements Screen {
	private Stage stage;
	private Label label;
	private BitmapFont font;
	
	private PerspectiveCamera cam;

	private Model model;
	private ModelBatch modelBatch;
	private AssetManager assets;
	private Array<GameObject> instances = new Array<GameObject>();
	private boolean loading;

	private Environment environment;

	private CameraInputController camController;

	private GameObject table;
	private ModelInstance ambient;
	
	private StringBuilder stringBuilder;
	
	
	
	public static class GameObject extends ModelInstance {
	    public final Vector3 center = new Vector3();
	    public final Vector3 dimensions = new Vector3();
	    public final float radius;
	     
	    private final static BoundingBox bounds = new BoundingBox();
	     
	    public GameObject(Model model, String rootNode, boolean mergeTransform) {
	        super(model, rootNode, mergeTransform);
	        calculateBoundingBox(bounds);
	        bounds.getCenter(center);
	        bounds.getDimensions(dimensions);
	        radius = dimensions.len() / 2f;
	    }

		public GameObject(Model model) {
			super(model);
			calculateBoundingBox(bounds);
	        bounds.getCenter(center);
	        bounds.getDimensions(dimensions);
	        radius = dimensions.len() / 2f;
		}
	}
	

	@Override
	public void show() {
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
		cam.position.set(1.17f, 2f, 0f);
		cam.lookAt(0, 0, 0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();

		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);

		assets = new AssetManager();
		assets.load("models/table/table2.g3dj", Model.class);
		assets.load("models/ambient/ambient1.obj", Model.class);
		loading = true;
	}

	private void doneLoading() {
		table = new GameObject(assets.get("models/table/table2.g3dj",
				Model.class));
		table.transform.rotate(Vector3.Y, 180);
		instances.add(table);

		ambient = new ModelInstance(assets.get("models/ambient/ambient1.obj",
				Model.class));
		ambient.transform.rotate(Vector3.Z, 180);
		loading = false;
	}
	
	private Vector3 position = new Vector3();
	public boolean isVisible(final Camera cam, final GameObject instance){
		instance.transform.getTranslation(position);
	    position.add(instance.center);
	    return cam.frustum.sphereInFrustum(position, instance.radius);
	}

	private int visibleCount;
	@Override
	public void render(float delta) {
		if (loading && assets.update())
            doneLoading();
        camController.update();
         
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        
        modelBatch.begin(cam);
        visibleCount = 0;
        for (final ModelInstance instance : instances) {
            if (isVisible(cam, (GameObject) instance)) {
            	visibleCount++;
                modelBatch.render(instance, environment);
            }
        }
        
        modelBatch.end();
        
        System.out.println(cam.position);
        
        if (ambient != null)
           modelBatch.render(ambient);
        
        
        stringBuilder.setLength(0);
        stringBuilder.append(" FPS: ").append(Gdx.graphics.getFramesPerSecond());
        stringBuilder.append(" Visible: ").append(visibleCount);
        label.setText(stringBuilder);
        stage.draw();
        
        
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
