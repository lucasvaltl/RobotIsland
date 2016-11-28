package robot;

import java.util.ArrayList;

import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import tests.Driver;

public class CollisionDetection {

	 public static boolean collisionDetection(Rectangle robot,
	 ArrayList<Rectangle> blocks) {
	 for (Rectangle staticblocs : blocks) {
	 if
	 (robot.getBoundsInParent().intersects(staticblocs.getBoundsInParent())) {
	 System.out.println("collision detected");
	 robot.setFill(Color.RED);
	 return true; // collision
	 }
	 }
	 return false;
	 }

//	public static boolean collisionDetection(Rectangle robot, ArrayList<Rectangle> blocks) {
//
//		for (Rectangle block : blocks) {
//			Bounds objA = robot.localToScene(robot.getBoundsInLocal());
//			Bounds objB = block.localToScene(block.getBoundsInLocal());
//			System.out.println("detected");
//			if (objA.intersects(objB)) {
//				robot.setFill(Color.RED);
//				System.out.println("collision detected");
//				return true;
//			} else {
//				return false;
//			}
//		}
//		return false;
//	}
}