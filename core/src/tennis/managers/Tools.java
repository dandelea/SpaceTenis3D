package tennis.managers;

import tennis.SpaceTennis3D;
import tennis.managers.physics.Constructor;
import tennis.managers.physics.Flags;
import tennis.managers.physics.GameObject;
import tennis.managers.physics.ParticleController;
import tennis.objects.Opponent;
import tennis.objects.Scoreboard;
import tennis.screens.scenes3d.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

public class Tools {
	private static float[] playerHitZoneZ = { -1.1f, 0 };

	// THREADS

	
	public static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// COLORS

	/**
	 * Get a random color
	 * 
	 * @return Color
	 */
	public static Color randomColor() {
		return randomColor(0f, 1f);
	}

	/**
	 * Get a random color between lo and hi values
	 * 
	 * @param lo
	 *            Lower value
	 * @param hi
	 *            Higher value
	 * @return Random Color
	 */
	public static Color randomColor(float lo, float hi) {
		Color col = new Color();
		col.r = MathUtils.random(lo, hi);
		col.g = MathUtils.random(lo, hi);
		col.b = MathUtils.random(lo, hi);
		col.a = 1f;
		return col;
	}

	// GEOMETRY

	private static Matrix4 mtx = new Matrix4();
	private static Quaternion q = new Quaternion();

	/**
	 * Rotate a position around an axis
	 * 
	 * @param position
	 * @param axis
	 * @param angle
	 */
	public static void rotateAround(Vector3 position, Vector3 axis, float angle) {
		q.setFromAxis(axis, angle);
		mtx.set(q);
		position.prj(mtx);
	}

	// GAME SCREEN

	/**
	 * Tell if the ball is in the player side of the table (First half) or the
	 * opponent's. Z>0 is the opponent's side and Z<0 is player's side.
	 * 
	 * @param table
	 * @param ball
	 */
	public static boolean onPlayerSide(GameObject table, GameObject ball) {
		boolean res;
		Vector3 ballPosition = ball.getPosition();
		res = ballPosition.z < 0;
		return res;
	}

	/**
	 * Tell if the ball is in the player hittable zone or not.
	 * 
	 * @param table
	 * @param ball
	 */
	public static boolean onPlayerHittable(GameObject table, GameObject ball) {
		boolean res;
		Vector3 ballPosition = ball.getPosition();
		res = ballPosition.z > playerHitZoneZ[0]
				&& ballPosition.z < playerHitZoneZ[1];
		return res;
	}

	/**
	 * Tell if the ball is in the opponent hittable zone.
	 * 
	 * @param table
	 * @param ball
	 */
	public static boolean onOpponentHittable(GameObject table, GameObject ball) {
		BoundingBox bounds = table.getBoundingBox();
		return ball.getPosition().z >= bounds.max.x;
	}

	/**
	 * Tell if the table and the ball has been loaded in the scene.
	 * 
	 * @param instances
	 */
	public static boolean allObjectsLoaded(Array<GameObject> instances) {
		return instances.size == 2;
	}

	/**
	 * Remove the ball from physic world and from the instances collection
	 * Also dispose the model instance.
	 */
	public static void disposeBall(Array<GameObject> instances,
			GameObject ball, btDynamicsWorld dynamicsWorld) {
		instances.removeValue(ball, false);
		dynamicsWorld.removeRigidBody(ball.body);
		ball.disposed = true;
		ball.dispose();
	}

	/**
	 * Spawns a new ball in a certain location, with the certain characteristics.
	 */
	public static void spawn(ArrayMap<String, Constructor> constructors,
			Vector3 ballPosition, Array<GameObject> instances,
			btDynamicsWorld dynamicsWorld) {
		GameObject obj = constructors.values[0].construct();
		obj.transform.trn(ballPosition);
		obj.bounces++;
		obj.hitted = false;
		obj.lastPlayer = 0;
		obj.body.proceedToTransform(obj.transform);
		obj.body.setUserValue(instances.size);
		obj.body.setCollisionFlags(obj.body.getCollisionFlags()
				| btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
		instances.add(obj);
		obj.body.setFriction(1);
		obj.body.setRestitution(0);
		obj.body.setContactCallbackFlag(Flags.OBJECT_FLAG);
		obj.body.setContactCallbackFilter(Flags.GROUND_FLAG);
		dynamicsWorld.addRigidBody(obj.body);
	}

	/**
	 * Updates the scoreboard, according to the screen state.
	 */
	public static void point(Array<GameObject> instances, Scoreboard scoreBoard) {
		GameObject table = instances.get(0);
		GameObject ball = instances.get(1);

		// BALL FELL ON PLAYERS SIDE
		if (Tools.onPlayerSide(table, ball)) {

			// OPPONENT FAULT
			if (ball.lastPlayer == 2 && ball.bounces == 0) {
				scoreBoard.point1();
			} else {
				scoreBoard.point2();
			}

			// BALL FELL ON OPPONENTS SIDE
		} else {

			// PLAYER FAULT
			if (ball.lastPlayer == 1 && ball.bounces == 0) {
				scoreBoard.point2();
			} else {
				scoreBoard.point1();
			}

		}
		if (scoreBoard.isFinished()) {
			GameScreen.state = GameScreen.GAME_OVER;
		}
	}

	/**
	 * Apply force to the instance to move it to a certain location
	 * 
	 * @param instance
	 *            Ball to move
	 * @param position
	 *            Location
	 * @param intensity
	 *            Force of the movement
	 */
	public static void moveTo(GameObject instance, Vector3 position,
			int intensity) {
		Vector3 instPos = instance.getPosition();
		instance.body.setLinearVelocity(new Vector3());
		instance.body.applyCentralForce(new Vector3((position.x - instPos.x)
				* intensity, 10, (position.z - instPos.z) * intensity));
	}

	/**
	 * Checks if the ball is in a bad state (out of table or stopped).
	 * If true, dispose the ball, point and spawn a new ball.
	 * 
	 * @return ball have been disposed and renewed
	 */
	public static boolean outOfTable(Array<GameObject> instances,
			Scoreboard scoreBoard, btDynamicsWorld dynamicsWorld,
			GameObject table, GameObject ball,
			ArrayMap<String, Constructor> constructors,
			ParticleController particleController) {
		boolean res;
		
		Vector3 ballPosition = ball.getPosition();
		BoundingBox tableBounds = GameObject.bounds;
		Vector3 min = new Vector3();
		tableBounds.getCorner000(min);
		Vector3 max = new Vector3();
		tableBounds.getCorner111(max);
		
		res = ((ballPosition.y < min.y && Math.abs(ballPosition.x) < 5
				&& Math.abs(ballPosition.z) < 5) ||
				 ball.bounces >= GameObject.MAX_BOUNCES) && !ball.disposed;
		if (res) {
			Tools.point(instances, scoreBoard);
			particleController.explosion(ball.position);
			Tools.disposeBall(instances, ball, dynamicsWorld);
			Tools.spawn(constructors, GameScreen.ballPosition, instances, dynamicsWorld);
		}
		return res;
	}

	/**
	 * Hit the ball oriented to the center of the table with a given intensity.
	 * 
	 * @param intensity Force to be applied to the ball
	 * @param opponentWillHit Advance if opponent is going to hit, according to difficulty
	 * @return opponent hit the ball
	 */
	public static boolean hit(Array<GameObject> instances, int intensity,
			Opponent opponent, ParticleController particleController,
			boolean opponentWillHit) {
		GameObject table = instances.get(0);
		GameObject ball = instances.get(1);
		ball.bounces = 0;
		ball.hitted = Tools.onPlayerSide(table, ball);
		ball.lastPlayer = Tools.onPlayerSide(table, ball) ? 1 : 2;
		if (ball.lastPlayer == 1) {
			if (MathUtils.random(1) < opponent.getHitRate()) {
				opponentWillHit = true;
			} else {
				opponentWillHit = false;
			}
		}

		if (Tools.onPlayerSide(table, ball)) {
			particleController.explodeHit(1, ball.position);
		} else {
			particleController.explodeHit(2, ball.position);
		}

		Soundbox.play("laser");
		moveTo(ball, new Vector3(0, 0, 0), intensity);
		return opponentWillHit;
	}

	// SETTINGS

	/**
	 * @return if vsync has been enabled.
	 */
	public static boolean vSync() {
		return Gdx.app.getPreferences(SpaceTennis3D.TITLE).getBoolean("vsync");
	}

	/**
	 * @return if fullscreen mode has been enabled.
	 */
	public static boolean fullscreen() {
		return Gdx.app.getPreferences(SpaceTennis3D.TITLE).getBoolean(
				"fullscreen");
	}

	/**
	 * @return if music has been enabled.
	 */
	public static boolean music() {
		return Gdx.app.getPreferences(SpaceTennis3D.TITLE).getBoolean("music");
	}

	/**
	 * @return if sound has been enabled.
	 */
	public static boolean sound() {
		return Gdx.app.getPreferences(SpaceTennis3D.TITLE).getBoolean("sound");
	}
}
