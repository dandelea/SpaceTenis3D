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
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.CollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btBox2dShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionAlgorithm;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDispatcherInfo;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;
import com.badlogic.gdx.physics.bullet.collision.btManifoldResult;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

public class GameScreen implements Screen {
	

	
	private Stage stage;
	private Label label;
	private BitmapFont font;

	private PerspectiveCamera cam;

	private ModelBatch modelBatch;
	private AssetManager assets;
	private Array<GameObject> instances = new Array<GameObject>();
	private boolean loading;

	private Environment environment;

	private CameraInputController camController;

	private GameObject table;
	private Model ballModel;
	private GameObject ball;
	private Vector3 ballPosition = new Vector3(0, 0, 0);
	private Vector3 ballScale = new Vector3(0.05f, 0.05f, 0.05f);
	private float speed;
	private ModelInstance ambient;

	// Collisions
	private boolean collision = false;
	private btCollisionShape tableShape;
	private btCollisionShape ballShape;
	private btCollisionObject tableObject;
    private btCollisionObject ballObject;
    btCollisionConfiguration collisionConfig;
    btDispatcher dispatcher;

	private StringBuilder stringBuilder;

	@Override
	public void show() {
		/*
		 * Initialize bullet wrapper. Must be before any call to a constructor
		 * of Bullet related objects.
		 */
		Bullet.init();
		
		collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        
        
        
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
		cam.position.set(1.3f, 0f, 0f);
		cam.lookAt(-0.5f, -0.75f, 0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();

		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);

		speed = 10;

		assets = new AssetManager();
		assets.load("models/table/table2.g3dj", Model.class);
		assets.load("models/ambient/ambient1.obj", Model.class);
		loading = true;

	}

	private void doneLoading() {
		// TABLE
		table = new GameObject("table", assets.get("models/table/table2.g3dj",
				Model.class), tableShape);
		table.transform.rotate(Vector3.Y, 180);
		table.transform.setToTranslation(new Vector3(0, -table.transform
				.getScaleY(), 0));
		tableShape = new btBox2dShape(new Vector3(table.dimensions.x, 0.01F, table.dimensions.z));
		tableObject = new btCollisionObject();
		tableObject.setCollisionShape(tableShape);
		tableObject.setWorldTransform(table.transform);

		
		// BALL
		ModelBuilder modelBuilder = new ModelBuilder();
		ballModel = modelBuilder.createSphere(2f, 2f, 2f, 20, 20,
				new Material(), Usage.Position | Usage.Normal
						| Usage.TextureCoordinates);
		ball = new GameObject("ball", ballModel, ballShape);
		Attribute attribute = ColorAttribute.createSpecular(Color.BLUE);
		Attribute attribute2 = ColorAttribute.createDiffuse(Color.RED);
		ball.materials.get(0).set(attribute, attribute2);
		ball.transform.translate(ballPosition).scale(ballScale.x, ballScale.y,
				ballScale.z);
		ballShape = new btSphereShape(ball.radius);
		ballObject = new btCollisionObject();
		ballObject.setCollisionShape(ballShape);
		ballObject.setWorldTransform(ball.transform);

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
				if (instance.name == "ball" && !collision) {
					instance.transform.translate(0, -speed * delta, 0);
					ballObject.setWorldTransform(ball.transform);
					
					collision = checkCollision();
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
		CollisionObjectWrapper co0 = new CollisionObjectWrapper(ballObject);
	    CollisionObjectWrapper co1 = new CollisionObjectWrapper(tableObject);
	     
	    btCollisionAlgorithm algorithm = dispatcher.findAlgorithm(co0.wrapper, co1.wrapper);
	 
	    btDispatcherInfo info = new btDispatcherInfo();
	    btManifoldResult result = new btManifoldResult(co0.wrapper, co1.wrapper);
	     
	    algorithm.processCollision(co0.wrapper, co1.wrapper, info, result);
	 
	    boolean r = result.getPersistentManifold().getNumContacts() > 0;
	 
	    dispatcher.freeCollisionAlgorithm(algorithm.getCPointer());
	    result.dispose();
	    info.dispose();
	    co1.dispose();
	    co0.dispose();
	     
	    return r;
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
		tableObject.dispose();
	    tableShape.dispose();
	     
	    ballObject.dispose();
	    ballShape.dispose();
	     
	    dispatcher.dispose();
	    collisionConfig.dispose();
		
	    ballModel.dispose();
	    
		modelBatch.dispose();
		instances.clear();
		assets.dispose();

	}

}
