package minild.game.entity;

import minild.game.InputHandler;
import minild.game.graphics.Colors;
import minild.game.graphics.Screen;
import minild.game.level.Level;
import minild.game.level.tile.Tile;

public class Player extends Mob {

	private InputHandler input;
	private int color = Colors.get(000, 540, 543, -1);
	protected boolean isSwimming = false;
	private int updateCount = 0;

	public Player(Level level, int x, int y, InputHandler input) {
		super(level, "Player", x, y, 1);
		this.input = input;
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
		if (input.up) ya--;
		if (input.down) ya++;
		if (input.left) xa--;
		if (input.right) xa++;

		if (xa != 0 || ya != 0) {
			move(xa, ya);
			isMoving = true;
		} else {
			isMoving = false;
		}
		
		if(level.getTile((this.x >> 4), (this.y >> 4)) == Tile.water) isSwimming = true;
		if(isSwimming && level.getTile((this.x >> 4), (this.y >> 4)) != Tile.water) isSwimming = false;
		updateCount++;
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
