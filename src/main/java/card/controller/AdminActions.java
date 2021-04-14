package card.controller;

import org.springframework.web.bind.annotation.RestController;

import card.dao.CardCache;
import card.dao.DeckCache;
import card.dao.GameCache;
import card.model.cards.BattleCard;
import card.model.cards.Deck;
import card.model.game.GameState;
import card.model.requests.PlayersRequest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class AdminActions {
	
	@Autowired
	CardCache cardCache;
	
	@Autowired
	DeckCache deckCache;
	
	@Autowired
	GameCache gameCache;
    
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

    @RequestMapping(value = "/start-match",
    		method = RequestMethod.POST, 
    		consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public void startup(
    		@RequestBody PlayersRequest playersRequest) {
    	gameCache.startup(playersRequest);
    }
    
    @RequestMapping(value = "/get-game",
    		method = RequestMethod.POST, 
    		consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public GameState getGameState(
    		@RequestBody PlayersRequest playersRequest) {
        return gameCache.getGameState(playersRequest);
    }
    
}