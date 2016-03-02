package minild.game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import minild.game.entity.Player;
import minild.game.graphics.Colors;
import minild.game.graphics.Screen;
import minild.game.graphics.SpriteSheet;
import minild.game.level.Level;
import minild.game.level.tile.Tile;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public static final String NAME = "Game";
	public static final int WIDTH = 160;
	public static final int HEIGHT = WIDTH / 12 * 9;
	public static final int SCALE = 3;

	private Thread thread;

	public JFrame frame;

	private boolean running = false;

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	private int[] colors = new int[6 * 6 * 6];
	
	private Screen screen;
	private InputHandler input;
	
	private Level level;
	private Player player;

	public Game() {
		setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		frame = new JFrame();
	}

	public void init() {
		int index = 0;
		for (int r = 0; r < 6; r++) {
			for (int g = 0; g < 6; g++) {
				for (int b = 0; b < 6; b++) {
					int rr = (r * 255 / 5);
					int gg = (g * 255 / 5);
					int bb = (b * 255 / 5);
					colors[index++] = rr << 16 | gg << 8 | bb;
				}
			}
		}
		
		screen = new Screen(WIDTH, HEIGHT, SpriteSheet.tiles);
		input = new InputHandler(this);
		
		level = new Level(128, 128);
		player = new Player(level, 0, 0, input);
		player.findStartPos(level);
		level.addEntity(player);
		level.trySpawn(25);
	}

	public synchronized void start() {
		running = true;

		thread = new Thread(this, NAME + "Display");
		thread.start();
	}

	public synchronized void stop() {
		running = false;

		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		init();
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		int frames = 0;
		int updates = 0;
		requestFocus();
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				update();
				updates++;
				delta--;
			}
			render();
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				frame.setTitle(NAME + "   |   " + updates + " ups, " + frames + " fps");
				updates = 0;
				frames = 0;
			}
		}
	}
	
	public void update() {
		if (!hasFocus()) {
			input.releaseAll();
		}
			
		input.update();
		level.update();
		Tile.updateCount++;
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();

		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		screen.clear(0);
	
		int xScroll = player.x - (screen.width / 2);
		int yScroll = player.y - (screen.height / 2);
		level.render(xScroll, yScroll, screen);
		level.renderEntities(screen);
		
		//if(!hasFocus()) renderFocusScreen();
		
		for(int y = 0; y < screen.height; y++) {
			for(int x = 0; x < screen.width; x++) {
				int colorCode = screen.pixels[x + y * screen.width];
				if(colorCode < 255) pixels[x + y * WIDTH] = colors[colorCode];
			}
		}
	
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}
	
	private void renderFocusScreen() {
		String msg = "Click to focus!";
		int xx = (WIDTH - msg.length() * 8) / 2;
		int yy = (HEIGHT - 8) / 2;
		int w = msg.length();
		int h = 1;

		screen.render(xx - 8, yy - 8, 0 + 13 * 32, Colors.get(-1, 1, 5, 445), (byte) 0);
		screen.render(xx + w * 8, yy - 8, 0 + 13 * 32, Colors.get(-1, 1, 5, 445), (byte) 1);
		screen.render(xx - 8, yy + 8, 0 + 13 * 32, Colors.get(-1, 1, 5, 445), (byte) 2);
		screen.render(xx + w * 8, yy + 8, 0 + 13 * 32, Colors.get(-1, 1, 5, 445), (byte) 3);
		for (int x = 0; x < w; x++) {
			screen.render(xx + x * 8, yy - 8, 1 + 13 * 32, Colors.get(-1, 1, 5, 445), (byte) 0);
			screen.render(xx + x * 8, yy + 8, 1 + 13 * 32, Colors.get(-1, 1, 5, 445), (byte) 2);
		}
		for (int y = 0; y < h; y++) {
			screen.render(xx - 8, yy + y * 8, 2 + 13 * 32, Colors.get(-1, 1, 5, 445), (byte) 0);
			screen.render(xx + w * 8, yy + y * 8, 2 + 13 * 32, Colors.get(-1, 1, 5, 445), (byte) 1);
		}

	}

	public static void main(String[] args) {
		Game game = new Game();
		game.frame.setResizable(false);
		game.frame.setTitle(Game.NAME);
		game.frame.add(game, null);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLayout(new BorderLayout());
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);

		game.start();
	}

}
