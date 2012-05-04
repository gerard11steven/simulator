package fireSimulator;

public abstract class MapItem implements Cloneable {
	boolean onFire;
	MapItemTypes item;
	
	public MapItem() {
		onFire = false;
	}
	
	public MapItem clone() throws CloneNotSupportedException {
	    return (MapItem) super.clone();
	}
}