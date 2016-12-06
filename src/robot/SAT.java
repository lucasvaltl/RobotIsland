package robot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

public class SAT {

	
	public static boolean isColliding(Entity rob, Entity rec){
		
		
		double robMin = 0;
		double robMax= 0;
		double recMin = 0;
		double recMax = 0;
		
		List<Point2D> axes = new ArrayList<>();
		axes.addAll(getAxes(rob.getRotate()));
		axes.addAll(getAxes(rec.getRotate()));
		
		axes = axes.stream()
				.map(Point2D::normalize)
				.collect(Collectors.toList());
		List<Point2D> robCorners = rob.corners();
		List<Point2D> recCorners = rec.corners();
		
		for (Point2D axis : axes) {
			robMin = robCorners.stream().mapToDouble(p -> p.dotProduct(axis)).min().getAsDouble();
			robMax = robCorners.stream().mapToDouble(p -> p.dotProduct(axis)).max().getAsDouble();
			
			recMin = recCorners.stream().mapToDouble(p -> p.dotProduct(axis)).min().getAsDouble();
			recMax = recCorners.stream().mapToDouble(p -> p.dotProduct(axis)).max().getAsDouble();
			
			if (robMax < recMin || recMax < robMin ){
				return false;
			}
		}
		System.out.println(" robmin: " + robMin + " robMax: " +robMax + " recmin: " + recMin + " recMax: " + recMax);
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
