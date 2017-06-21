package card.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Random;

import card.model.game.PlayerArea;

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
	
	public static void drawUntilFull(PlayerArea playerArea) throws NoSuchElementException {
		ArrayList<String> hand = playerArea.getHand();
		LinkedList<String> deck = playerArea.getDeck().getSkillCards();
		if(sizeMinusNulls(hand) >= MAX_HAND_SIZE) {
			// if full already, discard top card and make 1 GUTS
			playerArea.getDiscard().add(deck.pop());
			playerArea.setGutsPool(playerArea.getGutsPool() + 1);
		}
		while(sizeMinusNulls(hand) < MAX_HAND_SIZE) {
			draw(deck, hand);
		}
	}
	
	public static void draw(LinkedList<String> deck, ArrayList<String> hand) throws NoSuchElementException {
		// try to draw a card
		String cardDrawn = deck.pop();
		//replace empty slots if there are any
		for (int i = 0; i < hand.size(); i++) {
			if (hand.get(i) == null) {
				hand.set(i, cardDrawn);
				return;
			}
		}
		hand.add(cardDrawn);
	}
	
	public static boolean discard(ArrayList<String> hand, ArrayList<String> discard, int index) {
		// validate card exists and remove if so
		if (hand.get(index) != null) {
			discard.add(hand.get(index));
			hand.set(index, null);
			return true;
		}
		else {
			return false;
		}
	}
	
	public static String coinFlip(String player1, String player2) {
		int coinFace = rng.nextInt(2);
		if (coinFace == 1) {
			return player1;
		}
		else {
			//TODO: change this to player2
			return player1;
		}
	}
	
	public static int sizeMinusNulls (ArrayList<String> hand) {
		int sizeMinusNulls = 0;
		for (String card : hand) {
			if (card != null) {
				sizeMinusNulls++;
			}
		}
		return sizeMinusNulls;
	}
}
