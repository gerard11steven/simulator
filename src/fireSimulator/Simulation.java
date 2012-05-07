package fireSimulator;

import java.util.Random;
import java.util.Scanner;

public class Simulation {
	private MapItem[][] mapItems;
	public final static int mapSize = 40;
	private windData wind;
	private Random rand;
	private static final int lowChance = 25;
	private static final int medChance = 50;
	private static final int lrgChance = 75;
	public Scanner input;
	
	public Simulation() {
		ItemFactory factory = new ItemFactory();
		mapItems = new MapItem[mapSize][mapSize];
		rand = new Random();
		for (int y = 0; y < mapSize; y++) {
			for (int x = 0; x < mapSize; x++) {
				int i = rand.nextInt(100);
				if (i < 1)
					mapItems[x][y] = factory.makeItem(MapItemTypes.dirt);
				else if (i < 35)
					mapItems[x][y] = factory.makeItem(MapItemTypes.grass);
				else if (i < 45)
					mapItems[x][y] = factory.makeItem(MapItemTypes.house);
				else if (i < 55)
					mapItems[x][y] = factory.makeItem(MapItemTypes.road);
				else if (i < 65)
					mapItems[x][y] = factory.makeItem(MapItemTypes.sand);
				else if (i < 90)
					mapItems[x][y] = factory.makeItem(MapItemTypes.tree);
				else
					mapItems[x][y] = factory.makeItem(MapItemTypes.water);	
			}
		}
		
		input = new Scanner(System.in);
		System.out.println("Enter a starting position(s) for the fire (format: 1,1 [1,1]): ");
		String inS = input.nextLine();
		for (String s : inS.split(" ")) {
			String[] ss = s.split(",");
			if (mapItems[Integer.parseInt(ss[0])][Integer.parseInt(ss[1])].flammable) {
				mapItems[Integer.parseInt(ss[0])][Integer.parseInt(ss[1])].onFire = 1;
			}
		}
		System.out.println("Enter a wind speed and wind direction (format: 1,1,1");
		inS = input.nextLine();
		String[] ss = inS.split(",");
		wind = new windData(Integer.parseInt(ss[0]),
							Integer.parseInt(ss[1]),
							Integer.parseInt(ss[2]));
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

	public boolean checkForFire(int x, int y) {
		if (checkMapItem(x, y) && mapItems[x][y].onFire > 0 && mapItems[x][y].burntOut == false) {
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
//		System.out.println(String.format("spreadfire(%d, %d, %d)", xCord, yCord, movements));
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
			makeFire(tarXCord+1, tarYCord, movements, lowChance);
			makeFire(tarXCord-1, tarYCord, movements, lowChance);
		} else if (wind.yDir == 0) {
			makeFire(tarXCord, tarYCord+1, movements, lowChance);
			makeFire(tarXCord, tarYCord-1, movements, lowChance);
		} else {
			makeFire(xCord, tarYCord, movements, lowChance);
			makeFire(tarXCord, yCord, movements, lowChance);
		}
		
	}
	
	private void makeFire(int xCord, int yCord, int movement, int probability) {
		if (checkMapBounds(xCord, yCord) && mapItems[xCord][yCord].flammable && rand.nextInt(100) > probability) {
			mapItems[xCord][yCord].onFire = 1;
			spreadFire(xCord, yCord, movement - 1);
		}
	}
	
	private boolean checkMapBounds(int x, int y) {
//		System.out.println(String.format("checkMapBounds(%d, %d)", x, y));
		if (x >= mapSize || x < 0) 
			return false;
		if (y >= mapSize || y < 0)
			return false;
		return true;
	}

	public void extinguish() {
		for (int y = 0; y < mapSize; y++) {
			for (int x = 0; x < mapSize; x++) {
//				System.out.println(String.format("[%d][%d] burntime = %d, onFire = %d", x,y,mapItems[x][y].burnTime,mapItems[x][y].onFire));
				if (mapItems[x][y].onFire > 0 && mapItems[x][y].burnTime > 0) {
//					System.out.println("-->Decrement");
					mapItems[x][y].burnTime--;
					if (mapItems[x][y].burnTime == 0) {
						mapItems[x][y].burntOut = true;
					}
				}
			}
		}
	}
}