package minild.game.entity;

import java.util.List;

import minild.game.graphics.Colors;
import minild.game.graphics.Screen;
import minild.game.level.Level;
import minild.game.level.tile.Node;
import minild.game.level.tile.Tile;
import minild.game.util.Vector2i;

public class Zombie extends Mob {
	
	private int color = Colors.get(000, 131, 141, -1);
	private int updateCount = 0;
	
	private List<Node> path = null;

	public Zombie(Level level) {
		super(level, "Zombie", random.nextInt(level.width), random.nextInt(level.height), 1);
		level.addEntity(this);
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
	
	public void update() {
		int xa = 0;
		int ya = 0;
		
		int px = level.entities.get(0).x;
		int py = level.entities.get(0).y; 
		Vector2i start = new Vector2i(x >> 4, y >> 4);
		Vector2i destination = new Vector2i(px >> 4, py >> 4);
		if((px - x) * (px - x) + (py - y) * (py - y) < 100 * 100 && updateCount % 20 == 0) path = level.findPath(start, destination);
			if(path != null) {
			if(path.size() > 0) {
				Vector2i vec = path.get(path.size() - 1).tile;
				if(x < vec.getX() << 4) xa++;
				if(x > vec.getX() << 4) xa--;
				if(y < vec.getY() << 4) ya++;
				if(y > vec.getY() << 4) ya--;
			}
		}
		if (xa != 0 || ya != 0) {
			move(xa, ya);
			isMoving = true;
		} else {
			isMoving = false;
		}
		
		if(level.getTile((this.x >> 4), (this.y >> 4)) == Tile.water) {
			isSwimming = true;
			speed = 1;
		}
		if(isSwimming && level.getTile((this.x >> 4), (this.y >> 4)) != Tile.water) {
			isSwimming = false;
			speed = 1;
		}
		updateCount++;
		if(isSwimming) swimmingCounter++;
	}

	public void render(Screen screen) {
		int xTile = 0;
		int yTile = 28;
		int walkingSpeed = 4;
		int flipTop = (numSteps >> walkingSpeed) & 1;
		int flipBottom = (numSteps >> walkingSpeed) & 1;

		if (movingDir == 1) {
			xTile += 2;
		} else if (movingDir > 1) {
			xTile += 4 + ((numSteps >> walkingSpeed) & 1) * 2;
			flipTop = (movingDir - 1) % 2;
		}

		int modifier = 8;
		int xOffset = x - modifier / 2;
		int yOffset = y - modifier / 2;
		if(isSwimming) {
			int waterColor = 0;
			yOffset += 4;
			if(updateCount % 60 < 15) {
				waterColor = Colors.get(-1, -1, 225, -1);
			} else if(updateCount % 60 >= 15 && updateCount % 60 < 30) {
				waterColor = Colors.get(-1, 225, 115, -1);
			} else if(updateCount % 60 >= 30 && updateCount % 60 < 45) {
				waterColor = Colors.get(-1, 115, -1, 225);
			} else if(updateCount % 60 >= 45 && updateCount % 60 < 60) {
				waterColor = Colors.get(-1, 225, 115, 225);
			}
			
			screen.render(xOffset, yOffset + 3, 0 + 27 * 32, waterColor, (byte) 0x00);
			screen.render(xOffset + 8, yOffset + 3, 0 + 27 * 32, waterColor, (byte) 0x01);
		}
		screen.render(xOffset + (modifier * flipTop), yOffset, xTile + yTile * 32, color, (byte) flipTop);
		screen.render(xOffset + modifier - (modifier * flipTop), yOffset, (xTile + 1) + yTile * 32, color, (byte) flipTop);
		
		if(!isSwimming) {
			screen.render(xOffset + (modifier * flipBottom), yOffset + modifier, xTile + (yTile + 1) * 32, color, (byte) flipBottom);
			screen.render(xOffset + modifier - (modifier * flipBottom), yOffset + modifier, (xTile + 1) + (yTile + 1) * 32, color, (byte) flipBottom);
		}
	}

	public boolean hasCollided(int xa, int ya) {
		int xMin = 0;
		int xMax = 7;
		int yMin = 3;
		int yMax = 10;

		for (int x = xMin; x < xMax; x++) {
			if (isSolidTile(xa, ya, x, yMin)) return true;
		}
		
		for (int x = xMin; x < xMax; x++) {
			if (isSolidTile(xa, ya, x, yMax)) return true;
		}
		
		for (int y = yMin; y < yMax; y++) {
			if (isSolidTile(xa, ya, xMin, y)) return true;
		}
		
		for (int y = yMin; y < yMax; y++) {
			if (isSolidTile(xa, ya, xMax, y)) return true;
		}
		return false;
	}

}
