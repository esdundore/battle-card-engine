package card.controller;

import org.springframework.web.bind.annotation.RestController;

import card.dao.CardCache;
import card.dao.DeckCache;
import card.dao.GameCache;
import card.model.cards.Deck;
import card.model.cards.MonsterCard;
import card.model.cards.SkillCard;
import card.model.game.GameState;
import card.model.requests.PlayersRequest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
public class AdminActions {

	@Autowired
	CardCache cardCache;

	@Autowired
	DeckCache deckCache;

	@Autowired
	GameCache gameCache;

	@RequestMapping("/monster-cards")
	public Map<String, MonsterCard> viewMonsterCards() {
		return cardCache.getMonsterCards();
	}
	
	@RequestMapping("/skill-cards")
	public Map<String, SkillCard> viewSkillCards() {
		return cardCache.getSkillCards();
	}

	@RequestMapping("/decks")
	public Map<String, Deck> viewDecks() {
		return deckCache.getDecks();
	}

	@RequestMapping(value = "/get-game", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public GameState getGameState(@RequestBody PlayersRequest playersRequest) {
		return gameCache.getGameState(playersRequest);
	}

}