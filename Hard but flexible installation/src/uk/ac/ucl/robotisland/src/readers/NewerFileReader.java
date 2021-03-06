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
public class NewerFileReader {

/**
 * Description: This method scans and validates a file containing movement instructions for a robot. 
 * Upon validation it returns an ArrayList of strings with the instructions. Should the instructions in the file 
 * not match the instructions expected, an InvalidFormatException is thrown.
 * 
 * @param uri: The location of the file
 * @return: An ArrayList of Strings with validated movement instructions
 * @throws InvalidFormatException: Thrown when the file is in an invalid format.
 * @throws FileNotFoundException: Thrown when the file is not found.
 */
	public ArrayList<String> scanFile(String uri) throws InvalidFormatException, FileNotFoundException {

	
			ArrayList<String> input = new ArrayList<String>();
			Scanner scanner = new Scanner(new File(uri));
			while (scanner.hasNext()) {
				input.add(scanner.nextLine());
			}
			
			// Validate format
			for (int i = 0; i < input.size(); i++) {
				
				switch (input.get(i)) {
				case "moveDown":
					break;
				case "moveUp":
					break;
				case "moveDownLeft":
					break;
				case "moveDownRight":
					break;
				case "moveUpLeft":
					break;
				case "moveUpRight":
					break;
				case "moveLeft":
					break;
				case "moveRight":
					break;
				case "decelerate":
					break;
				default:
					System.out.println(input.get(i));
					throw new InvalidFormatException("Invalid formatting found in movement file");
				}

			}
			scanner.close();
			return input;
		
	}
	public ArrayList<String> scanFile(File file) throws InvalidFormatException, FileNotFoundException {

		ArrayList<String> input = new ArrayList<String>();
		Scanner scanner = new Scanner(file);
		while (scanner.hasNext()) {
			input.add(scanner.nextLine());
		}
		for (int i = 0; i < input.size(); i++) {
			
			switch (input.get(i)) {
			case "moveDown":
				break;
			case "moveUp":
				break;
			case "moveDownLeft":
				break;
			case "moveDownRight":
				break;
			case "moveUpLeft":
				break;
			case "moveUpRight":
				break;
			case "moveLeft":
				break;
			case "moveRight":
				break;
			case "decelerate":
				break;
			default:
				
				throw new InvalidFormatException("Invalid formatting found in movement file on line / word " + i+1);
			}

		}
		scanner.close();
		return input;
	
}
}
