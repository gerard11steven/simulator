package fireSimulator;

public class ItemFactory {
	
	public MapItem makeItem(MapItemTypes type) {
		MapItem item = null;
		switch (type) {
			case dirt:
				item = new DirtItem();
				break;
			case grass:
				item = new GrassItem();
				break;
			case house:
				item = new HouseItem();
				break;
			case road:
				item = new RoadItem();
				break;
			case sand:
				item = new SandItem();
				break;
			case tree:
				item = new TreeItem();
				break;
			case water:
				item = new WaterItem();
				break;
		}
		return item;
	}
	
	public class TreeItem extends MapItem {
		public TreeItem() {
			flammable = true;
			burnTime = 4;
			item = MapItemTypes.tree;
			burntOut = false;
		}
	}
	
	public class GrassItem extends MapItem {
		public GrassItem() {
			burnTime = 1;
			flammable = true;
			item = MapItemTypes.grass;
			burntOut = false;
		}
	}

	public class DirtItem extends MapItem {
		public DirtItem() {
			burnTime = 0;
			flammable = false;
			item = MapItemTypes.dirt;
			burntOut = false;
		}
	}
	
	public class SandItem extends MapItem {
		public SandItem() {
			burnTime = 0;
			flammable = false;
			item = MapItemTypes.sand;
			burntOut = false;
		}
	}
	
	public class WaterItem extends MapItem {
		public WaterItem() {
			burnTime = 0;
			flammable = false;
			item = MapItemTypes.water;
			burntOut = false;
		}
	}
	
	public class RoadItem extends MapItem {
		public RoadItem() {
			burnTime = 0;
			flammable = false;
			item = MapItemTypes.road;
			burntOut = false;
		}
	}
	
	public class HouseItem extends MapItem {
		public HouseItem() {
			burnTime = 7;
			flammable = true;
			item = MapItemTypes.house;
			burntOut = false;
		}
	}
}