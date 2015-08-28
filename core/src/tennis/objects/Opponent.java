package tennis.objects;

/**
 * Manages the opponent params and difficulty.
 * 
 * @author Daniel de los Reyes Leal
 * @version 1
 */
public class Opponent {
	private int force;
	private float hitRate;
	private Difficulty difficulty;

	public Opponent(Difficulty difficulty) {
		switch (difficulty) {
		case EASY:
			hitRate = 0.5f;
			force = 50;
			break;
		case MEDIUM:
			hitRate = 0.8f;
			force = 60;
			break;
		case HARD:
			hitRate = 1f;
			force = 70;
			break;
		}
	}

	public Opponent(int force, float hitRate, Difficulty difficulty) {
		super();
		this.force = force;
		this.difficulty = difficulty;
	}

	public float getHitRate() {
		return hitRate;
	}

	public void setHitRate(float hitRate) {
		this.hitRate = hitRate;
	}

	public int getForce() {
		return force;
	}

	public void setForce(int force) {
		this.force = force;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	@Override
	public String toString() {
		return "Opponent [getForce()=" + getForce() + ", getDifficulty()="
				+ getDifficulty() + "]";
	}

}
