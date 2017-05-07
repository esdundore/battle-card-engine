package card.dao;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.springframework.stereotype.Component;

import card.model.Deck;

/**
* Class run on engine startup to load all decks
 */
@Component("deckCache")
public class DeckCache {
	
	private Map<String,Deck> decks = new HashMap<String,Deck>();

	DeckCache() throws Exception {
		JAXBContext deckContext = JAXBContext.newInstance(Deck.class);
		
		File directory = new File("C:/battle-card/decks");
		File[] files = directory.listFiles();
		for (File file : files) {
			Unmarshaller jaxbUnmarshaller = deckContext.createUnmarshaller();
			Deck deck= (Deck) jaxbUnmarshaller.unmarshal(file);
			decks.put(deck.getOwnerId(), deck);
		}
	}
	
	public Deck getDeck(String id) {
		return decks.get(id);
	}
	
	public Map<String,Deck> getDecks() {
		return decks;
	}
	
}
