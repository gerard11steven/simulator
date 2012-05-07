package fireSimulator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Graphic extends JPanel{
	private static final long serialVersionUID = -6103563191773906122L;
	static final int size = 15; // size of each box
	static int frameSize = size * (Simulation.mapSize + (Simulation.mapSize / 10));
	public JFrame frame;
	public VGTimerTask vgTask;
	public Simulation sim;
	public Rectangle screen;
	HashMap<String, Image> imagesHash;

	public Graphic(){
		super();
		sim = new Simulation();
		screen = new Rectangle(0, 0, frameSize, frameSize);
    	frame = new JFrame("Fire Simulator");
    	vgTask = new VGTimerTask();
    	imagesHash = new HashMap<String, Image>();
	}

	class VGTimerTask extends TimerTask{
		public void run(){
			makeChanges();
			frame.repaint();
		}
	}

	public void paintComponent(Graphics g){
		//DrawFrame frame = new DrawFrame();
			// x coordinate of the point where drawing starts
		int XXX=15;
		int YYY=5;
		int coX=XXX;
		int coY=YYY;
		setBackground(new Color(80,40,40));
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		for(int j=0;j<Simulation.mapSize;j++) {
			coX=XXX;
			for(int i=0;i<Simulation.mapSize;i++) {
				MapItem item = sim.getMapItem(i, j);
				if (item == null)
					continue;
				insertJpg(g2, coX, coY, item.item);
				if (item.onFire > 0) {
//					System.out.println(String.format("burntime = %d, onFire = %d", item.burnTime,item.onFire));
					if (item.burntOut) {
//						System.out.println("burntOut");
						insertPng(g2, coX, coY, MapItemTypes.fireOut);
					} else {
//						System.out.println("onFire");
						insertPng(g2, coX, coY, MapItemTypes.fire);
					}
				}
				coX+=size;
			}                       
			coY+=size;
		}
	}

	private void insertPng(Graphics2D g, int xCord, int yCord, MapItemTypes item) {
		if (!imagesHash.containsKey(item.toString())) {
			String loc = "pictures//" + item.toString() + ".png";
			// System.out.println(loc);
			try {
				imagesHash.put(item.toString(), ImageIO.read((new File(loc)).toURI().toURL()));
			} catch (Exception e) {
				imagesHash.remove(item.toString());
				e.printStackTrace();
			}
		}
		if (imagesHash.containsKey(item.toString())) {
			g.drawImage(imagesHash.get(item.toString()),
						xCord, yCord, size, size, null);
		}
	}
	
	private void insertJpg(Graphics2D g, int xCord, int yCord, MapItemTypes item) {
		if (!imagesHash.containsKey(item.toString())) {
			String loc = "pictures//" + item.toString() + ".jpg";
			// System.out.println(loc);
			try {
				imagesHash.put(item.toString(), ImageIO.read((new File(loc)).toURI().toURL()));
			} catch (Exception e) {
				imagesHash.remove(item.toString());
				e.printStackTrace();
			}
		}
		if (imagesHash.containsKey(item.toString())) {
			g.drawImage(imagesHash.get(item.toString()),
						xCord, yCord, size, size, null);
		}
	}

		// everything going well? Add some fire!!!
	public void makeChanges(){
		ArrayList<Integer> xPoints = new ArrayList<Integer>();
		ArrayList<Integer> yPoints = new ArrayList<Integer>();
		for (int y = 0; y < Simulation.mapSize; y++) {
			for (int x = 0; x < Simulation.mapSize; x++) {
				if (sim.checkForFire(x, y)) {
					xPoints.add(x);
					yPoints.add(y);
				}
			}
		}
		for (int i = 0; i < xPoints.size(); i++) {
			sim.spreadFire(xPoints.get(i), yPoints.get(i), sim.getWindSpeed());
		}
		sim.extinguish();
	}

	public static void main(String arg[]){
		java.util.Timer vgTimer = new java.util.Timer();  // Create a Timer.
		Graphic panel = new Graphic(); // Create and instance of our kernel.
			// Set up our JFRame
		panel.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.frame.setSize(panel.screen.width, panel.screen.height);
		panel.frame.setContentPane(panel); 
		panel.frame.setVisible(true);

			// Set up a timer to do the vgTask regularly.
		vgTimer.schedule(panel.vgTask, 0, 2000);
	}
}