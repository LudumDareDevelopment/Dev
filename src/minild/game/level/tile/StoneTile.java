package minild.game.level.tile;

import minild.game.graphics.Colors;
import minild.game.graphics.Screen;
import minild.game.level.Level;

public class StoneTile extends Tile {

	public StoneTile(int id) {
		super(id);
	}
	
	public void render(int x, int y, Screen screen, Level level) {
		int col = Colors.get(Level.stoneColor, Level.stoneColor, Level.stoneColor + 111, Level.stoneColor + 111);
		int transitionColor = Colors.get(111, 333, 555, Level.dirtColor);
		boolean u = level.getTile(x, y - 1) != this;
		boolean d = level.getTile(x, y + 1) != this;
		boolean l = level.getTile(x - 1, y) != this;
		boolean r = level.getTile(x + 1, y) != this;

		boolean ul = level.getTile(x - 1, y - 1) != this;
		boolean dl = level.getTile(x - 1, y + 1) != this;
		boolean ur = level.getTile(x + 1, y - 1) != this;
		boolean dr = level.getTile(x + 1, y + 1) != this;
		
		if (!u && !l) {
			if (!ul)
				screen.render((x << 4) + 0, (y << 4) + 0, 0, col, (byte) 0);
			else
				screen.render((x << 4) + 0, (y << 4) + 0, 7 + 0 * 32, transitionColor, (byte)3);
		} else
			screen.render((x << 4) + 0, (y << 4) + 0, (l ? 6 : 5) + (u ? 2 : 1) * 32, transitionColor, (byte)3);

		if (!u && !r) {
			if (!ur)
				screen.render((x << 4) + 8, (y << 4) + 0, 1, col, (byte)0);
			else
				screen.render((x << 4) + 8, (y << 4) + 0, 8 + 0 * 32, transitionColor, (byte)3);
		} else
			screen.render((x << 4) + 8, (y << 4) + 0, (r ? 4 : 5) + (u ? 2 : 1) * 32, transitionColor, (byte)3);

		if (!d && !l) {
			if (!dl)
				screen.render((x << 4) + 0, (y << 4) + 8, 2, col, (byte)0);
			else
				screen.render((x << 4) + 0, (y << 4) + 8, 7 + 1 * 32, transitionColor, (byte)3);
		} else
			screen.render((x << 4) + 0, (y << 4) + 8, (l ? 6 : 5) + (d ? 0 : 1) * 32, transitionColor, (byte)3);
		if (!d && !r) {
			if (!dr)
				screen.render((x << 4) + 8, (y << 4) + 8, 3, col, (byte)0);
			else
				screen.render((x << 4) + 8, (y << 4) + 8, 8 + 1 * 32, transitionColor, (byte)3);
		} else
			screen.render((x << 4) + 8, (y << 4) + 8, (r ? 4 : 5) + (d ? 0 : 1) * 32, transitionColor, (byte)3);
	}
	
	public boolean solid() {
		return true;
	}

}
