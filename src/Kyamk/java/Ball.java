package Kyamk.java;

public class Ball {
	
	// Keskipiste
	protected static int RADIUS = 10;
	
	private int xPosition;
	private int yPosition;
	
	private int speedX = 8;
	private int speedY = 8;
	
	// Periytys Paddlen ja A.Gamen kanssa
	public Ball(ArkanoidGame mygame) {
		super();
		this.mygame = mygame;
		reset();
	}
	
	ArkanoidGame mygame = null;
	
	// Ilmoitetaan ohjelmalle pallon sijainti
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
	
	// Pallon liikerata osuman aikana. Mukana runko, jos tahdotaan simuloida vauhdin kasvua.
	public void collisionVert() {
		speedY = -speedY;
			if (speedY < 0 && speedY >= 8) {
				speedY--;
			}
		else {
			speedY++;
		}
	}
	
	public void collisionHori() {
		speedX = -speedX;
		if (speedX < 0 && speedX >= 8) {
				speedX--;
		}
		
		else {
			speedX++;
		}
	}
	
	// Suodattaa näytölle pallon liikkeen ja reagoi osumiin paddlen kanssa.
	public void updatePosition() {
		xPosition += speedX;
		yPosition += speedY;
		
		// Pallo kimpoaa ylös
		if (
				getxPosition() >= mygame.paddle.getxPosition() &&
				getxPosition() <= mygame.paddle.getxPosition() + Paddle.WIDTH &&
				getyPosition() + Ball.RADIUS >= mygame.paddle.getyPosition() &&
				getyPosition() + Ball.RADIUS <= mygame.paddle.getyPosition() + Paddle.HEIGHT &&
				!isMovingLeft()
			) {
				collisionVert();
			}
		
		// Pallo kimpoaa alas
		if (
				getxPosition() >= mygame.paddle.getxPosition() &&
				getxPosition() <= mygame.paddle.getxPosition() + Paddle.WIDTH &&
				getyPosition() - Ball.RADIUS >= mygame.paddle.getyPosition() &&
				getyPosition() - Ball.RADIUS <= mygame.paddle.getyPosition() + Paddle.HEIGHT &&
				!isMovingLeft()
			) {
				collisionVert();
			}
		
		// Pallo kimpoaa vasemmalle
		if (
				getyPosition() >= mygame.paddle.getyPosition() &&
				getyPosition() <= mygame.paddle.getyPosition() + Paddle.HEIGHT &&
				getxPosition() + Ball.RADIUS >= mygame.paddle.getxPosition() &&
				getxPosition() + Ball.RADIUS <= mygame.paddle.getxPosition() + Paddle.WIDTH &&
				!isMovingLeft()
			) {
				collisionHori();
			}
		
		// Pallo kimpoaa oikealle.
		if (
				getyPosition() >= mygame.paddle.getyPosition() &&
				getyPosition() <= mygame.paddle.getyPosition() + Paddle.HEIGHT &&
				getxPosition() - Ball.RADIUS >= mygame.paddle.getxPosition() &&
				getxPosition() - Ball.RADIUS <= mygame.paddle.getxPosition() + Paddle.WIDTH &&
				isMovingLeft()
			) {
				collisionHori();
			}
	}
	
	// Liikkeen tunnistus alkaa
	public boolean isMovingUp() {
		if (speedY < 0) {
			return true;
		}
		return false;
	}
	
	public boolean isMovingDown() {
		return !isMovingUp();
	}
	
	public boolean isMovingLeft() {
		if (speedX <= 0) {
			return true;
		}
		return false;
	}
	
	public boolean isMovingRight() {
		return !isMovingLeft();
	}
	// Liikkeen tunnistus päättyy
	
	// Uusi pallo kentälle vanhan poistuttua.
	public void reset() {
		this.xPosition = mygame.paddle.getxPosition() + Paddle.WIDTH / 2;
		this.yPosition = mygame.paddle.getyPosition() - Ball.RADIUS;
	}
}