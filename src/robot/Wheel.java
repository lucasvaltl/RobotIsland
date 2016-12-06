package robot;

import javafx.scene.shape.Rectangle;


/**
 * Description: Class that makes wheels for a robot
 * 
 * 
 * @author Lucas
 *
 */
public class Wheel extends Rectangle{
	
	private double height;
	private double width;
	private double xCoordinate;
	private double yCoordinate;
	
	/**
	 * Description: Adds a wheel to an existing robot
	 * 
	 * @param r: Robot that needs wheels
	 * @param left: Boolean that is true if this is a left wheel
	 */
	public Wheel(Robot r, boolean left) {
		if(left){
		
			double RobotsXCord = r.getxCoordinate();
			double RobotsYCord = r.getyCoordinate();
		//this.height=r.getHeight();
		super.setHeight(this.height);
		//this.width=((r.getWidth())/5);
		super.setWidth(this.width);
		this.xCoordinate = RobotsXCord;
		super.setX(this.xCoordinate);
		this.yCoordinate = RobotsYCord;
		super.setY(this.yCoordinate);
		}else{
			double RobotsXCord = r.getxCoordinate();
			double RobotsYCord = r.getyCoordinate();
		//this.height=r.getHeight();
		super.setHeight(this.height);
		//this.width=((r.getWidth())/5);
		super.setWidth(this.width);
		this.xCoordinate = RobotsXCord + (this.width*4);
		super.setX(this.xCoordinate);
		this.yCoordinate = RobotsYCord;
		super.setY(this.yCoordinate);
		}
		
		
		
	}

	
	
	
}
