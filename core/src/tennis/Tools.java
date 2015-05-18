package tennis;

import tennis.managers.Log;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public class Tools {
	
	// PRINT
	
	public static void print(Vector3 vec) {
		print(vec, null);
	}
	
	public static void print(Vector3 vec, String name) {
		System.out.println(Tools.fmt(vec, name));
	}
	
	public static String fmt(Vector3 vec, String name) {
		if (name == null) name = "";
		return String.format("(V3) %s x: %.3f, y: %.3f, z: %.3f", name, vec.x, vec.y, vec.z);
	}
	
	public static String fmt(float f, String name) {
		if (name == null) name = "";
		return String.format("%s: %.3f", name, f);
	}

	public static String fmt(float f) {
		return fmt(f, null);
	}

	public static void print(float f) {
		print(f, null);
	}

	public static void print(float f, String name) {
		System.out.println(fmt(f, name));
	}

	// THREADS
	
	public static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// DISPOSE
	
	public static void dispose(Disposable disp) {
		dispose(disp, "");
	}

	public static void dispose(Disposable disp, String name) {
		Log.debug("dispose: " + name + " -- " + disp);
		disp.dispose();
	}
	
	
	// COLORS
	
	public static Color randomColor() {
		return randomColor(0f, 1f);
	}
	
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

	public static void rotateAround(Vector3 position, Vector3 axis, float angle) {
		q.setFromAxis(axis, angle);
		mtx.set(q);
		position.prj(mtx);
	}
}
