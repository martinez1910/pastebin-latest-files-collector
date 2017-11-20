import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * (Single-threaded) Downloads the latest files from Pastebin.com as .txt files continuously.
 * @author A. Martínez
 * @version 1.0 21/11/2017
 */
public class Main {

	/**
	 * Executes the program continuously.
	 * @param args
	 * @throws IOException If an I/O error occurs reading from the file or a malformed or unmappable byte sequence is read.
	 */
	public static void main(String[] args) throws IOException {
		while(true) {
			String sourceCode = null;
			List<String> listFileNames, listFileTexts=null;

			long t1,t2, tTotal=0;

			t1 = System.currentTimeMillis();
			//Get source code
			sourceCode = getUrlSource("https://pastebin.com/archive");
			t2 = System.currentTimeMillis();
			System.out.println("Getting the source code of the URL: " +(t2-t1) +"ms");
			tTotal += t2-t1;

			t1 = System.currentTimeMillis();
			//Get list with names
			listFileNames = extractNames(sourceCode);
			t2 = System.currentTimeMillis();
			System.out.println("Getting the list with names: " +(t2-t1) +"ms");
			tTotal += t2-t1;

			t1 = System.currentTimeMillis();
			//Get list with texts
			listFileTexts = extractTexts(listFileNames);
			t2 = System.currentTimeMillis();
			System.out.println("Getting the list with texts: " +(t2-t1) +"ms");
			tTotal += t2-t1;

			t1 = System.currentTimeMillis();
			//Create .txt files locally
			createFiles(listFileNames, listFileTexts);
			t2 = System.currentTimeMillis();
			System.out.println("Getting the files locally: " +(t2-t1) +"ms");
			tTotal += t2-t1;
			System.out.println("-------------------------------------");
			System.out.println("Total time spent: " +(tTotal) +"ms");	
			System.out.println("-------------------------------------");
			System.out.println();
		}//while(true)
	}


	/**
	 * Returns the source code of a given URL.
	 * @param url The URL of the web page.
	 * @return The source code of the URL.
	 * @throws IOException If an I/O error occurs reading from the file or a malformed or unmappable byte sequence is read.
	 */
	private static String getUrlSource(String url) throws IOException {
		URL var = new URL(url);
		URLConnection connection = var.openConnection();

		BufferedReader br = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));//, "UTF-8"));

		String inputLine;
		StringBuilder a = new StringBuilder();
		while ((inputLine = br.readLine()) != null)
			a.append(inputLine);

		br.close();
		return a.toString();
	}

	/**
	 * Returns a list with the files' names given the source code of Pastebin's archive.
	 * @param htmlSourceCode The source code of the web page with the latest files.
	 * @return A list with the files' names.
	 */
	private static List<String> extractNames(String htmlSourceCode){
		List<String> list = new ArrayList<String>();
		String controlString = "class=\"i_p0\" alt=\"\" /><a href=\"/"; //After this string is the name of the files
		int beginIndex = htmlSourceCode.indexOf(controlString);
		while(beginIndex != -1) {
			//Cuts the source code after controlString so first 8 characters will be the name and we can use indexOf() again.
			htmlSourceCode = htmlSourceCode.substring(beginIndex+32); //32 is the amount of characters in controlString
			list.add(htmlSourceCode.substring(0, 8)); //adds the name to the list
			beginIndex = htmlSourceCode.indexOf(controlString);
		}
		return list;
	}

	/**
	 * Returns a list with the files' texts given their names.
	 * @param listFileNames The list with the names of the files.
	 * @return The list with the text of the files.
	 * @throws IOException If an I/O error occurs reading from the file or a malformed or unmappable byte sequence is read.
	 */
	private static List<String> extractTexts(List<String> listFileNames) throws IOException {
		List<String> list = new ArrayList<String>();

		for(String name : listFileNames) {
			list.add(getUrlSource("https://www.pastebin.com/raw/"+name));
		}

		return list;
	}

	/**
	 * Creates .txt files locally given their name and text.
	 * @param listFileNames The list with the files' names.
	 * @param listFileTexts The list with the files' texts.
	 * @throws IOException If an I/O error occurs reading from the file or a malformed or unmappable byte sequence is read.
	 */
	private static void createFiles(List<String> listFileNames, List<String> listFileTexts) throws IOException {
		if(listFileNames.size() != listFileTexts.size()) //Just in case
			throw new ArrayIndexOutOfBoundsException();

		int counter = 0;
		for(int i=0; i<listFileNames.size(); i++) {
			String str = listFileTexts.get(i); //String to write
			File file = new File("./files", listFileNames.get(i) +".txt");
			if(file.createNewFile())//true created, false already created
				try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
					writer.write(str);
				} catch (IOException x) {
					System.err.format("IOException: %s%n", x);
				}
			else counter++;
		}
		System.out.println("\tNumber of files that were already downloaded: " +counter);
		System.out.println("\tNumber of files that were created: " +(listFileNames.size()-counter));
	}
}
