package fireSimulator;

public abstract class MapItem implements Cloneable {
	int onFire;
	boolean burntOut;
	MapItemTypes item;
	public int xSize;
	public int ySize;
	public int burnTime;
	public boolean flammable;
	
	public MapItem() {
		onFire = 0;
		burntOut = false;
	}
	
	public MapItem clone() throws CloneNotSupportedException {
	    return (MapItem) super.clone();
	}
}