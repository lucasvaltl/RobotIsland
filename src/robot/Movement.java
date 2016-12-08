package robot;

import java.util.Arrays;

import loggers.CustomLevel;
import tests.Driver;

/**
 * Description: Class that stores all of the movement methods for the robot.
 * 
 * @author Geraint and Lucas
 *
 */
public class Movement {

	/**
	 * Description: Moves the robot down.
	 * Returns a double[] array of the wheel speeds in the form 
	 * {speedLeftWheel, speedRightWheel}.
	 * @param wallEcomponents:
	 *            a double array containing the orientation of a
	 *            robot(rectangle) in the form {xOrientation, yOrientation}
	 */
	public static double[] moveDown(double[] wallEcomponents) {
		Driver.LOGGER.info("moveDown");
		Driver.LOGGER.log(CustomLevel.INSTRUCTION, "moveDown");
		
		//cancel movement if battery is empty
				if(Driver.wallE.getBatteryLeft()<=0){
					System.out.println("Returning null");
					return null;
				}
		
		Driver.wallE.setSpeed(Driver.wallE.getSpeed() + Driver.wallE.getAcceleration());
		Driver.wallE.setxCoordinate(Driver.wallE.getxCoordinate() + Driver.wallE.getSpeed() * wallEcomponents[0]);
		Driver.wallE.setyCoordinate(Driver.wallE.getyCoordinate() - Driver.wallE.getSpeed() * wallEcomponents[1]);
	
		//calculate wheelspeeds
		double speedRightWheel = Driver.wallE.getSpeed();
		double speedLeftWheel = Driver.wallE.getSpeed();
		double[] wheelspeeds = {speedLeftWheel, speedRightWheel};
		Driver.wallE.setWheelspeeds(speedLeftWheel, speedRightWheel);
		
		Driver.LOGGER.fine(Arrays.toString(wheelspeeds));
		return wheelspeeds;
	}

	/**
	 * Description: Moves the robot up.
	 * Returns a double[] array of the wheel speeds in the form 
	 * {speedLeftWheel, speedRightWheel}.
	 * @param wallEcomponents:
	 *            a double array containing the orientation of a
	 *            robot(rectangle) in the form {xOrientation, yOrientation}
	 */
	public static double[] moveUp(double[] wallEcomponents) {
        Driver.LOGGER.info("moveUp");
        Driver.LOGGER.log(CustomLevel.INSTRUCTION, "moveUp");
		
		//cancel movement if battery is empty
				if(Driver.wallE.getBatteryLeft()<=0){
					System.out.println("Returning null");
					return null;
				}
		
		Driver.wallE.setSpeed(Driver.wallE.getSpeed() + Driver.wallE.getAcceleration());
		Driver.wallE.setxCoordinate(Driver.wallE.getxCoordinate() - Driver.wallE.getSpeed() * wallEcomponents[0]);
		Driver.wallE.setyCoordinate(Driver.wallE.getyCoordinate() + Driver.wallE.getSpeed() * wallEcomponents[1]);
	
		// calculate wheelspeeds
		double speedRightWheel = Driver.wallE.getSpeed();
		double speedLeftWheel = Driver.wallE.getSpeed();
		double[] wheelspeeds = {speedLeftWheel, speedRightWheel};
		Driver.wallE.setWheelspeeds(speedLeftWheel, speedRightWheel);
		
		Driver.LOGGER.fine(Arrays.toString(wheelspeeds));
		return wheelspeeds;
	}
	
	/**
	 * Description: Moves the robot 'backward' while rotating anti-clockwise.
	 * Returns a double[] array of the wheel speeds in the form 
	 * {speedLeftWheel, speedRightWheel}.
	 * @param wallEcomponents:
	 * 			  a double array containing the orientation of a
	 *            robot(rectangle) in the form {xOrientation, yOrientation}
	 */
	public static double[] moveDownLeft(double[] wallEcomponents) {
		Driver.LOGGER.info("moveDownLeft");
		Driver.LOGGER.log(CustomLevel.INSTRUCTION, "moveDownLeft");

		//cancel movement if battery is empty
				if(Driver.wallE.getBatteryLeft()<=0){
					System.out.println("Returning null");
					return null;
				}
				
		// Assume average speed is constant throughout the turn		
		Driver.wallE.setRotate(Driver.wallE.getRotate() - Math.abs(Driver.wallE.getAngularVelocity()));
		Driver.wallE.setxCoordinate(Driver.wallE.getxCoordinate() + Driver.wallE.getSpeed() * wallEcomponents[0]);
		Driver.wallE.setyCoordinate(Driver.wallE.getyCoordinate() - Driver.wallE.getSpeed() * wallEcomponents[1]);
		
		// get angular velocity and convert to radians
		double angularVelocity = (Driver.wallE.getAngularVelocity() * 2 * Math.PI) / 360.0;
		// get currentSpeed
		double currentSpeed = Driver.wallE.getSpeed();		
		
		// calculate wheelspeeds
		double speedRightWheel = (2 * currentSpeed + 
				angularVelocity * Driver.wallE.getAxleLength()) 
				/ (2 * Driver.wallE.getWheelRadius());
		double speedLeftWheel = (2 * currentSpeed -
				angularVelocity * Driver.wallE.getAxleLength())
				/ (2 * Driver.wallE.getWheelRadius());
		double[] wheelspeeds = {speedLeftWheel, speedRightWheel};
		Driver.wallE.setWheelspeeds(speedLeftWheel, speedRightWheel);
		
		Driver.LOGGER.fine(Arrays.toString(wheelspeeds));
		return wheelspeeds;
	}
	
	/**
	 * Description: Moves the robot 'backward' while rotating clockwise.
	 * Returns a double[] array of the wheel speeds in the form 
	 * {speedLeftWheel, speedRightWheel}.
	 * @param wallEcomponents:
	 * 			  a double array containing the orientation of a
	 *            robot(rectangle) in the form {xOrientation, yOrientation}
	 */
	public static double[] moveDownRight(double[] wallEcomponents) {
		Driver.LOGGER.info("moveDownRight");
		Driver.LOGGER.log(CustomLevel.INSTRUCTION, "moveDownRight");

		//cancel movement if battery is empty
				if(Driver.wallE.getBatteryLeft()<=0){
					System.out.println("Returning null");
					return null;
				}
		
		// Assume average speed is constant through turn
		Driver.wallE.setRotate(Driver.wallE.getRotate() + Math.abs(Driver.wallE.getAngularVelocity()));
		Driver.wallE.setxCoordinate(Driver.wallE.getxCoordinate() + Driver.wallE.getSpeed() * wallEcomponents[0]);
		Driver.wallE.setyCoordinate(Driver.wallE.getyCoordinate() - Driver.wallE.getSpeed() * wallEcomponents[1]);
				
		
		// get angular velocity and convert to radians
		double angularVelocity = (Driver.wallE.getAngularVelocity() * 2 * Math.PI) / 360.0;
		// get currentSpeed
		double currentSpeed = Driver.wallE.getSpeed();
		
		// calculate wheelspeeds
		double speedLeftWheel = (2 * currentSpeed + 
				angularVelocity * Driver.wallE.getAxleLength())
				/ (2 * Driver.wallE.getWheelRadius());
		double speedRightWheel = (2 * currentSpeed - 
				angularVelocity * Driver.wallE.getAxleLength())
				/ (2 * Driver.wallE.getWheelRadius());
		double[] wheelspeeds = {speedLeftWheel, speedRightWheel};
		Driver.wallE.setWheelspeeds(speedLeftWheel, speedRightWheel);
		
		Driver.LOGGER.fine(Arrays.toString(wheelspeeds));
		return wheelspeeds;
	}
	
	/**
	 * Description: Moves the robot 'forward' while rotating anti-clockwise.
	 * Returns a double[] array of the wheel speeds in the form 
	 * {speedLeftWheel, speedRightWheel}.
	 * @param wallEcomponents:
	 * 			  a double array containing the orientation of a
	 *            robot(rectangle) in the form {xOrientation, yOrientation}
	 */
	public static double[] moveUpLeft(double[] wallEcomponents) {
		Driver.LOGGER.info("moveUpLeft");
		Driver.LOGGER.log(CustomLevel.INSTRUCTION, "moveUpLeft");

		//cancel movement if battery is empty
				if(Driver.wallE.getBatteryLeft()<=0){
					System.out.println("Returning null");
					return null;
				}
		
		// Assume average speed is constant throughout turn
		Driver.wallE.setRotate(Driver.wallE.getRotate() - Math.abs(Driver.wallE.getAngularVelocity()));
		Driver.wallE.setxCoordinate(Driver.wallE.getxCoordinate() - Driver.wallE.getSpeed() * wallEcomponents[0]);
		Driver.wallE.setyCoordinate(Driver.wallE.getyCoordinate() + Driver.wallE.getSpeed() * wallEcomponents[1]);
	
		// get angular velocity and convert to radians
		double angularVelocity = (Driver.wallE.getAngularVelocity() * 2 * Math.PI) / 360.0;
		// get current speed
		double currentSpeed = Driver.wallE.getSpeed();
		
		// calculate wheelspeeds
		double speedLeftWheel = (2 * currentSpeed +
				angularVelocity * Driver.wallE.getAxleLength())
				/ (2 * Driver.wallE.getWheelRadius());
		double speedRightWheel = (2 * currentSpeed - 
				angularVelocity * Driver.wallE.getAxleLength())
				/ (2 * Driver.wallE.getWheelRadius());
		double[] wheelspeeds = {speedLeftWheel, speedRightWheel};
		Driver.wallE.setWheelspeeds(speedLeftWheel, speedRightWheel);
		
		Driver.LOGGER.fine(Arrays.toString(wheelspeeds));
		return wheelspeeds;
	}
	
	/**
	 * Description: Moves the robot 'forward' while rotating clockwise.
	 * Returns a double[] array of the wheel speeds in the form 
	 * {speedLeftWheel, speedRightWheel}.
	 * @param wallEcomponents:
	 * 			  a double array containing the orientation of a
	 *            robot(rectangle) in the form {xOrientation, yOrientation}
	 */
	public static double[] moveUpRight(double[] wallEcomponents) {
		Driver.LOGGER.info("moveUpRight");
		Driver.LOGGER.log(CustomLevel.INSTRUCTION, "moveUpRight");
		//cancel movement if battery is empty
				if(Driver.wallE.getBatteryLeft()<=0){
					System.out.println("Returning null");
					return null;
				}
		
		// Assume average speed is constant throughout turn
		Driver.wallE.setRotate(Driver.wallE.getRotate() + Math.abs(Driver.wallE.getAngularVelocity()));
		Driver.wallE.setxCoordinate(Driver.wallE.getxCoordinate() - Driver.wallE.getSpeed() * wallEcomponents[0]);
		Driver.wallE.setyCoordinate(Driver.wallE.getyCoordinate() + Driver.wallE.getSpeed() * wallEcomponents[1]);
	
		// get angular velocity and convert to radians
		double angularVelocity = (Driver.wallE.getAngularVelocity() * 2 * Math.PI) / 360.0;
		// get current speed
		double currentSpeed = Driver.wallE.getSpeed();
		
		// calculate wheelspeeds
		double speedRightWheel = (2 * currentSpeed + 
				angularVelocity * Driver.wallE.getAxleLength())
				/ (2 * Driver.wallE.getWheelRadius());
		double speedLeftWheel = (2 * currentSpeed - 
				angularVelocity * Driver.wallE.getAxleLength())
				/ (2 * Driver.wallE.getWheelRadius());
		double[] wheelspeeds = {speedLeftWheel, speedRightWheel};
		Driver.wallE.setWheelspeeds(speedLeftWheel, speedRightWheel);
		
		Driver.LOGGER.fine(Arrays.toString(wheelspeeds));
		return wheelspeeds;
	}
	
	/**
	 * Description: Turns the robot to the left
	 * 
	 */
	public static double[] moveLeft() {
		Driver.LOGGER.info("moveLeft");
		Driver.LOGGER.log(CustomLevel.INSTRUCTION, "moveLeft");
		//cancel movement if battery is empty
		if(Driver.wallE.getBatteryLeft()<=0){
			System.out.println("Returning null");
			return null;
		}
		
		Driver.wallE.setRotate(Driver.wallE.getRotate() - Math.abs(Driver.wallE.getAngularVelocity()));
		
		// get angularVelocity and convert to radians
		double angularVelocity = (Driver.wallE.getAngularVelocity() * 2 * Math.PI) / 360.0;
		// get current speed 
		double currentSpeed = Driver.wallE.getSpeed();
	
		double speedRightWheel = (2 * currentSpeed +
				angularVelocity * Driver.wallE.getAxleLength())
				/ (2 * Driver.wallE.getWheelRadius());
		double speedLeftWheel = (2 * currentSpeed -
				angularVelocity * Driver.wallE.getAxleLength())
				/ (2 * Driver.wallE.getWheelRadius());
		double[] wheelspeeds = {speedLeftWheel, speedRightWheel};
		Driver.wallE.setWheelspeeds(speedLeftWheel, speedRightWheel);
		
		Driver.LOGGER.fine(Arrays.toString(wheelspeeds));
		return wheelspeeds;
	
	}

	/**
	 * Description: Turns the robot to the right
	 * 
	 */
	public static double[] moveRight() {
		Driver.LOGGER.info("moveRight");
		Driver.LOGGER.log(CustomLevel.INSTRUCTION, "moveRight");

		//cancel movement if battery is empty
		if(Driver.wallE.getBatteryLeft()<=0){
			System.out.println("Returning null");
			return null;
		}
		
		Driver.wallE.setRotate(Driver.wallE.getRotate() + Math.abs(Driver.wallE.getAngularVelocity()));
		
		// get angularVelocity and convert to radians
		double angularVelocity = (Driver.wallE.getAngularVelocity() * 2 * Math.PI) / 360.0;
		// get current speed 
		double currentSpeed = Driver.wallE.getSpeed();
			
		double speedLeftWheel = (2 * currentSpeed +
				angularVelocity * Driver.wallE.getAxleLength())
				/ (2 * Driver.wallE.getWheelRadius());
		double speedRightWheel = (2 * currentSpeed -
				angularVelocity * Driver.wallE.getAxleLength())
				/ (2 * Driver.wallE.getWheelRadius());
		double[] wheelspeeds = {speedLeftWheel, speedRightWheel};
		Driver.wallE.setWheelspeeds(speedLeftWheel, speedRightWheel);
		
		Driver.LOGGER.fine(Arrays.toString(wheelspeeds));
		return wheelspeeds;
	
	}

	/**
	 * Description: causes the robot to decelerate
	 * 
	 * @param wallEcomponents:
	 *            a double array containing the orientation of a
	 *            robot(rectangle) in the form {xOrientation, yOrientation}
	 */
	public static void decelerate(double[] wallEcomponents) {
		Driver.LOGGER.info("decelerate");
		Driver.LOGGER.log(CustomLevel.INSTRUCTION, "decelerate");
		
		// Decelerate
		double speed = Driver.wallE.getSpeed() - Driver.wallE.getAcceleration();
		speed = (speed < 0) ? 0 : speed;
		Driver.wallE.setSpeed(speed);
		
		// Check whether down or up
		if (Driver.wallE.getLastUporDown().equals("UP")) {
			Driver.wallE.setxCoordinate(Driver.wallE.getxCoordinate() - Driver.wallE.getSpeed() * wallEcomponents[0]);
			Driver.wallE.setyCoordinate(Driver.wallE.getyCoordinate() + Driver.wallE.getSpeed() * wallEcomponents[1]);
		} else if (Driver.wallE.getLastUporDown().equals("DOWN")) {
			Driver.wallE.setxCoordinate(Driver.wallE.getxCoordinate() + Driver.wallE.getSpeed() * wallEcomponents[0]);
			Driver.wallE.setyCoordinate(Driver.wallE.getyCoordinate() - Driver.wallE.getSpeed() * wallEcomponents[1]);
		}

	}

}
