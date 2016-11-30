package robot;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import readers.FileReader;
import readers.InvalidFormatException;
import readers.XMLReader;
import tests.Driver;

public class Robot extends ImageView implements EventHandler<KeyEvent> {
	/** Class that represents the robot - its movement and perspective. 
	 * Overrides some JavaFX rectangle methods **/
	
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
	private Image looks;
	
	
	
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
		super.prefWidth(this.axleLength);
		this.wheelRadius = wheelRadius;
		super.prefHeight(this.wheelRadius);
	}
	/**
	 * Description: creates robot's parameters out of a xml file
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
		super.prefWidth(this.axleLength);
		this.wheelRadius = Double.valueOf(input.get(11));
		super.prefHeight(this.wheelRadius);
		looks = new Image(new File("src/eve.png").toURI().toString(), this.axleLength, this.wheelRadius, false, true);
		super.setImage(looks);
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
	
	public double getMaxSpeed() {
		return this.maxSpeed;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getAcceleration() {
		return this.acceleration;
	}
	
	public double getAngularVelocity() {
		/** Returns the robot's angular velocity in angle per second **/
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
		/** Set's the robot's speed and limits it to be less than or equal the 
		 * robot's max speed **/
		this.speed = (speed > this.maxSpeed) ? this.maxSpeed : speed;
	}
	
	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	
	public void setAcceleration(double acceleration) {
		/** Sets the robot's acceleration **/
		this.acceleration = acceleration;
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
		super.prefWidth(this.axleLength);
	}
	
	public void setWheelRadius(double radius) {
		/** Sets the radius of the robot's wheels to a given value 
		 * and calls the Rectangle.setHeight() method **/
		this.wheelRadius = radius;
		super.prefHeight(this.wheelRadius);
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
	
	public void render(GraphicsContext gc)
    {
//		ImageView view = new ImageView(looks);
//		view.setRotate(this.getRotate());
//		SnapshotParameters params = new SnapshotParameters();
//		params.setFill(Color.TRANSPARENT);
//		Image rotatedImage = view.snapshot(params, null);
//		gc.drawImage(looks, this.getxCoordinate(), this.getyCoordinate());

    }
	
	public double getOrientation() {
		/** Method that returns the orientation of the robot in radians 
		 * using the getRotate() method. **/
		
		// Get the orientation using the getRotate() method.
		double orientation = this.getRotate();
		orientation = (orientation < 0) ? orientation + 360.0 : orientation;
		orientation = (orientation > 360) ? orientation % 360.0 : orientation;
		// System.out.println("Angle: " + orientation);
		double orientationRadians = orientation / 180.0 * Math.PI;
		return orientationRadians;
	}
	public double[] getOrientationComponents(double orientationInRadians) {
		/** Method that resolves the robot's orientation to the x and y axis.
		 *  Returns a double array of the form {xOrientation, yOrientation} **/
		
		double xOrientation = Math.sin(orientationInRadians);
		double yOrientation = Math.cos(orientationInRadians);
		double[] orientationComponents = {xOrientation, yOrientation};
		return orientationComponents;
	}

	public void handle(KeyEvent event) {
		/** TODO Event handler for robot key events. **/
		
		/* Keydown */
		if (event.getEventType().equals(KeyEvent.KEY_PRESSED)) {
			switch (event.getCode()) {
				case UP: // increase forward velocity;
					// TODO
					Driver.currentKeyPresses[0] = event.getCode().toString();
					Driver.lastUporDown = event.getCode().toString();
					break;
				case DOWN: // increase backward velocity;
					Driver.currentKeyPresses[0] = event.getCode().toString();
					Driver.lastUporDown = event.getCode().toString();
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
			System.out.println(Arrays.toString(Driver.currentKeyPresses));
			event.consume();
			// Keyreleased
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