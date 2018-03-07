//required libraries
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

//class for spam filter
public class SpamFilter {
	
	//C:\\Jude\\assignment1_data\\data - Use it this way if directory gives trouble
	public static String FILENAME = "";
	
	//maps words to count and probability
	public static Map<String, Float> mapWC = new HashMap<String, Float> ();
	public static Map<String, Float> testHamFreq = new HashMap<String, Float> ();
	public static Map<String, Float> testSpamFreq = new HashMap<String, Float> ();
	public static List<TestFile> file = new ArrayList<TestFile>();
	public static float spamValue = 0.0f;
	
	//tests program
	public static void testDir() {
		directoryReader("\\test\\ham");
		directoryReader("\\test\\spam");
	}
	
	//trains program or initiator
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
		
	}
	//stores spam probability
	public static void storeSpamProb (Map<String,Float> map) {
		mapWC = new HashMap<String, Float> ();
		
		
		for (String key: map.keySet()) {
			if (!mapWC.containsKey(key)) {
				//cannot be null pointers
				if (testHamFreq.get(key) != null && testSpamFreq.get(key) != null) {
					
					float num = (testSpamFreq.get(key)/500.0f), den1 = (testSpamFreq.get(key)/500.0f),
					den2 = (testHamFreq.get(key)/2750.0f);
					//calculates the word's probability of being spam
					mapWC.put(key, num/(den1+den2));
				} else {
					mapWC.put(key, 0.0f);
				}
			}
		}
		
	}
	//func to read directories
	public static void directoryReader(String keyW) {
		File folder = new File(FILENAME + keyW);
		File[] listOfFiles = folder.listFiles();
	
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		    	bufferReader(file.getName(), keyW);
		    }
		}
	}
	
	//important function which does the most work
	//reads file and uses information wisely
	public static void bufferReader(String fileName, String keyW) {

    	try (BufferedReader br = new BufferedReader(new FileReader(FILENAME 
    			 + keyW +  "\\" + fileName))) {
    		
			String sCurrentLine;
			Boolean beginCount = false;
			ArrayList<String> wordDup = new ArrayList<String>(); //word occurence in one file
			float calculateN = 0.0f;  //n value in testing process
			String fileType = "";
			
			while ((sCurrentLine = br.readLine()) != null) {
				//splits strings into array using delimeter
				String[] words = sCurrentLine.split("\\W+");
				
				//counts from after the Date: because most messages come after
				if (beginCount) {
					Boolean isJunk = false;
					for (String word: words) {
					   word = word.toLowerCase(); //converts to lower case
					   if ((word.length() == 1) || word.matches("-?\\d+")) {isJunk = true;}
					   //don't need to count on for test files
						 if (!keyW.equals("\\test\\ham") && !keyW.equals("\\test\\spam")) {
						   if (!(isJunk) && !mapWC.containsKey(word)) {
							   //maps information
							   mapWC.put(word, 1.0f);
							   wordDup.add(word);
						   } else if (!(isJunk) && !wordDup.contains(word)) {
							   //count word only when no duplicate
						        float count = mapWC.get(word);
						        mapWC.put(word, count + 1);
						        wordDup.add(word);
					       }
						} else {
							//calculates n summation
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
			//determines file type
			if (keyW.equals("\\test\\ham")) {
				fileType = "Ham";
			} else if (keyW.equals("\\test\\spam")) {
				fileType = "Spam";
			}
			//adds files and their probability to an array list
			if (fileType.equals("Ham") || fileType.equals("Spam")) {
				spamValue = (float) (1/(1+ Math.pow(Math.E, calculateN)));
				TestFile newfile = new TestFile(fileName, (double)spamValue, fileType);
				file.add(newfile);
			}
		//exceptions
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	//calls all files to main class
    public static ObservableList<TestFile> getAllFiles() {
        ObservableList<TestFile> files = FXCollections.observableArrayList();
        
    	trainDir();
    	testDir();
    	int count = 0;
    	for (TestFile nfile: file) {
    		files.add(nfile);
    		count+=1;
    	}
        return files;
    }
}