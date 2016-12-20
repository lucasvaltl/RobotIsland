package uk.ac.ucl.robotisland.src.readers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 * Description: This class reads and validates a file. 
 * The file must include valid robot move instructions.
 * 
 * @author Geraint and Lucas
 * 
 */
public class FileReader {

/**
 * Description: This method scans and validates a file containing movement instructions for a robot. 
 * Upon validation it returns an ArrayList of strings with the instructions. Should the instructions in the file 
 * not match the instructions expected, an InvalidFormatException is thrown.
 * 
 * @param uri: The location of the file
 * @return: An ArrayList of Strings with validated movement instructions
 * @throws InvalidFormatException: Thrown when the form is in an invalid format.
 * @throws FileNotFoundException: Thrown when the file is not found.
 */
	public ArrayList<String> scanFile(String uri) throws InvalidFormatException, FileNotFoundException {

	
			ArrayList<String> input = new ArrayList<String>();
			Scanner scanner = new Scanner(new File(uri));
			while (scanner.hasNext()) {
				input.add(scanner.nextLine());
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
