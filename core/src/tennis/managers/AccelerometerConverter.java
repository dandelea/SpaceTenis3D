package tennis.managers;


public class AccelerometerConverter {

	public static String toString(float[] accelerometer) {
		return accelerometer[0] + "," + accelerometer[1] + ","
				+ accelerometer[2];
	}

	public static float[] toArray(String s) {
		String[] parts = s.split(",");
		float[] res = { Float.parseFloat(parts[0]), Float.parseFloat(parts[1]),
				Float.parseFloat(parts[2]) };
		return res;
	}
}
