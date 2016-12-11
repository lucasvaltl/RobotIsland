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
import javafx.scene.shape.Line;
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
	private int[] PROJECTIONPLANESIZE = {320, 200};
	private double height = 32;
	private int FOVsize = 60;
	private int[] PROJECTIONPLANECENTRE = {this.PROJECTIONPLANESIZE[0] / 2, 
			this.PROJECTIONPLANESIZE[1] / 2};
	private double PROJECTIONPLANEDISTANCETO = this.PROJECTIONPLANECENTRE[0] / 
			Math.tan(this.FOVsize / 2);
	private double ANGLEBETWEENRAYS = (1.0 * this.FOVsize) / PROJECTIONPLANESIZE[0]; 
	
	
	
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
	}
	
	public void castRays() {
		// Get the current orientation in degrees
		double currentOrientation= this.getRotate();
		// Calculate FOV range
			// Subtract half of the FOV to current orientation
		double leftFOV = currentOrientation - this.FOVsize / 2;
		if (leftFOV < 0) {
			// add 360degrees
			leftFOV += 360;
		} else if (leftFOV > 360) {
			// minus 360 degrees
			leftFOV -= 360;
		}
			
		// robot details
		double rowDouble;
		double colDouble;
		int rowInt;
		int colInt;
		int rowCeiling;
		int colLeftWall;
		double yDistanceFromCeiling; // used in horizontal checking
		double xDistanceToFirstIntersection; // used in horizontal checking
		double xDistanceFromLeftWall; // used in vertical checking
		double yDistanceToFirstIntersection; // used in vertical checking
			
		// space between intersection points
		double horizontalDy;
		double horizontalDx;
		double verticalDx;
		double verticalDy;

		// get which grid block robot is in: (rowInt, colInt)
		rowInt = (int) (this.yCoordinate * 1.0 / Driver.map.getBlockHeight());
		colInt = (int) (this.xCoordinate * 1.0 / Driver.map.getBlockWidth());
			
		// get the "ceiling" of robot block
		rowCeiling = rowInt * Driver.map.getBlockHeight();
		// left "wall" of block
		colLeftWall = colInt * Driver.map.getBlockWidth();
		
		// first horizontal intersection point, A
		double[] horizontalPointA = new double[2];
		double horizontalRowDoubleA;
		double horizontalColDoubleA;
		int horizontalRowIntA;
		int horizontalColIntA;
		
		// first vertical intersection point, B
		double[] verticalPointB = new double[2];
		double verticalRowDoubleB;
		double verticalColDoubleB;
		int verticalRowIntB;
		int verticalColIntB;
						
		// used to decide which ray was shorter, and calculate slice heights.
		double horizontalLastxCoordinate;
		double horizontalLastyCoordinate;
		double verticalLastxCoordinate;
		double verticalLastyCoordinate;
		double horizontalDistanceToIntersection = 0; 
		double verticalDistanceToIntersection = 0; // TODO could be causing an error
		
		double rayAngle = leftFOV;
		// Iterate over each column in projection plane and along FOV
		for (int i = 0; i < this.PROJECTIONPLANESIZE[0]; i++) {
			
			
			// space between intersection points
			horizontalDy = Driver.map.getBlockHeight();
			horizontalDx = horizontalDy / Math.tan(Math.toRadians(rayAngle)); // TODO fix error - currently infinity
			verticalDx = Driver.map.getBlockWidth();
			verticalDy = verticalDx * Math.tan(Math.toRadians(rayAngle)); // TODO fix error - currently infinity

			
			/* check for horizontal intersections */
			if ((rayAngle>= 270 && rayAngle <= 360) || 
					(rayAngle >= 0 && rayAngle <= 90)) {
				// ray facing up
				
				/* find coordinates of A */
				horizontalPointA[1] = ((int) (this.yCoordinate / 64)) * 64 - 1;
				horizontalPointA[1] = this.xCoordinate + (this.yCoordinate - 
						horizontalPointA[1]) / Math.tan(Math.toRadians(rayAngle));
				
				
				horizontalDy = 0 - Math.abs(horizontalDy); // horizontalDy is negative
				// calculate distance from "ceiling"
				yDistanceFromCeiling = this.yCoordinate - rowCeiling;
				// calculate corresponding horizontal distance
				xDistanceToFirstIntersection = yDistanceFromCeiling * Math.tan(Math.toRadians(rayAngle)); // TODO might be giving a negative value
				// calculate first intersection point, A (
				if (rayAngle >= 0 && rayAngle <= 90) {
					// ray is pointing right - add x Coordinate.
					horizontalPointA[0] = this.xCoordinate + xDistanceToFirstIntersection;
					horizontalPointA[1] = this.yCoordinate - yDistanceFromCeiling; 
					
				} else {
					// ray is point left - subtract x coordinate
					horizontalPointA[0] = this.xCoordinate - xDistanceToFirstIntersection;
					horizontalPointA[1] = this.yCoordinate - yDistanceFromCeiling;
				}				
				
			} else {
				// ray is facing down
				horizontalDy = Math.abs(horizontalDy);
				// calculate distance from "ceiling"
				yDistanceFromCeiling = rowCeiling + Driver.map.getBlockHeight() - this.yCoordinate;
				// calculate corresponding horizontal distance.
				xDistanceToFirstIntersection = yDistanceFromCeiling * Math.tan(Math.toRadians(rayAngle));
				
				// calculate first intersection point, A (x, y)
				if (rayAngle >=90 && rayAngle <= 180) {
					// ray is pointing right - add x distance
					horizontalPointA[0] = this.xCoordinate + xDistanceToFirstIntersection;
					horizontalPointA[1] = this.yCoordinate - yDistanceFromCeiling;
				} else {
					horizontalPointA[0] = this.xCoordinate - xDistanceToFirstIntersection;
					horizontalPointA[1] = this.yCoordinate - yDistanceFromCeiling;
				}
			}
			
			System.out.println(Arrays.toString(horizontalPointA));
			// TODO check behaviour below
			
			// get A's grid details (rowInt, colInt).
			horizontalRowDoubleA = horizontalPointA[0] * 1.0 /
					Driver.map.getBlockHeight();
			horizontalRowIntA = (int) horizontalRowDoubleA;
			horizontalRowIntA = (horizontalRowIntA == Driver.map.getRowSize()) ? --horizontalRowIntA : horizontalRowIntA;
			horizontalColDoubleA = horizontalPointA[1] * 1.0 / Driver.map.getBlockWidth();
			horizontalColIntA = (int) horizontalColDoubleA;
			horizontalColIntA = (horizontalColIntA == Driver.map.getColSize()) ? --horizontalColIntA : horizontalColIntA;
			
			// iterate along ray vector until wall is found 
			// or no more grid
			horizontalLastxCoordinate = horizontalPointA[0];
			horizontalLastyCoordinate = horizontalPointA[1];
			int horizontalCurrentRow = horizontalRowIntA;
			int horizontalCurrentCol = horizontalColIntA;
			boolean horizontalWallFound = false;
			
			while (horizontalWallFound == false) {
				if (rayAngle == 0) {
					// stops errors when dividing by tan(0)
					break;
				}
				if (Driver.map.getGrid()[horizontalCurrentRow][horizontalCurrentCol] == 1) {
					horizontalWallFound = true;
					// calculate distance to wall
					horizontalDistanceToIntersection = Math.abs((this.yCoordinate - horizontalLastyCoordinate) * 1.0 / 
							Math.cos(Math.toRadians(rayAngle)));
					//normalise by Beta
					
				} else if ((horizontalCurrentRow >= Driver.map.getRowSize() - 1) ||
						(horizontalCurrentRow < 0) ||
						(horizontalCurrentCol >= Driver.map.getColSize() - 1) ||
						(horizontalCurrentCol < 0)) {
					break;
				} else {
					// get next intersection coordinate
					
					System.out.println("horizontal Dx" + horizontalDx);
					horizontalLastxCoordinate += horizontalDx;
					horizontalLastyCoordinate += horizontalDy; // TODO hmm errors are probably due to minus signs
					System.out.println(horizontalLastxCoordinate);
					System.out.println(horizontalLastyCoordinate);
					// convert to grid details
					double horizontalCurrentRowDouble = horizontalLastyCoordinate / Driver.map.getBlockHeight();
					double horizontalCurrentColDouble = horizontalLastxCoordinate / Driver.map.getBlockWidth();
					horizontalCurrentRow = (int) horizontalCurrentRowDouble;
					horizontalCurrentCol = (int) horizontalCurrentColDouble;
				}
			}
			
			/* Do vertical stuff... */
			if (rayAngle >= 180 && (rayAngle <= 360 || rayAngle == 0)) {
				// ray is pointing left
				verticalDx = 0 - Math.abs(verticalDx);
				// calculate distance to left wall.
				xDistanceFromLeftWall = this.xCoordinate - colLeftWall;
				// calculate corresponding horizontal distance
				yDistanceToFirstIntersection = xDistanceFromLeftWall * 1.0 / Math.tan(Math.toRadians(rayAngle));
				
				if ((rayAngle >= 270 && rayAngle <= 360) ||
						(rayAngle >= 0 && rayAngle <= 90)){
					// ray is pointing up
						verticalPointB[0] = this.xCoordinate - xDistanceFromLeftWall;
						verticalPointB[1] = this.yCoordinate - yDistanceToFirstIntersection;
				} else {
					// ray is pointing down
						verticalPointB[0] = this.xCoordinate - xDistanceFromLeftWall;
						verticalPointB[1] = this.yCoordinate + yDistanceToFirstIntersection;
				}
				
			} else {
				// ray is pointing right
				verticalDx = Math.abs(verticalDx);
				// calculate to right wall
				xDistanceFromLeftWall = this.xCoordinate - colLeftWall;
				// calculate corresponding vertical distance
				yDistanceToFirstIntersection = xDistanceFromLeftWall * 1.0 / Math.tan(Math.toRadians(rayAngle));
				
				if ((rayAngle >= 270 && rayAngle <= 360) ||
						(rayAngle >= 0 && rayAngle <= 90)){
					// ray is pointing up
						verticalPointB[0] = this.xCoordinate + (Driver.map.getBlockWidth() - xDistanceFromLeftWall);
						verticalPointB[1] = this.yCoordinate - yDistanceToFirstIntersection;
				} else {
					// ray is pointing down
						verticalPointB[0] = this.xCoordinate - (Driver.map.getBlockWidth() - xDistanceFromLeftWall);
						verticalPointB[1] = this.yCoordinate + yDistanceToFirstIntersection;
				}
			}
			
			// get B's grid details
			verticalRowDoubleB = verticalPointB[0] * 1.0 / Driver.map.getRowSize();
			verticalRowIntB = (int) verticalRowDoubleB;
			verticalRowIntB = (verticalRowIntB == Driver.map.getRowSize()) ? --verticalRowIntB : verticalRowIntB;
			verticalColDoubleB = verticalPointB[1] * 1.0 / Driver.map.getColSize();
			verticalColIntB = (int) verticalColDoubleB;
			verticalColIntB = (verticalColIntB == Driver.map.getColSize()) ? --verticalColIntB : verticalColIntB;
			
			// iterate along orientation vector until wall is found 
			// or no more grid
			verticalLastxCoordinate = verticalPointB[0];
			verticalLastyCoordinate = verticalPointB[1];
			int verticalCurrentRow = verticalRowIntB;
			int verticalCurrentCol = verticalColIntB;
			boolean verticalWallFound = false;
			
			while (verticalWallFound == false) {
				if (Driver.map.getGrid()[verticalCurrentRow][verticalCurrentCol] == 1 || 
						(verticalCurrentRow >= Driver.map.getRowSize()) || 
						(verticalCurrentCol >= Driver.map.getColSize())) {
					verticalWallFound = true;
					// calculate distance to wall
					verticalDistanceToIntersection = Math.abs((this.yCoordinate - verticalLastyCoordinate) * 1.0 / 
							Math.cos(Math.toRadians(rayAngle)));
					// normalise by Beta
					
				} else {
					// get next intersection coordinate
					verticalLastxCoordinate += verticalDx;
					verticalLastyCoordinate += verticalDy;
					// convert to grid details
					double verticalCurrentRowDouble = verticalLastyCoordinate / Driver.map.getBlockHeight();
					double verticalCurrentColDouble = verticalLastxCoordinate / Driver.map.getBlockWidth();
					verticalCurrentRow = (int) verticalCurrentRowDouble;
					verticalCurrentCol = (int) verticalCurrentColDouble;
				}
			}
						
			// compare distances, choose shortest.
			double shortestDistance = (horizontalDistanceToIntersection <= 
					verticalDistanceToIntersection) ? horizontalDistanceToIntersection : 
						verticalDistanceToIntersection;
			
			// calculate slice height
			double sliceHeight = Driver.map.getBlockHeight() * 
					PROJECTIONPLANEDISTANCETO / shortestDistance;
			
			// calculate top and bottom of line
			double lineTop = PROJECTIONPLANECENTRE[1] - (sliceHeight / 2);
			double lineBottom = PROJECTIONPLANECENTRE[1] + (sliceHeight / 2);
			
			Line line = new Line(i, lineTop, i, lineBottom);
			line.setFill(Color.RED);
			
			Driver.root.getChildren().add(line);
			
			rayAngle += this.ANGLEBETWEENRAYS;
		}
					
	}
	
	public void castRaysAgain() {
		// Get the current orientation in degrees
		double currentOrientation= this.getRotate();
		// Calculate FOV range
			// Subtract half of the FOV to current orientation
		double leftFOV = currentOrientation - this.FOVsize / 2;
		if (leftFOV < 0) {
			// add 360degrees
			leftFOV += 360;
		} else if (leftFOV > 360) {
			// minus 360 degrees
			leftFOV -= 360;
		}
			
		// robot details
		double rowDouble;
		double colDouble;
		int rowInt;
		int colInt;
		int rowCeiling;
		int colLeftWall;
		double yDistanceFromCeiling; // used in horizontal checking
		double xDistanceToFirstIntersection; // used in horizontal checking
		double xDistanceFromLeftWall; // used in vertical checking
		double yDistanceToFirstIntersection; // used in vertical checking
			
		// space between intersection points
		double horizontalDy;
		double horizontalDx;
		double verticalDx;
		double verticalDy;

		// get which grid block robot is in: (rowInt, colInt)
		rowInt = (int) (this.yCoordinate * 1.0 / 64);
		colInt = (int) (this.xCoordinate * 1.0 / 64);
			
		// first horizontal intersection point, A
		double[] horizontalPointA = new double[2];
		int horizontalRowIntA;
		int horizontalColIntA;
		
		// first vertical intersection point, B
		double[] verticalPointB = new double[2];
		int verticalRowIntB;
		int verticalColIntB;
						
		// used to decide which ray was shorter, and calculate slice heights.
		double horizontalLastxCoordinate;
		double horizontalLastyCoordinate;
		double verticalLastxCoordinate;
		double verticalLastyCoordinate;
		double horizontalDistanceToIntersection = 0; 
		double verticalDistanceToIntersection = 0; // TODO could be causing an error
		
		double rayAngle = leftFOV;
		// Iterate over each column in projection plane and along FOV
		for (int i = 0; i < this.PROJECTIONPLANESIZE[0]; i++) {
			
			// space between intersection points
			horizontalDy = 64;
			horizontalDx = 64 / Math.tan(Math.toRadians(rayAngle)); // TODO Tends to infinity when theta is small
			verticalDx = 64;
			verticalDy = 64 * Math.tan(Math.toRadians(rayAngle)); // TODO Tends to zero when theta is zero
			
			/* check for horizontal intersections */
			if ((rayAngle>= 270 && rayAngle <= 360) || 
					(rayAngle >= 0 && rayAngle <= 90)) {
				// ray facing up
				horizontalDy = 0 - 64; // horizontalDy is negative				
				
				// Find A's coordinates
				horizontalPointA[1] = (int) ((this.yCoordinate / 64) * 64 - 1);
				horizontalPointA[0] = this.xCoordinate + (this.yCoordinate - 
						horizontalPointA[1]) / Math.tan(Math.toRadians(rayAngle)); 
					// TODO tends to infinity when theta is small
				
			} else {
				// ray is facing down
				horizontalDy = 64; // horizontalDy is positive;
				
				// Find A's coordinates
				horizontalPointA[1] = (int) ((this.yCoordinate / 64) * 64 + 64);
				horizontalPointA[0] = this.xCoordinate + (this.yCoordinate - 
						horizontalPointA[1]) / Math.tan(Math.toRadians(rayAngle)); 
					// TODO tends to infinity when theta is small
				
			}
			
			// get A's grid details (rowInt, colInt).
			horizontalRowIntA = (int) horizontalPointA[1] / 64;
			horizontalColIntA = (int) horizontalPointA[0] / 64;
			
			// iterate along ray vector until wall is found 
			// or no more grid
			horizontalLastxCoordinate = horizontalPointA[0];
			horizontalLastyCoordinate = horizontalPointA[1];
			int horizontalCurrentRow = horizontalRowIntA;
			int horizontalCurrentCol = horizontalColIntA;
			boolean horizontalWallFound = false;
			
			while (horizontalWallFound == false) {
				if (rayAngle == 0 || 
						(horizontalCurrentRow >= Driver.map.getRowSize() - 1) ||
						(horizontalCurrentRow < 0) ||
						(horizontalCurrentCol >= Driver.map.getColSize() - 1) ||
						(horizontalCurrentCol < 0)) {
					// stops errors when dividing by tan(0)
					break;
				} else if (Driver.map.getGrid()[horizontalCurrentRow]
						[horizontalCurrentCol] == 1) {
					horizontalWallFound = true;
					// calculate distance to wall
					horizontalDistanceToIntersection = Math.abs(this.xCoordinate - 
							horizontalLastxCoordinate) / Math.cos(Math.toRadians(rayAngle));
						// Tends to 0 when theta is 1;
					//normalise by Beta
				} else {
					
					// get next intersection coordinate
					horizontalLastxCoordinate += horizontalDx;
					horizontalLastyCoordinate += horizontalDy;
					
					// convert to grid details
					horizontalCurrentRow = (int) (horizontalLastyCoordinate / 64);
					horizontalCurrentCol = (int) (horizontalLastxCoordinate / 64);
				}
			}
			
			/* Do vertical stuff... */
			if (rayAngle >= 180 && (rayAngle <= 360 || rayAngle == 0)) {
				// ray is pointing left
				verticalDx = 0 - 64;
				
				// calculate B's coordinates
				verticalPointB[0] = (int)((this.xCoordinate / 64) * 64 - 1);
				verticalPointB[1] = this.yCoordinate + (this.xCoordinate - 
						verticalPointB[0]) * Math.tan(Math.toRadians(rayAngle)); // Tends to 0 when theta is small;
			} else {
				// ray is pointing right
				verticalDx = 64;
				
				// calculate B's coordinates
				verticalPointB[0] = (int) ((this.xCoordinate / 64) * 64 + 64);
				verticalPointB[1] = this.yCoordinate + (this.xCoordinate - 
						verticalPointB[0]) * Math.tan(Math.toRadians(rayAngle)); // Tends to 0 when theta is small;
			}
			
			// get B's grid details
			verticalRowIntB = (int) verticalPointB[1] / 64;
			verticalColIntB = (int) verticalPointB[0] / 64;
			
			// iterate along orientation vector until wall is found 
			// or no more grid
			verticalLastxCoordinate = verticalPointB[0];
			verticalLastyCoordinate = verticalPointB[1];
			int verticalCurrentRow = verticalRowIntB;
			int verticalCurrentCol = verticalColIntB;
			boolean verticalWallFound = false;
			
			while (verticalWallFound == false) {
				if (Driver.map.getGrid()[verticalCurrentRow][verticalCurrentCol] == 1 || 
						(verticalCurrentRow >= Driver.map.getRowSize()) || 
						(verticalCurrentCol >= Driver.map.getColSize())) {
					verticalWallFound = true;
					
					// TODO calculate distance to wall
					verticalDistanceToIntersection = Math.abs(this.yCoordinate - 
							verticalLastyCoordinate) / Math.sin(Math.toRadians(rayAngle));
						// tends to zero when theta is zero;
					// normalise by Beta
					
				} else {
					// get next intersection coordinate
					verticalLastxCoordinate += verticalDx;
					verticalLastyCoordinate += verticalDy;
					
					// convert to grid details
					verticalCurrentRow = (int) (verticalLastyCoordinate / 64);
					verticalCurrentCol = (int) (verticalLastxCoordinate / 64);
				}
			}
						
			// compare distances, choose shortest.
			double shortestDistance = (horizontalDistanceToIntersection <= 
					verticalDistanceToIntersection) ? horizontalDistanceToIntersection : 
						verticalDistanceToIntersection;
			
			// calculate slice height
			double sliceHeight = Driver.map.getBlockHeight() * 
					PROJECTIONPLANEDISTANCETO / shortestDistance;
			
			// calculate top and bottom of line
			double lineTop = PROJECTIONPLANECENTRE[1] - (sliceHeight / 2);
			double lineBottom = PROJECTIONPLANECENTRE[1] + (sliceHeight / 2);
			
			Line line = new Line(i, lineTop, i, lineBottom);
			line.setFill(Color.RED);
			
			Driver.root.getChildren().add(line);
			
			rayAngle += this.ANGLEBETWEENRAYS;
		}
					
	}
}