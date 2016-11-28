/**
 * Description: This XML reader is able to read through an XML file and return the attributes related to a specific id.
 * ArrayList of strings. 
 * 
 * @author Geraint Ballinger and Lucas Valtl
 * @version 1.0
 */

package readers;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class XMLReader {

	/**
	 * Reads through an XML file and returns the attributes related to a specific ID. 
	 * 
	 * @param s: String containing the type of robot you want to load from the xml file. This needs to correspond with an ID from the xml file.
	 * @param f: location of the xml file used
	 * @return: returns an ArrayList of attributes corresponding with the id specified in String s.
	 */
	
	public ArrayList<String> read(String s, String f){
	    try {
//get file and create file
			File XmlFile = new File(f);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(XmlFile);
			ArrayList<String> output = new ArrayList<String>();

			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("robot");

			outside: for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					if (eElement.getAttribute("id").equals(s)) {
						output.add(eElement.getElementsByTagName("name").item(0).getTextContent());
						output.add(eElement.getElementsByTagName("xCoordinate").item(0).getTextContent());
						output.add(eElement.getElementsByTagName("yCoordinate").item(0).getTextContent());
						output.add(eElement.getElementsByTagName("speed").item(0).getTextContent());
						output.add(eElement.getElementsByTagName("xVelocity").item(0).getTextContent());
						output.add(eElement.getElementsByTagName("yVelocity").item(0).getTextContent());
						output.add(eElement.getElementsByTagName("xAcceleration").item(0).getTextContent());
						output.add(eElement.getElementsByTagName("yAcceleration").item(0).getTextContent());
						output.add(eElement.getElementsByTagName("angularVelocity").item(0).getTextContent());
						output.add(eElement.getElementsByTagName("odometer").item(0).getTextContent());
						output.add(eElement.getElementsByTagName("batteryLeft").item(0).getTextContent());
						output.add(eElement.getElementsByTagName("batteryCapacity").item(0).getTextContent());
						output.add(eElement.getElementsByTagName("axleLength").item(0).getTextContent());
						output.add(eElement.getElementsByTagName("wheelRadius").item(0).getTextContent());
						break outside;
					}
				}
			}
			return output;
		} catch (Exception e) {
			e.printStackTrace();
			return null; 
		}
	    
	   
	}
	
	
	
}
