package minild.game.level.tile;

import java.util.Random;

import minild.game.graphics.Colors;
import minild.game.graphics.Screen;
import minild.game.level.Level;

public class WaterTile extends Tile {
	
	private Random waterRandom = new Random();

	public WaterTile(int id) {
		super(id);
		connectsToSand = true;
		connectsToWater = true;
	}
	
	public void render(int x, int y, Screen screen, Level level) {
		waterRandom.setSeed((updateCount + (x / 2 - y) * 4311) / 10 * 54687121l + x * 3271612l + y * 3412987161l);
		int color = Colors.get(Level.waterColor, Level.waterColor, Level.waterColor + 111, Level.waterColor + 111);
		int transitionColor1 = Colors.get(3, Level.waterColor, Level.dirtColor - 111, Level.dirtColor);
		int transitionColor2 = Colors.get(3, Level.waterColor, Level.sandColor - 110, Level.sandColor);

		boolean u = !level.getTile(x, y - 1).connectsToWater;
		boolean d = !level.getTile(x, y + 1).connectsToWater;
		boolean l = !level.getTile(x - 1, y).connectsToWater;
		boolean r = !level.getTile(x + 1, y).connectsToWater;

		boolean su = u && level.getTile(x, y - 1).connectsToSand;
		boolean sd = d && level.getTile(x, y + 1).connectsToSand;
		boolean sl = l && level.getTile(x - 1, y).connectsToSand;
		boolean sr = r && level.getTile(x + 1, y).connectsToSand;

		if (!u && !l) {
			screen.render(x * 16 + 0, y * 16 + 0, waterRandom.nextInt(4), color, (byte) waterRandom.nextInt(4));
		} else
			screen.render(x * 16 + 0, y * 16 + 0, (l ? 14 : 15) + (u ? 0 : 1) * 32, (su || sl) ? transitionColor2 : transitionColor1, (byte) 0);

		if (!u && !r) {
			screen.render(x * 16 + 8, y * 16 + 0, waterRandom.nextInt(4), color, (byte) waterRandom.nextInt(4));
		} else
			screen.render(x * 16 + 8, y * 16 + 0, (r ? 16 : 15) + (u ? 0 : 1) * 32, (su || sr) ? transitionColor2 : transitionColor1, (byte) 0);

		if (!d && !l) {
			screen.render(x * 16 + 0, y * 16 + 8, waterRandom.nextInt(4), color, (byte) waterRandom.nextInt(4));
		} else
			screen.render(x * 16 + 0, y * 16 + 8, (l ? 14 : 15) + (d ? 2 : 1) * 32, (sd || sl) ? transitionColor2 : transitionColor1, (byte) 0);
		if (!d && !r) {
			screen.render(x * 16 + 8, y * 16 + 8, waterRandom.nextInt(4), color, (byte) waterRandom.nextInt(4));
		} else
			screen.render(x * 16 + 8, y * 16 + 8, (r ? 16 : 15) + (d ? 2 : 1) * 32, (sd || sr) ? transitionColor2 : transitionColor1, (byte) 0);
	}
	
	public boolean solid() {
		return false;
	}
	
}
