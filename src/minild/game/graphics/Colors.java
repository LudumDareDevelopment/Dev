package minild.game.graphics;

public class Colors {
	public static int get(int color1, int color2, int color3, int color4) {
		// bind all colors associated with the respective monochrome color into one int
		return (get(color4) << 24) + (get(color3) << 16) + (get(color2) << 8) + (get(color1));  
	}

	private static int get(int color) { // converts into rgb format
		if (color < 0) return 255;
		int r = color / 100 % 10; // get first digit
		int g = color / 10 % 10; // second digit
		int b = color % 10; // third digit
		return r * 36 + g * 6 + b; // bind into one int
	}
}
