package card.dao;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import card.model.cards.Deck;

/**
* Class run on engine startup to load all decks
 */
@Component("deckCache")
public class DeckCache {
	
	private static final String PLAYER1_DECK_PATH = "card/decks/player1.json";
	private static final String PLAYER2_DECK_PATH = "card/decks/player2.json";
	
	private Map<String,Deck> decks = new HashMap<String,Deck>();
	private static final ObjectMapper objMapper = new ObjectMapper();

	DeckCache() throws Exception {
		objMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
		objMapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
		Deck player1Deck = objMapper.readValue(convertFileToString(PLAYER1_DECK_PATH), Deck.class);
		Deck player2Deck = objMapper.readValue(convertFileToString(PLAYER2_DECK_PATH), Deck.class);
		decks.put(player1Deck.getOwnerId(), player1Deck);
		decks.put(player2Deck.getOwnerId(), player2Deck);
	}
	
	private String convertFileToString(String path) {
		ClassPathResource resource = new ClassPathResource(path);
		StringBuffer sb = new StringBuffer();
		try {
			InputStream is = resource.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public Deck getDeck(String id) {
		return decks.get(id);
	}
	
	public Map<String,Deck> getDecks() {
		return decks;
	}
	
}
