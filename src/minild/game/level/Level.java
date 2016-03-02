package minild.game.level;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import minild.game.Game;
import minild.game.entity.Entity;
import minild.game.entity.Mob;
import minild.game.entity.Zombie;
import minild.game.graphics.Screen;
import minild.game.level.tile.Node;
import minild.game.level.tile.Tile;
import minild.game.util.Vector2i;

public class Level {

	public int width, height;
	protected byte[] tiles;

	public List<Entity> entities = new ArrayList<Entity>();

	private Comparator<Node> nodeSorter = new Comparator<Node>() {
		public int compare(Node n0, Node n1) {
			if (n1.fCost < n0.fCost) return +1;
			if (n1.fCost > n0.fCost) return -1;
			return 0;
		}
	};

	public static int grassColor = 242;
	public static int stoneColor = 333;
	public static int waterColor = 114;
	public static int dirtColor = 322;
	public static int sandColor = 553;

	public Level(int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new byte[width * height];

		generateLevel();
	}

	protected void generateLevel() {
		tiles = LevelGeneration.createAndValidateTopMap(width, height);
	}

	public void update() {
		for (Entity e : entities) {
			e.update();
		}
	}

	public void render(int xScroll, int yScroll, Screen screen) {
		screen.setOffset(xScroll, yScroll);
		int x0 = xScroll >> 4;
		int x1 = ((xScroll + screen.width) >> 4) + 15;
		int y0 = yScroll >> 4;
		int y1 = ((yScroll + screen.height) >> 4) + 15;

		for (int y = y0; y < y1; y++) {
			for (int x = x0; x < x1; x++) {
				getTile(x, y).render(x, y, screen, this);
			}
		}
	}

	public void renderEntities(Screen screen) {
		for (Entity e : entities) {
			e.render(screen);
		}
	}

	public Tile getTile(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) return Tile.water;
		return Tile.tiles[tiles[x + y * width]];
	}

	public void addEntity(Entity entity) {
		this.entities.add(entity);
	}

	public void trySpawn(int count) {
		for (int i = 0; i < count; i++) {
			Mob mob;

			mob = new Zombie(this);
			mob.findStartPos(this);
		}
	}

	public List<Node> findPath(Vector2i start, Vector2i goal) {
		//long prevTime = System.currentTimeMillis();
		List<Node> openList = new ArrayList<Node>();
		List<Node> closedList = new ArrayList<Node>();
		Node current = new Node(start, null, 0, getDistance(start, goal));
		openList.add(current);
		while (openList.size() > 0) {
			//long currentTime = System.currentTimeMillis();
			//if(currentTime - prevTime >= 250) return null;
			Collections.sort(openList, nodeSorter);
			current = openList.get(0);
			if (current.tile.equals(goal)) {
				List<Node> path = new ArrayList<Node>();
				while (current.parent != null) {
					path.add(current);
					current = current.parent;
				}
				openList.clear();
				closedList.clear();
				return path;
			}
			openList.remove(current);
			closedList.add(current);
			for (int i = 0; i < 9; i++) {
				if (i == 4) continue;
				int x = current.tile.getX();
				int y = current.tile.getY();
				int xi = (i % 3) - 1;
				int yi = (i / 3) - 1;
				Tile at = getTile(x + xi, y + yi);
				if (at == null) continue;
				if (at.solid()) continue;
				Vector2i a = new Vector2i(x + xi, y + yi);
				double gCost = current.gCost + (getDistance(current.tile, a) == 1 ? 1 : 0.95);;
				double hCost = getDistance(a, goal);
				Node node = new Node(a, current, gCost, hCost);
				if (vecInList(closedList, a) && gCost >= current.gCost) continue;
				if (!vecInList(openList, a) || gCost < current.gCost) openList.add(node);
			}
		}
		closedList.clear();
		return null;
	}

	private boolean vecInList(List<Node> list, Vector2i vector) {
		for (Node n : list) {
			if (n.tile.equals(vector)) return true;
		}
		return false;
	}

	private double getDistance(Vector2i tile, Vector2i goal) {
		double dx = tile.getX() - goal.getX();
		double dy = tile.getY() - goal.getY();
		double distance = Math.sqrt(dx * dx + dy * dy);
		return distance;
	}

}
