import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;


public class ScrabbleDictionary {

	private ArrayList<ArrayList<String>> dictionary;
	private int capacity;
	
	private int valuesStored;
	private int collisions;		// really hard to count - not implemented yet
	
	/***************
	 * CONSTRUCTOR *
	 **************/
	
	public ScrabbleDictionary(String filename, int capacity) {
		this.capacity = capacity;
		dictionary = new ArrayList<ArrayList<String>>(this.capacity);
		initDictionary(dictionary);
		this.valuesStored = 0;
		readFile(filename);
	}
	
	/******************
	 * PUBLIC METHODS *
	 *****************/
	
	/*
	 * Looks up a normalized string, the letter tiles, 
	 * in the table and returns all matching words.
	 */
	public String[] getPossibleWords(String tiles) {
		//normalize the tiles string
		tiles = this.normalize(tiles);
		
		// get all entries at hash index
		int index = this.getHash(tiles);
		ArrayList<String> wordsFound = this.dictionary.get(index);
		
		// compare the normalized version of the found words with tiles
		// We need an iterator to safely remove the strings that don't match
		Iterator<String> it = wordsFound.iterator();
		while(it.hasNext()) {
			// if the word doesn't match the tiles remove it
			if(!this.isPermutation(tiles, it.next())) {
				it.remove();
			}
		}
		
		// return the remaining words
		String[] wordsArray = new String[wordsFound.size()];
		return wordsFound.toArray(wordsArray);
	}
	
	/*
	 * Outputs the complete Hashtable and some stats 
	 * to the console.
	 */
	public void outputHashtable(boolean statsOnly) {
		int emptySlots = 0;
		int usedSlots = 0;
		int collisions = 0;
		
		for(int i=0; i<this.capacity; i++) {
			// output index
			if(!statsOnly)
				System.out.print(i + ":");
			
			// Get the word bucket at index
			ArrayList<String> values = dictionary.get(i);
			int valuesSize = values.size();
			
			// check if bucket is empty, count collisions if not
			if(valuesSize == 0) {
				emptySlots++;
			} else {
				usedSlots++;
				
				// count collisions via HashSet
				HashSet<String> collisionTest = new HashSet<String>();
				for(String s : values) {
					collisionTest.add(this.normalize(s));
				}
				collisions += collisionTest.size() - 1;
			}
			
			// output values in word bucket
			if(!statsOnly) {
				for(int j=0; j<values.size(); j++) {
					System.out.print("\t'" + values.get(j) + "'");
				}
				// new line
				System.out.println();
			}
		}
		
		double usedPercent = (usedSlots / (double)this.capacity) * 100;
		double collisionPercent = (collisions / (double)this.valuesStored) * 100;
		System.out.println("\nValues stored: " + this.valuesStored);
		System.out.println("Slots used: " + usedSlots);
		System.out.println("Slots empty: " + emptySlots);
		System.out.println("Number of collisions: " + collisions);
		
		System.out.println(usedPercent + "% of capacity is being used.");
		System.out.println(collisionPercent + "% collision rate.\n");
	}
	
	/*******************
	 * PRIVATE METHODS *
	 ******************/

	/*
	 * Takes a file name as parameter, and generates a Hashtable
	 * from the words in it.
	 */
	private void readFile(String filename) {
		File myFile = new File(filename);
		
		try {
			RandomAccessFile raf = new RandomAccessFile(myFile, "r");
			
			String nextWord;
			while((nextWord = raf.readLine()) != null) {
				// Unnecessary for this hash to normalize, but we still do it 
				// in case hash gets changed in the future
				String wordNormalized = normalize(nextWord);   
				
				// generate hash
				int hash = getHash(wordNormalized);
				
				// store word in bucket at hash key
				dictionary.get(hash).add(nextWord);
				
				// count the new value
				this.valuesStored++;		
			}
			
			raf.close();
		} catch(IOException e) {
			e.printStackTrace();
		}	
	}
	
	/*
	 * Initialize "word buckets"
	 * Called from constructor
	 */
	private void initDictionary(ArrayList<ArrayList<String>> dict) {
		for(int i=0; i<this.capacity; i++) {
			ArrayList<String> wordBucket = new ArrayList<String>();
			dict.add(i, wordBucket);
		}
	}
		
	/*
	 * Takes a normalized word as parameter and
	 * returns a hash value.
	 */
	private int getHash(String wordNormalized) {
		int hash = 31;
		int prime = 503;
		
		for(int i=0; i<wordNormalized.length(); i++) {
			hash = prime * hash + wordNormalized.charAt(i);
			hash %= this.capacity;
		}
		
		return hash;
	}

	/*
	 * Takes to strings as parameter and checks whether
	 * they are permutations of each other by normalizing both.
	 */
	private boolean isPermutation(String a, String b) {
		return this.normalize(a).equals(this.normalize(b));
	}
	
	/*
	 * Takes a word as parameter and returns the normalized String.
	 * Sorts the characters alphabetically.
	 * MAKE PRIVATE
	 */
	private String normalize(String word) {
		// convert to char array and convert capital letters to lower case
		char[] letters = word.toLowerCase().toCharArray();
		int length = word.length();
		
		// use bubble sort -- what performs best at this size < 20?? quick? heap?
		char swap;
		boolean isUnsorted = true;

		while(isUnsorted) {
			isUnsorted = false;
			
			for(int i=0; i<length-1; i++) {
				if(letters[i] > letters[i+1]) {
					swap = letters[i];
					letters[i] = letters[i+1];
					letters[i+1] = swap;
					isUnsorted = true;
				}
			}
		}
			

		return String.valueOf(letters);
	}	
}
