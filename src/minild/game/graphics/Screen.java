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
			int ySheet = y;
			if(mirrorY) ySheet = 7 - y;
			if(y + yPos < 0 || y + yPos >= height) continue;
			for(int x = 0; x < 8; x++) {
				int xSheet = x;
				if(mirrorX) xSheet = 7 - x;
				if(x + xPos < 0 || x + xPos >= width) continue;
				int color = (colors >> (sheet.pixels[xSheet + ySheet * sheet.width + tileOffset] * 8)) & 255;
				if(color < 255) pixels[(x + xPos) + (y + yPos) * width] = color;
			}
		}
	}

	public void renderLight(int x, int y, int r) {
		x -= xOffset;
		y -= yOffset;
		int x0 = x - r;
		int x1 = x + r;
		int y0 = y - r;
		int y1 = y + r;

		if (x0 < 0) x0 = 0;
		if (y0 < 0) y0 = 0;
		if (x1 > width) x1 = width;
		if (y1 > height) y1 = height;
		for (int yy = y0; yy < y1; yy++) {
			int yd = yy - y;
			yd = yd * yd;
			for (int xx = x0; xx < x1; xx++) {
				int xd = xx - x;
				int dist = xd * xd + yd;
				if (dist <= r * r) {
					int br = (255 - dist * 255 / (r * r)) & 255;
					if (pixels[xx + yy * width] < br) pixels[xx + yy * width] = br;
				}
			}
		}
	}
	
	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	

}
