package tennis.objects;

import com.badlogic.gdx.math.Vector3;

public class Opponent {
	private Vector3 lastHit;
	private float velocity;
	private Difficulty difficulty;
	
	public Opponent(Difficulty difficulty){
		lastHit = new Vector3();
		switch (difficulty){
		case EASY:
			velocity = 1;
			break;
		case MEDIUM:
			velocity = 2;
			break;
		case HARD:
			velocity = 3;
			break;
		}
	}

	public Opponent(Vector3 lastHit, float velocity, Difficulty difficulty) {
		super();
		this.lastHit = lastHit;
		this.velocity = velocity;
		this.difficulty = difficulty;
	}

	public Vector3 getLastHit() {
		return lastHit;
	}

	public void setLastHit(Vector3 lastHit) {
		this.lastHit = lastHit;
	}

	public float getVelocity() {
		return velocity;
	}

	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	@Override
	public String toString() {
		return "Opponent [getLastHit()=" + getLastHit() + ", getVelocity()="
				+ getVelocity() + ", getDifficulty()=" + getDifficulty() + "]";
	}
	
	

}
