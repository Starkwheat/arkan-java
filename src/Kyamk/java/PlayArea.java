package Kyamk.java;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PlayArea extends JPanel {
	
	ArkanoidGame mygame = null;
	
	// Pelin graafiset resurssit. Taulukointia voidaan käyttää eri pelistatuksiin.
	private BufferedImage imgBall = null;
	private BufferedImage imgPaddle = null;
	private BufferedImage imgLives = null;
	private BufferedImage imgTile = null;
	private BufferedImage[] imgTileOver = new BufferedImage[4]; 
	
	private BufferedImage[] imgBg = new BufferedImage[5];
	
	boolean imgError = true;
	
	private static int WIDTH = 600;
	private static int HEIGHT = 500;
	
	public PlayArea(ArkanoidGame game, int WIDTH, int HEIGHT) {
		super();
		this.mygame = game;
		
		// Hakee ohjelman käyttämät spritet kansiosta "res/img"
		imgBall = ArkanoidGame.loadImage("res/img/ball.png");
		imgPaddle = ArkanoidGame.loadImage("res/img/paddle.png");
		imgTile = ArkanoidGame.loadImage("res/img/tile.png");
		imgLives = ArkanoidGame.loadImage("res/img/life.png");
		imgBg[1] = ArkanoidGame.loadImage("res/img/bg_Intro.png");
		imgBg[2] = ArkanoidGame.loadImage("res/img/bg_Playing.png");
		imgBg[3] = ArkanoidGame.loadImage("res/img/bg_Complete.png");
		imgBg[4] = ArkanoidGame.loadImage("res/img/bg_GameOver.png");
 		
		imgTileOver[1] = ArkanoidGame.loadImage("res/img/tile.png");
		imgTileOver[2] = ArkanoidGame.loadImage("res/img/tOver1.png");
		imgTileOver[3] = ArkanoidGame.loadImage("res/img/tOver2.png");
		
		// Virheentunnistus puuttuville tiedostoille
		if ((new File("res/img/")).exists())
		{
			imgError = false;
		}
	}
	
	// Kentän pituudet leveyden ja korkeuden mukaisesti.
	public Dimension getPreferredSize() {
		return new Dimension(PlayArea.WIDTH, PlayArea.HEIGHT);
	}	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Graphics2D:n hyödyntäminen käyttöön.
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH - 1, HEIGHT - 1);
	
		// 0 = Oletustausta. Asetetaan taustavakioksi aina Statustilan mukainen kuva.
		if (!imgError) 
		{
			if (mygame.Status.isMain()) {
				imgBg[0] = imgBg[1];
			}
			else if (mygame.Status.isBegin()) {
				imgBg[0] = imgBg[2];
			}
			else if (mygame.Status.isComplete()) {
				imgBg[0] = imgBg[3];
			}
			else if (mygame.Status.isGameOver()) {
				imgBg[0] = imgBg[4];
			}
			
			g2.drawImage
				(imgBg[0],
				(WIDTH - imgBg[0].getWidth()) / 2,
				(HEIGHT - imgBg[0].getHeight()) / 2,
				null);
		}
		
		// Valikkoruutu
		if (mygame.Status.isMain()) {
			g2.drawImage
				(imgBg[1],
				(WIDTH - imgBg[1].getWidth()) / 2,
				HEIGHT,
				null);
		}
		
		// Läpäisyruutu
		else if (mygame.Status.isComplete()) {
			g2.drawImage
			(imgBg[3],
			(WIDTH - imgBg[2].getWidth()) / 2,
			(HEIGHT - imgBg[2].getHeight()) / 2,
			null);
		}
	
		// Kuolinruutu
		else if (mygame.Status.isGameOver()) {
			g2.drawImage
			(imgBg[4],
			(WIDTH - imgBg[2].getWidth()) / 2,
			(HEIGHT - imgBg[2].getHeight()) / 2,
			null);
		}
		
		// Muut ruudut
		if (mygame.Status.isBegin() || mygame.Status.isPlay() || mygame.Status.isPause()) {
			for (int i = 0; i < mygame.numLives; i++) {
				if (imgError) {
					g.setColor(Color.WHITE);
					g.fillOval(WIDTH - 80 - i * 50, 30, 30, 30); 
				}
				else {
					g2.drawImage
					(imgLives,
					WIDTH - 50 - i * 40, 
					30, 
					null);
				}
			}
		}
		
		// Mailan piirto //
		///////////////////
		
		// Eliminoitu &&-käskyillä objektien ylivuoto muihin tiloihin.
		if (mygame.paddle != null && !mygame.Status.isMain() && !mygame.Status.isComplete() && !mygame.Status.isGameOver()) {
			if (imgError) {
				g.setColor(Color.WHITE);
				g.fillRect(mygame.paddle.getxPosition(), mygame.paddle.getyPosition(),
				Paddle.WIDTH, Paddle.HEIGHT);
				}
			
			else {
				g2.drawImage(imgPaddle, mygame.paddle.getxPosition(), 
				mygame.paddle.getyPosition(), null);
			}
		}
		
		   ///////////////
		// Pallon piirto //
		 ///////////////
		
		if (mygame.ball != null && !mygame.Status.isMain() && !mygame.Status.isComplete() && !mygame.Status.isGameOver()) {
			if (imgError) {
				g.setColor(Color.WHITE);
				g.fillOval(mygame.ball.getxPosition() - Ball.RADIUS, 
				mygame.ball.getyPosition() - Ball.RADIUS,
				Ball.RADIUS * 2, 
				Ball.RADIUS * 2);
			}
		
		else {
			g2.drawImage(imgBall, 
			mygame.ball.getxPosition() - Ball.RADIUS, 
			mygame.ball.getyPosition() - Ball.RADIUS, 
			null);
		}
		}
		
		// Tiilien luonti.
		for (Tile tile : mygame.level.tiles) {
			if (tile != null && !mygame.Status.isMain() && !mygame.Status.isComplete() && !mygame.Status.isGameOver()) {
				if (tile.getHealth() > 0) {
					switch (tile.getHealth()) {
					case 2:
						g.setColor(Color.YELLOW);
						break;
					case 1:
						g.setColor(Color.RED);
						break;
					default:
						g2.setColor(Color.GREEN);
					}
					
					if (imgError) {
						g.fillRect(
							tile.getxPosition(), 
							tile.getyPosition(), 
							Tile.WIDTH,
							Tile.HEIGHT);
					} 
					
					else {
						g2.drawImage(
							imgTile,
							tile.getxPosition(),
							tile.getyPosition(),
							null
						);
					}
					
					// Laaditaan uusi tiilenpinta kestävämmille tapauksille.
					if (tile.getHealth() >= 1 && tile.getHealth() <= 3) {
							g2.drawImage (
								imgTileOver[tile.getHealth()],
								tile.getxPosition(),
								tile.getyPosition(),
								null
							);
					}
				}
			}
		}	
	
	}
}
