package card.dao;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import card.model.game.Deck;

/**
* Class run on engine startup to load all decks
 */
@Component("deckCache")
public class DeckCache {
	
	private Map<String,Deck> decks = new HashMap<String,Deck>();

	DeckCache() throws Exception {
		JAXBContext deckContext = JAXBContext.newInstance(Deck.class);
		Unmarshaller jaxbUnmarshaller = deckContext.createUnmarshaller();
		
		ClassPathResource player1PathResource = new ClassPathResource("card/decks/player1.xml");
        InputStream player1Stream = player1PathResource.getInputStream();
		Deck player1Deck = (Deck) jaxbUnmarshaller.unmarshal(new InputStreamReader(player1Stream));
		
		ClassPathResource player2PathResource = new ClassPathResource("card/decks/player2.xml");
        InputStream player2Stream = player2PathResource.getInputStream();
		Deck player2Deck = (Deck) jaxbUnmarshaller.unmarshal(new InputStreamReader(player2Stream));
		
		decks.put(player1Deck.getOwnerId(), player1Deck);
		decks.put(player2Deck.getOwnerId(), player2Deck);
	}
	
	public Deck getDeck(String id) {
		return decks.get(id);
	}
	
	public Map<String,Deck> getDecks() {
		return decks;
	}
	
}
