package readers;

import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
	public ArrayList<String> read(String s, String path){
	    try {
	    	//get xml file
	    	InputStream inputStream = XMLReader.class.getResourceAsStream(path);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputStream);
			ArrayList<String> output = new ArrayList<String>();

			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("robot");
			outside: for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					if (eElement.getAttribute("id").equals(s)) {
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
		} catch (Exception e) {
			e.printStackTrace();
			return null; 
		}
	}
}