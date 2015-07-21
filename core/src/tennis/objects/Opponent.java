package tennis.objects;

import com.badlogic.gdx.math.Vector3;

public class Opponent {
	private Vector3 lastHit;
	private int velocity;
	private float hitRate;
	private Difficulty difficulty;
	
	public Opponent(Difficulty difficulty){
		lastHit = new Vector3();
		switch (difficulty){
		case EASY:
			hitRate = 0.3f;
			velocity = 60;
			break;
		case MEDIUM:
			hitRate = 0.6f;
			velocity = 60;
			break;
		case HARD:
			hitRate = 0.9f;
			velocity = 90;
			break;
		}
	}

	public Opponent(Vector3 lastHit, int velocity, Difficulty difficulty) {
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

	public float getHitRate() {
		return hitRate;
	}

	public void setHitRate(float hitRate) {
		this.hitRate = hitRate;
	}

	public int getVelocity() {
		return velocity;
	}

	public void setVelocity(int velocity) {
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
