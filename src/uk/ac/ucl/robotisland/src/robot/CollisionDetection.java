package uk.ac.ucl.robotisland.src.robot;

import java.util.ArrayList;

import uc.ac.ucl.robotisland.src.map.Map;
import uk.ac.ucl.robotisland.src.tests.Driver;

/**
 * Description: Class that holds all of the collision methods for the robot.
 * 
 * @author Geraint and Lucas
 */
public class CollisionDetection {

	/**
	 * 
	 * Description: This function detects if a rectangle (here, a robot) is
	 * colliding.
	 * 
	 * @param robot:
	 *            The robot to test collisions against.
	 * @return: True if a collision was detected
	 */
	public static boolean collisionDetection(Robot robot) {
		// get nearest nearest blocks depending on robots current position (for
		// optimisation)
		ArrayList<Entity> nearestblocks = getAdjacentBlocks(robot);

		for (Entity staticblock : nearestblocks) {
			// regular bounding box based collision detection for broad phase
			// detection
			if (robot.getBoundsInParent().intersects(staticblock.getBoundsInParent())) {
				// proprietary seperating axis theorem based collision detection
				// for narrow phase detection
				if (staticblock.isColliding(robot)) {

					// Play soundFX
					if (Driver.collisionSound.isPlaying() == true) {
						Driver.collisionSound.stop();
					}
					if (Driver.collisionSound.isPlaying() == false) {
						Driver.collisionSound.play(0.4);
					}

					return true; // collision
				}
			}
		}
		return false;
	}

	public static boolean collisionDetection(DummyRobot dummy) {
		// get nearest nearest blocks depending on robots current position (for
		// optimisation)
		ArrayList<Entity> nearestblocks = getAdjacentBlocks(dummy);

		for (Entity staticblock : nearestblocks) {
			// regular bounding box based collision detection for broad phase
			// detection
			if (dummy.getBoundsInParent().intersects(staticblock.getBoundsInParent())) {
				// proprietary seperating axis theorem based collision detection
				// for narrow phase detection
				if (staticblock.isColliding(dummy)) {
					return true; // collision
				}
			}
		}
		return false;
	}

	/**
	 * Description: This function detects if the robot is colliding with any
	 * block within an array list of blocks.
	 * 
	 * @param robot:
	 *            The robot to detect collisions against.
	 * @param blocks:
	 *            An array list of entity objects.
	 * @return: True when a collision is detected.
	 */
	public static boolean detectLocation(Robot robot, ArrayList<Entity> blocks) {
		for (Entity block : blocks) {
			// regular bounding box based collision detection for broad phase
			// detection
			if (robot.getBoundsInParent().intersects(block.getBoundsInParent())) {
				// proprietary separating axis theorem based collision detection
				// for narrow phase detection
				if (block.isColliding(robot)) {

					return true; // collision
				}
			}
		}
		return false;
	}

	/**
	 * Description: This method returns an array list of adjacent entities from
	 * a given robot
	 * 
	 * @param robot:
	 *            The robot object.
	 * @return: An array list of entities representing the adjacent blocks
	 *          surrounding the robot.
	 */
	public static ArrayList<Entity> getAdjacentBlocks(Robot robot) {
		// rounded down current position in rows and columns

		int currentRow = (int) ((robot.getyCoordinate() / Driver.map.getBlockHeight() * 1.0));
		int currentCol = (int) (robot.getxCoordinate() / Driver.map.getBlockWidth() * 1.0);

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
				adjacentBlocks.add(Map.getBlocks(Map.getBlockPosition(x, y)));
			}
		}

		return adjacentBlocks;
	}

}