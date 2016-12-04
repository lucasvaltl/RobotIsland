package robot;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import readers.FileReader;
import readers.InvalidFormatException;
import readers.XMLReader;
import tests.Driver;

/** Description: Class that represents the robot - its movement and perspective. 
 *  Overrides some JavaFX rectangle methods 
 * 
 * @author Geraint and Lucas
 *
 */
public class Robot extends Rectangle implements EventHandler<KeyEvent> {
	
	private String name;
	private double xCoordinate;
	private double yCoordinate;
	private double speed;
	private double maxSpeed;
	private double acceleration;
	private double angularVelocity;
	private double odometer;
	private double batteryLeft;
	private double batteryCapacity;
	private double axleLength;
	private double wheelRadius;

	/** Description: Verbose robot class constructor
	 * 
	 * @param name: TODO The robot's ID.
	 * @param xCoordinate: The robot's initial x position.
	 * @param yCoordinate: The robot's initial y position.
	 * @param speed: The robot's initial speed.
	 * @param maxSpeed: The robot's max speed.
	 * @param acceleration: The robot's acceleration.
	 * @param angularVelocity: The robot's angular velocity.
	 * @param odometer: The robot's initial distance travelled.
	 * @param batteryLeft: The robot's initial battery level.
	 * @param batteryCapacity: The robot's battery capacity.
	 * @param axleLength: The robot's axle length.
	 * @param wheelRadius: The robot's wheel radius.
	 */
	public Robot(String name, double xCoordinate, double yCoordinate, double speed, 
			double maxSpeed, double acceleration, double angularVelocity, double odometer, 
			double batteryLeft, double batteryCapacity, 
			double axleLength, double wheelRadius) {
		/** Robot class constructor **/
		this.name = name;
		this.xCoordinate = xCoordinate;
		super.setX(this.xCoordinate);
		this.yCoordinate = yCoordinate;
		super.setY(this.yCoordinate);
		this.speed = speed;
		this.maxSpeed = maxSpeed;
		this.acceleration = acceleration;
		this.angularVelocity = angularVelocity;
		this.odometer = odometer;
		this.batteryLeft = batteryLeft;
		this.batteryCapacity = batteryCapacity;
		this.axleLength = axleLength;
		super.setWidth(this.axleLength);
		this.wheelRadius = wheelRadius;
		super.setHeight(this.wheelRadius);
	}
	/**
	 * Description: Creates the robot's parameters from an XML file.
	 * @param s: The ID of the robot you want to load
	 */
	public Robot(String s) {
		
		XMLReader xmlr = new XMLReader();
		ArrayList<String> input = xmlr.read(s, "src/robots.xml");
		this.name = input.get(0);
		this.xCoordinate = Double.valueOf(input.get(1));
		super.setX(this.xCoordinate);
		this.yCoordinate = Double.valueOf(input.get(2));
		super.setY(this.yCoordinate);
		this.speed = Double.valueOf(input.get(3));
		this.maxSpeed = Double.valueOf(input.get(4));
		this.acceleration = Double.valueOf(input.get(5));
		this.angularVelocity = Double.valueOf(input.get(6));
		this.odometer = Double.valueOf(input.get(7));
		this.batteryLeft = Double.valueOf(input.get(8));
		this.batteryCapacity = Double.valueOf(input.get(9));
		this.axleLength = Double.valueOf(input.get(10));
		super.setWidth(this.axleLength);
		this.wheelRadius = Double.valueOf(input.get(11));
		super.setHeight(this.wheelRadius);
	}

	/** Description: Returns the robot's x coordinate.
	 * 
	 * @return: The robot's current x position.
	 */
	public double getxCoordinate() {
		return this.xCoordinate;
	}
	
	/** Description: Returns the robot's y coordinate
	 * 
	 * @return: The robot's current y position
	 */
	public double getyCoordinate() {
		return this.yCoordinate;
	}
	
	/** Description: Returns the robot's speed.
	 * 
	 * @return: The robot's current speed.
	 */
	public double getSpeed() {
		return this.speed;
	}
	
	/** Description: Returns the robot's max speed. 
	 * 
	 * @return: The robot's max speed.
	 */
	public double getMaxSpeed() {
		return this.maxSpeed;
	}
	
	/** Description: Return's the robot's acceleration
	 * 
	 * @return: The robot's acceleration.
	 */
	public double getAcceleration() {
		return this.acceleration;
	}
	
	/** Description: Returns the robot's angular velocity in degrees per second.
	 * 
	 * @return: The robot's angular velocity in degrees per second.
	 */
	public double getAngularVelocity() {
		return this.angularVelocity;
	}
	
	/** Description: Returns the robot's distance travelled.
	 * 
	 * @return: The robot's distance travelled.
	 */
	public double getOdometer() {
		return this.odometer;
	}
	
	/** Description: Returns a double representing the amount of battery left.
	 *  
	 * @return: A double representing the amount of battery left.
	 */
	public double getBatteryLeft() {
		return this.batteryLeft;
	}

	/** Description: Returns the robot's axle length.
	 * 
	 * @return: A double representing the robot's axle length.
	 */
	public double getAxleLength() {
		return this.axleLength;
	}
	
	/** Description: Returns the robot's wheel radius.
	 * 
	 * @return: A double representing the robot's wheel radius.
	 */
	public double getWheelRadius() {
		return this.wheelRadius;
	}
	
	/** Description: Sets the robot's x position to a given value and calls the 
	 *  parent .setX() method.
	 * 
	 * @param xCoordinate: The x position to be moved to.
	 */
	public void setxCoordinate(double xCoordinate) {
		this.xCoordinate = xCoordinate;
		super.setX(this.xCoordinate);
	}
	
	/** Description: Sets the robot's y position to a given value and calls the 
	 *  parent .setY() method.
	 * 
	 * @param yCoordinate: The y position to be moved to.
	 */
	public void setyCoordinate(double yCoordinate) {
		this.yCoordinate = yCoordinate;
		super.setY(this.yCoordinate);
	}
	
	/** Description: Sets the robot's speed and limits it to be less than or equal the 
	 *  robot's maximum speed.
	 * 
	 * @param speed: The speed the robot will set to, providing that it is less than or 
	 *  equal to the maximum speed.
	 */
	public void setSpeed(double speed) {
		this.speed = (speed > this.maxSpeed) ? this.maxSpeed : speed;
	}
	
	/** Description: Sets the robot's maximum speed.
	 * 
	 * @param maxSpeed: The speed to set as the robot's maximum speed.
	 */
	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	
	/** Description: Set's the robot's acceleration. 
	 * 
	 * @param acceleration: The acceleration to set as the robot's acceleration.
	 */
	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}
	
	/** Description: Set's the robot's angular velocity (in degrees per second).
	 * 
	 * @param angularVelocity: The value in degrees per second to set the robot's
	 *  angular velocity.
	 */
	public void setAngularVelocity(double angularVelocity) {
		this.angularVelocity = angularVelocity;
	}
	
	/** Description: Sets the robot's odometer to a given distance.
	 * 
	 * @param distance: The distance the robot has travelled so far.
	 */
	public void setOdometer(double distance) {
		this.odometer = distance;
	}
	
	/** Description: Sets the robot's battery level to a given value.
	 * 
	 * @param batteryLeft: The amount of battery left.
	 */
	public void setBatteryLeft(double batteryLeft) {
		this.batteryLeft = batteryLeft;
	}
	
	/** Description: Sets the axle length to a given value 
	 * and calls the Rectangle.setWidth() method 
	 * (Currently assumes wheels as having no width)
	 * 
	 * @param axleLength: The robot's axle length.
	 */
	public void setAxleLength(double axleLength) {
		this.axleLength = axleLength;
		super.setWidth(this.axleLength);
	}
	
	/** Description: Sets the radius of the robot's wheels to a given value 
	 * and calls the Rectangle.setHeight() method 
	 * 
	 * @param radius: The radius of the robot's wheels.
	 */
	public void setWheelRadius(double radius) {
		this.wheelRadius = radius;
		super.setHeight(this.wheelRadius);
	}

	/** 
	 *  Description: Alerts the user of the robot's low battery status.
	 */
	public void batteryLowAlert() {
		// TODO implement audio alert
		if (this.batteryLeft < 10) {
			System.out.print("Battery low");
		}
	}

	/** Description: Method used to decrease robot charge by a given value at 
	 *  the end of every move.
	 * 
	 * @param decrementValue: The value to decrement the robot's battery by.
	 */
	public void decreaseCharge(double decrementValue) {
		this.batteryLeft -= decrementValue;
	}
	
	/**
	 *  Description: Method used to decrease robot charge at the 
	 *  end of every move by a default value of 1, when no charge is given.  
	 */
	public void decreaseCharge() {
		this.batteryLeft -= 1;
	}
	
	/** Description: Method that returns the orientation of the robot in radians 
     * using the getRotate() method.
	 * 
	 * @return: The current orientation of the robot in radians relative to y axis, clockwise.
	 */
	public double getOrientation() {
		/**  **/
		
		// Get the orientation using the getRotate() method.
		double orientation = this.getRotate();
		orientation = (orientation < 0) ? orientation + 360.0 : orientation;
		orientation = (orientation > 360) ? orientation % 360.0 : orientation;
		// System.out.println("Angle: " + orientation);
		double orientationRadians = orientation / 180.0 * Math.PI;
		return orientationRadians;
	}
	
	/** Description: Method that resolves the robot's orientation to the x and y axis.
	 * 
	 * @param orientationInRadians: An orientation in radians to resolve.
	 * @return: a double array of the form {xOrientation, yOrientation}
	 */
	public double[] getOrientationComponents(double orientationInRadians) {		
		double xOrientation = Math.sin(orientationInRadians);
		double yOrientation = Math.cos(orientationInRadians);
		double[] orientationComponents = {xOrientation, yOrientation};
		return orientationComponents;
	}

	/** Description: Event handler for robot key events. 
	 *  Adds strings to currentKeyPresses array to keep track of 
	 *  which buttons are being held down. 
	 * 
	 */
	public void handle(KeyEvent event) {
		
		/* Keydown */
		if (event.getEventType().equals(KeyEvent.KEY_PRESSED)) {
			System.out.println(Arrays.toString(Driver.currentKeyPresses));
			switch (event.getCode()) {
				case UP: // increase forward velocity;
					/*Driver.decelerate = (Objects.equals(Driver.currentKeyPresses[0], 
							event.getCode().toString())) ? false : true; */
					Driver.decelerate = (Objects.equals(Driver.currentKeyPresses[0], 
							event.getCode().toString()) || 
							(Driver.currentKeyPresses[0] == null &&
							Driver.lastUporDown.equals(event.getCode().toString()) ||
							Driver.lastUporDown.isEmpty())) ? false : true;
					// decelerate is false when last currentKeyPresses[0] is "UP" or 
					// when last currentKeyPress is null and lastUpOrDown is "UP"
					
					Driver.currentKeyPresses[0] = event.getCode().toString();
					if (Driver.decelerate == false || (Driver.decelerate == true &&
							Driver.wallE.getSpeed() < 0.4)) { // hack
						// Only change last up or down if the robot is not in 
						// decelerate mode (stops weird behaviour when switching 
						// suddenly from up to down.
						Driver.lastUporDown = event.getCode().toString();	
					}
					break;
				case DOWN: // increase backward velocity;
					Driver.decelerate = (Objects.equals(Driver.currentKeyPresses[0], 
							event.getCode().toString()) || 
							(Driver.currentKeyPresses[0] == null &&
							(Driver.lastUporDown.equals(event.getCode().toString()) ||
									Driver.lastUporDown.isEmpty()))) ? false : true;
					// decelerate is false when currentKeyPresses[0] is "DOWN" or 
					// when last currentKeyPress is null and lastUpOrDown is "UP"
					
					Driver.currentKeyPresses[0] = event.getCode().toString();
					if (Driver.decelerate == false || (Driver.decelerate == true && 
							Driver.wallE.getSpeed() < 0.4)) { // hack
						// Only change last up or down if the robot is not in 
						// decelerate mode (stops weird behaviour when switching 
						// suddenly from up to down.
						Driver.lastUporDown = event.getCode().toString();	
					}
					break;
				case LEFT: // rotate left
					Driver.currentKeyPresses[1] = event.getCode().toString();
					break;
				case RIGHT: // rotate right
					Driver.currentKeyPresses[1] = event.getCode().toString();
					break;
				default:
					break;
			}
			event.consume();
			// Keyreleased
		} else if (event.getEventType().equals(KeyEvent.KEY_RELEASED)) {
			/**  **/
			switch (event.getCode()) {
				case UP: // 
					Driver.currentKeyPresses[0] = null;
					Driver.decelerate = true;
					break;
				case DOWN: //
					Driver.currentKeyPresses[0] = null;
					Driver.decelerate = true;
					break;
				case LEFT: //
					Driver.currentKeyPresses[1] = null;
					break;
				case RIGHT: //
					Driver.currentKeyPresses[1] = null;
					break;
			}
			event.consume();
		}		
	}
	
	/** Description: Method that reads moves from an input file and executes them in order.
	 * 
	 * @param path: The path of the file to read.
	 */
	public void moveViaFile(String path){
		// TODO Get the orientation using the getRotate() method.
		double orientation = this.getRotate();
		orientation = (orientation < 0) ? orientation + 360.0 : orientation;
		orientation = (orientation > 360) ? orientation % 360.0 : orientation;
//		System.out.println("Angle: " + orientation);
		double orientationRadians = orientation / 180.0 * Math.PI;
//		System.out.println("Radians: " + orientationRadians);
		double yOrientation = Math.cos(orientationRadians);
		double xOrientation = Math.sin(orientationRadians);
//		System.out.println("x: " + xOrientation  + ", y: " + yOrientation);
		FileReader fr = new FileReader();
		ArrayList<String> input;
		try {
			input = fr.scanFile(path);
			
			for (int i = 0; i< input.size(); i++){
				switch (input.get(i)){
				case "UP": // increase forward velocity;
					System.out.println("UP");
					this.setxCoordinate(this.xCoordinate + this.speed * xOrientation);
					this.setyCoordinate(this.yCoordinate - this.speed  * yOrientation);
					break;
				case "DOWN": // increase backward velocity;
					System.out.println("DOWN");				
					this.setxCoordinate(this.xCoordinate - this.speed * xOrientation);
					this.setyCoordinate(this.yCoordinate + this.speed  * yOrientation);
					break;
				case "LEFT": // rotate left
					System.out.println("LEFT");
					
					this.setRotate(this.getRotate() - Math.abs(this.angularVelocity));
					System.out.println(this.getRotate());
					
					break;
				case "RIGHT": // rotate right
					System.out.println("RIGHT");
					this.setRotate(this.getRotate() + Math.abs(this.angularVelocity));
					break;
				default:
					System.out.println("INVALID");
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	/** Description: Method that reads moves from an input file and 
	 *  executes them in order.
	 * 
	 * @param path: The path of the file to read.
	 *
	 */
	public void newMoveViaFile (String path) {
	// TODO
		FileReader fr = new FileReader();
		ArrayList<String> input;
		
		try {
			input = fr.scanFile(path);
			
			for (int i=0; i < input.size(); i++) {
				switch (input.get(i)) {
					case "[UP, null]": 
						Driver.currentKeyPresses[0] = "UP";
						Driver.currentKeyPresses[1] = null;
						break;
					case "[UP, LEFT]" :
						Driver.currentKeyPresses[0] = "UP";
						Driver.currentKeyPresses[1] = "LEFT";
						break;
					case "[UP, RIGHT]" :
						Driver.currentKeyPresses[0] = "UP";
						Driver.currentKeyPresses[1] = "RIGHT";
						break;
					case "[DOWN, null]":
						Driver.currentKeyPresses[0] = "DOWN";
						Driver.currentKeyPresses[1] = null;
						break;
					case "[DOWN, LEFT]":
						Driver.currentKeyPresses[0] = "DOWN";
						Driver.currentKeyPresses[1] = "LEFT";
						break;
					case "[DOWN, RIGHT]":
						Driver.currentKeyPresses[0] = "DOWN";
						Driver.currentKeyPresses[1] = "RIGHT";
						break;
					case "[null, null]":
						Driver.currentKeyPresses[0] = null;
						Driver.currentKeyPresses[1] = null;
						break;
					default: //
				}
			}
			
		} catch (Exception e) {
		e.printStackTrace();
		}
	}
}