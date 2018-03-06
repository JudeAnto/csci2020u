package assignment1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SpamFilter {
	
	private static final String FILENAME = "C:\\Jude\\assignment1_data\\data\\train";
	//private static String hamF = "ham", hamF2 = "ham2", spamF = "spam";
	public static Map<String, Integer> mapWC = new HashMap<String, Integer> ();
	public static Map<String, Integer> testHamFreq = new HashMap<String, Integer> ();
	public static Map<String, Integer> testSpamFreq = new HashMap<String, Integer> ();

	
	
	public static void trainDir() {
		directoryReader("\\ham");
		directoryReader("\\ham2");
		testHamFreq = mapWC;
		//System.out.println(testHamFreq.get("again"));
		
		mapWC = new HashMap<String, Integer> ();
		directoryReader("\\spam");
		testSpamFreq = mapWC;
		Map<String, Float> mapWC = new HashMap<String, Float> ();
		
		for (String key: testSpamFreq.keySet()) {
			if (testHamFreq.get(key) != null) {
				mapWC.put(key, (float) ((testSpamFreq.get(key)/500)/
						((float)(testSpamFreq.get(key)/500) + 
								(float)(testHamFreq.get(key)/2750))));
			} else {
				mapWC.put(key, (float) 0.0);
			}
		}
		DecimalFormat dec = new DecimalFormat("#0.0000000");
		float num = (testSpamFreq.get("again")/500.0f);
		float den1 = (testSpamFreq.get("again")/500.0f);
		float den2 = (testHamFreq.get("again")/2750.0f);
		System.out.println(dec.format(num));

		
		System.out.println(dec.format(num/(den1+den2)));

		//System.out.println(dec.format(t));
		/*(testSpamFreq.get(key)/500)/
		((testSpamFreq.get(key)/500) + 
				(testHamFreq.get(key)/2750))*/
		
	}
	public static void directoryReader(String keyW) {
		File folder = new File(FILENAME + keyW);
		File[] listOfFiles = folder.listFiles();
	
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		    	//System.out.println(file.getName());
		        bufferReader(file.getName(), keyW);
		    }
		}
	}
	
	public static void bufferReader(String fileName, String keyW) {
    	try (BufferedReader br = new BufferedReader(new FileReader(FILENAME 
    			+  "\\" + keyW +  "\\" + fileName))) {
    		
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
    	trainDir();
    	//System.out.println(mapWC);
	}

}