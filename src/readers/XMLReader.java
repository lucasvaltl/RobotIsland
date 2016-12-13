package readers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Description: This XML reader reads through an XML file and return the attributes related to a specific id.
 * 
 * @author Geraint and Lucas
 */
public final class XMLReader {

	/**
	 * Reads through an XML file and returns the attributes related to a specific ID. 
	 * 
	 * @param s: String containing the type of robot you want to load from the XML file. This needs to correspond with an ID from the XML file.
	 * @param f: The location of the XML file being used
	 * @return: An ArrayList of attributes corresponding with the id specified in s.
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
						output.add(eElement.getElementsByTagName("maxSpeed").item(0).getTextContent());
						output.add(eElement.getElementsByTagName("acceleration").item(0).getTextContent());
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return null;
	}
}