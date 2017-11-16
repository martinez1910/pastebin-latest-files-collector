import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FindFile {
	private final static String CRITERIA_RELATIVE_PATH = "criteria.txt";
	private final static String FILES_RELATIVE_PATH = "./files";
	private static HashMap<String, Integer> criteria = new HashMap<String, Integer>();
	private static HashMap<File, List<String>> filesMatchCriteria = new HashMap<File, List<String>>();
	
	public static void main(String[] args) throws IOException {
		loadCriteria();
		
		File filesFolder = new File(FILES_RELATIVE_PATH);
		loadFilesAndCheckCriteria(filesFolder);
				
		printFiles();
	}
	
	

	private static void loadCriteria() throws IOException{
		Path file = Paths.get(CRITERIA_RELATIVE_PATH);
		List<String> list = readFile(file);
		for(String str : list)
			criteria.put(str.toLowerCase(), null);
	}
	
	private static void loadFilesAndCheckCriteria(File folder) throws IOException {		
		    for (File fileEntry : folder.listFiles()) {
		        /*Unnecessary
		    	if (fileEntry.isDirectory())
		            loadFiles(fileEntry);
		        */
		    	List<String> list = readFile(Paths.get(fileEntry.getAbsolutePath()));
		    	List<String> wordsMatched = checkCriteria(list);
		    	if(!wordsMatched.isEmpty())
		    		filesMatchCriteria.put(fileEntry, wordsMatched);
		    }
	}
	
	
	private static List<String> readFile(Path file) throws IOException{
		return Files.readAllLines(file);
	}
	
	private static List<String> checkCriteria(List<String> text) {
		List<String> wordsFound = new ArrayList<String>();
		for(String line : text)
			for(String word : line.split(" "))
				if(criteria.containsKey(word.toLowerCase()))
					if(!wordsFound.contains(word))
						wordsFound.add(word);
		return wordsFound;
	}
	
	private static void printFiles() {
		System.out.println("Files that met the criteria:");
		for(File file : filesMatchCriteria.keySet()) {
			System.out.println("\t" +file.getName());
			for(String wordFound : filesMatchCriteria.get(file))
				System.out.println("\t\t"+wordFound);
		}
	}
}
