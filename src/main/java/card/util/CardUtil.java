package card.util;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import card.model.cards.SkillCard;
import card.model.game.ActiveSkill;
import card.model.game.Monster;

public class CardUtil {
	
	private static Random rng = new Random();
	private static final int MAX_HAND_SIZE = 5;
	
	/** Shuffling algorithm source: http://stackoverflow.com/questions/273313/randomize-a-listt/1262619#1262619 **/
	public static void shuffle(List<SkillCard> deck) {
		int deckSize = deck.size();  
		while (deckSize > 1) {  
			deckSize--;  
			int random = rng.nextInt(deckSize + 1);  
			SkillCard randomCard = deck.get(random);  
			deck.set(random, deck.get(deckSize));  
			deck.set(deckSize, randomCard);  
		} 
	}
	
	/** Coin flip simulator to determine starting player **/
	public static String coinFlip(String player1, String player2) {
		int coinFace = rng.nextInt(2);
		if (coinFace == 1) {
			return player1;
		}
		else {
			return player2;
		}
	}
	
	/** If (hand) is full, discard one card from (deck) to (discard). Otherwise, draw cards from (deck) until (hand) is full. **/
	public static boolean drawUntilFull(LinkedList<SkillCard> deck, LinkedList<SkillCard> discards, List<SkillCard> hand) throws NoSuchElementException {
		if (sizeMinusNulls(hand) >= MAX_HAND_SIZE) {
			discardFromDeck(deck, discards);
			return false;
		}
		else {
			draw(deck, hand, MAX_HAND_SIZE - sizeMinusNulls(hand));
			return true;
		}
	}
	
	/** Draw (drawCount) cards from the top of (deck) to (hand). **/
	public static void draw(LinkedList<SkillCard> deck, List<SkillCard> hand, int drawCount) throws NoSuchElementException {
		for (int i = 0; i < drawCount; i++) {
			draw(deck, hand);
		}
	}
	
	/** Draw one card from the top of (deck) to (hand). Throws NoSuchElementException if there are no cards in the deck. **/
	public static void draw(LinkedList<SkillCard> deck, List<SkillCard> hand) throws NoSuchElementException {
		SkillCard cardDrawn = deck.pop();
		for (int i = 0; i < hand.size(); i++) {
			if (hand.get(i) == null) {
				hand.set(i, cardDrawn);
				return;
			}
		}
		hand.add(cardDrawn);
	}
	
	/** Discard a card from the top of (deck) to (discard). Throws NoSuchElementException if there are no cards in (deck). **/
	public static void discardFromDeck(LinkedList<SkillCard> deck, LinkedList<SkillCard> discard) throws NoSuchElementException {
		discard.addFirst(deck.pop());
	}
	
	/** Discard a card from (hand) at (index) to (discard). **/
	public static void discardHand(List<SkillCard> hand, LinkedList<SkillCard> discard) {
		for (int i = 0; i < hand.size(); i++) {
			if (hand.get(i) != null) {
				discardFromHand(hand, i, discard);
			}
		}
	}
	
	/** Discard a card from (hand) at (index) to (discard). **/
	public static void discardFromHand(List<SkillCard> hand, Integer index, LinkedList<SkillCard> discard) {
		discard.addFirst(hand.get(index));
		hand.set(index, null);
	}
	
	/** Discard a card from (hand) at (index) to (discard). **/
	public static void putOnBottom(List<SkillCard> hand, Integer index, LinkedList<SkillCard> discard) {
		discard.addLast(hand.get(index));
		hand.set(index, null);
	}
	
	/** Play a card from (hand) at (index) to (skills) in a SkillArea. **/
	public static void playCard(List<SkillCard> hand, Monster user, Integer target, Integer index, List<ActiveSkill> skills) {
		ActiveSkill skill = new ActiveSkill(hand.get(index), user, target, index);
		skills.add(skill);
		hand.set(index, null);
	}
	
	/** Add (card) to the top of (cards). **/
	public static void putOnTop(LinkedList<SkillCard> cards, SkillCard card) {
		cards.addFirst(card);
	}
	
	public static int sizeMinusNulls(List<SkillCard> hand) {
		int sizeMinusNulls = 0;
		for (SkillCard card : hand) {
			if (card != null) {
				sizeMinusNulls++;
			}
		}
		return sizeMinusNulls;
	}
	
}
