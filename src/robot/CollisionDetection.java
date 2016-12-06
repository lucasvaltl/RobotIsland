package robot;

import java.io.File;
import java.util.ArrayList;

import gametimer.GameTimer;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
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
	 * @param robot: inpuat the robot you want to test collisions against
	 * @param blocks: enter an ArrayList containing the rectangles that the robot
	 * could collide with
	 * @return: returns true if a collision was detected
	 */

	public static boolean collisionDetection(Robot robot, ArrayList<Entity> blocks) {
//		Bounds objA = robot.localToScene(robot.getBoundsInLocal());
       
		
		
		for (Entity staticblock : blocks) {
			
			if (staticblock.isColliding(robot)) {
//				System.out.println("robot box: "+ (robot.getBoundsInParent()));
//				System.out.println("Robot get width: ");
//				System.out.println("Block box: "+ (staticblock.getBoundsInParent()));
				
				Image pattern = new Image(new File("src/eveCollision.png").toURI().toString(), 32, 48, false, true);
				ImagePattern skin = new ImagePattern(pattern);
				Driver.wallE.setFill(skin);
				GameTimer.setCollisionDetected(true);
				return true; // collision
			}
		} 
		return false; 
	}

	
}