package uk.ac.ucl.robotisland.src.robot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.ImagePattern;
import uk.ac.ucl.robotisland.src.encryption.CryptoException;
import uk.ac.ucl.robotisland.src.encryption.CryptoUtils;
import uk.ac.ucl.robotisland.src.map.Map;
import uk.ac.ucl.robotisland.src.readers.FileReader;
import uk.ac.ucl.robotisland.src.readers.InvalidFormatException;
import uk.ac.ucl.robotisland.src.readers.NewerFileReader;
import uk.ac.ucl.robotisland.src.readers.XMLReader;
import uk.ac.ucl.robotisland.src.tests.Driver;

/**
 * Description: Class that represents the robot - its movement and perspective.
 * Overrides some JavaFX rectangle methods
 * 
 * @author Geraint and Lucas
 *
 */
public class Robot extends Entity implements EventHandler<KeyEvent> {

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
	private ImagePattern[][] animimages;
	private int timeSinceCollision = 0;
	private boolean collisionDetected;
	private double distancetravelled;
	private boolean recharging;;
	private boolean lapInProgress = false;
	private double startTime;
	private double stopTime;
	private double lastLapTime;
	private double highscore = 999999999;
	private double currentLapTime;
	DecimalFormat df = new DecimalFormat("#.00");
	private boolean notCheating = true;
	private boolean newHighScore;
	private int timeSinceHighScore;
	private int highScoreToggle = 1;
	private String lapTimeTrialMoves = "";

	/**
	 * Description: Verbose robot class constructor
	 * 
	 * @param name: The robot's ID.
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
	public Robot(String name, double xCoordinate, double yCoordinate, double speed, double maxSpeed,
			double acceleration, double angularVelocity, double odometer, double batteryLeft, double batteryCapacity,
			double axleLength, double wheelRadius) {
		/** Robot class constructor **/
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
		this.loadHighScore();
	}

	/**
	 * Description: Creates the robot's parameters from an XML file.
	 * 
	 * @param s: The ID of the robot you want to load
	 * @param hs: Boolean value defining if you want to load the highscore (true is true)
	 */
	public Robot(String s, boolean loadhighscore) {

		XMLReader xmlr = new XMLReader();
		ArrayList<String> input = xmlr.read(s, "/uk/ac/ucl/robotisland/src/xml/robots.xml");
		this.xCoordinate = Double.valueOf(input.get(0));
		this.setX(this.xCoordinate);
		this.yCoordinate = Double.valueOf(input.get(1));
		this.setY(this.yCoordinate);
		this.speed = Double.valueOf(input.get(2));
		this.maxSpeed = Double.valueOf(input.get(3));
		this.globalMaxSpeed = Double.valueOf(input.get(3));
		this.acceleration = Double.valueOf(input.get(4));
		this.angularVelocity = Double.valueOf(input.get(5));
		this.odometer = Double.valueOf(input.get(6));
		this.batteryLeft = Double.valueOf(input.get(7));
		this.batteryCapacity = Double.valueOf(input.get(8));
		this.axleLength = Double.valueOf(input.get(9));
		this.setWidth(this.axleLength);
		this.wheelRadius = Double.valueOf(input.get(10));
		this.setHeight(this.wheelRadius);
		if(loadhighscore){
		this.loadHighScore();
		}
		
	}

	/**
	 * Description: Animate the robot to reflect it's behaviour.
	 * @param wallEcomponents: The robot's x and x components.
	 * @return: A boolean value used to break out of the method.
	 */
	public boolean animate(double[] wallEcomponents) {
		
		if (recharging) {
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
		
		// high score animation
				if (this.newHighScore) {
					
					if (this.timeSinceHighScore == 100) {
						this.newHighScore = false;
						this.timeSinceHighScore = 0;
					} else {
						//toggle between images to create animation
						if(timeSinceHighScore % 10 == 0){
							this.highScoreToggle = 	this.highScoreToggle == 1 ? 2 : 1;
						}
						this.setFill(this.getAnimatedImage(4, highScoreToggle));
						this.timeSinceHighScore++;
						// used to break out of method
						return false;
					}
				}

		// check if robot is even moving:
		if (this.wheelspeeds[0] == 0 && this.wheelspeeds[1] == 0 && this.speed == 0) {
			this.setFill(this.getAnimatedImage(1, 1));
			return false;
		}

		// animate eyes to look into direction of movement using the difference in wheelspeeds
		int i = 1;
		int j = 1;

		if (this.getLastUporDown().equals("UP")) {
			if (this.speed == maxSpeed) {
				i = 2;
			}
			if (this.wheelspeeds[0] == this.wheelspeeds[1]) {

			} else if (this.speed == 0 && this.wheelspeeds[0] > this.wheelspeeds[1]
					|| this.decelerate == true && this.wheelspeeds[0] > this.wheelspeeds[1]) {
				j = 0;
			} else if (this.speed == 0 && this.wheelspeeds[0] < this.wheelspeeds[1]
					|| this.decelerate == true && this.wheelspeeds[0] < this.wheelspeeds[1]) {
				j = 2;
			} else if (this.wheelspeeds[0] < this.wheelspeeds[1]) {
				j = 0;
			} else {
				j = 2;
			}
			this.setFill(this.getAnimatedImage(i, j));

		}

		if (this.getLastUporDown().equals("DOWN")) {
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
	
	/**
	 * Description: Reads a single move from a given file.
	 * @param file: A file to read from.
	 * @param wallEcomponents: The robot's position vectors.
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
				// Driver.labelinfo.setText("WARNING: Invalid command in text
				// file");
				Driver.LOGGER.severe("WARNING: Invalid command in text file " + e.toString());
				this.inputCommandsReadingInProgress = false;
				return;
			} catch (FileNotFoundException e) {
				// Driver.labelinfo.setText("WARNING: File not found");
				Driver.LOGGER.severe("WARNING: File not found " + e.toString());
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
					// Robot must decelerate after previous motion in the
					// opposite
					// direction
					Movement.decelerate(wallEcomponents);
				} else {
					// accelerate
					Movement.moveUp(wallEcomponents);
					this.setLastMovement("moveUp");
				}

			} else if (this.inputCommands.get(this.inputCommandsIndex).equals("moveDown")) {

				if (this.getDecelerate() == true) {
					// Robot must decelerate after previous motion in the
					// opposite
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
			this.inputCommands = null;
			this.currentKeyPresses[0] = null;
			this.currentKeyPresses[1] = null;
		} else {
			this.inputCommandsIndex++;

		}
	}
	
	/**
	 * Description: Checks if the robot is in the refueling area. If it is there
	 * the robot is charged.
	 */
	public void checkForCharging() {
		if (CollisionDetection.detectLocation(this, Map.chargingstation)) {
			this.increaseCharge(5);
			this.recharging = true;
			// reset time since collision to overwrite collisions happening in
			// the charging station
			this.setCollisionDetected(false);
			this.timeSinceCollision = 0;
			if (Driver.toggledevmode) {
				if (Driver.textinfo.getText().equals("Battery less than 10%!!!")) {
					Driver.textinfo.setText("Recharging!");
				}
			}

		} else if (this.recharging == true) {
			this.recharging = false;
		}
	}

	/**
	 * Description: Checks if the robot passes the finish line. DIsabled if
	 * player is trying to cheat by not doing a full lap.
	 */
	public void checkForFinishLine() {
		if (CollisionDetection.detectLocation(this, Map.finishLine)) {
			// check if player is cheating
			if (notCheating) {
				this.timeLap();
				if (Driver.toggledevmode) {
					Driver.textinfo.setText("Crossed Finishline");
				}
				this.notCheating = false;
				return;
			}
		}
	}

	/**
	 * Description: Checks if robot actually did a lap around the track to avoid cheating.
	 */
	public void checkIfCheckIfCheating() {
		if (CollisionDetection.detectLocation(this, Map.antiCheatLine)) {
			this.notCheating = true;
		}
	}
	
	/**
	 * Description: Consume battery when moving. If battery is low (less than
	 * 10%) the robots speed will decrease significantly. If the battery is
	 * empty, the robot will fail to move.
	 * 
	 */
	protected void consumeBattery(double[] ds) {

		//update battery left label
		Driver.batteryLeft.setText(df.format(this.getBatteryLeft() / this.getBatteryCapacity() * 100) + "%");
		
		if (this.getSpeed() > 0) {
			if (this.getBatteryLeft() >= (this.getBatteryCapacity() / 10)) {
				// reduce battery relative to distance traveled
				this.decreaseCharge(0.25 * this.getSpeed());
			}
			// if battery lower than 10% of charge, reduce speed, consume less
			// battery
			if ((this.getBatteryLeft() > 0) && (this.getBatteryLeft() < (this.getBatteryCapacity() / 10))) {
				this.decreaseCharge(0.25);
				this.setMaxSpeed(1);
				if (Driver.toggledevmode) {
					Driver.textinfo.setText("Battery less than 10%!!!");

					if (Driver.batteryLowSound.isPlaying() == false) {
						Driver.batteryLowSound.play(0.4);
					}

				}
			}
		} // decrease charge if robot is turning around its own axis
			// (wheelspeeds unequal) but not moving forward
		else if (this.getSpeed() == 0 && (ds[0] != ds[1])) {
			if (this.getBatteryLeft() >= (this.getBatteryCapacity() / 10)) {
				this.decreaseCharge(0.0625);
			}
			// if battery lower than 10% of charge, reduce speed, consume less
			// battery
			if ((this.getBatteryLeft() > 0) && (this.getBatteryLeft() < (this.getBatteryCapacity() / 10))) {
				this.decreaseCharge(0.0625);
				this.setMaxSpeed(1);
				if (Driver.toggledevmode) {
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
			this.gameOver();
			this.reset(Driver.robotType);
		}
	}
	
	/**
	 * Description: Create a array of arrays storing images later used
	 * for animation.
	 */
	public void createAnimatedImages() {
		Image[][] images = new Image[5][3];
		animimages = new ImagePattern[5][3];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 3; j++) {
				images[i][j] = new Image(Driver.class.getResource("/uk/ac/ucl/robotisland/src/img/eve" + i + "" + j + ".png").toString(),
						Driver.wallE.getWidth(), Driver.wallE.getWidth(), false, true);

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
	 * Description: Method used to decrease robot charge by a given value at the
	 * end of every move.
	 * 
	 * @param decrementValue: The value to decrement the robot's battery by.
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
	 * Description: Detects if the robot is about to collide with a boundary and
	 * reacts accordingly
	 * 
	 * @param robot: robot you want to check for collisions
	 * @param wallEcomponents: robots orientation components derived from its orientation
	 * 
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
	 * Description: Calculate and display the current lap time in the display label
	 */
	public void displayLapTime() {
		if (this.startTime != 0) {
			this.currentLapTime = (System.currentTimeMillis() - this.startTime) / 1000.0;

			Driver.time.setText(df.format(this.currentLapTime) + " s");
		}
	}
	
	/**
	 * Description: Resets the lap and resets the robot to an initial state.
	 */
	public void gameOver(){
		//delete the dummy if there is one
		if(Driver.dummy != null){
		Driver.dummy.setVisible(false);
		}
		Driver.gameOverScreen.setVisible(true);
	}
	
	/**
	 * Description: Return's the robot's acceleration
	 * @return: The robot's acceleration.
	 */
	public double getAcceleration() {
		return this.acceleration;
	}

	/**
	 * Description: Returns the robot's angular velocity in degrees per second.
	 * @return: The robot's angular velocity in degrees per second.
	 */
	public double getAngularVelocity() {
		return this.angularVelocity;
	}
	
	/**
	 * Description: Get an image from the image array
	 * @param row: Row number
	 * @param column: Column number
	 * @return: An ImagePattern object to display.
	 */
	public ImagePattern getAnimatedImage(int row, int column) {
		return this.animimages[row][column];
	}
	
	/**
	 * Description: Returns the robot's axle length.
	 * @return: A double representing the robot's axle length.
	 */
	public double getAxleLength() {
		return this.axleLength;
	}
	
	/**
	 * Description: Returns a double representing the amount of battery
	 * capacity.
	 * @return: A double representing the amount of battery capacity.
	 */
	public double getBatteryCapacity() {
		return this.batteryCapacity;
	}
	
	/**
	 * Description: Returns a double representing the amount of battery left.
	 * @return: A double representing the amount of battery left.
	 */
	public double getBatteryLeft() {
		return this.batteryLeft;
	}
	
	/**
	 * Description: Check if a collision has been detected. 
	 * @return: A boolean value - true when a collision has been detected.
	 */
	public boolean getCollisionDetected() {
		return this.collisionDetected;
	}
	
	/**
	 * Description: Method that returns the robot's current key presses.
	 * @return A string array of length 2, representing the current key presses.
	 */
	public String[] getCurrentKeyPresses() {
		return this.currentKeyPresses;
	}
	
	/**
	 * Description: Method that returns a boolean to represent the robot's
	 * deceleration status
	 * @return: A boolean that represents the robot's deceleration status.
	 */
	public boolean getDecelerate() {
		return this.decelerate;
	}
	
	/**
	 * Description: A method to get the amount of distance travelled.
	 * @return: The robot's distance travelled.
	 */
	public double getDistanceTravelled() {
		return this.distancetravelled;
	}
	
	/**
	 * Description: Method that returns a boolean which will be true when robot
	 * commands are being read from a file.
	 * @return: A boolean that will be true when robot commands are being read from a file.
	 */
	public boolean getInputCommandsReadingInProgress() {
		return this.inputCommandsReadingInProgress;
	}
	
	/**
	 * Description: Method that returns the robot's last left or right command
	 * property.
	 * @return: The last up or down command assigned to the lastUporDown field.
	 */
	public String getLastMovement() {
		return this.lastMovement;
	}
	
	/** 
	 * Description: Method that returns the robot's lapInProgressField.
	 * @return: True when a lap is in progress
	 */
	public boolean getLapInProgress() {
		return this.lapInProgress;
	}
	
	/**
	 * Description: Method that returns the robot's last up or down command
	 * property.
	 * @return: The last up or down command assigned to the lastUporDown field.
	 */
	public String getLastUporDown() {
		return this.lastUporDown;
	}
	
	/**
	 * Description: Returns the robot's maximum speed.
	 * @return: The robot's maximum speed.
	 */
	public double getMaxSpeed() {
		return this.maxSpeed;
	}
	
	/**
	 * Description: Returns the robot's distance travelled.
	 * @return: The robot's distance travelled.
	 * @version 0.1
	 */
	public double getOdometer() {
		return this.odometer;
	}
	
	/**
	 * Description: Returns the robot's speed. * 
	 * @return: The robot's current speed.
	 */
	public double getSpeed() {
		return this.speed;
	}

	/**
	 * Description: Returns the robot's wheel radius.
	 * @return: A double representing the robot's wheel radius.
	 */
	public double getWheelRadius() {
		return this.wheelRadius;
	}

	/**
	 * @return: A double array of the robot's relative wheel speeds.
	 */
	public double[] getWheelspeeds() {
		return this.wheelspeeds;
	}

	/**
	 * Description: Returns the robot's x coordinate.
	 * @return: The robot's current x position.
	 */
	public double getxCoordinate() {
		return this.xCoordinate;
	}
	
	/**
	 * Description: Returns the robot's y coordinate
	 * @return: The robot's current y position
	 */
	public double getyCoordinate() {
		return this.yCoordinate;
	}
	
	/**
	 * Description: Recharges the robot by a set value
	 * @param decrementValue: Amount by which the robot is charged
	 */
	public void increaseCharge(double decrementValue) {

		if (this.getBatteryLeft() < this.getBatteryCapacity()) {
			this.batteryLeft += decrementValue;
		}
		// avoids over-charging
		else if (this.getBatteryLeft() > this.getBatteryCapacity()) {
			this.batteryLeft = this.getBatteryCapacity();
			if (Driver.toggledevmode) {
				Driver.textinfo.setText("Recharged!");

				if (Driver.batteryLowSound.isPlaying() == true) {
					Driver.batteryLowSound.stop();
				}
				if (Driver.batteryFullSound.isPlaying() == false) {
					Driver.batteryFullSound.play();
				}
			}
		}
		this.setMaxSpeed(this.globalMaxSpeed);
	}
	
	/**
	 * Description: Resets the robot to its initial position using an 
	 * XML file to load the different specifications of the robot.
	 * 
	 * @param robottype: type of robot you want to load (type found in XML)
	 */
	public void reset(String robottype) {
		//reload robot from xml
		XMLReader xmlr = new XMLReader();
		ArrayList<String> input = xmlr.read(robottype, "/xml/robots.xml");
		this.xCoordinate = Double.valueOf(input.get(0));
		this.setX(this.xCoordinate);
		this.yCoordinate = Double.valueOf(input.get(1));
		this.setY(this.yCoordinate);
		this.speed = Double.valueOf(input.get(2));
		this.maxSpeed = Double.valueOf(input.get(3));
		this.acceleration = Double.valueOf(input.get(4));
		this.angularVelocity = Double.valueOf(input.get(5));
		this.odometer = Double.valueOf(input.get(6));
		this.batteryLeft = Double.valueOf(input.get(7));
		this.batteryCapacity = Double.valueOf(input.get(8));
		this.axleLength = Double.valueOf(input.get(9));
		this.setWidth(this.axleLength);
		this.wheelRadius = Double.valueOf(input.get(10));
		this.setHeight(this.wheelRadius);
		this.setRotate(0);
		//reset lap 
		Driver.gameInProgress = false;
		this.notCheating = true;
		this.lapInProgress = false;
	}
	
	/**
	 * Description: Save high score to file
	 */
	private void saveHighScore() {
		BufferedWriter bw = null;
		try {
			String key = "tMWhcuxOJ5zU4uvx";
			File file = new File("res/highscore.txt");
			bw = new BufferedWriter(new FileWriter(file, false));																				// false
			bw.write("" + highscore);
			bw.flush();
			bw.close();
	            CryptoUtils.encrypt(key, file, file);
	        
		} catch (IOException e) {
			Driver.LOGGER.severe("error while saving highscore to file");
		}catch (CryptoException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
	}
	
	/**
	 * Description: Saves the moves associated with the high score for replay/time trial
	 */
	private void saveLapMoves() {
		BufferedWriter bw = null;
		try {
			File file = new File("res/highscorelap.txt");
			bw = new BufferedWriter(new FileWriter(file, false));
			bw.write(this.lapTimeTrialMoves);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			Driver.LOGGER.severe("error while saving highscorelap to file");
		}
	}
	
	/**
	 * Description: Sets the axle length to a given value and calls the
	 * Rectangle.setWidth() method (Currently assumes wheels as having no width)
	 * @param axleLength: The robot's axle length.
	 */
	public void setAxleLength(double axleLength) {
		this.axleLength = axleLength;
		super.prefWidth(this.axleLength);
	}
	
	/**
	 * Description: Set's the robot's acceleration.
	 * @param acceleration: The acceleration to set as the robot's acceleration.
	 */
	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}

	/**
	 * Description: Set's the robot's angular velocity (in degrees per second).
	 * @param angularVelocity: The value in degrees per second to set the robot's angular velocity.
	 */
	public void setAngularVelocity(double angularVelocity) {
		this.angularVelocity = angularVelocity;
	}
	
	/**
	 * Description: Sets the robot's battery level to a given value.
	 * @param batteryLeft: The amount of battery left.
	 */
	public void setBatteryLeft(double batteryLeft) {
		this.batteryLeft = batteryLeft;
	}
	
	/**
	 * Description: Set the if the robot detected a collision
	 * @param b: true or false
	 */
	public void setCollisionDetected(boolean b) {
		this.collisionDetected = b;
	}
	
	/**
	 * Description: Method that sets a particular index in the CurrentKeyPresses
	 * string array to a given value.
	 * @param index: The index of the value to change (must be either 0 or 1.
	 * @param value: The value to change to.
	 */
	public void setCurrentKeyPresses(int index, String value) {
		if ((index == 0 || index == 1) && (Objects.equals(value, "UP") || Objects.equals(value, "DOWN"))) {
			this.currentKeyPresses[index] = value;
		} else if ((index == 0 || index == 1) && Objects.equals(value, null)) {
			this.currentKeyPresses[index] = null;
		}
	}
	
	/**
	 * Description: Method used to flag the robot's deceleration status.
	 * @param value: The boolean value to be set.
	 */
	public void setDecelerate(boolean value) {
		this.decelerate = value;
	}
	
	/**
	 * Description: set if the robot is reading inputs from a file
	 * @param value: true or false
	 */
	public void setInputCommandsReadingInProgress(boolean value) {
		this.inputCommandsReadingInProgress = value;
	}
	
	/**
	 * Description: Method used to set the robot's last up or down property.
	 * @param value: The string to be set (must be "UP", "DOWN", or "null")
	 */
	public void setLastUporDown(String value) {
		if (Objects.equals(value, "UP") || Objects.equals(value, "DOWN")) {
			this.lastUporDown = value;
		} else if (Objects.equals(value, "null")) {
			this.lastUporDown = null;
		}
	}

	/**
	 * Description: Appends the given value to the lapTimeTrialMoves string
	 * @param robotInfo: A string representation of a double array.
	 */
	public void setLapTimeTrialMoves(String robotInfo) {
		this.lapTimeTrialMoves += robotInfo + "\r\n";
	}
	
	/**
	 * Description: Method used to set the robot's last up or down property.
	 * @param move: The string to be set (must be "LEFT", "RIGHT", or "null")
	 */
	public void setLastMovement(String move) {
		this.lastMovement = move;
	}
	
	/**
	 * Description: Sets the robot's maximum speed.
	 * @param maxSpeed: The speed to set as the robot's maximum speed.
	 */
	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	/**
	 * Description: Sets the robot's odometer to a given distance.
	 * @param distance: The distance the robot has travelled so far.
	 */
	public void setOdometer(double distance) {
		this.odometer = distance;
	}
	
	/**
	 * Description: Sets the robot's speed and limits it to be less than or
	 * equal the robot's maximum speed.
	 * @param speed: The speed the robot will set to, providing that it is less than or equal to the maximum speed.
	 */
	public void setSpeed(double speed) {
		this.speed = (speed > this.maxSpeed) ? this.maxSpeed : speed;
	}

	/**
	 * Description: Sets the radius of the robot's wheels to a given value and
	 * calls the Rectangle.setHeight() method
	 * 
	 * @param radius: The radius of the robot's wheels.
	 */
	public void setWheelRadius(double radius) {
		this.wheelRadius = radius;
		super.prefHeight(this.wheelRadius);
	}

	/**
	 * Description: Set the robots wheel speeds
	 * @param left: Left wheel speed
	 * @param right: Right wheel speed
	 */
	public void setWheelspeeds(double left, double right) {
		this.wheelspeeds[0] = left;
		this.wheelspeeds[1] = right;
	}
	
	/**
	 * Description: Sets the robot's x position to a given value and calls the
	 * parent .setX() method.
	 * @param xCoordinate: The x position to be moved to.
	 */
	public void setxCoordinate(double xCoordinate) {
		this.xCoordinate = xCoordinate;
		super.setX(this.xCoordinate);
	}

	/**
	 * Description: Sets the robot's y position to a given value and calls the
	 * parent .setY() method.
	 * @param yCoordinate: The y position to be moved to.
	 */
	public void setyCoordinate(double yCoordinate) {
		this.yCoordinate = yCoordinate;
		super.setY(this.yCoordinate);
	}

	/**
	 * Description: Time the current lap. Checks if new lap time is the new high
	 * score
	 */
	public void timeLap() {
		
		this.stopTime = System.currentTimeMillis();
		this.lastLapTime = (this.stopTime - this.startTime) / 1000.0;
		//create new dummy at start of lap when in time trial mode
		if(lapInProgress){
			
			if (Driver.timeTrialMode) {
				try {
					Driver.timeTrialInputStream = new FileInputStream("res/highscorelap.txt");
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				
				if (Driver.timeTrialInputStream != null) {
					//delete old dummy if there is one
					if(Driver.dummy != null)
					Driver.dummy.setVisible(false);
					//create new dummy without loading the highscore
					Driver.dummy = new DummyRobot("fast",false);
					Driver.dummy.createAnimatedImages();
					Driver.dummy.setFill(Driver.dummy.getAnimatedImage(1, 1));
					Driver.dummy.setVisible(true);
					Driver.root.getChildren().add(Driver.dummy);
					Driver.dummy.setTimeTrialInputInProgress(true);
					Driver.wallE.requestFocus(); // sets the focus back to main robot
				}
			}
			
		Driver.lastLapTime.setText(df.format(lastLapTime) + " s");
		if (lastLapTime < this.highscore) {
			// new highscore
			this.highscore = this.lastLapTime;
			Driver.highscore.setText(df.format(this.highscore) + " s");
			saveHighScore();
			newHighScore = true;
			
			// save lap moves
			saveLapMoves();
			// play highscore soundFX
			if (Driver.highscoreSound.isPlaying() == false) {
				Driver.highscoreSound.play();
			}
		} else {
			// No new highscore - play normal sound
			if (Driver.finishLine.isPlaying() == false) {
				Driver.finishLine.play();
			}
			this.lapTimeTrialMoves = "";
		}}
		this.startTime = System.currentTimeMillis();
		this.lapInProgress = true;
		Driver.gameInProgress = true;
		Driver.splashscreen.setVisible(false);
		Driver.gameOverScreen.setVisible(false);
	}

	/**
	 * Description: Load high score from file
	 */
	private void loadHighScore() {

		String line = "";
		try {
			String key = "tMWhcuxOJ5zU4uvx";
			File file = new File("res/highscore.txt");
			CryptoUtils.decrypt(key, file, file);
			InputStream fis = new FileInputStream("res/highscore.txt");
			InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			BufferedReader br = new BufferedReader(isr);
			line = br.readLine();
			br.close();
			//encrypt the file again
			CryptoUtils.encrypt(key, file, file);
		} catch (IOException e) {
			line = "";
			Driver.LOGGER.info("No highscore file found");
		}catch (CryptoException ex) {
			Driver.LOGGER.info("No highscore file found");
			line = "";
			System.out.println("EXCEPTION IN LOADING HIGHSCORE");
	    }

		if (line != "") {
			highscore = Double.parseDouble(line);
			Driver.highscore.setText(df.format(this.highscore) + " s");
		}
	}

	/**
	 * Description: Update the robots distance travelled
	 */
	protected void updateDistance(){
		this.distancetravelled += this.speed;
	}

	/**
	 * Description: Updates the Developer panel if developer mode is turned on
	 */
	public void updateDevPanel() {

		Point2D centercoordinates = this.center();
		DecimalFormat numberFormat = new DecimalFormat("#.0");
		Driver.textx.setText(numberFormat.format(centercoordinates.getX()));
		Driver.texty.setText(numberFormat.format(centercoordinates.getY()));
		Driver.textcharge.setText(numberFormat.format(this.getBatteryLeft() / this.getBatteryCapacity() * 100) + "%");
		Driver.textdistance.setText(numberFormat.format(this.getDistanceTravelled()));
		// calculate orientation of robots front based on the default
		// getRotate() method;
		double currentrotation;
		if (this.getRotate() > 0) {
			currentrotation = this.getRotate() % 360;
		} else {
			currentrotation = this.getRotate() % 360 + 360;
		}
		if (currentrotation > 180) {
			currentrotation -= 180;
		} else {
			currentrotation += 180;
		}
		Driver.textangle.setText(numberFormat.format(currentrotation));
	}

	/**
	 * Description: Event handler for robot key events. Adds strings to
	 * currentKeyPresses array to keep track of which buttons are being held
	 * down.
	 * 
	 * Logs to src/logs file
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
			default:
				//do nothing, as any other key event is irrelevant
				break;
			}
			event.consume();
		}
	}

	/**
	 * Description: Moves the robot depending on keypresses. It calls the static
	 * methods in the movement class to translate and transform the robot, and
	 * calculate the relative velocities of the left and right wheels.
	 * @param wallEcomponents: orientation components derived from the robots orientation
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
	 * Description: Method that reads moves from an input file and executes them
	 * in order.
	 * @param path: The path of the file to read.
	 */
	public void moveViaFile(String path) {
		// Get the orientation using the getRotate() method.
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
	 * Description: Perform all the actions needed to update the robot in each
	 * game cycle.
	 * 
	 */
	public void update() {

		final double wallEorientation = this.getOrientation();

		double[] wallEcomponents = this.getOrientationComponents(wallEorientation);

		this.setWheelspeeds(0, 0);

		detectCollision(this, wallEcomponents);

		// read commands from file
		if (Driver.wallE.getInputCommandsReadingInProgress() == true) {

			// Request a new move
			Driver.wallE.anotherSingleMoveViaFile(Driver.movementFile, wallEcomponents);
		} else {
			this.move(wallEcomponents);
		}
		this.animate(wallEcomponents);
		this.consumeBattery(this.getWheelspeeds());
		this.checkForCharging();
		this.checkIfCheckIfCheating();
		this.checkForFinishLine();
		this.displayLapTime();
		if (Driver.toggledevmode)
			this.updateDevPanel();
		this.updateDistance();
	}

}