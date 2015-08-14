package tennis.objects;

import tennis.managers.Soundbox;

/**
 * Manages the tennis game scoreboard
 * @author Daniel de los Reyes Leal
 * @version 1
 */
public class Scoreboard {
	private int score1;
	private int score2;
	private int set;
	private boolean isAdvantaged1;
	private boolean isAdvantaged2;
	private boolean finished;
	private int[] sets;
	private static final int MAX_SETS = 3;

	public Scoreboard() {
		score1 = score2 = 0;
		set = 1;
		isAdvantaged1 = isAdvantaged2 = finished = false;
		sets = new int[MAX_SETS];
	}

	public Scoreboard(int score1, int score2, int set, boolean isAdvantaged1,
			boolean isAdvantaged2, boolean finished) {
		super();
		this.score1 = score1;
		this.score2 = score2;
		this.set = set;
		this.isAdvantaged1 = isAdvantaged1;
		this.isAdvantaged2 = isAdvantaged2;
		this.finished = finished;
		this.sets = new int[MAX_SETS];
	}

	public int getScore1() {
		return score1;
	}

	public void setScore1(int score1) {
		this.score1 = score1;
	}

	public int getScore2() {
		return score2;
	}

	public void setScore2(int score2) {
		this.score2 = score2;
	}

	public int getSet() {
		return set;
	}

	public void setSet(int set) {
		this.set = set;
	}

	public boolean isAdvantaged1() {
		return isAdvantaged1;
	}

	public void setAdvantaged1(boolean isAdvantaged1) {
		this.isAdvantaged1 = isAdvantaged1;
	}

	public boolean isAdvantaged2() {
		return isAdvantaged2;
	}

	public void setAdvantaged2(boolean isAdvantaged2) {
		this.isAdvantaged2 = isAdvantaged2;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public int getSetsOfPlayer(int player) {
		int res = 0;
		for (int i : sets) {
			if (i == player)
				res++;
		}
		return res;
	}

	/**
	 * @return Winner code for player 1 or player 2.
	 */
	public int getWinner() {
		assert isFinished();

		int count = 1, tempCount;
		int popular = sets[0];
		int temp = 0;
		for (int i = 0; i < (sets.length - 1); i++) {
			temp = sets[i];
			tempCount = 0;
			for (int j = 1; j < sets.length; j++) {
				if (temp == sets[j])
					tempCount++;
			}
			if (tempCount > count) {
				popular = temp;
				count = tempCount;
			}
		}
		return popular;
	}

	/**
	 * @return If game state is deuce.
	 */
	public boolean isDeuce() {
		return getScore1() == getScore2() && getScore1() == 40;
	}

	/**
	 * @param number
	 *            Actual number.
	 * @return Next value of tennis scoreboard.
	 */
	private int nextNumber(int number) {
		int res = -1;
		switch (number) {
		case 0:
			res = 15;
			break;
		case 15:
			res = 30;
			break;
		case 30:
			res = 40;
			break;
		case 40:
			res = 0;
			break;
		}
		return res;
	}

	/**
	 * Player 1 scores.
	 */
	public void point1() {
		if (isDeuce()) {
			if (isAdvantaged1()) {
				sets[getSet() - 1] = 1;
				updateSets();
				Soundbox.play("success");
			} else {
				if (isAdvantaged2()) {
					setAdvantaged2(false);
				} else {
					setAdvantaged1(true);
				}
			}
		} else {
			if (getScore1() == 40) {
				sets[getSet() - 1] = 1;
				updateSets();
				Soundbox.play("success");
			} else {
				setScore1(nextNumber(getScore1()));
			}
		}
	}

	/**
	 * Player 2 scores
	 */
	public void point2() {
		if (isDeuce()) {
			if (isAdvantaged2()) {
				sets[getSet() - 1] = 2;
				updateSets();
				Soundbox.play("error");
			} else {
				if (isAdvantaged1()) {
					setAdvantaged1(false);
				} else {
					setAdvantaged2(true);
				}
			}
		} else {
			if (getScore2() == 40) {
				sets[getSet() - 1] = 2;
				updateSets();
				Soundbox.play("error");
			} else {
				setScore2(nextNumber(getScore2()));
			}
		}
	}

	/**
	 * Auxiliar method. Update scoreboard after a player scores.
	 */
	private void updateSets() {
		// WIN ALL
		if (getSet() == MAX_SETS) {
			setFinished(true);
		} else {
			// WIN A SET
			setSet(getSet() + 1);
			// RESET SCORES
			setScore1(0);
			setScore2(0);
			setAdvantaged1(false);
			setAdvantaged2(false);
		}
	}

}
