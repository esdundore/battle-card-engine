package card.controller;

import org.springframework.web.bind.annotation.RestController;

import card.dao.CardCache;
import card.dao.DeckCache;
import card.model.BattleCard;
import card.model.Deck;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class AdminRequests {
	
	@Autowired
	CardCache cardCache;
	
	@Autowired
	DeckCache deckCache;

    @RequestMapping("/")
    public String index() {
        return "Battle Card Engine is Online";
    }
    
    @RequestMapping("/cards")
    public Map<String, BattleCard> viewCards() {
    	return cardCache.getCards();
    }
    
    @RequestMapping("/card")
    public BattleCard viewCard(
    		@RequestParam(value="id") String id) {
    	return cardCache.getCard(id);
    }
    
    @RequestMapping("/decks")
    public Map<String, Deck> viewDecks() {
    	return deckCache.getDecks();
    }
    
    @RequestMapping("/deck")
    public Deck viewDeck(
    		@RequestParam(value="owner-id") String ownerId) {
    	return deckCache.getDeck(ownerId);
    }
    
}