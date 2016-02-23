package minild.game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import minild.game.graphics.SpriteSheet;

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
	private int[] colors = new int [6 * 6 * 6];
	
	public Game() {
		setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		
		frame = new JFrame();
	}
	
	public void init() {
		int index = 0;
		for(int r = 0; r < 6; r++) {
			for(int g = 0; g < 6; g++) {
				for(int b = 0; b < 6;b++) {
					int rr = (r * 255 / 5);
					int gg = (r * 255 / 5);
					int bb = (r * 255 / 5);
					colors[index++] = rr << 16 + gg << 8 + bb;
				}
			}
		}
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
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				update();
				updates++;
				delta--;
			}
			render();
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				frame.setTitle(NAME + "   |   " + updates + " ups, " + frames + " fps");
				updates = 0;
				frames = 0;
			}
		}
	}
	
	public void update() {
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
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
