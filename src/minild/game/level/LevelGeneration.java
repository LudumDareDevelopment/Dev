package minild.game.level;

import java.util.Random;

import minild.game.level.tile.Tile;

public class LevelGeneration {
	
	private static final Random random = new Random();
	public double[] values;
	private int width, height;
	
	public LevelGeneration(int width, int height, int featureSize) {
		this.width = width;
		this.height = height;
		
		values = new double[width * height];
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				setSample(x, y, random.nextFloat() * 2 - 1);
			}
		}
		
		int stepSize = featureSize;
		double scale = 1.0 / width;
		double scaleMod = 1;
		do {
			int halfStep = stepSize / 2;
			for (int y = 0; y < width; y += stepSize) {
				for (int x = 0; x < width; x += stepSize) {
					double a = sample(x, y);
					double b = sample(x + stepSize, y);
					double c = sample(x, y + stepSize);
					double d = sample(x + stepSize, y + stepSize);

					double e = (a + b + c + d) / 4.0 + (random.nextFloat() * 2 - 1) * stepSize * scale;
					setSample(x + halfStep, y + halfStep, e);
				}
			}
			for (int y = 0; y < width; y += stepSize) {
				for (int x = 0; x < width; x += stepSize) {
					double a = sample(x, y);
					double b = sample(x + stepSize, y);
					double c = sample(x, y + stepSize);
					double d = sample(x + halfStep, y + halfStep);
					double e = sample(x + halfStep, y - halfStep);
					double f = sample(x - halfStep, y + halfStep);

					double H = (a + b + d + e) / 4.0 + (random.nextFloat() * 2 - 1) * stepSize * scale * 0.5;
					double g = (a + c + d + f) / 4.0 + (random.nextFloat() * 2 - 1) * stepSize * scale * 0.5;
					setSample(x + halfStep, y, H);
					setSample(x, y + halfStep, g);
				}
			}
			stepSize /= 2;
			scale *= (scaleMod + 0.8);
			scaleMod *= 0.3;
		} while(stepSize > 1);
	}
	
	public static byte[] createAndValidateTopMap(int w, int h) {
		int attempt = 0;
		do {
			byte[] result = createTopMap(w, h);

			int[] count = new int[256];

			for (int i = 0; i < w * h; i++) {
				count[result[i] & 0xff]++;
			}
			if (count[Tile.stone.id & 0xff] < 100) continue;
			if (count[Tile.grass.id & 0xff] < 100) continue;
			return result;

		} while (true);
	}

	
	public static byte[] createTopMap(int width, int height) {
		LevelGeneration mnoise1 = new LevelGeneration(width, height, 16);
		LevelGeneration mnoise2 = new LevelGeneration(width, height, 16);
		LevelGeneration mnoise3 = new LevelGeneration(width, height, 16);

		LevelGeneration noise1 = new LevelGeneration(width, height, 32);
		LevelGeneration noise2 = new LevelGeneration(width, height, 32);

		byte[] map = new byte[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int i = x + y * width;

				double val = Math.abs(noise1.values[i] - noise2.values[i]) * 3 - 2;
				double mval = Math.abs(mnoise1.values[i] - mnoise2.values[i]);
				mval = Math.abs(mval - mnoise3.values[i]) * 3 - 2;

				double xd = x / (width - 1.0) * 2 - 1;
				double yd = y / (height - 1.0) * 2 - 1;
				if (xd < 0) xd = -xd;
				if (yd < 0) yd = -yd;
				double dist = xd >= yd ? xd : yd;
				dist = dist * dist * dist * dist;
				dist = dist * dist * dist * dist;
				val = val + 1 - dist * 20;

				if (val < -0.5) {
					map[i] = Tile.water.id;
				} else if (val > 0.5 && mval < -1.5) {
					map[i] = Tile.stone.id;
				} else {
					map[i] = Tile.grass.id;
				}
			}
		}
		
		for (int i = 0; i < width * height / 400; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			for (int j = 0; j < 200; j++) {
				int xx = x + random.nextInt(15) - random.nextInt(15);
				int yy = y + random.nextInt(15) - random.nextInt(15);
				if (xx >= 0 && yy >= 0 && xx < width && yy < height) {
					if (map[xx + yy * width] == Tile.grass.id) {
						map[xx + yy * width] = Tile.tree.id;
					}
				}
			}
		}

		return map;
	}
	
	private double sample(int x, int y) {
		return values[(x & (width - 1)) + (y & (height - 1)) * width];
	}
	
	private void setSample(int x, int y, double value) {
		values[(x & (width - 1)) + (y & (height - 1)) * width] = value;
	}

}
