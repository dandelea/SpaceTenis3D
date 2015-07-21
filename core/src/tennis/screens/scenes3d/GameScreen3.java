package tennis.screens.scenes3d;

import java.util.Random;

import tennis.SpaceTennis3D;
import tennis.managers.Assets;
import tennis.managers.Tools;
import tennis.managers.bluetooth.BluetoothServer;
import tennis.objects.Difficulty;
import tennis.objects.Opponent;
import tennis.objects.Scoreboard;
import tennis.references.Models;
import tennis.screens.MainMenuScreen;
import tennis.screens.ScoreScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.CollisionConstants;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
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
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen3 extends InputAdapter implements Screen {

	class MyContactListener extends ContactListener {
		
		long lastBounceTime = 0;
		@Override
		public boolean onContactAdded(int userValue0, int partId0, int index0,
				boolean match0, int userValue1, int partId1, int index1,
				boolean match1) {
			final GameObject ball = instances.get(userValue0);
			GameObject table = instances.get(userValue1);
			Vector3 inertia = ball.body.getLinearVelocity();
			Vector3 normal = new Vector3(0,1,0);
			Vector3 reaction = new Vector3(inertia.x, -tableBouncingFactor
					* inertia.y, inertia.z);
			if (TimeUtils.timeSinceNanos(lastBounceTime) < 500000000){
				// BALL IN FLOOR.
				//System.out.println("ERROR");
			}
			lastBounceTime = TimeUtils.nanoTime();
			
			if (ball.lastPlayer == 1 && Tools.onPlayerSide(table, ball) && ball.bounced){
				// DOBLE BOTE
				//System.out.println("doble bote");
				/*new java.util.Timer().schedule( 
				        new java.util.TimerTask() {
				            @Override
				            public void run() {
				            	point();
				    			instances.removeValue(ball, false);
				    			dynamicsWorld.removeRigidBody(ball.body);
				    			spawn();
				            }
				        }, 
				        1000
				);*/
			}
			ball.bounced = true;
			instances.get(userValue0).body.setLinearVelocity(reaction);
			
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

	public static class GameObject extends ModelInstance implements Disposable {
		public final Vector3 center = new Vector3();
		public final Vector3 dimensions = new Vector3();
		public final float radius;
		private final static BoundingBox bounds = new BoundingBox();
		private final static Vector3 position = new Vector3();
		public final btRigidBody body;
		public final MyMotionState motionState;
		public int lastPlayer;
		public static Vector3 lastPosition =  new Vector3();
		public boolean bounced;
		public boolean hitted;

		public GameObject(Model model, String rootNode,
				btRigidBody.btRigidBodyConstructionInfo constructionInfo) {
			super(model, rootNode);
			calculateBoundingBox(bounds);
			bounds.getCenter(center);
			bounds.getDimensions(dimensions);
			radius = dimensions.len() / 2f;
			motionState = new MyMotionState();
			motionState.transform = transform;
			body = new btRigidBody(constructionInfo);
			body.setMotionState(motionState);
		}

		public Vector3 getPosition() {
			Vector3 res = new Vector3();
			transform.getTranslation(position);
			transform.getTranslation(res);
			return res;
		}
		
		public BoundingBox getBoundingBox(){
			calculateBoundingBox(bounds);
			return bounds;
		}
		
		public boolean isVisible(Camera cam) {
			return cam.frustum.sphereInFrustum(
					transform.getTranslation(position).add(center), radius);
		}

		/**
		 * @return -1 on no intersection, or when there is an intersection: the
		 *         squared distance between the center of this object and the
		 *         point on the ray closest to this object when there is
		 *         intersection.
		 */
		public float intersects(Ray ray) {
			transform.getTranslation(position).add(center);
			final float len = ray.direction.dot(position.x - ray.origin.x,
					position.y - ray.origin.y, position.z - ray.origin.z);
			if (len < 0f)
				return -1f;
			float dist2 = position.dst2(ray.origin.x + ray.direction.x * len,
					ray.origin.y + ray.direction.y * len, ray.origin.z
							+ ray.direction.z * len);
			return (dist2 <= radius * radius) ? dist2 : -1f;
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
	private Window pause;
	private Label label;
	private BitmapFont font;
	private StringBuilder stringBuilder;

	private static PerspectiveCamera cam;

	private static ModelBatch modelBatch;
	private boolean loading;

	private Environment environment;

	private CameraInputController camController;

	// MODELS
	private Assets assets;
	private ModelBuilder modelBuilder;
	private Model ball;
	private Model table;
	private ModelInstance ambient;

	// POSITIONS
	//private Vector3 camPosition = new Vector3(2, 2, 0);
	private Vector3 camPosition = new Vector3(0,2,-2);
	private Vector3 camDirection = new Vector3();
	private float tableBouncingFactor = 1.05f;
	private Vector3 tablePosition = new Vector3(0, 1, 0);
	private Vector3 ballScale = new Vector3(0.05f, 0.05f, 0.05f);
	private Vector3 ballPosition = new Vector3(-0.5f, .5f, -1);
	private float gravity = -2;

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

	private btCollisionShape tableShape;
	private btCollisionShape gridShape;
	
	private DebugDrawer debugDrawer;

	float spawnTimer = 1f;
	

	// SELECTION
	private int selected, selecting = -1;
	private Vector3 touchPoint;

	// GAME STATES
	public static final int GAME_READY = 0;
	public static final int GAME_RUNNING = 1;
	public static final int GAME_PAUSED = 2;
	public static final int GAME_OVER = 3;
	public int state;

	// GAME
	private Scoreboard scoreBoard = new Scoreboard();
	private Opponent opponent;
	private boolean nextRound;

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

		// UI
		stage = new Stage();
		stage.setDebugAll(true);
		font = new BitmapFont();
		label = new Label(" ", new Label.LabelStyle(font, Color.WHITE));
		stage.addActor(label);
		stringBuilder = new StringBuilder();

		// PAUSE SCREEN
		pause = new Window("PAUSE", Assets.skin);
		pause.setSize(stage.getWidth() / 1.5f, stage.getHeight() / 1.5f);
		pause.setPosition(stage.getWidth() / 2 - pause.getWidth() / 2,
				stage.getHeight() / 2 - pause.getHeight() / 2);
		TextButton resume = new TextButton("Resume", Assets.skin);
		resume.pad(20);
		resume.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				state = GAME_RUNNING;
				disposePause();
			}
		});
		TextButton quit = new TextButton("Quit", Assets.skin);
		quit.pad(20);
		quit.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SpaceTennis3D.goTo(new MainMenuScreen());
			}
		});
		pause.add(resume).row();
		pause.add(quit).row();
		pause.setVisible(false);
		stage.addActor(pause);

		// GAMES
		state = GAME_READY;
		nextRound = true;

		// ENVIRONMENT
		modelBatch = new ModelBatch();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f,
				0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f,
				-0.8f, -0.2f));

		// CAMERA
		cam = new PerspectiveCamera(Gdx.app.getPreferences(SpaceTennis3D.TITLE).getInteger("FOV"), SpaceTennis3D.WIDTH,
				SpaceTennis3D.HEIGHT);
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
		dynamicsWorld.setGravity(new Vector3(0, gravity, 0));
		
		// MAKE AND REGISTER RENDERER
        debugDrawer = new DebugDrawer();
        debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
        dynamicsWorld.setDebugDrawer(debugDrawer);
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
		
		// CREATE TABLE
		table = assets.get(Models.MODEL_TABLE, Model.class);
		
		// INITIAL OPPONENT POSITION
		opponent = new Opponent(Difficulty.EASY);
		BoundingBox box = new BoundingBox();
		table.calculateBoundingBox(box);
		opponent.setLastHit(new Vector3(box.getMax().z, 1, 0));

		// CREATE BALL
		ModelBuilder mb = new ModelBuilder();
		mb.begin();
		mb.node().id = "ball";
		mb.part("ball", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal,
				new Material(ColorAttribute.createDiffuse(Color.WHITE))).sphere(
				0.1f, 0.1f, 0.1f, 10, 10);
		ball = mb.end();

		// FILL CONSTRUCTORS
		constructors = new ArrayMap<String, GameObject.Constructor>(
				String.class, GameObject.Constructor.class);
		constructors.put("ball", new GameObject.Constructor(ball, "ball",
				new btSphereShape(0.05f), 1));
		constructors.put("table",
				new GameObject.Constructor(table, table.nodes.get(0).id,
						Bullet.obtainStaticNodeShape(table.nodes), 0));

		// ADD TABLE
		GameObject finalTable = constructors.get("table").construct();
		finalTable.transform.rotate(new Vector3(0, 1, 0), 90);
		finalTable.body.setCollisionFlags(finalTable.body.getCollisionFlags()
				| btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
		finalTable.body.proceedToTransform(finalTable.transform);
		finalTable.body.setContactCallbackFlag(GROUND_FLAG);
		finalTable.body.setContactCallbackFilter(0);
		finalTable.body.setActivationState(CollisionConstants.DISABLE_DEACTIVATION);
		instances.add(finalTable);

		dynamicsWorld.addRigidBody(finalTable.body);

		// FINISHED LOADING
		loading = false;

	}

	public void spawn() {
		GameObject obj = constructors.values[0].construct();
		obj.transform.trn(ballPosition);
		obj.bounced = false;
		obj.hitted = false;
		obj.lastPlayer = 0;
		// obj.transform.trn(0, 0, 0);
		obj.body.proceedToTransform(obj.transform);
		obj.body.setUserValue(instances.size);
		obj.body.setCollisionFlags(obj.body.getCollisionFlags()
				| btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
		instances.add(obj);
		obj.body.setFriction(1);
        obj.body.setRestitution(0);
		obj.body.setContactCallbackFlag(OBJECT_FLAG);
		obj.body.setContactCallbackFilter(GROUND_FLAG);
		dynamicsWorld.addRigidBody(obj.body);
	}

	@Override
	public void render(float delta) {
		// VIEWPORT
		Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1.f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		if (delta > 0.1f)
			delta = 0.1f;

		switch (state) {
		case GAME_READY:
			updateReady();
			break;
		case GAME_RUNNING:
			updateRunning(delta);
			break;
		case GAME_PAUSED:
			updatePaused();
			break;
		case GAME_OVER:
			SpaceTennis3D.lastScoreboard = scoreBoard;
			SpaceTennis3D.goTo(new ScoreScreen());
			break;
		}

	}

	private void updateGame() {
		GameObject table = instances.get(0);
		GameObject ball = instances.get(1);
		
		
		modelBatch.begin(cam);
		
		// AMBIENT
		if (ambient != null)
			modelBatch.render(ambient);
		// PHYSICS
		for (GameObject instance : instances) {
			if (instance.isVisible(cam)
					&& (!outOfTable(table, instance) || instances
							.indexOf(instance, true) == 0)) {
				modelBatch.render(instance, environment);
			}
		}
		if (Tools.allObjectsLoaded(instances)) {
			if (Tools.onPlayerHittable(table, ball)) {
				((ColorAttribute) ball.materials.get(0).get(
						ColorAttribute.Diffuse)).color.set(Color.RED);
			} else {
				((ColorAttribute) ball.materials.get(0).get(
						ColorAttribute.Diffuse)).color.set(Color.WHITE);
			}
		}
		modelBatch.end();
		
		//debugDrawer.begin(cam);
        //dynamicsWorld.debugDrawWorld();
        //debugDrawer.end();

		// UI
		stringBuilder.setLength(0);
		stringBuilder.append(" FPS: ")
				.append(Gdx.graphics.getFramesPerSecond());
		stringBuilder.append(" Selected: ").append(selected);
		stringBuilder.append(" Set: ").append(scoreBoard.getSet());
		stringBuilder.append(" Game: ").append(scoreBoard.getGame());
		if (scoreBoard.isDeuce()) {
			if (scoreBoard.isAdvantaged1()) {
				stringBuilder.append(" ADV. ");
			}
			stringBuilder.append(" DEUCE ");
			if (scoreBoard.isAdvantaged2()) {
				stringBuilder.append(" ADV. ");
			}
		} else {
			stringBuilder.append(" Player: ").append(scoreBoard.getScore1());
			stringBuilder.append(" Enemy: ").append(scoreBoard.getScore2());
		}
		
		label.setText(stringBuilder);
		stage.act();
		stage.draw();
		
		GameObject.lastPosition = GameObject.position;
		
	}

	private void createPauseMenu() {
		pause.setVisible(true);
	}

	private void updatePaused() {
		handleInput();
		updateGame();
	}

	private void disposePause() {
		pause.setVisible(false);
	}

	boolean firstBall = true;
	private void updateRunning(float delta) {
		handleInput();
		
		if (firstBall)
			spawn();
		firstBall = false;
		
		nextRound = Tools.allObjectsLoaded(instances) && outOfTable(instances.get(0), instances.get(1));
		
		instances.get(1);
		// Opponent hit the ball
		if (Tools.allObjectsLoaded(instances) &&
				Tools.onOpponentHittable(instances.get(0), instances.get(1)) &&
				instances.get(1).lastPlayer==1) {
			GameObject ball = instances.get(1);
			Random random = new Random();
			float f = random.nextFloat();
			if (f <= opponent.getHitRate()){
				hit(opponent.getVelocity());
			}
			ball.hitted = true;
			ball.lastPlayer = 2;
		}
		
		dynamicsWorld.stepSimulation(delta, 5, 1f / 60f);
		camController.update();

		updateGame();
	}

	private void updateReady() {
		if (loading)
			doneLoading();
		if (Gdx.input.justTouched()) {
			state = GAME_RUNNING;
		}
	}

	public int getObject(int screenX, int screenY) {
		Ray ray = cam.getPickRay(screenX, screenY);
		int result = -1;
		float distance = -1;
		for (int i = 0; i < instances.size; ++i) {
			final float dist2 = instances.get(i).intersects(ray);
			if (dist2 >= 0f && (distance < 0f || dist2 <= distance)) {
				result = i;
				distance = dist2;
			}
		}
		selected = result;
		return result;
	}
	
	/**
	 * Apply force to the instance to move it to a certain location
	 * @param instance Ball to move
	 * @param position Location
	 * @param intensity Force of the movement
	 */
	public void moveTo(GameObject instance, Vector3 position, int intensity) {
		Vector3 instPos = instance.getPosition();
		instance.body.applyCentralForce(new Vector3((position.x - instPos.x)
				* intensity, 10, (position.z - instPos.z) * intensity));
	}

	public void point() {
		GameObject table = instances.get(0);
		GameObject ball = instances.get(1);
		// ball fell on players table
		if (Tools.onPlayerSide(table, ball)) {
			scoreBoard.point2();
		} else {
			// ball hit by player
			if (ball.lastPlayer == 1) {
				// ball did bounced on opponent table
				if (ball.bounced == true){
					scoreBoard.point1();
				// ball out
				} else {
					scoreBoard.point2();
				}
				
			// ball hit by opponent
			} else if (ball.lastPlayer == 2) {
				scoreBoard.point2();
			}
		}
		if (scoreBoard.isFinished()){
			state = GAME_OVER;
		}
	}

	/**
	 * Dice que esta fuera del Eje Y de la tabla.
	 * 
	 * @param table
	 * @param ball
	 * @return
	 */
	public boolean outOfTable(GameObject table, GameObject ball) {
		boolean res;
		Vector3 ballPosition = ball.getPosition();
		BoundingBox tableBounds = GameObject.bounds;
		Vector3 min = new Vector3();
		tableBounds.getCorner000(min);
		Vector3 max = new Vector3();
		tableBounds.getCorner111(max);
		res = ballPosition.y < min.y && Math.abs(ballPosition.x) < 5 && Math.abs(ballPosition.z) < 5;
		if (res) {
			point();
			instances.removeValue(ball, false);
			dynamicsWorld.removeRigidBody(ball.body);
			spawn();
		}
		return res;
	}
	
	public void hit(int intensity) {
		GameObject table = instances.get(0);
		GameObject ball = instances.get(1);
		ball.bounced = false;
		ball.hitted = Tools.onPlayerSide(table, ball);
		ball.lastPlayer = Tools.onPlayerSide(table, ball) ? 1:2;
		moveTo(ball, new Vector3(0, 0, 0), intensity);
	}

	long time = 0;
	public void handleInput() {
		// STARTED TO SWING
		
		if (Tools.allObjectsLoaded(instances) && Tools.onPlayerHittable(instances.get(0), instances.get(1)) 
				&& !instances.get(1).hitted && BluetoothServer.accelerometer.z > 10) {
				GameObject table = instances.get(0);
				GameObject ball = instances.get(1);
				Vector3 inertia = ball.body.getLinearVelocity().scl(-1);
				Vector3 normal = BluetoothServer.accelerometer.nor();
				Vector3 reaction = normal.scl(2*normal.dot(inertia)).sub(inertia).scl(0.2f);
				reaction.z = Math.abs(reaction.z)*10 + 1;
				ball.bounced = false;
				ball.hitted = true;
				ball.lastPlayer = Tools.onPlayerSide(table, ball) ? 1:2;
				ball.body.applyCentralImpulse(reaction);
				System.out.println(reaction);
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
			hit(50);
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.E)){
			SpaceTennis3D.goTo(new MainMenuScreen());
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
				&& state == GAME_RUNNING) {
			state = GAME_PAUSED;
			createPauseMenu();
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
				&& state == GAME_PAUSED) {
			state = GAME_RUNNING;
			disposePause();
		}
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		selecting = getObject(screenX, screenY);
		return selecting >= 0;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return selecting >= 0;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (selecting >= 0) {
			if (selecting == getObject(screenX, screenY))
				// setSelected(selecting);
				selecting = -1;
			return true;
		}
		return false;
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
		ball.dispose();

		tableShape.dispose();

		broadphase.dispose();
		dispatcher.dispose();
		collisionConfig.dispose();

		contactListener.dispose();

		modelBatch.dispose();
		
		debugDrawer.dispose();
		assets.dispose();
	}
}