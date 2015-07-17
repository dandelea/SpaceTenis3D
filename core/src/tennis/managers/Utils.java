package tennis.managers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import tennis.SpaceTennis3D;
import tennis.screens.scenes3d.GameScreen3.GameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Utils {
	
	private static float[] playerHitZoneZ = {-1.1f, 0};
	
	/**
	 * Dice si esta en la primera mitad de la tabla del jugador.
	 * 
	 * @param table
	 * @param ball
	 * @return
	 */
	public static boolean onPlayerSide(GameObject table, GameObject ball) {
		boolean res;
		Vector3 ballPosition = ball.getPosition();
		res = ballPosition.z < 0;
		return res;
	}
	
	public static boolean onPlayerHittable(GameObject table, GameObject ball) {
		boolean res;
		Vector3 ballPosition = ball.getPosition();
		res = ballPosition.z > playerHitZoneZ[0] && ballPosition.z < playerHitZoneZ[1];
		return res;
	}
	
	public static boolean allObjectsLoaded(Array<GameObject> instances){
		return instances.size == 2;
	}
	
	/** @return if vSync is enabled */
	public static boolean vSync() {
		return Gdx.app.getPreferences(SpaceTennis3D.TITLE).getBoolean("vsync");
	}
	
	public static boolean fullscreen(){
		return Gdx.app.getPreferences(SpaceTennis3D.TITLE).getBoolean("fullscreen");
	}
	
	public static boolean music(){
		return Gdx.app.getPreferences(SpaceTennis3D.TITLE).getBoolean("music");
	}
	
	public static boolean sound(){
		return Gdx.app.getPreferences(SpaceTennis3D.TITLE).getBoolean("sound");
	}
	
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
	        System.out.println("Bluetooth. Cast exception at sending end ...");
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
	    catch(Exception ex)
	    {
	        System.out.println("Bluetooth. Cast exception at receiving end ...");
	    }

	    return obj;
	}

}
