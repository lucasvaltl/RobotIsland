package robot;

import tests.Driver;

/**
 * Description: Class that stores all of the movement methods for the robot.
 * 
 * @author Geraint and Lucas
 *
 */
public class Movement {

	/**
	 * Description: Moves the robot up
	 * 
	 * @param wallEcomponents:
	 *            a double array containing the orientation of a
	 *            robot(rectangle) in the form {xOrientation, yOrientation}
	 */
	public static void moveUp(double[] wallEcomponents) {
		Driver.wallE.setSpeed(Driver.wallE.getSpeed() + Driver.wallE.getAcceleration());
		Driver.wallE.setxCoordinate(Driver.wallE.getxCoordinate() + Driver.wallE.getSpeed() * wallEcomponents[0]);
		Driver.wallE.setyCoordinate(Driver.wallE.getyCoordinate() - Driver.wallE.getSpeed() * wallEcomponents[1]);
	}

	/**
	 * Description: Moves the robot down
	 * 
	 * @param wallEcomponents:
	 *            a double array containing the orientation of a
	 *            robot(rectangle) in the form {xOrientation, yOrientation}
	 */
	public static void moveDown(double[] wallEcomponents) {
		Driver.wallE.setSpeed(Driver.wallE.getSpeed() + Driver.wallE.getAcceleration());
		Driver.wallE.setxCoordinate(Driver.wallE.getxCoordinate() - Driver.wallE.getSpeed() * wallEcomponents[0]);
		Driver.wallE.setyCoordinate(Driver.wallE.getyCoordinate() + Driver.wallE.getSpeed() * wallEcomponents[1]);
	}

	/**
	 * Description: Turns the robot to the left
	 * 
	 */
	public static void moveLeft() {
		Driver.wallE.setRotate(Driver.wallE.getRotate() - Math.abs(Driver.wallE.getAngularVelocity()));
	}

	/**
	 * Description: Turns the robot to the right
	 * 
	 */
	public static void moveRight() {
		Driver.wallE.setRotate(Driver.wallE.getRotate() + Math.abs(Driver.wallE.getAngularVelocity()));
	}

	/**
	 * Description: causes the robot to decelerate
	 * 
	 * @param wallEcomponents:
	 *            a double array containing the orientation of a
	 *            robot(rectangle) in the form {xOrientation, yOrientation}
	 */
	public static void deccelerate(double[] wallEcomponents) {
		// TODO Decelerate
		System.out.println(Driver.wallE.getSpeed());
		System.out.println(Driver.wallE.getAcceleration());
		double speed = Driver.wallE.getSpeed() - Driver.wallE.getAcceleration();
		speed = (speed < 0) ? 0 : speed;
		Driver.wallE.setSpeed(speed);
		// System.out.println(speed);

		if (Driver.lastUporDown.equals("DOWN")) {
			Driver.wallE.setxCoordinate(Driver.wallE.getxCoordinate() - Driver.wallE.getSpeed() * wallEcomponents[0]);
			Driver.wallE.setyCoordinate(Driver.wallE.getyCoordinate() + Driver.wallE.getSpeed() * wallEcomponents[1]);
		} else if (Driver.lastUporDown.equals("UP")) {
			Driver.wallE.setxCoordinate(Driver.wallE.getxCoordinate() + Driver.wallE.getSpeed() * wallEcomponents[0]);
			Driver.wallE.setyCoordinate(Driver.wallE.getyCoordinate() - Driver.wallE.getSpeed() * wallEcomponents[1]);
		}

	}

}
