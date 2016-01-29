// Hirvosen avustuksella luotu. Kiitokset hänelle.

package Kyamk.java;

public class GameStatus {
	private enum Status { MAIN, BEGIN, PLAY, PAUSE, COMPLETE, GAMEOVER };
	private Status activeStatus;
	
	public GameStatus() {
		activeStatus = Status.MAIN;
	}
	
	public void Main() {
		activeStatus = Status.MAIN;
	}
	
	public void Begin() {
		activeStatus = Status.BEGIN;
	}
	
	public void Play() {
		activeStatus = Status.PLAY;
	}
	
	public void Pause() {
		activeStatus = Status.PAUSE;
	}
	
	public void Complete() {
		activeStatus = Status.COMPLETE;
	}
	
	public void gameOver() {
		activeStatus = Status.GAMEOVER;
	}
	
	// Tunnistetaan tilan totuusarvo - eli onko NYT voimassa?
	public boolean isMain () {
		return (activeStatus == Status.MAIN ? true : false);
	}
	
	public boolean isBegin () {
		return (activeStatus == Status.BEGIN ? true : false);
	}
	
	public boolean isPlay() {
		return (activeStatus == Status.PLAY ? true : false);
	}
	
	public boolean isPause() {
		return (activeStatus == Status.PAUSE ? true : false);
	}
	
	public boolean isComplete() {
		return (activeStatus == Status.COMPLETE ? true : false);
	}
	
	public boolean isGameOver() {
		return (activeStatus == Status.GAMEOVER ? true : false);
	}
}
