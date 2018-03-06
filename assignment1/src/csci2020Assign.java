package assignment1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SpamFilter {
	
	private static final String FILENAME = "C:\\Jude\\assignment1_data\\data\\train\\spam";
	private static String hamF = "ham"; private static String hamF2 = "ham2"; private static String spamF = "spam";
	public static Map<String, Integer> mapWC = new HashMap<String, Integer> ();
	public static Map<String, Integer> mapWC = new HashMap<String, Integer> ();
	public static Map<String, Integer> mapWC = new HashMap<String, Integer> ();
	
	public static void directoryReader() {
		File folder = new File(FILENAME);
		File[] listOfFiles = folder.listFiles();
	
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		    	//System.out.println(file.getName());
		        bufferReader(file.getName());
		    }
		}
	}
	
	public static void bufferReader(String fileName) {
    	try (BufferedReader br = new BufferedReader(new FileReader(FILENAME + "\\" + fileName))) {
    		
			String sCurrentLine;
			Boolean beginCount = false;
			ArrayList<String> wordDup = new ArrayList<String>(); //word occurence in one file
			
			while ((sCurrentLine = br.readLine()) != null) {
				String[] words = sCurrentLine.split("\\W+");
				
				if (beginCount) {
					Boolean isJunk = false;
					for (String word: words) {
					   word = word.toLowerCase();
					   if ((word.length() == 1) || word.matches("-?\\d+")) {isJunk = true;}

					   if (!(isJunk) && !mapWC.containsKey(word)) {
						   mapWC.put(word, 1);
						   wordDup.add(word);
					   } else if (!(isJunk) && !wordDup.contains(word)) {
					        int count = mapWC.get(word);
					        mapWC.put(word, count + 1);
					        wordDup.add(word);
				       }
					}
				}
				if(Arrays.asList(words).contains("Date")) {
					beginCount = true;
				}
			}
			//System.out.println(mapWC);

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	//use the files.getname.equals if necessary! == wont work! files.getname does return string so
	//make it a new file everytime u run buffereader! good luck :3
	
    public static void main(String args[]) {
    	directoryReader();
    	System.out.println(mapWC);
	}

}