package readers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileReader {


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
