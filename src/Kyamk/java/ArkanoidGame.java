package Kyamk.java;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class ArkanoidGame implements ActionListener, MouseMotionListener, MouseListener, KeyListener {
	
	/*---------------------------------------------------------
	 * Arkan [Java / 2011 / Stavelon]
	 * 
	 * Hyvin yksinkertainen Breakout/Arkanoid-klooni. Peli koostuu 
	 * kolmesta erillisest� tasosta, joiden j�lkeen peli on l�p�isty
	 * onnistuneesti. El�mien (3) loppuessa pelaajalle tuodaan esiin
	 * kuolinruutu. 
	 * 
	 * Graafinen toteutus on tehty vierell� olleen sekuntikellon
	 * mukaan 51 minuutissa ja 32 sekunnissa. Antaa siis ymm�rt��,
	 * ett� parempaakin olisi luvassa, jos pelintekoon keskityt��n
	 * hyviss� ajoin. 
	 * 
	 * [Tulevia muutoksia:]
	 * - Kielet (Englanti, Ven�j�)
	 * - Tarina (Spacemarines)
	 * - Yl�palkin t�ydennys ja pistelasku.
	 * - Graafista parannusta
	 * 
	 * Special thanks to JH and KK.
	 ----------------------------------------------------------*/
	
	
	/*-----------------------
	  Pysyv�t kokomuuttujat
	-----------------------*/
	private static final int GA_WIDTH = 600;
	private static final int GA_HEIGHT = 500;
	
	// Pelin tilat
	protected GameStatus Status;
	
	/*------------
	  Muuttujat
	------------*/
	private JLabel status = null;
	private PlayArea area = null;
	
	JMenuBar Jm_Bar = new JMenuBar();
	JMenu Jm_File = new JMenu("Tiedosto");
	JMenu Jm_Info = new JMenu("Info");
	JMenuItem Jm_Close = new JMenuItem("Sulje");
	JMenuItem Jm_Developer = new JMenuItem("Tekij�");
	
	protected Paddle paddle = null;
	protected Ball ball = null;
	protected Tile tile = null;
	protected Timer timer = null;

	protected Level level = null;
	
	protected int GameStatus = -1;
	protected int numLives = 3;
	protected int check = 0;
	
	/*-------------
	  Main-metodi
	-------------*/
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater ( 
				new Runnable() {
					public void run() {
						createAndShowGUI();
					}
				}
		);
	}	
	
	/*------------------------------------------------------
	  K�ytt�liittym�, kutsutaan metodit muista funktioista
	------------------------------------------------------*/
	private static void createAndShowGUI() {
		
		initLookAndFeel();
		
		ArkanoidGame game = new ArkanoidGame(); // Luodaan sovellus-olio
		
		JFrame frame = new JFrame("Arkan [Java]");
		frame.getContentPane().add(game.createComponents());
		
		javax.swing.SwingUtilities.updateComponentTreeUI(frame);
		
		// Ikkunan keskitys ruudun mukaan
		Dimension cScreen = Toolkit.getDefaultToolkit().getScreenSize();
		
		int korkeus = cScreen.height;
		int leveys = cScreen.width;
		
		frame.setSize(leveys / 2, korkeus / 2);
		frame.setLocationRelativeTo(null);
		
		// N�ytt� esiin
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	/*----------------
	  Ikkunan kuori
	----------------*/
	private static void initLookAndFeel() {
		
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				System.out.println(info.getName());
				if (info.getName().equals("Nimbus")) {
					UIManager.setLookAndFeel(info.getClassName());
				}
			}
		}
		catch (Exception e) {
			// Set UI for another if Nimbus not available.
			}
	}
	
	/*-------------------
	  Ikkuna
	-------------------*/
	private JPanel createComponents() {
		
		JPanel paneeli = new JPanel(new BorderLayout());
		
		// Laaditaan valinnat menua varten.
		Jm_Bar.add(Jm_File);
			Jm_File.add(Jm_Close);
				Jm_Close.addActionListener(this);
				
		Jm_Bar.add(Jm_Info);
			Jm_Info.add(Jm_Developer);
				Jm_Developer.addActionListener(this);
				
		paneeli.add(Jm_Bar, BorderLayout.PAGE_START);
		
		// Luodaan pelikentt�
		level = new Level(this);
		
		// Luodaan lautapaddle
		paddle = new Paddle();
		paddle.setxPosition(GA_WIDTH / 2 - Paddle.WIDTH / 2);
		paddle.setyPosition(GA_HEIGHT - Paddle.HEIGHT - 35);
		
		// Luodaan pallontapainen
		ball = new Ball(this);
		
		// Luodaan pelattava alue
		area = new PlayArea(this, GA_WIDTH, GA_HEIGHT);
		area.addMouseMotionListener(this);
		area.addMouseListener(this);
		paneeli.add(area, BorderLayout.CENTER);
		
		// Luodaan koordinaatiopalkki
		status = new JLabel("Status: ");
		paneeli.add(status, BorderLayout.PAGE_END);
		
		// Luodaan ajastin palloa varten
		Status = new GameStatus();
		timer = new Timer(20, this);
		
		return paneeli;
		}
	
	/*------------------
	  Lataa grafiikan
	------------------*/
	
	public static BufferedImage loadImage(String filename) {
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File(filename));
		}
		catch (IOException e) {
			System.out.println("Ei voida lukea tiedostoa: \"" + filename + "\"");
		}
		
		return img;
	}
	
	/*---------------
	  Lukee osumat
	---------------*/
	public void checkCollisions() {
	
		// Yl�reunan t�rm�ys
		if (ball.getyPosition() - Ball.RADIUS <= 0 && ball.isMovingUp()) {
			ball.collisionVert();
		}
	 
		// Alareunan t�rm�ys
		if (ball.getyPosition() + Ball.RADIUS >= GA_HEIGHT && !ball.isMovingUp()) {
			if (numLives == 0) {
			Status.gameOver();
			}
			
			else {
				numLives--;
				Status.Begin();
			}
			
			timer.stop();
			area.repaint();
			ball.reset();
		}
	 
		// Vasemman reunan t�rm�ys
		if (ball.getxPosition() - Ball.RADIUS <= 0 && ball.isMovingLeft()) {
			ball.collisionHori();
		}
	 
		// Oikean reunan t�rm�ys
		if (ball.getxPosition() + Ball.RADIUS >= GA_WIDTH && !ball.isMovingLeft()) {
			ball.collisionHori();
		}
	 
		// Paddlen t�rm�ys
		if (ball.getxPosition() >= paddle.getxPosition() && ball.getxPosition()
			<=  paddle.getxPosition() + Paddle.WIDTH) 
		{
			if (ball.getyPosition() + Ball.RADIUS >= paddle.getyPosition() && ball.isMovingDown()) {
				ball.collisionVert();
			}
		}
	}
	
	/*---------------
	  Hiirenlukija
	---------------*/
	public void mouseDragged(MouseEvent e) {
	}
	
	// Aseta pallo paddlen ja hiiren johdolla.
	public void mouseMoved(MouseEvent me) {
		
		if (Status.isBegin()) {
			ball.reset();
		}
		
		this.paddle.setxPosition(me.getX() - Paddle.WIDTH /2);
		
		area.repaint();
		
		status.setText("Coords. [x,y]: " + me.getX() + "," + me.getY());
	}

	/*------------------------------------------
	  Lukee osumat ja pit�� pallon liikkeess�
	------------------------------------------*/
	public void actionPerformed(ActionEvent ae) {	
		
		 // T�rm�ystunnistus
		 if (ae.getSource() == timer) {	
			 checkCollisions();
			 ball.updatePosition();
			 area.repaint();
		 }
		
		 // Tiedoston alaiset valinnat.
		 else if (ae.getActionCommand().equals("Sulje")) {
			 System.exit(0);
		 }
		 
		 // Infon alaiset valinnat.
		 else if (ae.getActionCommand().equals("Tekij�")) {
			 try {
				String url = "http://www2.kyamk.fi/~ati0jusi/";
				java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
			 }
			 catch (java.io.IOException e){
				System.out.println(e.getMessage());
			 } 
		 }
		 
		 // Aiheutetaan vahinkoa tiilelle osuttaessa.
		 for (Tile tile : level.tiles) {
			 tile.checkHit(ball);
		 }
		 
		 statusCheck();
	}
	
	/*----------------------------------------------
	  Katsoo tiilet ja siirtyy seuraavaan kentt��n
	----------------------------------------------*/
	
	public void statusCheck() {
		if (level.isClear() && !Status.isComplete()) {
			timer.stop();
			ball.reset();
			Status.Begin();
			level.nextLevel();
		}
		
		if (numLives == 0) {
			numLives = 3;
			level.resetLevel();
			Status.gameOver();
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent arg0) {	
	}

	// Alla olevat keyTypet eiv�t ole toiminnassa. Mahdollisesti my�hempi� versioita varten.
	@Override
	public void keyTyped(KeyEvent ke) {
		if (ke.getKeyChar() == 'U' || ke.getKeyChar() == 'u') {
			Status.Begin();
			area.repaint();
		}
		
		if (ke.getKeyChar() == 'P' || ke.getKeyChar() == 'p') {
			if (Status.isPause()) {
				timer.stop();
				Status.Pause();
			}
			else {
				timer.start();
				Status.Play();
			}
		}
		
		if (ke.getKeyChar() == 'Q' || ke.getKeyChar() == 'q') {
			System.exit(0);
		}
	}

	// Hiirell� suoritettavat k�skyt.
	public void mouseClicked(MouseEvent me) {
		if (me.getButton() == MouseEvent.BUTTON1) {
			{
				if(Status.isBegin()) {
					Status.Play();
					timer.start();
				}
				
				else if (Status.isMain()) {
					Status.Begin();
					area.repaint();
				}
				
				else if (Status.isComplete()) {
					Status.Main();
					numLives = 3;
					area.repaint();
				}
				
				else if (Status.isGameOver()) {
					Status.Main();
					numLives = 3;
					area.repaint();
				}
			}
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {	
	}
	@Override
	public void mouseExited(MouseEvent e) {	
	}
	@Override
	public void mousePressed(MouseEvent e) {
	}
	@Override
	public void mouseReleased(MouseEvent e) {
	}
	
}
