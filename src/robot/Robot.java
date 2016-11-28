package robot;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import readers.FileReader;
import readers.InvalidFormatException;
import readers.XMLReader;
import tests.Driver;

public class Robot extends Rectangle implements EventHandler<KeyEvent> {
	/** Class that represents the robot - its movement and perspective. 
	 * Overrides some JavaFX rectangle methods **/
	
	private String name;
	private double xCoordinate;
	private double yCoordinate;
	private double speed;
	private double xVelocity;
	private double yVelocity;
	private double xAcceleration;
	private double yAcceleration;
	private double angularVelocity;
	private double odometer;
	private double batteryLeft;
	private double batteryCapacity;
	private double axleLength;
	private double wheelRadius;

	public Robot(String name, double xCoordinate, double yCoordinate, double speed, double xVelocity, 
			double yVelocity, double xAcceleration, double yAcceleration, 
			double angularVelocity, double odometer, double batteryLeft, double batteryCapacity, 
			double axleLength, double wheelRadius) {
		/** Robot class constructor **/
		this.name = name;
		this.xCoordinate = xCoordinate;
		super.setX(this.xCoordinate);
		this.yCoordinate = yCoordinate;
		super.setY(this.yCoordinate);
		this.speed = speed;
		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
		this.xAcceleration = xAcceleration;
		this.yAcceleration = yAcceleration;
		this.angularVelocity = angularVelocity;
		this.odometer = odometer;
		this.batteryLeft = batteryLeft;
		this.batteryCapacity = batteryCapacity;
		this.axleLength = axleLength;
		super.setWidth(this.axleLength);
		this.wheelRadius = wheelRadius;
		super.setHeight(this.wheelRadius);
	}
	public Robot(String s) {
		/** Robot class constructor using an xml file **/
		XMLReader xmlr = new XMLReader();
		ArrayList<String> input = xmlr.read(s, "src/robots.xml");
		this.name = input.get(0);
		this.xCoordinate = Double.valueOf(input.get(1));
		super.setX(this.xCoordinate);
		this.yCoordinate = Double.valueOf(input.get(2));
		super.setY(this.yCoordinate);
		this.speed = Double.valueOf(input.get(3));
		this.xVelocity = Double.valueOf(input.get(4));
		this.yVelocity = Double.valueOf(input.get(5));
		this.xAcceleration = Double.valueOf(input.get(6));
		this.yAcceleration = Double.valueOf(input.get(7));
		this.angularVelocity = Double.valueOf(input.get(8));
		this.odometer = Double.valueOf(input.get(9));
		this.batteryLeft = Double.valueOf(input.get(10));
		this.batteryCapacity = Double.valueOf(input.get(11));
		this.axleLength = Double.valueOf(input.get(12));
		super.setWidth(this.axleLength);
		this.wheelRadius = Double.valueOf(input.get(13));
		super.setHeight(this.wheelRadius);
	}
	
	public double getxCoordinate() {
		/** Returns the robot's x coordinate **/
		return this.xCoordinate;
	}
	
	public double getyCoordinate() {
		/** Returns the robot's y coordinate. **/
		return this.yCoordinate;
	}
	
	public double getSpeed() {
		/** Returns the robot's speed. **/
		return this.speed;
	}
	
	public double getxVelocity() {
		/** Returns the robot's x Velocity **/
		return this.xVelocity;
	}
	
	public double getyVelocity() {
		/** Returns the robot's y velocity **/
		return this.yVelocity;
	}
	
	public double getxAcceleration() {
		/** Returns the robot's x acceleration **/
		return this.xAcceleration;
	}
	
	public double getyAcceleration() {
		/** Returns the robot's y acceleration **/
		return this.yAcceleration;
	}
	
	public double getAngularVelocity() {
		/** Returns the robot's angular velocity **/
		return this.angularVelocity;
	}
	
	public double getOdometer() {
		/** Returns the distance travelled by the robot so far **/
		return this.odometer;
	}
	
	public double getBatteryLeft() {
		/** Returns a double representing the amount of battery left **/
		return this.batteryLeft;
	}

	public double getAxleLength() {
		/** Returns the robot's axle length  **/
		return this.axleLength;
	}
	
	public double getWheelRadius() {
		/** Returns the radius of the robot's wheels  **/
		return this.wheelRadius;
	}
	
	public void setxCoordinate(double xCoordinate) {
		/** Sets the robot's x coordinate and calls the parent setX() method. **/
		this.xCoordinate = xCoordinate;
		super.setX(this.xCoordinate);
	}
	
	public void setyCoordinate(double yCoordinate) {
		/** Sets the robot's y coordinate and calls the parent setY() method.  **/
		this.yCoordinate = yCoordinate;
		super.setY(this.yCoordinate);
	}
	
	public void setSpeed(double speed) {
		/** Set's the robot's speed **/
		this.speed = speed;
	}
	
	public void setxVelocity(double xVelocity) {
		/** Sets the robot's x velocity **/
		this.xVelocity = xVelocity;
	}
	
	public void setyVelocity(double yVelocity) {
		/** Sets the robot's y velocity **/
		this.yVelocity = yVelocity;
	}
	
	public void setxAcceleration(double xAcceleration) {
		/** Sets the robot's x acceleration **/
		this.xAcceleration = xAcceleration;
	}
	
	public void setyAcceleration(double yAcceleration) {
		/** Sets the robot's y acceleration **/
		this.yAcceleration = yAcceleration;
	}
	
	public void setAngularVelocity(double angularVelocity) {
		/** Sets the robot's angular velocity **/
		this.angularVelocity = angularVelocity;
	}
	
	public void setOdometer(double distance) {
		/** Sets the odometer to a given distance **/
		this.odometer = distance;
	}
	
	public void setBatteryLeft(double batteryLeft) {
		/** Sets the battery left to a given value **/
		this.batteryLeft = batteryLeft;
	}
	
	public void setAxleLength(double axleLength) {
		/** Sets the axle length to a given value 
		 * and calls the Rectangle.setWidth() method 
		 * (Currently assumes wheels as having no width) **/
		this.axleLength = axleLength;
		super.setWidth(this.axleLength);
	}
	
	public void setWheelRadius(double radius) {
		/** Sets the radius of the robot's wheels to a given value 
		 * and calls the Rectangle.setHeight() method **/
		this.wheelRadius = radius;
		super.setHeight(this.wheelRadius);
	}

	public void batteryLowAlert() {
		// TODO implement audio alert
		if (this.batteryLeft < 10) {
			System.out.print("Battery low");
		}
	}

	public void decreaseCharge(double decrementValue) {
		/** Method used to decrease robot charge by a given value at 
		 * the end of every move. **/
		this.batteryLeft -= decrementValue;
	}
	
	public void decreaseCharge() {
		/** Method used to decrease robot charge at the 
		 * end of every move by a default value of 1, when no charge is given. **/
		this.batteryLeft -= 1;
	}

	public void handle(KeyEvent event) {
		/** TODO Event handler for robot key events. **/
		
		// Get the orientation using the getRotate() method.
		double orientation = this.getRotate();
		orientation = (orientation < 0) ? orientation + 360.0 : orientation;
		orientation = (orientation > 360) ? orientation % 360.0 : orientation;
//		System.out.println("Angle: " + orientation);
		double orientationRadians = orientation / 180.0 * Math.PI;
//		System.out.println("Radians: " + orientationRadians);
		double yOrientation = Math.cos(orientationRadians);
		double xOrientation = Math.sin(orientationRadians);
//		System.out.println("x: " + xOrientation  + ", y: " + yOrientation);
		
		/* Keydown */
		if (event.getEventType().equals(KeyEvent.KEY_PRESSED)) {
			switch (event.getCode()) {
				case UP: // increase forward velocity;
					// TODO
					Driver.currentKeyPresses[0] = event.getCode().toString();
					this.setxCoordinate(this.xCoordinate + this.speed * xOrientation);
					this.setyCoordinate(this.yCoordinate - this.speed  * yOrientation);
					break;
				case DOWN: // increase backward velocity;
					Driver.currentKeyPresses[0] = event.getCode().toString();
					this.setxCoordinate(this.xCoordinate - this.speed * xOrientation);
					this.setyCoordinate(this.yCoordinate + this.speed  * yOrientation);
					break;
				case LEFT: // rotate left
					Driver.currentKeyPresses[1] = event.getCode().toString();
					this.setRotate(this.getRotate() - Math.abs(this.angularVelocity));
					System.out.println(this.getRotate());
					
					break;
				case RIGHT: // rotate right
					Driver.currentKeyPresses[1] = event.getCode().toString();
					this.setRotate(this.getRotate() + Math.abs(this.angularVelocity));
					break;
				default:
					break;
			}
			System.out.println(Arrays.toString(Driver.currentKeyPresses));
			event.consume();
		} else if (event.getEventType().equals(KeyEvent.KEY_RELEASED)) {
			/** TODO **/
			switch (event.getCode()) {
				case UP: // 
					Driver.currentKeyPresses[0] = null;
					break;
				case DOWN: //
					Driver.currentKeyPresses[0] = null;
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
	public void moveViaFile(String path){
		// Get the orientation using the getRotate() method.
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
				// TODO
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
}}