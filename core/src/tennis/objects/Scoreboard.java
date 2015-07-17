package tennis.objects;

public class Scoreboard {
	private int score1;
	private int score2;
	private int game;
	private int set;
	private boolean isAdvantaged1;
	private boolean isAdvantaged2;
	private boolean finished;
	private int winner;
	private static final int MAX_SETS = 10;
	private static final int MAX_GAMES = 10;
	
	public Scoreboard(){
		score1 = score2 = 0;
		game = set = 1;
		isAdvantaged1 = isAdvantaged2 = finished = false;
		winner = 0;
	}

	public Scoreboard(int score1, int score2, int game, int set,
			boolean isAdvantaged1, boolean isAdvantaged2, boolean finished, Integer winner) {
		super();
		this.score1 = score1;
		this.score2 = score2;
		this.game = game;
		this.set = set;
		this.isAdvantaged1 = isAdvantaged1;
		this.isAdvantaged2 = isAdvantaged2;
		this.finished = finished;
		this.winner = winner;
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

	public int getGame() {
		return game;
	}

	public void setGame(int game) {
		this.game = game;
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
	
	public Integer getWinner() {
		return winner;
	}

	public void setWinner(Integer winner) {
		this.winner = winner;
	}

	public boolean isDeuce(){
		return getScore1() == getScore2() && getScore1() == 40;
	}
	
	public int nextNumber(int number){
		int res = -1;
		switch(number){
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
	
	public void point1(){
		if (isDeuce()) {
			if (isAdvantaged1()){
				updateGamesAndSet();
			} else {
				if (isAdvantaged2()){
					setAdvantaged2(false);
				} else {
					setAdvantaged1(true);
				}
			}
		} else {
			if (getScore1()==40) {
				updateGamesAndSet();
			} else {
				setScore1(nextNumber(getScore1()));
			}
		}
		setWinner(1);
	}
	
	public void point2(){
		if (isDeuce()) {
			if (isAdvantaged2()){
				updateGamesAndSet();
			} else {
				if (isAdvantaged1()){
					setAdvantaged1(false);
				} else {
					setAdvantaged2(true);
				}
			}
		} else {
			if (getScore2()==40) {
				updateGamesAndSet();
			} else {
				setScore2(nextNumber(getScore2()));
			}
		}
		setWinner(2);
	}
	
	public void updateGamesAndSet(){
		// WIN ALL
		if (getSet()==MAX_SETS & getGame()==MAX_GAMES){
			setFinished(true);
		} else{
			// WIN A SET
			if (getGame()==MAX_GAMES){
				setSet(getSet() + 1);
				setGame(1);
			} else {
				// WIN A GAME
				setGame(getGame() + 1);
			}
			// RESET SCORES
			setScore1(0);
			setScore2(0);
			setAdvantaged1(false);
			setAdvantaged2(false);
		}
	}

	@Override
	public String toString() {
		return "Scoreboard [getScore1()=" + getScore1() + ", getScore2()="
				+ getScore2() + ", getGame()=" + getGame() + ", getSet()="
				+ getSet() + ", isAdvantaged1()=" + isAdvantaged1()
				+ ", isAdvantaged2()=" + isAdvantaged2() + ", isFinished()="
				+ isFinished() + ", getWinner()=" + getWinner()
				+ ", isDeuce()=" + isDeuce() + "]";
	}
	
	
	
	
	
	

}
