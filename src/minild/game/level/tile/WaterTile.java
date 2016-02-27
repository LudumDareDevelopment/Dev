package minild.game.level.tile;

import java.util.Random;

import minild.game.graphics.Colors;
import minild.game.graphics.Screen;
import minild.game.level.Level;

public class WaterTile extends Tile {
	
	private Random waterRandom = new Random();

	public WaterTile(int id) {
		super(id);
	}
	
	public void render(int x, int y, Screen screen) {
		waterRandom.setSeed((updateCount + (x / 2 - y) * 4311) / 10 * 54687121l + x * 3271612l + y * 3412987161l);
		int color = Colors.get(Level.waterColor, Level.waterColor, Level.waterColor + 111, Level.waterColor + 111);
		screen.render(x << 3, y << 3, waterRandom.nextInt(4), color, (byte) 0x00);
	}
	
	public boolean solid() {
		return false;
	}
	
}
