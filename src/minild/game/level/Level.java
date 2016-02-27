package minild.game.level;

import minild.game.graphics.Screen;
import minild.game.level.tile.Tile;

public class Level {

	protected int width, height;
	protected byte[] tiles;

	public static int grassColor = 131;
	public static int stoneColor = 333;
	public static int waterColor = 114;

	public Level(int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new byte[width * height];
		
		generateLevel();
	}

	protected void generateLevel() {
		tiles = LevelGeneration.createTopMap(width, height);
	}

	public void update() {
	}

	public void render(int xScroll, int yScroll, Screen screen) {
		screen.setOffset(xScroll, yScroll);
		int x0 = xScroll >> 3;
		int x1 = ((xScroll + screen.width) >> 3) + 8;
		int y0 = yScroll >> 3;
		int y1 = ((yScroll + screen.height) >> 3) + 8;

		for (int y = y0; y < y1; y++) {
			for (int x = x0; x < x1; x++) {
				getTile(x, y).render(x, y, screen);
			}
		}
	}
	
	public Tile getTile(int x, int y) {
		if(x < 0 || x >= width || y < 0 || y >= height) return Tile.water;
		return Tile.tiles[tiles[x + y * width]];
	}
	
}