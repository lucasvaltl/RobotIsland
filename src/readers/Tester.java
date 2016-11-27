package learning.javafx;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;

public class Tester {

  public static void main(String argv[]) {
	  Robot r = new Robot("slow");
	  System.out.println("name: " + r.name +" Bsize: " + r.batterysize + " Clevel: " + r.chargelevel + " MaxSpeed: " + r.maxspeed);
	  FileReader reader = new FileReader();
	  System.out.println("created reader");
	  ArrayList<String> movements = reader.scanFile("/Users/Lucas/Dropbox/UCL/Proprietary/Eclipse Workspace/CourseWork/src/learning/javafx/movements.txt");
	  System.out.println(movements);
	  System.out.println(reader.validate(movements));
	
	  
}}