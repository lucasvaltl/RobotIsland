package robot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.geometry.Point2D;
/**
 * Description: Abstract class holding static methods used to 
 * implement SAT collision detection. Based on Almas Baimagambetov’s 
 * tutorial “GameDev: Collision Detection - SAT” found on 
 * https://www.youtube.com/watch?v=Sv42pfgiAI4.
 *
 * @author Geraint and Lucas
 *
 */
public abstract class SAT {

	/** Definition: Wrapper function that returns 
	 *  the cosine of a given angle.
	 * 
	 * @param angle: An angle in degrees.
	 * @return: The cosine of an angle.
	 */
	private static double cos(double angle){
		return Math.cos(Math.toRadians(angle));
	}
	
	/** Definition: Resolves a given angle into two perpendicular axes.
	 * 
	 * @param angle: An angle in degrees.
	 * @return: A List of Point2D objects containing the two perpendicular axes.
	 */
	public static List<Point2D> getAxes(double angle){
		return Arrays.asList(
				// 2nd rectangle axis is perpendicular (hence add 90 degrees)
				new Point2D(cos(angle), sin(angle)),
				new Point2D(cos(angle+90), sin(angle+90))
				);
				
	}
	
	/**
	 * Description: Method used to determine whether two entities are colliding.
	 * @param rob: The first entity to check against.
	 * @param rec: The second entity to check against.
	 * @return: True when the entities intersect.
	 */
	public static boolean isColliding(Entity rob, Entity rec){
		
		double robMin = 0;
		double robMax= 0;
		double recMin = 0;
		double recMax = 0;
		
		// Map corner points of rectangle onto produced axes
		
		// Get axes and normalise them
		List<Point2D> axes = new ArrayList<>();
		axes.addAll(getAxes(rob.getRotate())); // gets the two axes for robot
		axes.addAll(getAxes(rec.getRotate())); // gets the two axes for rectangle
		
		axes = axes.stream()
				.map(Point2D::normalize) // Normalise returns a vector in the 
				// same direction but with magnitude 1
				.collect(Collectors.toList());
		List<Point2D> robCorners = rob.corners(); // corner points after rotation
		List<Point2D> recCorners = rec.corners(); // corner points after rotation
		
		// check overlap between axes
		for (Point2D axis : axes) {
			// get max and min values relative to axis.
			
			// map corner point to axis using dot product
			robMin = robCorners.stream().mapToDouble(p -> p.dotProduct(axis)).min().getAsDouble();
			robMax = robCorners.stream().mapToDouble(p -> p.dotProduct(axis)).max().getAsDouble();
			
			recMin = recCorners.stream().mapToDouble(p -> p.dotProduct(axis)).min().getAsDouble();
			recMax = recCorners.stream().mapToDouble(p -> p.dotProduct(axis)).max().getAsDouble();
			
			// check if there is not a collision
			if (robMax < recMin || recMax < robMin){
				return false;
			}
		}
		
		return true;
	}
	
	/** Definition: Wrapper function that returns 
	 *  the sine of a given angle. 
	 * 
	 * @param angle: An angle in degrees.
	 * @return: The cosine of an angle.
	 */
	private static double sin(double angle){
		return Math.sin(Math.toRadians(angle));
	}
}
