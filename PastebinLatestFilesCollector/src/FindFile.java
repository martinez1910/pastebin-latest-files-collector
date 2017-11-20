import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Finds the words (written in a criteria file and separated by a line) that exists in the files of a given folder. 
 * @author A. Martínez
 * @version 1.0 21/11/2017
 */
public class FindFile {
	private final static String CRITERIA_RELATIVE_PATH = "criteria.txt";
	private final static String FILES_RELATIVE_PATH = "./files";
	private static HashMap<String, Integer> criteria = new HashMap<String, Integer>();
	private static HashMap<File, List<String>> filesMatchCriteria = new HashMap<File, List<String>>();

	/**
	 * Executes the program.
	 * @param args
	 * @throws IOException If an I/O error occurs reading from the file or a malformed or unmappable byte sequence is read.
	 */
	public static void main(String[] args) throws IOException {
		loadCriteria();

		File filesFolder = new File(FILES_RELATIVE_PATH);
		loadFilesAndCheckCriteria(filesFolder);

		printFiles();
	}


	/**
	 * Loads the words present in the criteria file.
	 * @throws IOException If an I/O error occurs reading from the file or a malformed or unmappable byte sequence is read.
	 */
	private static void loadCriteria() throws IOException{
		Path file = Paths.get(CRITERIA_RELATIVE_PATH);
		List<String> list = readFile(file);
		for(String str : list)
			criteria.put(str.toLowerCase(), null);
	}

	/**
	 * Loads the files in the given folder and invokes checkCriteria() in each of them.
	 * @param folder The folder where the files are.
	 * @throws IOException If an I/O error occurs reading from the file or a malformed or unmappable byte sequence is read.
	 */
	private static void loadFilesAndCheckCriteria(File folder) throws IOException {		
		for (File fileEntry : folder.listFiles()) {
			/*Unnecessary if files not organised in sub-folders.
		    if (fileEntry.isDirectory())
		    	loadFilesAndCheckCriteria(fileEntry);
			 */
			List<String> list = readFile(Paths.get(fileEntry.getAbsolutePath()));
			List<String> wordsMatched = checkCriteria(list);
			if(!wordsMatched.isEmpty())
				filesMatchCriteria.put(fileEntry, wordsMatched);
		}
		System.out.println("Number of files checked: " +folder.listFiles().length);
	}

	/**
	 * Extracts a list with the lines of the text that is in the given file.
	 * @param file The path of the file.
	 * @return A list with the lines of the text.
	 * @throws IOException If an I/O error occurs reading from the file or a malformed or unmappable byte sequence is read.
	 */
	private static List<String> readFile(Path file) throws IOException{
		return Files.readAllLines(file);
	}

	/**
	 * Finds the words listed in the criteria file. 
	 * Compares both words with their lower case version using String toLowerCase().
	 * @param text The text of each file to be checked.
	 * @return A list with the words found.
	 */
	private static List<String> checkCriteria(List<String> text) {
		List<String> wordsFound = new ArrayList<String>();
		for(String line : text)
			for(String word : line.split("\\W+"))//Anything that is not a word [\W], in groups of at least one [+]. It will take for instance ';' and ' ' together as one delimiter.
				if(criteria.containsKey(word.toLowerCase()))
					if(!wordsFound.contains(word))//Can add same word but different spelling like 'word' and 'Word' (if both were found)
						wordsFound.add(word);
		return wordsFound;
	}

	/**
	 * Prints the files that match the criteria and the words found as they are in the file (case-sensitive).
	 */
	private static void printFiles() {
		System.out.println("Files that met the criteria: " +filesMatchCriteria.size());
		for(File file : filesMatchCriteria.keySet()) {
			System.out.println("\t" +file.getName());
			for(String wordFound : filesMatchCriteria.get(file))
				System.out.println("\t\t"+wordFound);
		}
	}
}
