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
import map.Map;
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

//	public static boolean collisionDetection(Robot robot, ArrayList<Entity> blocks) {
////		Bounds objA = robot.localToScene(robot.getBoundsInLocal());
//		/**
//		 * Returns the adjacent blocks surrounding the robot.
//		 * @return: an array list with the adjacent blocks in the form [row, col];
//		 */
//		
//		
//		
//		for (Entity staticblock : blocks) {
//			//regular bounding box based collision detection for broad phase detection
//			if(robot.getBoundsInParent().intersects(staticblock.getBoundsInParent())){
//				//prorprietary seperating axis theorem based collision detection for narrow phase detection
//			if (staticblock.isColliding(robot)) {
//				
//				Image pattern = new Image(new File("src/eveCollision.png").toURI().toString(), 32, 48, false, true);
//				ImagePattern skin = new ImagePattern(pattern);
//				Driver.wallE.setFill(skin);
//				GameTimer.setCollisionDetected(true);
//				return true; // collision
//			}
//		}}
//		return false; 
//	}
	
	public static boolean collisionDetection(Robot robot) {
//		Bounds objA = robot.localToScene(robot.getBoundsInLocal());
		
		
		//get nearest nearest blocks depending on robots current position (optimising processing neeD)
		ArrayList<Entity> nearestblocks = getAdjacentBlocks(robot);
		
		
		for (Entity staticblock : nearestblocks) {
			//regular bounding box based collision detection for broad phase detection
			if(robot.getBoundsInParent().intersects(staticblock.getBoundsInParent())){
				//prorprietary seperating axis theorem based collision detection for narrow phase detection
			if (staticblock.isColliding(robot)) {
				
				Image pattern = new Image(new File("src/eveCollision.png").toURI().toString(), 32, 48, false, true);
				ImagePattern skin = new ImagePattern(pattern);
				Driver.wallE.setFill(skin);
				GameTimer.setCollisionDetected(true);
				return true; // collision
			}
		}}
		return false; 
	}
	
	public static ArrayList<Entity> getAdjacentBlocks(Robot robot) {
		// rounded down.
		double ycor = robot.getyCoordinate();
		double blockh = Driver.map.getBlockHeight();
		double screenh = Driver.SCREENWIDTH;
		
		
		int currentRow = (int) ((robot.getyCoordinate() / Driver.map.getBlockHeight() * 1.0) );
		int currentCol = (int) (robot.getxCoordinate() / Driver.map.getBlockWidth() * 1.0);
		
		System.out.println("CurrentRow: " + currentRow + " CurrentCol: " + currentCol);
		
		ArrayList<Integer> rows = new ArrayList<Integer>(3);
		ArrayList<Integer> cols = new ArrayList<Integer>(3);
		if (currentRow == 0) {
			rows.add(currentRow);
			rows.add(currentRow + 1);
		} else if (currentRow >= Driver.map.getRowSize() - 1) {
			rows.add(currentRow - 1);
			rows.add(currentRow);
		} else {
			rows.add(currentRow - 1);
			rows.add(currentRow);
			rows.add(currentRow + 1);
		}
		
		if (currentCol == 0) {
			cols.add(currentCol);
			cols.add(currentCol + 1);
		} else if (currentCol >= Driver.map.getColSize() - 1) {
			cols.add(currentCol - 1);
			cols.add(currentCol);
		} else {
			cols.add(currentCol - 1);
			cols.add(currentCol);
			cols.add(currentCol + 1);
		}
		
		ArrayList<Entity> adjacentBlocks = new ArrayList<Entity>(6);
		for (int i = 0; i < rows.size(); i++) {
			for (int j = 0; j < cols.size(); j++) {
				int x = rows.get(i);
				int y = cols.get(j);
//				System.out.println("X: " + x + " Y: " + y);
				adjacentBlocks.add(Map.getBlocks(Map.getBlockPosition(x, y)));
			}
		}
		
		
		
		
		return adjacentBlocks;
	} 
	
}