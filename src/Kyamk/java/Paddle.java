package Kyamk.java;

/* Paddle sisältää vain välttämättömät koordinaattien haut. */

public class Paddle {

	protected static int WIDTH = 80;
	protected static int HEIGHT = 20;
	
	private int xPosition;
	private int yPosition;
	
	public int getxPosition() {
		return xPosition;
	}
	
	public void setxPosition(int xPosition) {
		this.xPosition = xPosition;
	}
	
	public int getyPosition() {
		return yPosition;
	}
	
	public void setyPosition(int yPosition) {
		this.yPosition = yPosition;
	}
	
}
