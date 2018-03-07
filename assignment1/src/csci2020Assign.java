package assignment1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpamFilter {
	
	private static final String FILENAME = "C:\\Jude\\assignment1_data\\data";
	//private static String hamF = "ham", hamF2 = "ham2", spamF = "spam";
	public static Map<String, Float> mapWC = new HashMap<String, Float> ();
	public static Map<String, Float> testHamFreq = new HashMap<String, Float> ();
	public static Map<String, Float> testSpamFreq = new HashMap<String, Float> ();
	public static List<TestFile> file = new ArrayList<TestFile>();
	public static float spamValue = 0.0f;

	public static void testDir() {
		//directoryReader("\\test\\ham");
		directoryReader("\\test\\ham");
	}
	
	public static void trainDir() {
		directoryReader("\\train\\ham");
		directoryReader("\\train\\ham2");
		testHamFreq = mapWC;
		
		mapWC = new HashMap<String, Float> ();
		directoryReader("\\train\\spam");
		testSpamFreq = mapWC;
		//Map<String, String> mapWC = new HashMap<String, String> ();
		
		storeSpamProb(testHamFreq);
		storeSpamProb(testSpamFreq);
		

		System.out.println(mapWC);
		System.out.println(mapWC.get("again"));
		
	}
	public static void storeSpamProb (Map<String,Float> map) {
		//Map<String, String> mapWC = new HashMap<String, String> ();
		mapWC = new HashMap<String, Float> ();
		
		for (String key: map.keySet()) {
			if (!mapWC.containsKey(key)) {
				if (testHamFreq.get(key) != null && testSpamFreq.get(key) != null) {
					
					float num = (testSpamFreq.get(key)/500.0f), den1 = (testSpamFreq.get(key)/500.0f),
					den2 = (testHamFreq.get(key)/2750.0f);
					
					mapWC.put(key, num/(den1+den2));
				} else {
					mapWC.put(key, 0.0f);
				}
			}
		}
		
	}
	public static void directoryReader(String keyW) {
		File folder = new File(FILENAME + keyW);
		File[] listOfFiles = folder.listFiles();
	
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		    	bufferReader(file.getName(), keyW);
		    }
		}
	}
	
	
	public static void bufferReader(String fileName, String keyW) {

    	try (BufferedReader br = new BufferedReader(new FileReader(FILENAME 
    			 + keyW +  "\\" + fileName))) {
    		
			String sCurrentLine;
			Boolean beginCount = false;
			ArrayList<String> wordDup = new ArrayList<String>(); //word occurence in one file
			float calculateN = 0.0f;
			String fileType = "";
			
			while ((sCurrentLine = br.readLine()) != null) {
				String[] words = sCurrentLine.split("\\W+");
				
				if (beginCount) {
					Boolean isJunk = false;
					for (String word: words) {
					   word = word.toLowerCase();
					   if ((word.length() == 1) || word.matches("-?\\d+")) {isJunk = true;}
						 if (!keyW.equals("\\test\\ham") && !keyW.equals("\\test\\spam")) {
						   if (!(isJunk) && !mapWC.containsKey(word)) {
							   mapWC.put(word, 1.0f);
							   wordDup.add(word);
						   } else if (!(isJunk) && !wordDup.contains(word)) {
						        float count = mapWC.get(word);
						        mapWC.put(word, count + 1);
						        wordDup.add(word);
					       }
						} else {
							if (!wordDup.contains(word) && mapWC.get(word) != null) {
								float tempVal = (float) (Math.log(1- mapWC.get(word)) - Math.log(mapWC.get(word)));
								calculateN += tempVal;
								wordDup.add(word);
							}
						}
					}
					
				}
				if(Arrays.asList(words).contains("Date")) {
					beginCount = true;
				}
			}
			if (keyW.equals("\\test\\ham")) {
				fileType = "Ham";
			} else if (keyW.equals("\\test\\spam")) {
				fileType = "Spam";
			}
			if (fileType.equals("Ham") || fileType.equals("Spam")) {
				spamValue = (float) (1/(1+ Math.pow(Math.E, calculateN)));
				TestFile newfile = new TestFile(fileName, (double)spamValue, fileType);
				file.add(newfile);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	//use the files.getname.equals if necessary! == wont work! files.getname does return string so
	//make it a new file everytime u run buffereader! good luck :3
	
    public static void main(String args[]) {
    	trainDir();
    	testDir();
    	for (TestFile nfile: file) {
    		System.out.println(nfile.getFilename());
    	}
    	//
	}

