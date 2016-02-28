package minild.game.level.tile;

import minild.game.graphics.Colors;
import minild.game.graphics.Screen;
import minild.game.level.Level;

public class GrassTile extends Tile {

	public GrassTile(int id) {
		super(id);
	}
	
	public void render(int x, int y, Screen screen, Level level) {
		int color = Colors.get(Level.grassColor, Level.grassColor, Level.grassColor + 111, Level.grassColor + 111);
		screen.render(x << 4, y << 4, 0 + 0 * 32, color, (byte) 0x00);
		screen.render((x << 4) + 8, y << 4, 0 + 0 * 32, color, (byte) 0x00);
		screen.render(x << 4, (y << 4) + 8, 0 + 0 * 32, color, (byte) 0x00);
		screen.render((x << 4) + 8, (y << 4) + 8, 0 + 0 * 32, color, (byte) 0x00);
	}
	
	public boolean solid() {
		return false;
	}

}
