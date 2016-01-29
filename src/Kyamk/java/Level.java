package Kyamk.java;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

public class Level {

	// Muuttujat tason vaihdolle.
	protected int nowLevel = 0;
	protected int maxLevel = 3;
	protected Vector<Tile> tiles = new Vector<Tile>();
	
	ArkanoidGame mygame = null;
	
	public Level(ArkanoidGame mygame) {
		super();
		this.mygame = mygame;
		nextLevel();
	}
	
	// Lataa levelin tekstitiedostosta (menee Level-luokkaan)
	public void loadTextFile(File file) throws IOException {
	FileInputStream fin = new FileInputStream(file);

	int c;
	int blanks_x = 0, blanks_y = 0;
	int x = 0, y = 0;
	double max_w = 0.0, max_h = 0.0;

	while ((c = fin.read()) != -1) {
		Tile tile = null;

		switch ((char)c) {

		case '\n':
			x = 0;
			++blanks_y;
			continue;

		case '.':
			++blanks_x;
			continue;

		case '0':
			tile = createBasicTile(1);
			break;

		case '1':
			tile = createBasicTile(1);
			break;

		case '2':
			tile = createBasicTile(2);
			break;

		case '3':
			tile = createBasicTile(3);
			break;

		default:
			continue; // Unknown character.
	}

	max_w = Math.max(max_w, tile.getWidth());
	max_h = Math.max(max_h, tile.getHeight());

	x += blanks_x * (max_w + 1);
	y += blanks_y * (max_h + 1);

	tile.setxPosition(x);
	tile.setyPosition(y);

	x += max_w + 1;

	tiles.add(tile);
	blanks_x = 0;
	blanks_y = 0;
	}

	fin.close();
	}

	// Vaihda tasoa.
	public void nextLevel() {
		nowLevel++;
		if (nowLevel > maxLevel) {
			mygame.Status.Complete();
			nowLevel = 1;
		}
		
		try {
			loadTextFile(new File("res/lvl/level" + nowLevel + ".lvl"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	// Palauta tasot alkuperäiseen tilaan.
	public void resetLevel() {
		nowLevel = 0;
		for (Tile tile : mygame.level.tiles) {
			tile.setHealth(0);
		}
		nextLevel();
	}
	
	protected Tile createBasicTile (int hit) {
		return new Tile(hit);
	}
	
	// Katsoo tiilien määrän/keston ja niiden mukaan lopulta toteaa
	// kentän päättyneeksi - kun jäljellä olevia arvoja [3,2,1] on yhtä kuin nolla.
	public boolean isClear() {
		int i = 0;
		for (Tile tile : mygame.level.tiles) {
			if (tile.getHealth() > 0) {
				i++;
			}
		}
		if (i > 0) {
			return false;
		} else {
			return true;
		}
	}
}