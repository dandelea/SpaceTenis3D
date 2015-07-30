package tennis.managers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import tennis.SpaceTennis3D;
import tennis.managers.physics.GameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

public class Tools {
	private static float[] playerHitZoneZ = {-1.1f, 0};

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
	 * @return Color
	 */
	public static Color randomColor() {
		return randomColor(0f, 1f);
	}
	
	/**
	 * Get a random color between lo and hi values
	 * @param lo Lower value
	 * @param hi Higher value
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
	 * Tell if the ball is in the player side of the table
	 * (First half) or the opponent's. Z>0 is the opponent's
	 * side and Z<0 is player's side.
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
	 * @param table
	 * @param ball
	 */
	public static boolean onPlayerHittable(GameObject table, GameObject ball) {
		boolean res;
		Vector3 ballPosition = ball.getPosition();
		res = ballPosition.z > playerHitZoneZ[0] && ballPosition.z < playerHitZoneZ[1];
		return res;
	}
	
	/**
	 * Tell if the ball is in the opponent hittable zone.
	 * @param table
	 * @param ball
	 */
	public static boolean onOpponentHittable(GameObject table, GameObject ball){
		BoundingBox bounds = table.getBoundingBox();
		return ball.getPosition().z >= bounds.max.x;
	}
	
	/**
	 * Tell if the table and the ball has been loaded in the scene.
	 * @param instances
	 */
	public static boolean allObjectsLoaded(Array<GameObject> instances){
		return instances.size == 2;
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
	public static boolean fullscreen(){
		return Gdx.app.getPreferences(SpaceTennis3D.TITLE).getBoolean("fullscreen");
	}
	
	/**
	 * @return if music has been enabled.
	 */
	public static boolean music(){
		return Gdx.app.getPreferences(SpaceTennis3D.TITLE).getBoolean("music");
	}
	
	/**
	 * @return if sound has been enabled.
	 */
	public static boolean sound(){
		return Gdx.app.getPreferences(SpaceTennis3D.TITLE).getBoolean("sound");
	}
	
	// BLUETOOTH
	
	public static byte[] toByteArray(Object obj)
	{
	    byte[] bytes = null;
	    ObjectOutputStream oos = null;
	    ByteArrayOutputStream bos = null;

	    try 
	    {
	        bos = new ByteArrayOutputStream();
	        oos = new ObjectOutputStream(bos);
	        oos.writeObject(obj);
	        return bos.toByteArray();
	    }
	    catch(Exception e)
	    {
	        System.out.println("Bluetooth. Cast exception at sending: " + e.getMessage());
	        Log.error("Bluetooth. Cast exception at sending: " + e.getMessage());
	    }

	    return bytes;
	}
	
	public static Object toObject(byte[] bytes) 
	{
	    Object obj = null;
	    ObjectInputStream ois = null;

	    try 
	    {
	        ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
	        return ois.readObject();
	    } 
	    catch(Exception e)
	    {
	    	System.out.println("Bluetooth. Cast exception at receiving: " + e.getMessage());
	        Log.error("Bluetooth. Cast exception at receiving: " + e.getMessage());
	    }

	    return obj;
	}
}
