package uk.ac.ucl.robotisland.src.robot;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

/**
 * Description: Class used to represent blocks in the SAT collision detection methods.
 * @author Geraint and Lucas
 *
 */
public class Entity extends Rectangle{
	
	double x;
	double y;
	double width;
	double height;
	
	/**
	 * Description: Initialises an Entity object with given attributes.
	 * @param x: The x coordinate of the entity.
	 * @param y: The y coordinate of the entity.
	 * @param width: The width of the entity.
	 * @param height: The height of the entity.
	 */
	public Entity(int x, int y, int width, int height){
		super.setX(x);
		super.setY(y);
		super.setWidth(width);
		super.setHeight(height);
	}

	/**
	 * Description: Overwrite the super constructor.
	 */
	public Entity(){
	}
	
	/**
	 * Description: Find the entity's centre point
	 * 
	 * @return: The centre point of the entity
	 */
	public Point2D center(){
		return new Point2D((this.getX()+(this.getWidth()/2)),(this.getY()+(this.getHeight()/2)));
	}
	
	/**
	 * Description: Change the entity's y coordinate.
	 * @param r: The value to add onto the entity's current y coordinate.
	 */
	public void changeY(double r) {
		this.setY(this.getY()+r);
	}
	
	/**
	 * Description: Change the entity's x coordinate.
	 * 
	 * Source: Based on Almas Baimagambetov’s 
	 * tutorial “GameDev: Collision Detection - SAT” found on 
	 * https://www.youtube.com/watch?v=Sv42pfgiAI4.
	 * 
	 * @param r: The value to add onto the entity's current x coordinate.
	 */
	public void changeX(double r) {
		this.setX(this.getX()+r);
	}
	
	/**
	 * Description: Returns the corner points of the entity.
	 * 
	 * Source: Based on Almas Baimagambetov’s 
	 * tutorial “GameDev: Collision Detection - SAT” found on 
	 * https://www.youtube.com/watch?v=Sv42pfgiAI4.
	 * 
	 * @return: A list of Point2Ds representing the entity's corner points.
	 */
	public List<Point2D> corners(){
		return cornerVectors().stream()
				.map(v -> new Point2D(
						(v.getX() *cos(this.getRotate()))- (v.getY() * sin(this.getRotate())),
						(v.getX() * sin(this.getRotate())) + (v.getY() * cos(this.getRotate()))		
						))
				.map(v -> v.add(center()))
				.collect(Collectors.toList());
	}
	
	/**
	 * Description: Create a normalised list of the entity's corner vectors from the centre.
	 * 
	 * Source: Based on Almas Baimagambetov’s 
	 * tutorial “GameDev: Collision Detection - SAT” found on 
	 * https://www.youtube.com/watch?v=Sv42pfgiAI4.
	 * @return: A List of Point2D objects representing the normalised entity corners.
	 */
	public List<Point2D> cornerVectors(){
		return Arrays.asList(
		new Point2D( this.getX(), this.getY()),
		new Point2D( this.getX() + this.getWidth(), this.getY()),
		new Point2D( this.getX() + this.getWidth(), this.getY()+ this.getHeight()),
		new Point2D( this.getX(), this.getY() + this.getHeight())
	)
		.stream()
		.map(v -> v.subtract(center()))
		.collect(Collectors.toList());
		
	}
	
	/**
	 * Description: Introduce Math.cos into the package name space.
	 * @param angle: An angle in degrees.
	 * @return: The cosine of an angle.
	 */
	private static double cos(double angle){
		return Math.cos(Math.toRadians(angle));
	}
	
	/**
	 * Description: Renders the entity object. 
	 * 
	 * Source: Based on Almas Baimagambetov’s 
	 * tutorial “GameDev: Collision Detection - SAT” found on 
	 * https://www.youtube.com/watch?v=Sv42pfgiAI4.
	 * @param g: The graphics context associated with a javaFX scene.
	 */
	public void draw(GraphicsContext g){
		g.save();
		
		Rotate r = new Rotate(this.getRotate(), this.getX()+ this.getWidth()/2, this.getY()+ this.getHeight()/2);
		g.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
		g.strokeRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		
		g.restore();
	}
	
	/**
	 * Description: Returns the orientation of the entity in radians.
	 * @return: The orientation of the entity in radians.
	 */
	public double getOrientation() {
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
	
	/**
	 * Description: Method that returns the orientation of the robot in radians 
     * using the getRotate() method.
	 * 
	 * @param e2: An entity object.
	 * @return: The current orientation of the robot in radians relative to y axis, clockwise.
	 */
	public boolean isColliding(Entity e2){
		return SAT.isColliding(e2, this);
	}
	
	/**
	 * Description: Set the entity height to a given value.
	 * @param h: The height to set the robot to.
	 */
	public void setHeight(int h){
		super.setHeight(h);
	}
	
	/**
	 * Description: Set the entity width to a given value.
	 * @param w: The width to set the robot to.
	 */
	public void setWidth(int w){
		super.setWidth(w);
	}
	
	/**
	 * Description: Set the entity x coordinate to a given value.
	 * @param x: An x coordinate to set to.
	 */
	public void setX(int x){
		super.setX(x);
	}
	
	/**
	 * Description: Set the entity y coordinate to a given value.
	 * @param y: A y coordinate to set to.
	 */
	public void setY(int y){
		super.setY(y);
	}
	
	/**
	 * Description: Introduce Math.sin into the package name space.
	 * @param angle: An angle in degrees.
	 * @return: The sine of an angle.
	 */
	private static double sin(double angle){
		return Math.sin(Math.toRadians(angle));
	}
}