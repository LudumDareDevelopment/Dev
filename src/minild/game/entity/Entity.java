package minild.game.entity;

import java.util.Random;

import minild.game.graphics.Screen;
import minild.game.level.Level;

public abstract class Entity {
	
	protected final Random random = new Random();
	public int x, y;
	protected Level level;
	
	public Entity(Level level) {
		this.level = level;
	}
	
	public abstract void update();
	
	public abstract void render(Screen screen);

}
