package robot;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import map.Map;
import readers.NewFileReader;
import readers.NewerFileReader;
import readers.FileReader;
import readers.InvalidFormatException;
import readers.XMLReader;
import tests.Driver;

/**
 * Description: Class that represents the robot - its movement and perspective.
 * Overrides some JavaFX rectangle methods
 * 
 * @author Geraint and Lucas
 *
 */
public class Robot extends Entity implements EventHandler<KeyEvent> {

	private String name;
	private double xCoordinate;
	private double yCoordinate;
	private double speed;
	private double maxSpeed;
	private final double globalMaxSpeed;
	private double acceleration;
	private double angularVelocity;
	private double odometer;
	private double batteryLeft;
	private double batteryCapacity;
	private double axleLength;
	private double wheelRadius;
	protected String[] currentKeyPresses = new String[2];
	private String lastUporDown = "";
	private String lastMovement = "";
	private boolean decelerate = false;
	private ArrayList<String> inputCommands = null;
	private int inputCommandsIndex = 0;
	private boolean inputCommandsReadingInProgress = false;
	private double[] wheelspeeds = { 0, 0 };
	private Image skin;
	private ImagePattern[][] animimages;
	private int timeSinceCollision = 0;
	private boolean collisionDetected;
	private double distancetravelled;
	private boolean recharging;
	

	/**
	 * Description: Verbose robot class constructor
	 * 
	 * @param name:
	 *            The robot's ID.
	 * @param xCoordinate:
	 *            The robot's initial x position.
	 * @param yCoordinate:
	 *            The robot's initial y position.
	 * @param speed:
	 *            The robot's initial speed.
	 * @param maxSpeed:
	 *            The robot's max speed.
	 * @param acceleration:
	 *            The robot's acceleration.
	 * @param angularVelocity:
	 *            The robot's angular velocity.
	 * @param odometer:
	 *            The robot's initial distance travelled.
	 * @param batteryLeft:
	 *            The robot's initial battery level.
	 * @param batteryCapacity:
	 *            The robot's battery capacity.
	 * @param axleLength:
	 *            The robot's axle length.
	 * @param wheelRadius:
	 *            The robot's wheel radius.
	 */
	public Robot(String name, double xCoordinate, double yCoordinate, double speed, double maxSpeed,
			double acceleration, double angularVelocity, double odometer, double batteryLeft, double batteryCapacity,
			double axleLength, double wheelRadius) {
		/** Robot class constructor **/
		this.name = name;
		this.xCoordinate = xCoordinate;
		super.setX(this.xCoordinate);
		this.yCoordinate = yCoordinate;
		super.setY(this.yCoordinate);
		this.speed = speed;
		this.maxSpeed = maxSpeed;
		this.globalMaxSpeed = maxSpeed;
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
	 * 
	 * @param s:
	 *            The ID of the robot you want to load
	 */
	public Robot(String s) {

		XMLReader xmlr = new XMLReader();
		ArrayList<String> input = xmlr.read(s, "src/robots.xml");
		this.name = input.get(0);
		this.xCoordinate = Double.valueOf(input.get(1));
		this.setX(this.xCoordinate);
		this.yCoordinate = Double.valueOf(input.get(2));
		this.setY(this.yCoordinate);
		this.speed = Double.valueOf(input.get(3));
		this.maxSpeed = Double.valueOf(input.get(4));
		this.globalMaxSpeed = Double.valueOf(input.get(4));
		this.acceleration = Double.valueOf(input.get(5));
		this.angularVelocity = Double.valueOf(input.get(6));
		this.odometer = Double.valueOf(input.get(7));
		this.batteryLeft = Double.valueOf(input.get(8));
		this.batteryCapacity = Double.valueOf(input.get(9));
		this.axleLength = Double.valueOf(input.get(10));
		this.setWidth(this.axleLength);
		this.wheelRadius = Double.valueOf(input.get(11));
		this.setHeight(this.wheelRadius);
	}

	/**
	 * Description: Returns the robot's x coordinate.
	 * 
	 * @return: The robot's current x position.
	 */
	public double getxCoordinate() {
		return this.xCoordinate;
	}

	/**
	 * Description: Returns the robot's y coordinate
	 * 
	 * @return: The robot's current y position
	 */
	public double getyCoordinate() {
		return this.yCoordinate;
	}

	/**
	 * Description: Returns the robot's speed.
	 * 
	 * @return: The robot's current speed.
	 */
	public double getSpeed() {
		return this.speed;
	}

	/**
	 * Description: Returns the robot's max speed.
	 * 
	 * @return: The robot's max speed.
	 */
	public double getMaxSpeed() {
		return this.maxSpeed;
	}

	/**
	 * Description: Return's the robot's acceleration
	 * 
	 * @return: The robot's acceleration.
	 */
	public double getAcceleration() {
		return this.acceleration;
	}

	/**
	 * Description: Returns the robot's angular velocity in degrees per second.
	 * 
	 * @return: The robot's angular velocity in degrees per second.
	 */
	public double getAngularVelocity() {
		return this.angularVelocity;
	}

	/**
	 * Description: Returns the robot's distance travelled.
	 * 
	 * @return: The robot's distance travelled.
	 */
	public double getOdometer() {
		return this.odometer;
	}

	/**
	 * Description: Returns a double representing the amount of battery left.
	 * 
	 * @return: A double representing the amount of battery left.
	 */
	public double getBatteryLeft() {
		return this.batteryLeft;
	}

	/**
	 * Description: Returns a double representing the amount of battery
	 * capacity.
	 * 
	 * @return: A double representing the amount of battery capacity.
	 */
	public double getBatteryCapacity() {
		return this.batteryCapacity;
	}

	/**
	 * Description: Returns the robot's axle length.
	 * 
	 * @return: A double representing the robot's axle length.
	 */
	public double getAxleLength() {
		return this.axleLength;
	}

	/**
	 * Description: Returns the robot's wheel radius.
	 * 
	 * @return: A double representing the robot's wheel radius.
	 */
	public double getWheelRadius() {
		return this.wheelRadius;
	}

	/**
	 * Description: Method that returns the robot's current key presses.
	 * 
	 * @return A string array of length 2, representing the current key presses.
	 */
	public String[] getCurrentKeyPresses() {
		return this.currentKeyPresses;
	}

	/**
	 * Description: Method that returns the robot's last up or down command
	 * property.
	 * 
	 * @return: The last up or down command assigned to the lastUporDown field.
	 */
	public String getLastUporDown() {
		return this.lastUporDown;
	}

	/**
	 * Description: Method that returns the robot's last left or right command
	 * property.
	 * 
	 * @return: The last up or down command assigned to the lastUporDown field.
	 */
	public String getLastMovement() {
		return this.lastMovement;
	}

	/**
	 * Description: Method that returns a boolean to represent the robot's
	 * deceleration status
	 * 
	 * @return: A boolean that represents the robot's deceleration status.
	 */
	public boolean getDecelerate() {
		return this.decelerate;
	}

	/**
	 * Description: Method that returns a boolean which will be true when robot
	 * commands are being read from a file.
	 * 
	 * @return: A boolean that will be true when robot commands are being read
	 *          from a file.
	 */
	public boolean getInputCommandsReadingInProgress() {
		return this.inputCommandsReadingInProgress;
	}

	public double[] getWheelspeeds() {
		return this.wheelspeeds;
	}

	public boolean getCollisionDetected() {
		return this.collisionDetected;
	}
	
	public double getDistanceTravelled() {
		return this.distancetravelled;
	}
	
	/**
	 * Description: Sets the robot's x position to a given value and calls the
	 * parent .setX() method.
	 * 
	 * @param xCoordinate:
	 *            The x position to be moved to.
	 */
	public void setxCoordinate(double xCoordinate) {
		this.xCoordinate = xCoordinate;
		super.setX(this.xCoordinate);
	}

	/**
	 * Description: Sets the robot's y position to a given value and calls the
	 * parent .setY() method.
	 * 
	 * @param yCoordinate:
	 *            The y position to be moved to.
	 */
	public void setyCoordinate(double yCoordinate) {
		this.yCoordinate = yCoordinate;
		super.setY(this.yCoordinate);
	}

	/**
	 * Description: Sets the robot's speed and limits it to be less than or
	 * equal the robot's maximum speed.
	 * 
	 * @param speed:
	 *            The speed the robot will set to, providing that it is less
	 *            than or equal to the maximum speed.
	 */
	public void setSpeed(double speed) {
		this.speed = (speed > this.maxSpeed) ? this.maxSpeed : speed;
	}

	/**
	 * Description: Sets the robot's maximum speed.
	 * 
	 * @param maxSpeed:
	 *            The speed to set as the robot's maximum speed.
	 */
	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	/**
	 * Description: Set's the robot's acceleration.
	 * 
	 * @param acceleration:
	 *            The acceleration to set as the robot's acceleration.
	 */
	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}

	/**
	 * Description: Set's the robot's angular velocity (in degrees per second).
	 * 
	 * @param angularVelocity:
	 *            The value in degrees per second to set the robot's angular
	 *            velocity.
	 */
	public void setAngularVelocity(double angularVelocity) {
		this.angularVelocity = angularVelocity;
	}

	/**
	 * Description: Sets the robot's odometer to a given distance.
	 * 
	 * @param distance:
	 *            The distance the robot has travelled so far.
	 */
	public void setOdometer(double distance) {
		this.odometer = distance;
	}

	/**
	 * Description: Sets the robot's battery level to a given value.
	 * 
	 * @param batteryLeft:
	 *            The amount of battery left.
	 */
	public void setBatteryLeft(double batteryLeft) {
		this.batteryLeft = batteryLeft;
	}

	/**
	 * Description: Sets the axle length to a given value and calls the
	 * Rectangle.setWidth() method (Currently assumes wheels as having no width)
	 * 
	 * @param axleLength:
	 *            The robot's axle length.
	 */
	public void setAxleLength(double axleLength) {
		this.axleLength = axleLength;
		super.prefWidth(this.axleLength);
	}

	/**
	 * Description: Sets the radius of the robot's wheels to a given value and
	 * calls the Rectangle.setHeight() method
	 * 
	 * @param radius:
	 *            The radius of the robot's wheels.
	 */
	public void setWheelRadius(double radius) {
		this.wheelRadius = radius;
		super.prefHeight(this.wheelRadius);
	}

	/**
	 * Description: Method that sets a particular index in the CurrentKeyPresses
	 * string array to a given value.
	 * 
	 * @param index:
	 *            The index of the value to change (must be either 0 or 1.
	 * @param value:
	 *            The value to change to.
	 */
	public void setCurrentKeyPresses(int index, String value) {
		if ((index == 0 || index == 1) && (Objects.equals(value, "UP") || Objects.equals(value, "DOWN"))) {
			this.currentKeyPresses[index] = value;
		} else if ((index == 0 || index == 1) && Objects.equals(value, null)) {
			this.currentKeyPresses[index] = null;
		}
	}

	/**
	 * Description: Method used to set the robot's last up or down property.
	 * 
	 * @param value:
	 *            The string to be set (must be "UP", "DOWN", or "null")
	 */
	public void setLastUporDown(String value) {
		if (Objects.equals(value, "UP") || Objects.equals(value, "DOWN")) {
			this.lastUporDown = value;
		} else if (Objects.equals(value, "null")) {
			this.lastUporDown = null;
		}
	}

	/**
	 * Description: Method used to set the robot's last up or down property.
	 * 
	 * @param value:
	 *            The string to be set (must be "LEFT", "RIGHT", or "null")
	 */
	public void setLastMovement(String move) {
		this.lastMovement = move;
	}

	/**
	 * Description: Method used to flag the robot's deceleration status.
	 * 
	 * @param value:
	 *            The boolean value to be set.
	 */
	public void setDecelerate(boolean value) {
		this.decelerate = value;
	}

	public void setWheelspeeds(double left, double right) {
		this.wheelspeeds[0] = left;
		this.wheelspeeds[1] = right;
	}

	public void setCollisionDetected(boolean b) {
		this.collisionDetected = b;
	}
	
	public void setInputCommandsReadingInProgress(boolean value) {
		this.inputCommandsReadingInProgress = value;
	}

	/**
	 * Description: Alerts the user of the robot's low battery status.
	 */
	public void batteryLowAlert() {
		// TODO implement audio alert
//		if (this.batteryLeft < 10) {
//			Image pattern = new Image(new File("src/eveLowBattery.png").toURI().toString(), 32, 48, false, true);
//			ImagePattern skin = new ImagePattern(pattern);
//			Driver.wallE.setFill(skin);
//			this.setMaxSpeed(1);
//		}
	}

	public ImagePattern getAnimatedImage(int i, int j) {
		return this.animimages[i][j];
	}

	public void createAnimatedImages() {
		Image[][] images = new Image[5][3];
		animimages = new ImagePattern[5][3];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 3; j++) {
				images[i][j] = new Image(new File("src/img/eve" + i + "" + j + ".png").toURI().toString(),
						this.getWidth(), this.getWidth(), false, true);

			}
		}
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 3; j++) {
				ImagePattern thisimage = new ImagePattern(images[i][j]);

				animimages[i][j] = thisimage;

			}
		}

	}

	/**
	 * Description: Animate the robot to reflect it's behavior. Direction/speed
	 * will change the position of its eyes, where as a collision will change
	 * the
	 * 
	 * @return
	 */
	public boolean animate(double[] wallEcomponents) {

		if(recharging){
			this.setFill(this.getAnimatedImage(4, 0));
			// used to jump out of method
			return false;
		}
		
		
		// warn if battery is below 20%
		if ((this.batteryLeft > (this.getBatteryCapacity() / 10))
				&& this.batteryLeft < (this.getBatteryCapacity() / 5)) {
			this.setFill(this.getAnimatedImage(3, 1));
			// used to jump out of method
			return false;
		}

		// warn if battery is below 10%
		if (this.batteryLeft < (this.getBatteryCapacity() / 10)) {
			this.setFill(this.getAnimatedImage(3, 2));
			// used to jump out of method
			return false;
		}

		// check if the robot collided recently
		if (this.getCollisionDetected()) {
			if (this.timeSinceCollision == 80) {
				this.setCollisionDetected(false);
				this.timeSinceCollision = 0;
			} else {
				this.setFill(this.getAnimatedImage(3, 0));
				this.timeSinceCollision++;
				// used to break out of method
				return false;
			}
		}
		
		//check if robot is even moving:
		if(this.wheelspeeds[0] == 0 && this.wheelspeeds[1] == 0 &&this.speed==0){
			this.setFill(this.getAnimatedImage(1, 1));
			return false;
		}

		// animate eyes to look into direction of movement
		int i = 1;
		int j = 1;
		
		
		if(this.getLastUporDown().equals("UP")){
		if (this.speed == maxSpeed) {
			i = 2;
		}
		if (this.wheelspeeds[0] == this.wheelspeeds[1]) {

		} else if (this.speed == 0 && this.wheelspeeds[0] > this.wheelspeeds[1] || this.decelerate == true && this.wheelspeeds[0] > this.wheelspeeds[1]) {
			j = 0;
		} else if (this.speed == 0 && this.wheelspeeds[0] < this.wheelspeeds[1] || this.decelerate == true && this.wheelspeeds[0] < this.wheelspeeds[1]) {
			j = 2;
		} else if (this.wheelspeeds[0] < this.wheelspeeds[1]) {
			j = 0;
		} else {
			j = 2;
		} 
		this.setFill(this.getAnimatedImage(i, j));
		
		}
		
		if(this.getLastUporDown().equals("DOWN")){
			i = 0;

			if (this.speed == 0) {
				i = 1;
			}

			if (this.wheelspeeds[0] == this.wheelspeeds[1]) {

			} else if (this.speed == 0 && this.wheelspeeds[0] > this.wheelspeeds[1]) {
				j = 0;
			} else if (this.speed == 0 && this.wheelspeeds[0] < this.wheelspeeds[1]) {
				j = 2;
			} else if (this.wheelspeeds[0] < this.wheelspeeds[1]) {
				j = 0;
			} else {
				j = 2;
			}
			this.setFill(this.getAnimatedImage(i, j));
			
			}
		// used to break out of method
		return true; 
	}

	/*
	 * Description: Consume battery when moving. If battery is low (less than
	 * 10%) the robots speed will decrease significantly. If the battery is
	 * empty, the robot will fail to move.
	 * 
	 * @author Geraint and Lucas
	 */

	protected void consumeBattery(double[] ds) {

		
		if (this.getSpeed() > 0) {
			if (this.getBatteryLeft() >= (this.getBatteryCapacity() / 10)) {
				//reduce battery relative to distance traveled
				this.decreaseCharge(0.25*this.getSpeed());
			}
			// if battery lower than 10% of charge, reduce speed, consume less battery
			if ((this.getBatteryLeft() > 0) && (this.getBatteryLeft() < (this.getBatteryCapacity() / 10))) {
				this.decreaseCharge(0.25);
				this.setMaxSpeed(1);
				if(Driver.toggledevmode){
					Driver.textinfo.setText("Battery less than 10%!!!");
					
					if (Driver.batteryLowSound.isPlaying() == false) {
						Driver.batteryLowSound.play(0.4);
					}
					
				}
			}
		}//decrease charge if robot is turning around its own axis (wheelspeeds unequal) but not moving forward
		else if (this.getSpeed() == 0 && (ds[0]!= ds[1])) {
			if (this.getBatteryLeft() >= (this.getBatteryCapacity() / 10)) {
				this.decreaseCharge(0.0625);
			}
			// if battery lower than 10% of charge, reduce speed, consume less
			// battery
			if ((this.getBatteryLeft() > 0) && (this.getBatteryLeft() < (this.getBatteryCapacity() / 10))) {
				this.decreaseCharge(0.0625);
				this.setMaxSpeed(1);
				if(Driver.toggledevmode){
					Driver.textinfo.setText("Battery less than 10%!!!");
				
					if (Driver.batteryLowSound.isPlaying() == false) {
						Driver.batteryLowSound.play();
					}
				}
			}
		}
		// if battery empty restrict movement
		if (this.getBatteryLeft() < 0) {
			this.setMaxSpeed(0);
			this.setBatteryLeft(0);
			
			if (Driver.batteryLowSound.isPlaying() == true) {
				Driver.batteryLowSound.stop();
			}
			if (Driver.batteryDeadSound.isPlaying() == false) {
				Driver.batteryDeadSound.play();
			}
			
		}

	}

	/**
	 * Description: Method used to decrease robot charge by a given value at the
	 * end of every move.
	 * 
	 * @param decrementValue:
	 *            The value to decrement the robot's battery by.
	 */
	public void decreaseCharge(double decrementValue) {
		this.batteryLeft -= decrementValue;
	}

	/**
	 * Description: Method used to decrease robot charge at the end of every
	 * move by a default value of 1, when no charge is given.
	 */
	public void decreaseCharge() {
		this.batteryLeft -= 1;
	}
	
	/**
	 * 
	 * Description: Recharges the robot by a set value
	 * 
	 * @param decrementValue: amount by which the robot is charged
	 */
	public void increaseCharge(double decrementValue){
	
		if(this.getBatteryLeft() < this.getBatteryCapacity()){
			this.batteryLeft += decrementValue;
		}
		//avoids overcharging
		else if (this.getBatteryLeft() > this.getBatteryCapacity()){
			this.batteryLeft = this.getBatteryCapacity();
			if(Driver.toggledevmode){
				Driver.textinfo.setText("Recharged!");
			
				if (Driver.batteryLowSound.isPlaying() == true) {
					Driver.batteryLowSound.stop();
				}
				if (Driver.batteryDeadSound.isPlaying() == false) {
					Driver.batteryDeadSound.play();
				}
			}
		}
		this.setMaxSpeed(this.globalMaxSpeed);
		
	}
	
	/**
	 * Description: checks if the robot is in the refueling area. If it is there the robot is charged.
	 * 
	 */
	public void checkForCharging(){
		if(CollisionDetection.chargingDetection(this, Map.chargingstation)){
			this.increaseCharge(5);
			this.recharging = true;
			//reset time since collision to overwrite collisions happening in the charging station
			this.setCollisionDetected(false);
			this.timeSinceCollision = 0;
			if(Driver.toggledevmode){
				if(Driver.textinfo.getText().equals("Battery less than 10%!!!")){
					Driver.textinfo.setText("Recharging!");
				}}
			
		} else if (this.recharging == true){
			this.recharging = false;
		}
	}

	/**
	 * Description: update the robots distance travelled
	 * 
	 */
	protected void updateDistance(){
		this.distancetravelled += this.speed;
	}
	
	/**
	 * Updates the Developer panel if developer mode is turned on
	 * 
	 */
	
	public void updateDevPanel(){
		
			Point2D centercoordinates = this.center();
			DecimalFormat numberFormat = new DecimalFormat("#.0"); 
			Driver.textx.setText(numberFormat.format(centercoordinates.getX()));
			Driver.texty.setText(numberFormat.format(centercoordinates.getY()));
			Driver.textcharge.setText(numberFormat.format(this.getBatteryLeft()/this.getBatteryCapacity()*100) + "%");
			Driver.textdistance.setText(numberFormat.format(this.getDistanceTravelled()));
			//calculate orientation of robots front based on the default getRotate() method;
			double currentrotation;
			if(this.getRotate() >0){
				currentrotation = this.getRotate()%360 ;
			} else{
				currentrotation = this.getRotate()%360 +360;
			}
			if(currentrotation>180){
				currentrotation -=180;
			}else{
				currentrotation +=180;
			}
			Driver.textangle.setText(numberFormat.format(currentrotation));
			
		
	}

	/**
	 * Description: Event handler for robot key events. Adds strings to
	 * currentKeyPresses array to keep track of which buttons are being held
	 * down.
	 * 
	 *  Logs to src/logs file
	 */
	public void handle(KeyEvent event) {

		/* Keydown */
		if (event.getEventType().equals(KeyEvent.KEY_PRESSED)) {
			// System.out.println(Arrays.toString(this.currentKeyPresses));
			
			switch (event.getCode()) {
			case UP: // increase forward velocity;
				/*
				 * this.decelerate = (Objects.equals(this.currentKeyPresses[0],
				 * event.getCode().toString())) ? false : true;
				 */
				this.decelerate = (Objects.equals(this.currentKeyPresses[0], event.getCode().toString())
						|| (this.currentKeyPresses[0] == null && this.lastUporDown.equals(event.getCode().toString())
								|| this.lastUporDown.isEmpty())) ? false : true;
				// decelerate is false when last currentKeyPresses[0] is "UP" or
				// when last currentKeyPress is null and lastUpOrDown is "UP"

				this.currentKeyPresses[0] = event.getCode().toString();
				if (this.decelerate == false || (this.decelerate == true && Driver.wallE.getSpeed() < 0.6)) { // hack
					// Only change last up or down if the robot is not in
					// decelerate mode (stops weird behaviour when switching
					// suddenly from up to down.
					this.lastUporDown = event.getCode().toString();
				}
				break;
			case DOWN: // increase backward velocity;
				this.decelerate = (Objects.equals(this.currentKeyPresses[0], event.getCode().toString())
						|| (this.currentKeyPresses[0] == null && (this.lastUporDown.equals(event.getCode().toString())
								|| this.lastUporDown.isEmpty()))) ? false : true;
				// decelerate is false when currentKeyPresses[0] is "DOWN" or
				// when last currentKeyPress is null and lastUpOrDown is "UP"

				this.currentKeyPresses[0] = event.getCode().toString();
				if (this.decelerate == false || (this.decelerate == true && Driver.wallE.getSpeed() < 0.6)) { // hack
					// Only change last up or down if the robot is not in
					// decelerate mode (stops weird behaviour when switching
					// suddenly from up to down.
					this.lastUporDown = event.getCode().toString();
				}
				break;
			case LEFT: // rotate left
				this.currentKeyPresses[1] = event.getCode().toString();

				break;
			case RIGHT: // rotate right
				this.currentKeyPresses[1] = event.getCode().toString();

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
				this.currentKeyPresses[0] = null;
				this.decelerate = true;
				break;
			case DOWN: //
				this.currentKeyPresses[0] = null;
				this.decelerate = true;
				break;
			case LEFT: //
				this.currentKeyPresses[1] = null;
				break;
			case RIGHT: //
				this.currentKeyPresses[1] = null;
				break;
			}
			event.consume();
		}
	}

	/**
	 * Descripion: Moves the robot depending on keypresses. It calls the static
	 * methods in the movement class to translate and transform the robot, and
	 * calculate the relative velocities of the left and right wheels.
	 * 
	 * @param wallEcomponents
	 *            orientation components derived from the robots orientation
	 */
	public void move(double[] wallEcomponents) {

		if (this.getCurrentKeyPresses()[0] == "UP" && this.getCurrentKeyPresses()[1] == "LEFT") {

			if (this.getDecelerate() == true) {
				Movement.decelerate(wallEcomponents);
			} else {
				Movement.moveUpLeft(wallEcomponents);
			}
			this.setLastMovement("moveUpLeft");

		} else if (this.getCurrentKeyPresses()[0] == "UP" && this.getCurrentKeyPresses()[1] == "RIGHT") {

			if (this.getDecelerate() == true) {
				Movement.decelerate(wallEcomponents);
			} else {
				Movement.moveUpRight(wallEcomponents);
			}
			this.setLastMovement("moveUpRight");

		} else if (this.getCurrentKeyPresses()[0] == "DOWN" && this.getCurrentKeyPresses()[1] == "LEFT") {
			if (this.getDecelerate() == true) {
				Movement.decelerate(wallEcomponents);
			} else {
				Movement.moveDownLeft(wallEcomponents);
			}
			this.setLastMovement("moveDownLeft");

		} else if (this.getCurrentKeyPresses()[0] == "DOWN" && this.getCurrentKeyPresses()[1] == "RIGHT") {
			if (this.getDecelerate() == true) {
				Movement.decelerate(wallEcomponents);
			} else {
				Movement.moveDownRight(wallEcomponents);
			}
			this.setLastMovement("moveDownRight");

		} else if (this.getCurrentKeyPresses()[0] == "UP") {
			if (this.getDecelerate() == true) {
				// Robot must decelerate after previous motion in the opposite
				// direction
				Movement.decelerate(wallEcomponents);
			} else {
				// accelerate
				Movement.moveUp(wallEcomponents);
				this.setLastMovement("moveUp");
			}

		} else if (this.getCurrentKeyPresses()[0] == "DOWN") {

			if (this.getDecelerate() == true) {
				// Robot must decelerate after previous motion in the opposite
				// direction
				Movement.decelerate(wallEcomponents);
			} else {
				// accelerate
				Movement.moveDown(wallEcomponents);
				this.setLastMovement("moveDown");
			}

		} else if (this.getCurrentKeyPresses()[1] == "LEFT") {
			Movement.moveLeft();
			this.setLastMovement("moveLeft");
			// allows robot to turn left during deceleration
			if (this.getDecelerate() == true) {
				Movement.decelerate(wallEcomponents);
			}

		} else if (this.getCurrentKeyPresses()[1] == "RIGHT") {
			Movement.moveRight();
			this.setLastMovement("moveRight");
			// allows robot to turn right during deceleration
			if (this.getDecelerate() == true) {
				Movement.decelerate(wallEcomponents);
			}

		} else if (this.getCurrentKeyPresses()[0] == null) {
			Movement.decelerate(wallEcomponents);

		}

		// change decelerate flag to false if speed is 0
		if (this.getSpeed() <= 0) {
			this.setDecelerate(false);
			// Driver.lastUporDown = "";
		}
	}

	/**
	 * Description: Detects if the robot is about to collide with a boundry and
	 * reacts accordingly
	 * 
	 * @param robot
	 *            : robot you want to check for collisions
	 * @param wallEcomponents:
	 *            robots orientation components derived from its orientation
	 * 
	 * @author Geraint and Lucas
	 */
	public void detectCollision(Robot robot, double[] wallEcomponents) {
		if (CollisionDetection.collisionDetection(robot)) {
			robot.setCollisionDetected(true);
			if (robot.getLastMovement().equals("moveDown")) {
				while (CollisionDetection.collisionDetection(robot)) {
					Movement.moveUp(wallEcomponents);
				}
				robot.setSpeed(0);
			} else if (robot.getLastMovement().equals("moveUp")) {
				while (CollisionDetection.collisionDetection(robot)) {
					Movement.moveDown(wallEcomponents);
				}
				robot.setSpeed(0);
			} else if (robot.getLastMovement().equals("moveLeft")) {
				while (CollisionDetection.collisionDetection(robot)) {
					Movement.moveRight();
				}
				robot.setSpeed(0);
			} else if (robot.getLastMovement().equals("moveRight")) {
				while (CollisionDetection.collisionDetection(robot)) {
					Movement.moveLeft();
				}
				robot.setSpeed(0);
			} else if (robot.getLastMovement().equals("moveUpLeft")) {
				while (CollisionDetection.collisionDetection(robot)) {
					Movement.moveDownRight(wallEcomponents);
				}
				robot.setSpeed(0);
			} else if (robot.getLastMovement().equals("moveUpRight")) {
				while (CollisionDetection.collisionDetection(robot)) {
					Movement.moveDownLeft(wallEcomponents);
				}
				robot.setSpeed(0);
			} else if (robot.getLastMovement().equals("moveDownLeft")) {
				while (CollisionDetection.collisionDetection(robot)) {
					Movement.moveUpRight(wallEcomponents);
				}
				robot.setSpeed(0);
			} else if (robot.getLastMovement().equals("moveDownRight")) {
				while (CollisionDetection.collisionDetection(robot)) {
					Movement.moveUpLeft(wallEcomponents);
				}
				robot.setSpeed(0);
			}

		}
	}

	/**
	 * Description: Method that reads moves from an input file and executes them
	 * in order.
	 * 
	 * @param path:
	 *            The path of the file to read.
	 */
	public void moveViaFile(String path) {
		// TODO Get the orientation using the getRotate() method.
		double orientation = this.getRotate();
		orientation = (orientation < 0) ? orientation + 360.0 : orientation;
		orientation = (orientation > 360) ? orientation % 360.0 : orientation;
		// System.out.println("Angle: " + orientation);
		double orientationRadians = orientation / 180.0 * Math.PI;
		// System.out.println("Radians: " + orientationRadians);
		double yOrientation = Math.cos(orientationRadians);
		double xOrientation = Math.sin(orientationRadians);
		// System.out.println("x: " + xOrientation + ", y: " + yOrientation);
		FileReader fr = new FileReader();
		ArrayList<String> input;
		try {
			input = fr.scanFile(path);

			for (int i = 0; i < input.size(); i++) {
				switch (input.get(i)) {
				case "UP": // increase forward velocity;
					System.out.println("UP");
					this.setxCoordinate(this.xCoordinate + this.speed * xOrientation);
					this.setyCoordinate(this.yCoordinate - this.speed * yOrientation);
					break;
				case "DOWN": // increase backward velocity;
					System.out.println("DOWN");
					this.setxCoordinate(this.xCoordinate - this.speed * xOrientation);
					this.setyCoordinate(this.yCoordinate + this.speed * yOrientation);
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

	/**
	 * Description: Method that reads moves from an input file and executes them
	 * in order.
	 * 
	 * @param path: The path of the file to read.
	 * @param wallEcomponents: The robot's orientation relative to the x and y axes.
	 */
	public void anotherSingleMoveViaFile(File file, double[] wallEcomponents) {
		if (this.inputCommands == null) {
			// No commands in file, load them up.
			this.inputCommands = new ArrayList<String>();
			this.inputCommandsReadingInProgress = true;
			NewerFileReader nfr = null;
			try {
				nfr = new NewerFileReader();
				this.inputCommands = nfr.scanFile(file);
				
			} catch (InvalidFormatException e) {
//				Driver.labelinfo.setText("WARNING: Invalid command in text file");
				Driver.LOGGER.severe("WARNING: Invalid command in text file "+ e.toString());
				this.inputCommandsReadingInProgress = false;
				return;
			}catch (FileNotFoundException e) {
//				Driver.labelinfo.setText("WARNING: File not found");
				Driver.LOGGER.severe("WARNING: File not found "+ e.toString());
				this.inputCommandsReadingInProgress = false;
				return;
			}
			return;
		}

		if (this.inputCommandsReadingInProgress == true) {
			if (this.inputCommands.get(this.inputCommandsIndex).equals("moveUpLeft")) {

			if (this.getDecelerate() == true) {
				Movement.decelerate(wallEcomponents);
			} else {
				Movement.moveUpLeft(wallEcomponents);
			}
			this.setLastMovement("moveUpLeft");

		} else if (this.inputCommands.get(this.inputCommandsIndex).equals("moveUpRight")) {

			if (this.getDecelerate() == true) {
				Movement.decelerate(wallEcomponents);
			} else {
				Movement.moveUpRight(wallEcomponents);
			}
			this.setLastMovement("moveUpRight");

		} else if (this.inputCommands.get(this.inputCommandsIndex).equals("moveDownLeft")) {
			if (this.getDecelerate() == true) {
				Movement.decelerate(wallEcomponents);
			} else {
				Movement.moveDownLeft(wallEcomponents);
			}
			this.setLastMovement("moveDownLeft");

		} else if (this.inputCommands.get(this.inputCommandsIndex).equals("moveDownRight")) {
			if (this.getDecelerate() == true) {
				Movement.decelerate(wallEcomponents);
			} else {
				Movement.moveDownRight(wallEcomponents);
			}
			this.setLastMovement("moveDownRight");

		} else if (this.inputCommands.get(this.inputCommandsIndex).equals("moveUp")) {
			if (this.getDecelerate() == true) {
				// Robot must decelerate after previous motion in the opposite
				// direction
				Movement.decelerate(wallEcomponents);
			} else {
				// accelerate
				Movement.moveUp(wallEcomponents);
				this.setLastMovement("moveUp");
			}

		} else if (this.inputCommands.get(this.inputCommandsIndex).equals("moveDown")) {

			if (this.getDecelerate() == true) {
				// Robot must decelerate after previous motion in the opposite
				// direction
				Movement.decelerate(wallEcomponents);
			} else {
				// accelerate
				Movement.moveDown(wallEcomponents);
				this.setLastMovement("moveDown");
			}

		} else if (this.inputCommands.get(this.inputCommandsIndex).equals("moveLeft")) {
			Movement.moveLeft();
			this.setLastMovement("moveLeft");
			// allows robot to turn left during deceleration
			if (this.getDecelerate() == true) {
				Movement.decelerate(wallEcomponents);
			}

		} else if (this.inputCommands.get(this.inputCommandsIndex).equals("moveRight")) {
			Movement.moveRight();
			this.setLastMovement("moveRight");
			// allows robot to turn right during deceleration
			if (this.getDecelerate() == true) {
				Movement.decelerate(wallEcomponents);
			}

		} else if (this.inputCommands.get(this.inputCommandsIndex).equals("decelerate")) {
			Movement.decelerate(wallEcomponents);

		}

		// change decelerate flag to false if speed is 0
		if (this.getSpeed() <= 0) {
			this.setDecelerate(false);
			// Driver.lastUporDown = "";
		}
			}
		

		// get the inputCommands arrayList size
		if (this.inputCommandsIndex >= this.inputCommands.size() - 1) {
			// Cause deceleration
			this.inputCommandsReadingInProgress = false;
			this.inputCommandsIndex = 0;
			this.currentKeyPresses[0] = null;
			this.currentKeyPresses[1] = null;
		} else {
			this.inputCommandsIndex++;
			
		}
}

	/**
	 * Description: Perform all the actions needed to update the robot in each
	 * game cycle.
	 * 
	 * @author: Geraint and Lucas
	 */
	public void update() {

		final double wallEorientation = this.getOrientation();

		double[] wallEcomponents = this.getOrientationComponents(wallEorientation);

		this.setWheelspeeds(0, 0);
		
		detectCollision(this, wallEcomponents);
		
		// read commands from file
		if (this.getInputCommandsReadingInProgress() == true) {
			// Request a new move
			this.anotherSingleMoveViaFile(Driver.movementFile, wallEcomponents);
			
		} else {
			this.move(wallEcomponents);}
			this.animate(wallEcomponents);
			this.consumeBattery(this.getWheelspeeds());
			this.checkForCharging();
			
			if(Driver.toggledevmode)
				this.updateDevPanel();
			
			this.updateDistance();
		}
	}