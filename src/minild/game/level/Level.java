package minild.game.level;

import java.util.ArrayList;
import java.util.List;

import minild.game.entity.Entity;
import minild.game.graphics.Screen;
import minild.game.level.tile.Tile;

public class Level {

	public int width, height;
	protected byte[] tiles;
	
	public List<Entity> entities = new ArrayList<Entity>();

	public static int grassColor = 242;
	public static int stoneColor = 333;
	public static int waterColor = 114;
	public static int dirtColor = 322;
	public static int sandColor = 553;
	
	public Level(int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new byte[width * height];
		
		generateLevel();
	}

	protected void generateLevel() {
		tiles = LevelGeneration.createAndValidateTopMap(width, height);
	}

	public void update() {
		for(Entity e : entities) {
			e.update();
		}
	}

	public void render(int xScroll, int yScroll, Screen screen) {
		screen.setOffset(xScroll, yScroll);
		int x0 = xScroll >> 4;
		int x1 = ((xScroll + screen.width) >> 4) + 15;
		int y0 = yScroll >> 4;
		int y1 = ((yScroll + screen.height) >> 4) + 15;

		for (int y = y0; y < y1; y++) {
			for (int x = x0; x < x1; x++) {
				getTile(x, y).render(x, y, screen, this);
			}
		}
	}
	
	public void renderEntities(Screen screen) {
		for(Entity e : entities) {
			e.render(screen);
		}
	}
	
	public Tile getTile(int x, int y) {
		if(x < 0 || x >= width || y < 0 || y >= height) return Tile.water;
		return Tile.tiles[tiles[x + y * width]];
	}

	public void addEntity(Entity entity) {
		this.entities.add(entity);
	}
	
}
