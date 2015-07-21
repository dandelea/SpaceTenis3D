package tennis.screens.scenes3d;

import tennis.managers.Assets;
import tennis.references.Models;
import tennis.screens.MainMenuScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.CollisionConstants;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Disposable;

public class GameScreen2 extends InputAdapter implements Screen {

	class MyContactListener extends ContactListener {
		@Override
		public boolean onContactAdded(int userValue0, int partId0, int index0,
				boolean match0, int userValue1, int partId1, int index1,
				boolean match1) {
			GameObject collisioner = instances.get(userValue1);
			Vector3 inertia = collisioner.body.getLinearVelocity();
			Vector3 reaction = new Vector3(inertia.x, -tableBouncingFactor
					* inertia.y, inertia.z);
			instances.get(userValue1).body.setLinearVelocity(reaction);
			((ColorAttribute)instances.get(userValue1).materials.get(0).get(ColorAttribute.Diffuse)).color.set(Color.WHITE);
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
		public final btRigidBody body;
		public final MyMotionState motionState;

		public GameObject (Model model, String node, btRigidBody.btRigidBodyConstructionInfo constructionInfo) {
			super(model, node);
			motionState = new MyMotionState();
			motionState.transform = transform;
			body = new btRigidBody(constructionInfo);
			body.setMotionState(motionState);
		}

		@Override
		public void dispose () {
			body.dispose();
			motionState.dispose();
		}

		static class Constructor implements Disposable {
			public final Model model;
			public final String node;
			public final btCollisionShape shape;
			public final btRigidBody.btRigidBodyConstructionInfo constructionInfo;
			private static Vector3 localInertia = new Vector3();

			public Constructor (Model model, String node, btCollisionShape shape, float mass) {
				this.model = model;
				this.node = node;
				this.shape = shape;
				if (mass > 0f)
					shape.calculateLocalInertia(mass, localInertia);
				else
					localInertia.set(0, 0, 0);
				this.constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, null, shape, localInertia);
			}

			public GameObject construct () {
				return new GameObject(model, node, constructionInfo);
			}

			@Override
			public void dispose () {
				shape.dispose();
				constructionInfo.dispose();
			}
		}
	}

	private static PerspectiveCamera cam;

	private static ModelBatch modelBatch;
	private boolean loading;

	private Environment environment;

	private CameraInputController camController;

	// MODELS
	private Assets assets;
	private ModelBuilder modelBuilder;
	private Model model;
	private Model table;
	private ModelInstance tableI;
	private ModelInstance ambient;

	// POSITIONS
	private Vector3 camPosition = new Vector3(2f, 1.22f, 0f);
	private Vector3 camDirection = new Vector3();
	private int camFOV = 67;
	private float tableBouncingFactor = 0.8f;
	private Vector3 tablePosition = new Vector3(0, 1, 0);
	private Vector3 ballScale = new Vector3(0.05f, 0.05f, 0.05f);

	// FLAGS
	final static short GROUND_FLAG = 1 << 8;
	final static short OBJECT_FLAG = 1 << 9;
	final static short ALL_FLAG = -1;

	// BULLET COLLISION STUFF
	btCollisionConfiguration collisionConfig;
	btDispatcher dispatcher;
	MyContactListener contactListener;
	btBroadphaseInterface broadphase;
	btDynamicsWorld dynamicsWorld;
	btConstraintSolver constraintSolver;

	// COLLISIONS INSTANCES
	private Array<GameObject> instances;
	private ArrayMap<String, GameObject.Constructor> constructors;
	private btSphereShape ballShape;
	private btBoxShape tableShape;

	float spawnTimer = 1f;

	@Override
	public void show() {
		/*
		 * Initialize bullet wrapper. Must be before any call to a constructor
		 * of Bullet related objects.
		 */
		Bullet.init();

		assets = new Assets();
		assets.loadAll();
		modelBuilder = new ModelBuilder();

		// ENVIRONMENT
		modelBatch = new ModelBatch();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f,
				0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f,
				-0.8f, -0.2f));

		// CAMERA
		cam = new PerspectiveCamera(camFOV, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		cam.position.set(camPosition);
		cam.lookAt(camDirection);
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
		dynamicsWorld.setGravity(new Vector3(0, -10, 0));
		contactListener = new MyContactListener();

		// INITIALIZE INSTANCES
		instances = new Array<GameObject>();

		// START LOADING
		loading = true;
	}

	private void doneLoading() {

		// AMBIENT
		ambient = new ModelInstance(assets.get(Models.MODEL_AMBIENT,
				Model.class));
		ambient.transform.rotate(Vector3.Z, 180);

		// CREATE BALL
		ModelBuilder mb = new ModelBuilder();
		mb.begin();
		mb.node().id = "ball";
		mb.part("ball", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal,
				new Material(ColorAttribute.createDiffuse(Color.RED))).sphere(
				0.2f, 0.2f, 0.2f, 10, 10);
		model = mb.end();

		// CREATE TABLE
		table = assets.get(Models.MODEL_TABLE, Model.class);
		BoundingBox box = new BoundingBox();
		table.calculateBoundingBox(box);
		tableShape = new btBoxShape(new Vector3(box.getDimensions().x/2, box.getDimensions().y/2, box.getDimensions().z/2));
		tableI = new ModelInstance(table);
		tableI.transform.scl(1);

		// FILL CONSTRUCTORS
		constructors = new ArrayMap<String, GameObject.Constructor>(
				String.class, GameObject.Constructor.class);
		constructors.put("ball", new GameObject.Constructor(model, "ball",
				new btSphereShape(0.5f), 1f));
		constructors.put("table", new GameObject.Constructor(table, "table",
				tableShape, 1));
		// ADD TABLE
		
		GameObject object = constructors.get("table").construct();
		object.body.setCollisionFlags(object.body.getCollisionFlags()
				| btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
		object.body.proceedToTransform(object.transform);
		object.body.setContactCallbackFlag(GROUND_FLAG);
		object.body.setContactCallbackFilter(0);
		object.body.setActivationState(CollisionConstants.DISABLE_DEACTIVATION);
		instances.add(object);

		dynamicsWorld.addRigidBody(object.body);

		// FINISHED LOADING
		loading = false;
	}

	public void spawn() {
		GameObject obj = constructors.values[0].construct();
		obj.transform.trn(MathUtils.random(-2.5f, 2.5f), 1f,
				MathUtils.random(-2.5f, 2.5f));
		obj.body.proceedToTransform(obj.transform);
		obj.body.setUserValue(instances.size);
		obj.body.setCollisionFlags(obj.body.getCollisionFlags()
				| btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
		instances.add(obj);
		dynamicsWorld.addRigidBody(obj.body);
		obj.body.setContactCallbackFlag(OBJECT_FLAG);
		obj.body.setContactCallbackFilter(GROUND_FLAG);
	}

	@Override
	public void render(float delta) {
		// INPUT
		handleInput();

		if ((spawnTimer -= delta) < 0) {
			spawn();
			spawnTimer = 1.5f;
		}

		// LOADING
		if (loading)
			doneLoading();

		// VIEWPORT
		Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1.f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		dynamicsWorld.stepSimulation(delta, 5, 1f / 60f);
		camController.update();

		modelBatch.begin(cam);

		// AMBIENT
		if (ambient != null)
			modelBatch.render(ambient);
		// PHYSICS
		modelBatch.render(tableI);

		modelBatch.render(instances, environment);
		modelBatch.end();

	}

	public void handleInput() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			((Game) Gdx.app.getApplicationListener())
					.setScreen(new MainMenuScreen());
		}
	}

	@Override
	public void resize(int width, int height) {

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

		tableShape.dispose();

		ballShape.dispose();

		broadphase.dispose();
		dispatcher.dispose();
		collisionConfig.dispose();

		contactListener.dispose();

		modelBatch.dispose();

	}
}