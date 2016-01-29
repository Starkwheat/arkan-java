package Kyamk.java;

public class Tile {

	private int health;
	
	private int xPosition;
	private int yPosition;
	
	static final int WIDTH = 60;
	static final int HEIGHT = 20;
	
	// Tiilelle osumakerrat. Luetaan Level:ssä.
	public Tile(int health) {
		this.health = health;
	}

	// Elinvoiman muutokseen tarvittavat attribuutit.
	public int getHealth() {
		return health;
	}
	
	public void setHealth(int h) {
		this.health = h;
	}
	
	public void getHit() {
		this.health--;
	}
	
	// Tiilen sijoitus kentälle. Pallon kanssa osumaa varten.
	public int getxPosition() {
		return xPosition;
	}
	
	public int getyPosition() {
		return yPosition;
	}
	
	public void setxPosition(int x) {
		this.xPosition = x;
	}
	
	public void setyPosition(int y) {
		this.yPosition = y;
	}
	
	public int getHeight() {
		return HEIGHT;
	}
	
	public int getWidth() {
		return WIDTH;
	}
	
	// Osumantunnistin!
	public boolean checkHit(Ball ball) {
		if (health > 0) {
			// Osuma ylhäältä
			if (
					ball.getxPosition() >= xPosition &&
					ball.getxPosition() <= xPosition + WIDTH &&
					ball.getyPosition() + Ball.RADIUS >= yPosition &&
					ball.getyPosition() + Ball.RADIUS <= yPosition + HEIGHT &&
					!ball.isMovingUp()
			) {
				ball.collisionVert();
				getHit();
			}
			
			// Osuma oikealta
			if (
					ball.getyPosition() >= yPosition &&
					ball.getyPosition() <= yPosition + HEIGHT	 &&
					ball.getxPosition() - Ball.RADIUS >= xPosition &&
					ball.getxPosition() - Ball.RADIUS <= xPosition + WIDTH &&
					ball.isMovingLeft()
			) {
				ball.collisionHori();
				getHit();
			}
			
			// Osuma alhaalta
			if (
					ball.getxPosition() >= xPosition &&
					ball.getxPosition() <= xPosition + WIDTH &&
					ball.getyPosition() - Ball.RADIUS >= yPosition &&
					ball.getyPosition() - Ball.RADIUS <= yPosition + HEIGHT &&
					ball.isMovingUp()
			) {
				ball.collisionVert();
				getHit();
			}
			
			// Osuma vasemmalta
			if (
					ball.getyPosition() >= yPosition &&
					ball.getyPosition() <= yPosition + HEIGHT &&
					ball.getxPosition() + Ball.RADIUS >= xPosition &&
					ball.getxPosition() + Ball.RADIUS <= xPosition + WIDTH &&
					!ball.isMovingLeft()
			) {
				ball.collisionHori();
				getHit();
			}
		}
		return false;
	}
}
