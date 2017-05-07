package card.util;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Random;

public class CardUtil {
	
	private static Random rng = new Random();
	final static int MAX_HAND_SIZE = 5;
	
	/*
     * Shuffling algorithm source: http://stackoverflow.com/questions/273313/randomize-a-listt/1262619#1262619 
     */
	public static void shuffle(LinkedList<String> deck) {
		int deckSize = deck.size();  
		while (deckSize > 1) {  
			deckSize--;  
			int random = rng.nextInt(deckSize + 1);  
			String value = deck.get(random);  
			deck.set(random, deck.get(deckSize));  
			deck.set(deckSize, value);  
		} 
	}
	
	public static void drawUntilFull(LinkedList<String> deck, LinkedList<String> hand) {
		while(hand.size() < MAX_HAND_SIZE) {
			draw(deck, hand);
		}
	}
	
	public static void draw(LinkedList<String> deck, LinkedList<String> hand) {
		String cardDrawn = null;
		// try to draw a card
		try {
			cardDrawn = deck.pop();
		} catch (NoSuchElementException nsee) {
			// can't draw a card - game lost
		}
		hand.add(cardDrawn);
	}
	
	public static void discard(LinkedList<String> hand, int index) {
		hand.remove(index);
	}
	
	public static String coinFlip(String player1, String player2) {
		int coinFace = rng.nextInt(2);
		if (coinFace == 1) {
			return player1;
		}
		else {
			return player2;
		}
	}
	
}
