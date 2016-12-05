package robot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

public class SAT {

	
	public static boolean isColliding(Robot rob, Entity rec){
		List<Point2D> axes = new ArrayList<>();
		axes.addAll(getAxes(rob.getRotate()));
		axes.addAll(getAxes(rec.getRotate()));
		
		axes = axes.stream()
				.map(Point2D::normalize)
				.collect(Collectors.toList());
		List<Point2D> robCorners = rob.corners();
		List<Point2D> recCorners = rec.corners();
		
		for (Point2D axis : axes) {
			double robMin = robCorners.stream().mapToDouble(p -> p.dotProduct(axis)).min().getAsDouble();
			double robMax = robCorners.stream().mapToDouble(p -> p.dotProduct(axis)).max().getAsDouble();
			
			double recMin = recCorners.stream().mapToDouble(p -> p.dotProduct(axis)).min().getAsDouble();
			double recMax = recCorners.stream().mapToDouble(p -> p.dotProduct(axis)).max().getAsDouble();
			if (robMax < recMax || robMin < recMin ){
				return true;
			}
		}
		
		
		
		
		
		
		return true;
	}
	
	public static List<Point2D> getAxes(double angle){
		return Arrays.asList(
				new Point2D(cos(angle), sin(angle)),
				new Point2D(cos(angle+90), sin(angle+90))
				);
				
	}
	
	private static double cos(double angle){
		return Math.cos(Math.toRadians(angle));
	}
	
	private static double sin(double angle){
		return Math.sin(Math.toRadians(angle));
	}
}
