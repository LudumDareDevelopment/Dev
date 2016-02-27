package minild.game.level.tile;

import minild.game.graphics.Colors;
import minild.game.graphics.Screen;
import minild.game.level.Level;

public class StoneTile extends Tile {

	public StoneTile(int id) {
		super(id);
	}
	
	public void render(int x, int y, Screen screen) {
		int color = Colors.get(Level.stoneColor, Level.stoneColor, Level.stoneColor + 111, Level.stoneColor + 111);
		screen.render(x << 3, y << 3, 4 + 0 * 32, color, (byte) 0x00);
	}
	
	public boolean solid() {
		return true;
	}

}
