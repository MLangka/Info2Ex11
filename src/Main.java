import java.util.Random;

public class Main {

	static ScrabbleDictionary dict;
	
	// array with the letters in the english version of scrabble
	public static final char[] BAG_OF_SCABBLE_TILES = {
		'e', 'e', 'e', 'e', 'e', 'e', 'e', 'e', 'e', 'e', 'e', 'e', // 12
		'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 				// 9
		'i', 'i', 'i', 'i', 'i', 'i', 'i', 'i', 'i', 				
		'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 					// 8
		'n', 'n', 'n', 'n', 'n', 'n', 								// 6
		'r', 'r', 'r', 'r', 'r', 'r', 								
		't', 't', 't', 't', 't', 't',
		'l', 'l', 'l', 'l', 										// 4
		's', 's', 's', 's', 
		'u', 'u', 'u', 'u', 
		'd', 'd', 'd', 'd',
		'g', 'g', 'g', 												// 3
		'b', 'b', 'c', 'c', 'm', 'm', 'p', 'p', 'f', 'f', 			// 2
		'h', 'h', 'v', 'v', 'w', 'w', 'y', 'y',
		'k', 'j', 'x', 'q', 'z',									// 1
	};
	
	
	
	public static void main(String[] args) {
	
		// use small list for debug
		boolean useSmallList = false;
		
		// instantiate dictionary
		if(useSmallList) {
			dict = new ScrabbleDictionary("scrabble_small.txt", 151);
		} else {
			dict = new ScrabbleDictionary("scrabble_full.txt", 150001);
		}

		// output the complete list (only stats for for file)
		dict.outputHashtable(!useSmallList);
	
		// Test some known combinations
		takeTurn("alcytoil");
		takeTurn("lebrub");
		
		// Try 10 random draws
		for(int i=0; i<10; i++) {
			takeTurn(7);
		}
	}
	
	/***********
	 * METHODS *
	 **********/
	
	/*
	 * Takes an integer parameter and
	 * Returns that many random tiles as string
	 * Scrabble rules are ignored (could be 10 times Q)
	 */
	private static String drawTiles(int numberOfTiles) {
		char[] tiles = new char[numberOfTiles];
		Random rnd = new Random();
		
		// all possible tiles for english scrabble
		char[] bag = BAG_OF_SCABBLE_TILES;

		// fisher-yates shuffle on bag
		for(int i=0; i<bag.length-1; i++) {
			int j = rnd.nextInt(bag.length-1);
			if(i!=j) {
				char swap = bag[i];
				bag[i] = bag[j];
				bag[j] = swap;
			}			
		}
		
		// take the first n tiles
		for(int i=0; i<numberOfTiles; i++) {
			tiles[i] = bag[i];			
		}
		
		return String.valueOf(tiles);
	}
	
	/*
	 * Draws tiles, outputs the draw and then
	 * calls the other takeTurn() method with
	 * the drawn tiiles as parameter.
	 */
	private static void takeTurn(int numberOfTiles) {
		// generate tiles
		String tiles = drawTiles(numberOfTiles);
		
		// output tiles
		System.out.println();
		System.out.print("The " + tiles.length() + " tiles you drew are: ");
		for(int i=0; i<tiles.length(); i++) {
			System.out.print("'" + tiles.charAt(i) + "' ");
		}
		System.out.println();
		
		// then go on with other takeTurn method.
		takeTurn(tiles);
	}
	
	/*
	 * Takes a list of tiles as string parameter,
	 * looks up all possible words for that combination
	 * and outputs them to the console.
	 */
	private static void takeTurn(String tiles) {
		// Look up possible combinations
		String[] possibleWords = dict.getPossibleWords(tiles);
		
		// output results
		System.out.println();
		if(possibleWords.length > 0) {
			System.out.println("The possible words for '" + tiles + "' are:");
			for(int i=0; i<possibleWords.length; i++) {
				System.out.println(possibleWords[i]);
			}
		} else {
			System.out.println("No possible words for '" + tiles + "'.");
		}
		System.out.println();
	}
}
