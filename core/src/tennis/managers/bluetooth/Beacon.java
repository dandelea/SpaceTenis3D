package tennis.managers.bluetooth;

import java.util.Arrays;

public class Beacon {
	public final static int MAIN_SCREEN = 1;
	public final static int PLAY = 2;
	public final static int SETTINGS = 3;
	public final static int EXIT = 4;
	public final static int PAUSE = 5;
	private BeaconType beaconType;
	private float[] coordinates;
	private Integer nextScreen;

	public Beacon() {
		beaconType = BeaconType.ACCELEROMETER;
	}

	public Beacon(BeaconType beaconType, float[] coordinates, Integer nextScreen) {
		if ( beaconType==null || 
				(beaconType.equals(BeaconType.ACCELEROMETER) && coordinates == null) ||
				(beaconType.equals(BeaconType.SCREEN) && nextScreen == null)){
			throw new IllegalArgumentException();
		}

		this.coordinates = coordinates;
		this.nextScreen = nextScreen;
	}

	public BeaconType getBeaconType() {
		return beaconType;
	}

	public void setBeaconType(BeaconType beaconType) {
		this.beaconType = beaconType;
	}

	public float[] getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(float[] coordinates) {
		this.coordinates = coordinates;
	}

	public Integer getNextScreen() {
		return nextScreen;
	}

	public void setNextScreen(int nextScreen) {
		this.nextScreen = nextScreen;
	}

	
	@Override
	public String toString() {
		String res = null;
		if (beaconType.equals(BeaconType.ACCELEROMETER)) {
			res = "Beacon [Type=" + getBeaconType() + ", Coordinates="
					+ Arrays.toString(getCoordinates()) + "]";
		} else{
			if (beaconType.equals(BeaconType.SCREEN)) {
				res = "Beacon [Type=" + getBeaconType() + ", Next Screen="
						+ getNextScreen() + "]";
			}
		}
		return res;
	}
}
