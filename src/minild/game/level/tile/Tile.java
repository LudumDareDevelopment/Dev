package minild.game.level.tile;

import minild.game.graphics.Screen;

public class Tile {
	
	public static int updateCount = 0;
	
	public static Tile[] tiles = new Tile[256];
	public static GrassTile grass = new GrassTile(0);
	public static StoneTile stone = new StoneTile(1);
	public static WaterTile water = new WaterTile(2);
	
	public final byte id;
	
	public Tile(int id) {
		this.id = (byte) id;
		if(tiles[id] != null) System.err.println("Duplicate tile id at: " + id);
		tiles[id] = this;
	} 
	
	public void render(int x, int y, Screen screen) {
	}
	
	public boolean solid() {
		return false;
	}

}
