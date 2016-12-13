package robot;

import javafx.scene.input.KeyEvent;

/**
 * Description: Dummy robot class used for replays.
 * @author Geraint and Lucas
 *
 */
public class DummyRobot extends Robot {

	/**
	 * Description: Calls the robot class constructor
	 * @param s: The name of the robot instance
	 */
	public DummyRobot(String s) {
		super(s);
		this.setOpacity(0.5);
	}

	/**
	 * Description: Override the robot's key handler to do nothing.
	 */
	public void handle(KeyEvent event) {
		// Do not respond to player's key presses.
	}
}
