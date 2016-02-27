package minild.game.graphics;

public class Screen {
	
	public int width, height;
	public int[] pixels;
	
	public static final byte BIT_MIRROR_X = 0x01;
	public static final byte BIT_MIRROR_Y = 0x02;
	
	private SpriteSheet sheet;
	
	public int xOffset, yOffset;
	
	public Screen(int width, int height, SpriteSheet sheet) {
		this.sheet = sheet;
		this.width = width;
		this.height = height;
		
		pixels = new int[width * height]; 
	}
	
	public void clear(int color) {
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = color;
		}
	}
	
	public void render(int xPos, int yPos, int tile, int colors, byte mirror) {
		xPos -= xOffset;
		yPos -= yOffset;
		
		int xTile = tile % 32;
		int yTile = tile / 32;
		int tileOffset = (xTile << 3) + ((yTile << 3) * sheet.width);
		
		boolean mirrorX = (mirror & BIT_MIRROR_X) > 0;
		boolean mirrorY = (mirror & BIT_MIRROR_Y) > 0;
	
		for(int y = 0; y < 8; y++) {
			if(y + yPos < 0 || y + yPos >= height) continue;
			int ySheet = y;
			if(mirrorX) ySheet = 7 - y;
			for(int x = 0; x < 8; x++) {
				if(x + xPos < 0 || x + xPos >= width) continue;
				int xSheet = x;
				if(mirrorY) xSheet = 7 - x;
				int color = (colors >> (sheet.pixels[xSheet + ySheet * sheet.width + tileOffset] * 8)) & 255;
				if(color < 255) pixels[(x + xPos) + ((y + yPos) * width)] = color;
			}
		}
	}
	
	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

}
