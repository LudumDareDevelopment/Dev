package minild.game.level.tile;

import minild.game.graphics.Screen;
import minild.game.level.Level;

public class Tile {
	
	public static int updateCount = 0;
	
	public static Tile[] tiles = new Tile[256];
	public static GrassTile grass = new GrassTile(0);
	public static StoneTile stone = new StoneTile(1);
	public static WaterTile water = new WaterTile(2);
	
	public final byte id;
	
	public boolean connectsToGrass = false;
	public boolean connectsToSand = false;
	public boolean connectsToLava = false;
	public boolean connectsToWater = false;
	
	public Tile(int id) {
		this.id = (byte) id;
		if(tiles[id] != null) System.err.println("Duplicate tile id at: " + id);
		tiles[id] = this;
	} 
	
	public void render(int x, int y, Screen screen, Level level) {
	}
	
	public boolean solid() {
		return false;
	}

}
