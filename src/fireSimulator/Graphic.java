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
	static int frameSize=850;
	public JFrame frame;
	public VGTimerTask vgTask;
	public Simulation sim;
	public Rectangle screen;
	static final int size=40; // size of each box
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
		for(int j=0;j<sim.mapSize;j++) {
			coX=XXX;
			for(int i=0;i<sim.mapSize;i++) {
				MapItem item = sim.getMapItem(i, j);
				if (item == null)
					continue;
				insertPicture(g2, coX, coY, item.item);
				if (item.onFire)
					drawFire(g2, coX, coY);
				// g2.setColor(Color.WHITE);
				// Rectangle2D rec = new Rectangle2D.Double(coX,coY,size,size);
				// g2.draw(rec);
				coX+=size;
			}                       
			coY+=size;
		}
	}

	private void drawFire(Graphics2D g, int xCord, int yCord) {
		if (!imagesHash.containsKey(MapItemTypes.fire.toString())) {
			String loc = "pictures//" + MapItemTypes.fire.toString() + ".png";
			// System.out.println(loc);
			try {
				imagesHash.put(MapItemTypes.fire.toString(), ImageIO.read((new File(loc)).toURI().toURL()));
			} catch (Exception e) {
				imagesHash.remove(MapItemTypes.fire.toString());
				e.printStackTrace();
			}
		}
		if (imagesHash.containsKey(MapItemTypes.fire.toString())) {
			g.drawImage(imagesHash.get(MapItemTypes.fire.toString()),
						xCord, yCord, size, size, null);
		}
	}
	
	private void insertPicture(Graphics2D g, int xCord, int yCord, MapItemTypes item) {
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
		for (int y = 0; y < sim.mapSize; y++) {
			for (int x = 0; x < sim.mapSize; x++) {
				if (sim.checkForFire(x, y)) {
					xPoints.add(x);
					yPoints.add(y);
				}
			}
		}
		for (int i = 0; i < xPoints.size(); i++) {
			sim.spreadFire(xPoints.get(i), yPoints.get(i), sim.getWindSpeed());
		}
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