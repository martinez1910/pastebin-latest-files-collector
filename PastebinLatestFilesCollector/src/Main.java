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

public class Main {

	public static void main(String[] args) {
		while(true) {
			String sourceCode = null;
			List<String> listFileNames, listFileTexts=null;

			long t1,t2, tTotal=0;

			t1 = System.currentTimeMillis();
			//Get source code
			try {
				sourceCode = getUrlSource("https://pastebin.com/archive");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			try {
				listFileTexts = extractTexts(listFileNames);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			t2 = System.currentTimeMillis();
			System.out.println("Getting the list with texts: " +(t2-t1) +"ms");
			tTotal += t2-t1;

			t1 = System.currentTimeMillis();
			//Create .txt files locally
			try {
				createFiles(listFileNames, listFileTexts);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			t2 = System.currentTimeMillis();
			System.out.println("Getting the files locally: " +(t2-t1) +"ms");
			tTotal += t2-t1;
			System.out.println("-------------------------------------");
			System.out.println("Total time spent: " +(tTotal) +"ms");	
			System.out.println("-------------------------------------");
			System.out.println();
		}
		
		
	}
	
	 

	



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
	 
	 private static List<String> extractTexts(List<String> listFileNames) throws IOException {

		 List<String> list = new ArrayList<String>();
		 
		 for(String name : listFileNames) {
			 list.add(getUrlSource("https://www.pastebin.com/raw/"+name));
		 }
		 
		 return list;
	 }
	 
	 private static void createFiles(List<String> listFileNames, List<String> listFileTexts) throws IOException {
		 if(listFileNames.size() != listFileTexts.size()) //Just in case
			 throw new ArrayIndexOutOfBoundsException();
		 
		 //Charset charset = Charset.forName("US-ASCII");
		 for(int i=0; i<listFileNames.size(); i++) {
			String str = listFileTexts.get(i); //String to write
			File file = new File("./files", listFileNames.get(i) +".txt");
			if(file.createNewFile())//true created, false already created
				try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))){//, charset)) {
					writer.write(str);
				} catch (IOException x) {
					System.err.format("IOException: %s%n", x);
				}
		 }
	 }
}
