package readers;


import java.util.ArrayList;

public class Robot {
	String name;
	int batterysize;
	int chargelevel;
	int maxspeed;

	Robot(String s){
	    XMLReader xmlr = new XMLReader();
	   ArrayList<String> input = xmlr.read(s, "/Users/Lucas/Dropbox/UCL/Proprietary/Eclipse Workspace/CourseWork/src/learning/javafx/robots.xml");
	   this.name = input.get(0);
	   this.batterysize = (int)Integer.valueOf(input.get(1));
	   this.chargelevel = (int)Integer.valueOf(input.get(2));
	   this.maxspeed = (int)Integer.valueOf(input.get(3));
	}

}
