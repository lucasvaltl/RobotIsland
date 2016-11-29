package robot;

import java.util.ArrayList;

import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import tests.Driver;

/**
 * Description: Class that stores all of the collision methods for the robot.
 * 
 * @author Geraint and Lucas
 *
 */

public class CollisionDetection {

	
	/**
	 * Description: this function detects if a rectangle (here a robot) is colliding
	 * with another rectangle stored in an ArrayList of rectangles
	 * 
	 * 
	 * @param robot: input the robot you want to test collisions against
	 * @param blocks: enter an ArrayList containing the rectangles that the robot
	 * could collide with
	 * @return: returns true if a collision was detected
	 */

	public static boolean collisionDetection(Rectangle robot, ArrayList<Rectangle> blocks) {
		/*for (Rectangle staticblocs : blocks) {
			if (robot.getBoundsInParent().intersects(staticblocs.getBoundsInParent())) {
				robot.setFill(Color.RED);
				return true; // collision
			}
		} */
		return false; 
	}

	
}