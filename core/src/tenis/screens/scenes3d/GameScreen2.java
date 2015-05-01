package tenis.screens.scenes3d;

import tenis.managers.Assets;
import tenis.references.Models;
import tenis.screens.MainMenuScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.ModelLoader;
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
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Disposable;

public class GameScreen2 extends InputAdapter implements Screen {

	class MyContactListener extends ContactListener {
		@Override
		public boolean onContactAdded (int userValue0, int partId0, int index0, int userValue1, int partId1, int index1) {
	        instances.get(userValue0).moving = false;
	        instances.get(userValue1).moving = false;
	        return true;
	    }
	}

	static class MyMotionState extends btMotionState {
		Matrix4 transform;

		@Override
		public void getWorldTransform(Matrix4 worldTrans) {
			worldTrans.set(transform);
		}

		@Override
		public void setWorldTransform(Matrix4 worldTrans) {
			transform.set(worldTrans);
		}
	}

	static class GameObject extends ModelInstance implements Disposable {
		public btRigidBody body;
		public final MyMotionState motionState;
		public Vector3 dimensions = new Vector3();
		public Vector3 center = new Vector3();
		public final float radius;
		public boolean moving;
		private final static BoundingBox bounds = new BoundingBox();

		public GameObject(Model model, String node,
				btRigidBody.btRigidBodyConstructionInfo constructionInfo) {
			super(model, node);
			motionState = new MyMotionState();
			motionState.transform = transform;
			body = new btRigidBody(constructionInfo);
			body.setMotionState(motionState);
			this.calculateBoundingBox(bounds);
			bounds.getCenter(center);
			bounds.getDimensions(dimensions);
			radius = dimensions.len() / 2f;
		}

		@Override
		public void dispose() {
			body.dispose();
			motionState.dispose();
		}

		static class Constructor implements Disposable {
			public final Model model;
			public final String node;
			public final btCollisionShape shape;
			public final btRigidBody.btRigidBodyConstructionInfo constructionInfo;
			private static Vector3 localInertia = new Vector3();

			public Constructor(Model model, String node,
					btCollisionShape shape, float mass) {
				this.model = model;
				this.node = node;
				this.shape = shape;
				if (mass > 0f)
					shape.calculateLocalInertia(mass, localInertia);
				else
					localInertia.set(0, 0, 0);
				this.constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(
						mass, null, shape, localInertia);
			}

			public GameObject construct() {
				return new GameObject(model, node, constructionInfo);
			}

			@Override
			public void dispose() {
				shape.dispose();
				constructionInfo.dispose();
			}
		}
	}
	
	

	private Stage stage;
	private Label label;
	private BitmapFont font;

	private PerspectiveCamera cam;

	private ModelBatch modelBatch;
	private boolean loading;

	private Environment environment;

	private CameraInputController camController;

	private Assets assets;
	private Model model;
	private Model table;
	private Model ball;
	private Vector3 ballPosition = new Vector3(0, 1, 0);
	private Vector3 ballScale = new Vector3(0.05f, 0.05f, 0.05f);
	private ModelInstance ambient;

	// FLAGS
	final static short GROUND_FLAG = 1 << 8;
	final static short OBJECT_FLAG = 1 << 9;
	final static short ALL_FLAG = -1;

	// Collisions
	private Array<GameObject> instances;
	ArrayMap<String, GameObject.Constructor> constructors;
	float spawnTimer;
	private GameObject ballObj;
	private btSphereShape ballShape;
	private btCollisionObject ballCollObj;
	private GameObject tableObj;
	private btBoxShape tableShape;
	private btCollisionObject tableCollObj;

	btCollisionConfiguration collisionConfig;
	btDispatcher dispatcher;
	MyContactListener contactListener;
	btBroadphaseInterface broadphase;
	btDynamicsWorld dynamicsWorld;
	btConstraintSolver constraintSolver;

	private StringBuilder stringBuilder;

	@Override
	public void show() {
		/*
		 * Initialize bullet wrapper. Must be before any call to a constructor
		 * of Bullet related objects.
		 */
		Bullet.init();
		
		assets = new Assets();
		assets.loadAll();

		// ENVIRONMENT
		modelBatch = new ModelBatch();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f,
				0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f,
				-0.8f, -0.2f));

		// CAMERA
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		cam.position.set(1.3f, 0f, 0f);
		cam.lookAt(-0.5f, -0.75f, 0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();
		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);

		// COLLISION STUFF
		collisionConfig = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfig);
		broadphase = new btDbvtBroadphase();
		constraintSolver = new btSequentialImpulseConstraintSolver();
		dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase,
				constraintSolver, collisionConfig);
		dynamicsWorld.setGravity(new Vector3(0, -10f, 0));
		contactListener = new MyContactListener();

		// INITIALIZE INSTANCES
		instances = new Array<GameObject>();

		// LABEL FOR FPS
		stage = new Stage();
		font = new BitmapFont();
		label = new Label(" ", new Label.LabelStyle(font, Color.WHITE));
		stage.addActor(label);
		stringBuilder = new StringBuilder();

		// START LOADING
		loading = true;
	}

	private void doneLoading() {
		// LOAD ASSETS
		table = assets.get(Models.MODEL_TABLE, Model.class);
		ModelBuilder modelBuilder = new ModelBuilder();
		ball = modelBuilder.createSphere(2f, 2f, 2f, 20, 20, new Material(),
				Usage.Position | Usage.Normal | Usage.TextureCoordinates);

		// CREATING OBJECTS SHAPES
		tableShape = new btBoxShape(new Vector3(1, 1, 1));
		ballShape = new btSphereShape(1);

		// CREATING GAME_OBJECTS: table
		tableObj = new GameObject.Constructor(table, "table", tableShape, 0f)
				.construct();
		tableObj.transform.rotate(Vector3.Y, 180);
		tableObj.moving = false;
		tableShape = new btBoxShape(new Vector3(tableObj.dimensions.x, 0.01f,
				tableObj.dimensions.z));
		tableObj.body = new btRigidBody(
				new btRigidBody.btRigidBodyConstructionInfo(0,
						tableObj.body.getMotionState(), tableShape,
						GameObject.Constructor.localInertia));

		// CREATING GAME_OBJECTS: ball
		ballObj = new GameObject.Constructor(ball, "ball", ballShape, 1f)
				.construct();
		ballShape = new btSphereShape(ballObj.radius);
		ballObj.body = new btRigidBody(
				new btRigidBody.btRigidBodyConstructionInfo(1,
						ballObj.body.getMotionState(), ballShape,
						GameObject.Constructor.localInertia));
		Attribute attribute = ColorAttribute.createSpecular(Color.BLUE);
		Attribute attribute2 = ColorAttribute.createDiffuse(Color.RED);
		ballObj.materials.add(new Material(attribute, attribute2));
		ballObj.transform.translate(ballPosition).scale(ballScale.x,
				ballScale.y, ballScale.z);
		ballObj.moving = true;
		ballObj.transform.setFromEulerAngles(MathUtils.random(360f),
				MathUtils.random(360f), MathUtils.random(360f));
		ballObj.transform.trn(MathUtils.random(-2.5f, 2.5f), 9f,
				MathUtils.random(-2.5f, 2.5f));
		ballObj.body.setWorldTransform(ballObj.transform);
		ballObj.body.setUserValue(instances.size);
		ballObj.body.setCollisionFlags(ballObj.body.getCollisionFlags()
				| btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);

		// CREATING COLLISION_OBJECTS: table
		tableCollObj = new btCollisionObject();
		tableCollObj.setCollisionShape(tableShape);
		tableCollObj.setWorldTransform(tableObj.transform);

		// CREATING COLLISION_OBJECTS: ball
		ballCollObj = new btCollisionObject();
		ballCollObj.setCollisionShape(ballShape);
		ballCollObj.setWorldTransform(ballObj.transform);

		// CONSTRUCTORS
		constructors = new ArrayMap<String, GameObject.Constructor>(
				String.class, GameObject.Constructor.class);
		constructors.put("table", new GameObject.Constructor(table, "table",
				tableShape, 0f));
		constructors.put("ball", new GameObject.Constructor(ball, "ball",
				ballShape, 1f));

		// ADD INSTANCES
		tableObj.body.setCollisionFlags(tableObj.body.getCollisionFlags()
				| btCollisionObject.CollisionFlags.CF_STATIC_OBJECT);
		dynamicsWorld.addRigidBody(tableObj.body);
		tableObj.body.setContactCallbackFlag(GROUND_FLAG);
		tableObj.body.setContactCallbackFilter(0);
		tableObj.body.setActivationState(Collision.DISABLE_DEACTIVATION);
		instances.add(tableObj);

		ballObj.transform.setFromEulerAngles(MathUtils.random(360f),
				MathUtils.random(360f), MathUtils.random(360f));
		ballObj.transform.trn(MathUtils.random(-2.5f, 2.5f), 9f,
				MathUtils.random(-2.5f, 2.5f));
		ballObj.body.setUserValue(instances.size);
		ballObj.body.setCollisionFlags(ballObj.body.getCollisionFlags()
				| btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
		dynamicsWorld.addRigidBody(ballObj.body);
		ballObj.body.setContactCallbackFlag(OBJECT_FLAG);
		ballObj.body.setContactCallbackFilter(GROUND_FLAG);
		BoundingBox out = new BoundingBox();
		ballObj.calculateBoundingBox(out);
		ballObj.dimensions = out.getDimensions();
		instances.add(ballObj);

		// AMBIENT
		model = assets.get(Models.MODEL_AMBIENT, Model.class);
		ambient = new ModelInstance(model);
		ambient.transform.rotate(Vector3.Z, 180);

		// FINISHED LOADING
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
		// INPUT
		handleInput();
	

		// LOADING
		if (loading)
			doneLoading();

		// VIEWPORT
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		// FPS LABEL
		stringBuilder.setLength(0);
		stringBuilder.append(" FPS: ")
				.append(Gdx.graphics.getFramesPerSecond());
		stringBuilder.append(" Visible: ").append(visibleCount);
		label.setText(stringBuilder);

		camController.update();

		modelBatch.begin(cam);
		// AMBIENT
		if (ambient != null)
			modelBatch.render(ambient);

		// PHYSICS
		visibleCount = 0;
		dynamicsWorld.stepSimulation(delta, 5, 1f / 60f);
		modelBatch.render(instances, environment);
		for (GameObject instance : instances) {
			// MOVEMENT
			if (instance.moving) {
				instance.transform.trn(0f, -10f * delta, 0f);
				instance.body.setWorldTransform(instance.transform);
			}

			// VISIBLE
			if (isVisible(cam, instance)) {
				visibleCount++;
				modelBatch.render(instance, environment);
			}
		}
		
		modelBatch.end();

		stage.draw();

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
		for (GameObject obj : instances) {
			obj.dispose();
		}
		instances.clear();
		model.dispose();

		tableObj.dispose();
		tableShape.dispose();

		ballObj.dispose();
		ballShape.dispose();

		broadphase.dispose();
		dispatcher.dispose();
		collisionConfig.dispose();

		contactListener.dispose();

		modelBatch.dispose();

	}
}