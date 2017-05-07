package card.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import card.dao.CardCache;
import card.dao.DeckCache;
import card.model.Deck;
import card.model.GameState;
import card.model.Monster;
import card.model.MonsterCard;
import card.model.PlayerArea;
import card.util.CardUtil;

@Component("gameManager")
public class GameManager {

	@Autowired
	CardCache cardCache;
	
	@Autowired
	DeckCache deckCache;
	
	public Map<String, GameState> gameStates = new HashMap<String, GameState>();
	
	public GameState startup(String player1, String player2) {
		// create a new game state
		GameState gameState = new GameState();
		// set state session
		gameState.setSessionId(player1 + player2);
		// initialize player areas
		gameState.setPlayer1(new PlayerArea());
		gameState.setPlayer2(new PlayerArea());
		// fetch deck recipes and create a copy
		gameState.getPlayer1().setDeck(new Deck(deckCache.getDeck(player1)));
		gameState.getPlayer2().setDeck(new Deck(deckCache.getDeck(player2)));
		// player 1 - play monsters
		ArrayList<Monster> monsters = new ArrayList<Monster>();
		for (String monsterName : gameState.getPlayer1().getDeck().getMonsterCards()) {
			MonsterCard monsterCard = (MonsterCard) cardCache.getCard(monsterName);
			Monster monster = new Monster(monsterCard);
			monster.setCurrentLife(monster.getMaxLife());
			monsters.add(monster);
		}
		gameState.getPlayer1().setMonsters(monsters);
		// player2 - play monsters
		monsters = new ArrayList<Monster>();
		for (String monsterName : gameState.getPlayer2().getDeck().getMonsterCards()) {
			MonsterCard monsterCard = (MonsterCard) cardCache.getCard(monsterName);
			Monster monster = new Monster(monsterCard);
			monster.setCurrentLife(monster.getMaxLife());
			monsters.add(monster);
		}
		gameState.getPlayer2().setMonsters(monsters);
		// shuffle decks
		CardUtil.shuffle(gameState.getPlayer1().getDeck().getSkillCards());
		CardUtil.shuffle(gameState.getPlayer2().getDeck().getSkillCards());
		// draw hand
		CardUtil.drawUntilFull(gameState.getPlayer1().getDeck().getSkillCards(), gameState.getPlayer1().getHand());
		CardUtil.drawUntilFull(gameState.getPlayer2().getDeck().getSkillCards(), gameState.getPlayer2().getHand());
		// determine order
		gameState.setCurrentPlayer(CardUtil.coinFlip(player1, player2));
		gameState.setPhase("guts");
		// cache game state
		gameStates.put(player1 + player2, gameState);
		// respond with game state
		return gameState;
	}
}
