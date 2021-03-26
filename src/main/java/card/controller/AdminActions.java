package card.controller;

import org.springframework.web.bind.annotation.RestController;

import card.dao.CardCache;
import card.dao.DeckCache;
import card.manager.GameManager;
import card.model.cards.BattleCard;
import card.model.game.Deck;
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
	GameManager gameManager;

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
    
    //TODO: to remove
    @RequestMapping(value = "/get-game-admin",
    		method = RequestMethod.POST, 
    		consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public GameState getGame(
    		@RequestBody PlayersRequest playersRequest) {
        return gameManager.getGameAdminView(playersRequest);
    }
    
}