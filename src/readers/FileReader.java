/**
 * Description: This class reads and validates a file. Th file needs to include valid instructions on how the a robot should move
 * 
 * @author Geraint Ballinger and Lucas Valtl
 * 
 */


package readers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileReader {

/**
 * Description: This method scans and validates a file containing movement instructions for a robot. 
 * Upon validation it returns an ArrayList of strings with the instructions. Should the instructions in the file 
 * not match the instructions expected, an InvalidFormatException is thrown.
 * 
 * 
 * @param uri: define the location of the file
 * @return: returns an ArrayList of Strings with validated movement instructions
 * @throws InvalidFormatException
 * @throws FileNotFoundException
 */
	public ArrayList<String> scanFile(String uri) throws InvalidFormatException, FileNotFoundException {

	
			ArrayList<String> input = new ArrayList<String>();
			Scanner scanner = new Scanner(new File(uri));
			while (scanner.hasNext()) {
				input.add(scanner.next());
			}
			for (int i = 0; i < input.size(); i++) {
				switch (input.get(i)) {
				case "UP":
					break;
				case "DOWN":
					break;
				case "LEFT":
					break;
				case "RIGHT":
					break;
				default:
					throw new InvalidFormatException("Invalid formatting found in movement file");
				}

			}
			scanner.close();
			return input;
		
	}
}
