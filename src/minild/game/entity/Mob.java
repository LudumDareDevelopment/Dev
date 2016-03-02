package minild.game.entity;

import minild.game.level.Level;
import minild.game.level.tile.Tile;

public abstract class Mob extends Entity {
	
	protected String name;
	protected int speed;
	protected int numSteps;
	protected boolean isMoving;
	protected int movingDir = 1;
	protected boolean isSwimming = false;
	protected int swimmingCounter = 0;

	public Mob(Level level, String name, int x, int y, int speed) {
		super(level);this.name = name;
		this.x = x;
		this.y = y;
		this.speed = speed;
	}
	
	public boolean findStartPos(Level level) {
		while (true) {
			int x = random.nextInt(level.width);
			int y = random.nextInt(level.height);
			if (level.getTile(x, y) == Tile.grass) {
				this.x = x * 16 + 8;
				this.y = y * 16 + 8;
				return true;
			}
		}
	}
	
	public void move(int xa, int ya) {
		if(isSwimming && swimmingCounter % 2 == 0) return;
		if(xa != 0 && ya != 0) {
			move(xa, 0);
			move(0, ya);
			numSteps--;
			return;
		}
		numSteps++;
		if(!hasCollided(xa, ya)) {
			if(ya < 0) movingDir = 0;
			if(ya > 0) movingDir = 1;
			if(xa < 0) movingDir = 2;
			if(xa > 0) movingDir = 3;
			x += xa * speed;
			y += ya * speed;
		}
	}

	public abstract boolean hasCollided(int xa, int ya);

	protected boolean isSolidTile(int xa, int ya, int x, int y) {
		if(level == null) return false;
		Tile lastTile = level.getTile((this.x + x) >> 4, (this.y + y) >> 4);
		Tile newTile = level.getTile((this.x + x + xa) >> 4, (this.y + y + ya) >> 4);
		if(!lastTile.equals(newTile) && newTile.solid()) return true;
		return false;
	}
	
	public String getName() {
		return name;
	}
}
