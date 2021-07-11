package card.dao;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import card.model.cards.Deck;
import card.model.cards.MonsterCard;
import card.model.cards.SkillCard;

@Component
public class DeckCache {
	
	private static final String PLAYER1_DECK_PATH = "card/decks/Player.txt";
	private static final String PLAYER2_DECK_PATH = "card/decks/player2.txt";
	private static final String AI_DECK_PATH = "card/decks/AI.txt";
	
	private Map<String,Deck> decks = new HashMap<String,Deck>();

	/** Loads deck recipes into cache at runtime **/
	DeckCache() throws Exception {
		loadDeck(PLAYER1_DECK_PATH);
		loadDeck(PLAYER2_DECK_PATH);
		loadDeck(AI_DECK_PATH);
	}
	
	public void loadDeck(String deckPath) throws Exception {
		BufferedReader bufferedDeckReader = null;
		try {
			// Load monster cards into cache
			String currentDeckLine;
			ClassPathResource deckPathResource = new ClassPathResource(deckPath);
	        InputStream deckStream = deckPathResource.getInputStream();
	        bufferedDeckReader = new BufferedReader(new InputStreamReader(deckStream));
			Deck deck = new Deck();
			deck.setMonsterCards(new ArrayList<MonsterCard>());
			deck.setSkillCards(new LinkedList<SkillCard>());
	        Integer lineNumber = -1;
			while ((currentDeckLine = bufferedDeckReader.readLine()) != null) {
				lineNumber++;
				if (lineNumber == 0) {
					deck.setOwnerID(currentDeckLine);
				}
				else if (lineNumber == 1) {
					deck.setDeckName(currentDeckLine);
				}
				else if (lineNumber == 2) {
					deck.setDeckAvatar(currentDeckLine);
				}
				else if (lineNumber > 2 && lineNumber < 6) {
					MonsterCard monsterCard = new MonsterCard();
					monsterCard.setName(currentDeckLine);
					deck.getMonsterCards().add(monsterCard);
				}
				else {
					SkillCard skillCard = new SkillCard();
					skillCard.setName(currentDeckLine);
					deck.getSkillCards().add(skillCard);
				}
			}
			decks.put(deck.getOwnerID(), deck);
		} finally {
			if (bufferedDeckReader != null) bufferedDeckReader.close();
		}
	}

	public Deck getDeck(String ID) {
		return decks.get(ID);
	}
	
	public Map<String,Deck> getDecks() {
		return decks;
	}
	
}
