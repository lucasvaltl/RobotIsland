package robot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javafx.scene.input.KeyEvent;
import readers.InvalidFormatException;
import readers.NewFileReader;
import tests.Driver;

/**
 * Description: Dummy robot class used for replays.
 * @author Geraint and Lucas
 *
 */
public class DummyRobot extends Robot {

	private int timeTrialInputCommandsIndex = 0;
	private boolean timeTrialInputInProgress = false;
	private ArrayList<String> timeTrialInputCommands = null;
	
	
	/**
	 * Description: Calls the robot class constructor
	 * @param s: The name of the robot instance
	 */
	public DummyRobot(String s) {
		super(s);
		this.setOpacity(0.5);
	}

	public boolean getTimeTrialInputInProgress() {
		return this.timeTrialInputInProgress;
	}
	
	/**
	 * Description: Override the robot's key handler to do nothing.
	 */
	public void handle(KeyEvent event) {
		// Do not respond to player's key presses.
	}
	
	public void setTimeTrialInputInProgress(boolean value) {
		this.timeTrialInputInProgress = value;
	}
	
	/**
	 * @deprecated
	 * @param file
	 */
	public void timeTrialSingleMoveViaFile(File file) {
		System.out.println("BOOM");
		
		if (this.timeTrialInputCommands == null) {
			
			// No commands in file, load them up.
			this.timeTrialInputCommands = new ArrayList<String>();
			this.timeTrialInputInProgress= true;
			NewFileReader nfr = null;
			try {
				nfr = new NewFileReader();
				this.timeTrialInputCommands = nfr.scanFile(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (InvalidFormatException e) {
				e.printStackTrace();
			}
			finally {
				// do nothing
			}
		}
		
		if (this.timeTrialInputInProgress == true) {
		
			String robotInfo = (this.timeTrialInputCommands.get(this.timeTrialInputCommandsIndex).toString());
			
			// Split string by whitespace
			String xPos = robotInfo.split("\\s+")[0];
			xPos = xPos.substring(1, xPos.length() - 1);
			String yPos = robotInfo.split("\\s+")[1];
			yPos = yPos.substring(0, yPos.length() - 1);
			String orientation = robotInfo.split("\\s+")[2];
			orientation = orientation.substring(0, orientation.length() - 1);
			String speed = robotInfo.split("\\s+")[3];
			speed = speed.substring(0, speed.length() - 1);
			String battery = robotInfo.split("\\s+")[4];
			battery = battery.substring(0, battery.length() - 1);
			
			this.setxCoordinate(Double.parseDouble(xPos));
			this.setyCoordinate(Double.parseDouble(yPos));
			this.setRotate(Double.parseDouble(orientation));
			this.setSpeed(Double.parseDouble(speed));
			this.setBatteryLeft(Double.parseDouble(battery));
		}

		// get the inputCommands arrayList size
		if (this.timeTrialInputCommandsIndex >= this.timeTrialInputCommands.size() - 1) {
			// Cause deceleration
			this.timeTrialInputInProgress = false;
			this.currentKeyPresses[0] = null;
			this.currentKeyPresses[1] = null;
			Driver.dummy.setVisible(false);
			Driver.dummy.timeTrialInputCommands = null;
			Driver.dummy = null; // TODO Experimental
		} else {
			this.timeTrialInputCommandsIndex++;
		}
	}
	
	public void timeTrialSingleMoveViaFile(InputStream inputStream) throws IOException {
		if (this.timeTrialInputCommands == null) {
			
			// No commands in file, load them up.
			this.timeTrialInputCommands = new ArrayList<String>();
			this.timeTrialInputInProgress= true;
			
			InputStreamReader isr = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
			BufferedReader br = null;
			
			try {
				br = new BufferedReader(isr);
				String line;
				while ((line = br.readLine()) != null) {
					this.timeTrialInputCommands.add(line);	
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				br.close();
			}
		}
		
		if (this.timeTrialInputInProgress == true) {
		
			String robotInfo = (this.timeTrialInputCommands.get(this.timeTrialInputCommandsIndex).toString());
			
			// Split string by whitespace
			String xPos = robotInfo.split("\\s+")[0];
			xPos = xPos.substring(1, xPos.length() - 1);
			String yPos = robotInfo.split("\\s+")[1];
			yPos = yPos.substring(0, yPos.length() - 1);
			String orientation = robotInfo.split("\\s+")[2];
			orientation = orientation.substring(0, orientation.length() - 1);
			String speed = robotInfo.split("\\s+")[3];
			speed = speed.substring(0, speed.length() - 1);
			String battery = robotInfo.split("\\s+")[4];
			battery = battery.substring(0, battery.length() - 1);
			
			this.setxCoordinate(Double.parseDouble(xPos));
			this.setyCoordinate(Double.parseDouble(yPos));
			this.setRotate(Double.parseDouble(orientation));
			this.setSpeed(Double.parseDouble(speed));
			this.setBatteryLeft(Double.parseDouble(battery));
		}

		// get the inputCommands arrayList size
		if (this.timeTrialInputCommandsIndex >= this.timeTrialInputCommands.size() - 1) {
			// Cause deceleration
			this.timeTrialInputInProgress = false;
			this.currentKeyPresses[0] = null;
			this.currentKeyPresses[1] = null;
			Driver.dummy.setVisible(false);
			Driver.dummy.timeTrialInputCommands = null;
			Driver.dummy = null; // TODO Experimental
		} else {
			this.timeTrialInputCommandsIndex++;
		}
	}
	
	/**
	 * Description: Override the robot update() method to check for time trial input commands.
	 */
	public void update() {

		final double wallEorientation = this.getOrientation();

		double[] wallEcomponents = this.getOrientationComponents(wallEorientation);

		this.setWheelspeeds(0, 0);
		
		detectCollision(this, wallEcomponents);
		
		// read commands from file
		if (this.getTimeTrialInputInProgress() == true) {
			try {
				this.timeTrialSingleMoveViaFile(Driver.timeTrialInputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			this.move(wallEcomponents);
		}
		this.animate(wallEcomponents);
		this.consumeBattery(this.getWheelspeeds());
		this.checkForCharging();
			
		if(Driver.toggledevmode)
			this.updateDevPanel();
			
		this.updateDistance();
	}
}
