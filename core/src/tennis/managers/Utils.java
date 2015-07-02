package tennis.managers;

import tennis.SpaceTennis3D;
import tennis.screens.scenes3d.GameScreen3.GameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Utils {
	
	private static float[] playerHitZoneX = {0,1};
	
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
		res = ballPosition.x > 0;
		return res;
	}
	
	public static boolean onPlayerHittable(GameObject table, GameObject ball) {
		boolean res;
		Vector3 ballPosition = ball.getPosition();
		res = ballPosition.x > playerHitZoneX[0] && ballPosition.x < playerHitZoneX[1];
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

}
