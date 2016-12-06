package robot;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class Entity extends Rectangle{
double x;
double y;
double width;
double height;
	
	public Entity(int x, int y, int width, int height){
		super.setX(x);
		super.setY(y);
		super.setWidth(width);
		super.setHeight(height);
	}
	
	public Entity(){
		
	}
	
	public void setX(int x){
		super.setX(x);
	}
	
	public void setY(int y){
		super.setY(y);
	}
	
	public void setWidth(int w){
		super.setWidth(w);
	}
	
	public void setHeight(int h){
		super.setHeight(h);
	}
	
	/**
	 * Description: find the center point
	 * 
	 * @return: Returns centerpoint of the object
	 */
	
	public Point2D center(){
		System.out.println("CENTER X: " + (this.getX()+(this.getWidth()/2)) + " Y: " + (this.getX()+(this.getHeight()/2)));
		return new Point2D((this.getX()+(this.getWidth()/2)),(this.getY()+(this.getHeight()/2)));
		
		
	}
	
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
	
	public List<Point2D> corners(){
		return cornerVectors().stream()
				.map(v -> new Point2D(
						(v.getX() *cos(this.getRotate()))- (v.getY() * sin(this.getRotate())),
						(v.getX() * sin(this.getRotate())) + (v.getY() * cos(this.getRotate()))		
						))
				.map(v -> v.add(center()))
				.collect(Collectors.toList());
	}
	
	private static double cos(double angle){
		return Math.cos(Math.toRadians(angle));
	}
	
	private static double sin(double angle){
		return Math.sin(Math.toRadians(angle));
	}
	
	public void draw(GraphicsContext g){
		g.save();
		
		Rotate r = new Rotate(this.getRotate(), this.getX()+ this.getWidth()/2, this.getY()+ this.getHeight()/2);
		g.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
		g.strokeRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		
		g.restore();
	}
	
	/** Description: Method that returns the orientation of the robot in radians 
     * using the getRotate() method.
	 * 
	 * @return: The current orientation of the robot in radians relative to y axis, clockwise.
	 */
	
	public boolean isColliding(Entity e2){
		return SAT.isColliding(e2, this);
	}
	
	public void changeY(double r) {
		this.setY(this.getY()+r);
	}
	
	public void changeX(double r) {
		this.setX(this.getX()+r);
	}
	
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
}
