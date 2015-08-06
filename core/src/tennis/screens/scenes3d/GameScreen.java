package tennis.screens.scenes3d;

import tennis.SpaceTennis3D;
import tennis.managers.Assets;
import tennis.managers.Jukebox;
import tennis.managers.Models;
import tennis.managers.Tools;
import tennis.managers.bluetooth.BluetoothServer;
import tennis.managers.physics.Constructor;
import tennis.managers.physics.Flags;
import tennis.managers.physics.GameObject;
import tennis.managers.physics.ParticleController;
import tennis.objects.Opponent;
import tennis.objects.Scoreboard;
import tennis.screens.GameOverScreen;
import tennis.screens.MainMenuScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.CollisionConstants;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

public class GameScreen implements Screen {

	class MyContactListener extends ContactListener {

		@Override
		public boolean onContactAdded(int userValue0, int partId0, int index0,
				boolean match0, int userValue1, int partId1, int index1,
				boolean match1) {
			final GameObject ball = instances.get(userValue0);
			Vector3 inertia = ball.body.getLinearVelocity();
			Vector3 reaction = new Vector3(inertia.x, -tableBouncingFactor
					* inertia.y, inertia.z);
			ball.body.setLinearVelocity(reaction);
			ball.bounces++;

			return true;
		}
	}

	private Stage stage;
	private Window pause;
	private Label setsLabel, pointsLabel;
	private BitmapFont titleFont;
	private StringBuilder stringBuilder;

	private static PerspectiveCamera cam;

	private static ModelBatch modelBatch;
	private boolean loading;

	private Environment environment;

	private CameraInputController camController;

	// MODELS
	private Assets assets;
	private Model ball;
	private Model table;
	private ModelInstance ambient;

	// POSITIONS
	private Vector3 camPosition = new Vector3(0, 2, -2);
	private Vector3 camDirection = new Vector3();
	private float tableBouncingFactor = 1.05f;
	public static Vector3 ballPosition = new Vector3(-0.5f, .5f, -1);
	private float gravity = -2;

	// BULLET COLLISION STUFF
	btCollisionConfiguration collisionConfig;
	btDispatcher dispatcher;
	MyContactListener contactListener;
	btBroadphaseInterface broadphase;
	btDynamicsWorld dynamicsWorld;
	btConstraintSolver constraintSolver;

	// COLLISIONS INSTANCES
	private Array<GameObject> instances;
	private ArrayMap<String, Constructor> constructors;

	// MUSIC
	private boolean firstSong;

	// DEBUG
	private DebugDrawer debugDrawer;

	// GAME STATES
	public static final int GAME_READY = 0;
	public static final int GAME_RUNNING = 1;
	public static final int GAME_PAUSED = 2;
	public static final int GAME_OVER = 3;
	public static int state;

	// GAME
	private Scoreboard scoreBoard = new Scoreboard();
	private Opponent opponent;
	private boolean opponentWillHit;

	@Override
	public void show() {

		/*
		 * Initialize bullet wrapper. Must be before any call to a constructor
		 * of Bullet related objects.
		 */

		Bullet.init();

		assets = new Assets();
		assets.loadScreen(Assets.GAME_SCREEN);

		setupUI();

		// PAUSE SCREEN
		pause = new Window("PAUSA", Assets.skin);
		pause.setSize(stage.getWidth() / 1.5f, stage.getHeight() / 1.5f);
		pause.setPosition(stage.getWidth() / 2 - pause.getWidth() / 2,
				stage.getHeight() / 2 - pause.getHeight() / 2);
		TextButton resume = new TextButton("Continuar", Assets.skin);
		resume.pad(20);
		resume.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				state = GAME_RUNNING;
				disposePause();
			}
		});
		TextButton quit = new TextButton("Salir", Assets.skin);
		quit.pad(20);
		quit.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				dispose();
				Jukebox.stopAll();
				SpaceTennis3D.games++;
				SpaceTennis3D.goTo(new MainMenuScreen());
			}
		});
		pause.add(resume).spaceBottom(SpaceTennis3D.HEIGHT * 0.05f).row();
		pause.add(quit).row();
		pause.setVisible(false);
		stage.addActor(pause);

		// GAMES
		state = GAME_READY;

		// ENVIRONMENT
		modelBatch = new ModelBatch();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f,
				0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f,
				-0.8f, -0.2f));

		// CAMERA
		cam = new PerspectiveCamera(Gdx.app.getPreferences(SpaceTennis3D.TITLE)
				.getInteger("FOV"), SpaceTennis3D.WIDTH, SpaceTennis3D.HEIGHT);
		cam.position.set(camPosition);
		cam.lookAt(camDirection);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();
		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(stage);

		// PARTICLES
		if (SpaceTennis3D.games == 0) {
			SpaceTennis3D.particleController = new ParticleController(cam, modelBatch);
		}

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
		debugDrawer
				.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
		dynamicsWorld.setDebugDrawer(debugDrawer);
		contactListener = new MyContactListener();

		// INITIALIZE INSTANCES
		instances = new Array<GameObject>();

		// START LOADING
		loading = true;
	}

	@SuppressWarnings("deprecation")
	private void doneLoading() {

		// AMBIENT
		ambient = new ModelInstance(assets.get(Models.MODEL_AMBIENT,
				Model.class));
		ambient.transform.rotate(Vector3.Z, 180);

		// CREATE TABLE
		table = assets.get(Models.MODEL_TABLE, Model.class);

		// INITIAL OPPONENT POSITION
		opponent = new Opponent(SpaceTennis3D.difficulty);
		BoundingBox box = new BoundingBox();
		table.calculateBoundingBox(box);
		opponent.setLastHit(new Vector3(box.getMax().z, 1, 0));

		// CREATE BALL
		ModelBuilder mb = new ModelBuilder();
		mb.begin();
		mb.node().id = "ball";
		mb.part("ball", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal,
				new Material(ColorAttribute.createDiffuse(Color.WHITE)))
				.sphere(0.1f, 0.1f, 0.1f, 10, 10);
		ball = mb.end();

		// FILL CONSTRUCTORS
		constructors = new ArrayMap<String, Constructor>(String.class,
				Constructor.class);
		constructors.put("ball", new Constructor(ball, "ball",
				new btSphereShape(0.05f), 1));
		constructors.put("table", new Constructor(table, table.nodes.get(0).id,
				Bullet.obtainStaticNodeShape(table.nodes), 0));

		// ADD TABLE
		GameObject finalTable = constructors.get("table").construct();
		finalTable.transform.rotate(new Vector3(0, 1, 0), 90);
		finalTable.body.setCollisionFlags(finalTable.body.getCollisionFlags()
				| btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
		finalTable.body.proceedToTransform(finalTable.transform);
		finalTable.body.setContactCallbackFlag(Flags.GROUND_FLAG);
		finalTable.body.setContactCallbackFilter(0);
		finalTable.body
				.setActivationState(CollisionConstants.DISABLE_DEACTIVATION);
		instances.add(finalTable);

		dynamicsWorld.addRigidBody(finalTable.body);

		Jukebox.play("game");
		firstSong = true;

		// FINISHED LOADING
		loading = false;

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
			dispose();
			Jukebox.stopAll();
			SpaceTennis3D.games++;
			SpaceTennis3D.goTo(new GameOverScreen());
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
					&& (!Tools.outOfTable(instances, scoreBoard, dynamicsWorld,
							table, ball, constructors, SpaceTennis3D.particleController) || instances.indexOf(
							instance, true) == 0)) {
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

		SpaceTennis3D.particleController.renderParticleEffects();

		/*
		 * debugDrawer.begin(cam); dynamicsWorld.debugDrawWorld();
		 * debugDrawer.end();
		 */

		renderUI();

		if (firstSong && !Jukebox.get("game").isPlaying()) {
			firstSong = false;
			Jukebox.stop("game");
			Jukebox.loop("game2");
		}
	}

	@SuppressWarnings("deprecation")
	private void setupUI() {
		// UI
		stage = new Stage();

		titleFont = Assets.titleGenerator.generateFont(40);
		setsLabel = new Label("", Assets.skin);
		setsLabel.setPosition(SpaceTennis3D.WIDTH / 10,
				5 * SpaceTennis3D.HEIGHT / 6);
		pointsLabel = new Label("",
				new Label.LabelStyle(titleFont, Color.WHITE));
		pointsLabel.setY(9 * SpaceTennis3D.HEIGHT / 10);
		stage.addActor(setsLabel);
		stage.addActor(pointsLabel);
		stringBuilder = new StringBuilder();
	}

	private void renderUI() {

		// SETS MARKER
		stringBuilder.setLength(0);
		stringBuilder.append("Sets").append("\n");
		stringBuilder.append("Player 1: ")
				.append(scoreBoard.getSetsOfPlayer(1)).append("\n")
				.append("Player 2: ").append(scoreBoard.getSetsOfPlayer(2));
		setsLabel.setText(stringBuilder);

		// POINTS MARKER
		stringBuilder.setLength(0);
		if (scoreBoard.isDeuce()) {

			if (!scoreBoard.isAdvantaged1() && !scoreBoard.isAdvantaged2()) {
				stringBuilder.append("DEUCE");
			} else {

				if (scoreBoard.isAdvantaged1()) {
					stringBuilder.append("ADV");
				} else {
					stringBuilder.append(scoreBoard.getScore1() == 0 ? "00"
							: scoreBoard.getScore1());
				}
				stringBuilder.append("-");
				if (scoreBoard.isAdvantaged2()) {
					stringBuilder.append("ADV");
				} else {
					stringBuilder.append(scoreBoard.getScore2() == 0 ? "00"
							: scoreBoard.getScore2());
				}
			}

		} else {
			stringBuilder
					.append(scoreBoard.getScore1() == 0 ? "00" : scoreBoard
							.getScore1())
					.append("-")
					.append(scoreBoard.getScore2() == 0 ? "00" : scoreBoard
							.getScore2());
		}
		pointsLabel.setText(stringBuilder);
		pointsLabel.setX(SpaceTennis3D.WIDTH / 2
				- titleFont.getBounds(pointsLabel.getText().toString()).width
				/ 2);

		stage.act();
		stage.draw();
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
			Tools.spawn(constructors, ballPosition, instances, dynamicsWorld);
		firstBall = false;

		instances.get(1);
		// Opponent hit the ball
		if (Tools.allObjectsLoaded(instances)
				&& Tools.onOpponentHittable(instances.get(0), instances.get(1))
				&& instances.get(1).lastPlayer == 1) {
			GameObject ball = instances.get(1);
			if (opponentWillHit) {
				Tools.hit(instances, opponent.getVelocity(), opponent,
						SpaceTennis3D.particleController, opponentWillHit);

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
		state = GAME_RUNNING;
	}

	long time = 0;

	public void handleInput() {
		// STARTED TO SWING

		// Log.debug(BluetoothServer.accelerometer.toString());

		if (Tools.allObjectsLoaded(instances)
				&& Tools.onPlayerHittable(instances.get(0), instances.get(1))
				&& !instances.get(1).hitted
				&& BluetoothServer.accelerometer.z > 10) {
			GameObject table = instances.get(0);
			GameObject ball = instances.get(1);
			Vector3 inertia = ball.body.getLinearVelocity().scl(-1);
			Vector3 normal = BluetoothServer.accelerometer.nor();
			Vector3 reaction = normal.scl(2 * normal.dot(inertia)).sub(inertia)
					.scl(0.2f);
			reaction.z = Math.abs(reaction.z) * 10 + 1;
			ball.bounces = 0;
			ball.hitted = true;
			ball.lastPlayer = Tools.onPlayerSide(table, ball) ? 1 : 2;
			ball.body.applyCentralImpulse(reaction);
			System.out.println(reaction);
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && state==GAME_RUNNING) {
			opponentWillHit = Tools.hit(instances, 50, opponent, SpaceTennis3D.particleController,
					opponentWillHit);
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
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		
		// ASSETS
		assets.dispose();
		
		// INSTANCES AND MODELS
		for (GameObject obj : instances) {
			obj.motionState.dispose();
			obj.body.dispose();
			obj.dispose();
		}
		instances.clear();
		ball.dispose();
		
		// BULLET COLLISIONS
		dynamicsWorld.dispose();
		broadphase.dispose();
		collisionConfig.dispose();
		dispatcher.dispose();
		constraintSolver.dispose();
		contactListener.dispose();
		
		// DEBUG
		debugDrawer.dispose();
		
		// PARTICLE
		SpaceTennis3D.particleController.dispose();
		
		// FONTS
		titleFont.dispose();
		
		// SCREEN
		modelBatch.dispose();
		stage.dispose();

	}
}