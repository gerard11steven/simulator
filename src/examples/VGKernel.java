package examples;
/* A simple video game style kernel
   by Mark Graybill, August 2010
   Uses the Timer Class to move a ball on a playfield.
*/

// Import Timer and other useful stuff:
import java.util.*;
// Import the basic graphics classes.
import java.awt.*;
import javax.swing.*;

public class VGKernel extends JPanel{
	private static final long serialVersionUID = -6103563191773906122L;
		// Set up the objects and variables we'll want.
	public Rectangle screen, ball; // The screen area and ball location/size.
	public Rectangle bounds;  // The boundaries of the drawing area.
	public JFrame frame; // A JFrame to put the graphics into.
	public VGTimerTask vgTask; // The TimerTask that runs the game.
	public boolean down, right; // Direction of ball's travel.

		// Create a constructor method that initializes things:
	public VGKernel(){
		super();
		screen = new Rectangle(0, 0, 600, 400);
		ball   = new Rectangle(0, 0, 20, 20);
    	bounds = new Rectangle(0, 0, 600, 400); // Give some starter values.
    	frame = new JFrame("VGKernel");
    	vgTask = new VGTimerTask();
	}
  
		// Create an inner TimerTask class that has access to the
		// members of the VGKernel.
	class VGTimerTask extends TimerTask{
		public void run(){
			moveBall();
			frame.repaint();
		}
	}

		// Now the instance methods:
	public void paintComponent(Graphics g){
			// Get the drawing area bounds for game logic.
		bounds = g.getClipBounds();
			// Clear the drawing area, then draw the ball.
		g.clearRect(screen.x, screen.y, screen.width, screen.height);
		g.fillRect(ball.x, ball.y, ball.width, ball.height);
	}

		// Ball should really be its own class with this as a method.
	public void moveBall(){
			// If right is true, move ball right,
		if (right) ball.x+=ball.width;
			// otherwise move left.
	    else ball.x-=ball.width;
	
			// Same for up/down.
	    if (down)  ball.y+=ball.height;
	    else ball.y-=ball.width;
	    
	    	// Detect edges and bounce.
	    if (ball.x > (bounds.width - ball.width)) {
	    	right = false; ball.x = bounds.width -  ball.width;
	    }
	    if (ball.y > (bounds.height - ball.height)) {
	    	down  = false; ball.y = bounds.height - ball.height;
	    }
	    if (ball.x <= 0) { right = true; ball.x = 0; }
	    if (ball.y <= 0) { down  = true; ball.y = 0; }
	}

	public static void main(String arg[]){
		java.util.Timer vgTimer = new java.util.Timer();  // Create a Timer.
		VGKernel panel = new VGKernel(); // Create and instance of our kernel.
			// Set the initial ball movement direction.
		panel.down = true;
		panel.right = true;

			// Set up our JFRame
		panel.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.frame.setSize(panel.screen.width, panel.screen.height);
		panel.frame.setContentPane(panel); 
		panel.frame.setVisible(true);

			// Set up a timer to do the vgTask regularly.
		vgTimer.schedule(panel.vgTask, 0, 100);
	}
}