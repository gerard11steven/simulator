package fireSimulator;

import java.util.Random;

public class Simulation {
	private MapItem[][] mapItems;
	public final int mapSize = 20;
	private windData wind;
	private Random rand;
	private static final int lowChance = 25;
	private static final int medChance = 50;
	private static final int lrgChance = 75;
	
	public Simulation() {
		mapItems = new MapItem[20][20];
		rand = new Random();
		for (int y = 0; y < mapSize; y++) {
			for (int x = 0; x < mapSize; x++) {
				mapItems[x][y] = new SimpleItem();
				int i = rand.nextInt(100);
				if (i < 15)
					mapItems[x][y].item = MapItemTypes.dirt;
				else if (i < 30)
					mapItems[x][y].item = MapItemTypes.grass;
				else if (i < 40)
					mapItems[x][y].item = MapItemTypes.house;
				else if (i < 50)
					mapItems[x][y].item = MapItemTypes.road;
				else if (i < 65)
					mapItems[x][y].item = MapItemTypes.sand;
				else if (i < 85)
					mapItems[x][y].item = MapItemTypes.tree;
				else
					mapItems[x][y].item = MapItemTypes.water;	
			}
		}
		mapItems[14][7].onFire = true;
		wind = new windData(1, 1, 1);
	}

		// returns a copy of a mapItem
	public MapItem getMapItem(int x, int y) {
		try {
			return mapItems[x][y].clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Boolean checkMapItem(int x, int y) {
		if (mapItems[x][y] == null) return false;
		return true;
	}

	public void addMapItem(MapItemTypes objType, int x, int y) {
		if (!checkMapItem(x, y)) {
			mapItems[x][y] = new SimpleItem();
		}
		if (objType == MapItemTypes.fire) {
			mapItems[x][y].onFire = true;
		}
	}

	public boolean checkForFire(int x, int y) {
		if (checkMapItem(x, y) && mapItems[x][y].onFire == true) {
			return true;
		}
		return false;
	}

	public windData getWind() {
		try {
			return wind.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public int getWindSpeed() {
		return wind.speed;
	}
	
	public class windData implements Cloneable {
		int xDir;
		int yDir;
		int speed;
		
		public windData(int speed, int xDirection, int yDirection) {
			this.speed = speed;
			this.xDir = xDirection;
			this.yDir = yDirection;
		}

		public windData clone() throws CloneNotSupportedException {
		    return (windData) super.clone();
		}
	}

	public void spreadFire(int xCord, int yCord, int movements) {
		System.out.println(String.format("spreadfire(%d, %d, %d)", xCord, yCord, movements));
		if (movements <= 0) return;
		
		if (wind.xDir == 0 && wind.yDir == 0) {
			makeFire(xCord, yCord+1, movements, medChance);
			makeFire(xCord, yCord-1, movements, medChance);
			makeFire(xCord+1, yCord, movements, medChance);
			makeFire(xCord-1, yCord, movements, medChance);
			return;
		}
		
		int tarXCord = xCord + wind.xDir;
		int tarYCord = yCord + wind.yDir;
		makeFire(tarXCord, tarYCord, movements, lrgChance);
		if (wind.xDir == 0) {
			makeFire(tarXCord, tarYCord+1, movements, lowChance);
			makeFire(tarXCord, tarYCord-1, movements, lowChance);
		} else if (wind.yDir == 0) {
			makeFire(tarXCord+1, tarYCord, movements, lowChance);
			makeFire(tarXCord-1, tarYCord, movements, lowChance);
		} else {
			makeFire(xCord, tarYCord, movements, lowChance);
			makeFire(tarXCord, yCord, movements, lowChance);
		}
		
	}
	
	private void makeFire(int xCord, int yCord, int movement, int probability) {
		if (checkMapBounds(xCord, yCord) && rand.nextInt(100) > probability) {
			mapItems[xCord][yCord].onFire = true;
			spreadFire(xCord, yCord, movement - 1);
		}
	}
	
	private boolean checkMapBounds(int x, int y) {
		System.out.println(String.format("checkMapBounds(%d, %d)", x, y));
		if (x >= mapSize || x < 0) 
			return false;
		if (y >= mapSize || y < 0)
			return false;
		return true;
	}
}