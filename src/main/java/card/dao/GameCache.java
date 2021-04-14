package card.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import card.enums.GamePhase;
import card.model.cards.MonsterCard;
import card.model.game.GameState;
import card.model.game.Monster;
import card.model.game.PlayerArea;
import card.model.game.SkillArea;
import card.model.requests.PlayersRequest;
import card.model.view.GameView;
import card.util.CardUtil;

/**
* Class run on engine startup to load all decks
 */
@Component("gameCache")
public class GameCache {
	
	@Autowired
	CardCache cardCache;
	
	@Autowired
	DeckCache deckCache;
	
	public Map<String, GameState> gameStates = new HashMap<String, GameState>();
	
	public GameState startup(PlayersRequest playersRequest) {
		// get player ids
		String player1 = playersRequest.getPlayer1();
		String player2 = playersRequest.getPlayer2();
		// create new game state
		GameState gameState = new GameState();
		// set state session
		gameState.setSessionId(player1 + player2);
		// initialize player areas
		gameState.setPlayerArea(new HashMap<String, PlayerArea>());
		gameState.getPlayerArea().put(player1, new PlayerArea());
		gameState.getPlayerArea().put(player2, new PlayerArea());
		PlayerArea player1Area = gameState.getPlayerArea().get(player1);
		PlayerArea player2Area = gameState.getPlayerArea().get(player2);
		// initialize skill area
		SkillArea skillArea = new SkillArea();
		gameState.setSkillArea(skillArea);
		// fetch deck recipes and create a copy
		player1Area.setDeck(deckCache.getDeck(player1).copy());
		player2Area.setDeck(deckCache.getDeck(player2).copy());
		// play monsters
		startupMonsters(player1Area);
		startupMonsters(player2Area);
		// shuffle decks
		CardUtil.shuffle(player1Area.getDeck().getSkillCards());
		CardUtil.shuffle(player2Area.getDeck().getSkillCards());
		// draw hand
		CardUtil.drawUntilFull(player1Area);
		CardUtil.drawUntilFull(player2Area);
		// determine order and go to first guts phase
		gameState.setCurrentPlayer(CardUtil.coinFlip(player1, player2));
		gameState.setPhase(GamePhase.GUTS);
		// cache game state
		gameStates.put(player1 + player2, gameState);
		// respond with game state
		return gameState;
	}
	
	public void startupMonsters(PlayerArea playerArea) {
		ArrayList<Monster> monsters = new ArrayList<Monster>();
		for (String monsterName : playerArea.getDeck().getMonsterCards()) {
			MonsterCard monsterCard = (MonsterCard) cardCache.getCard(monsterName);
			Monster monster = new Monster(monsterCard);
			monster.setCurrentLife(monster.getMaxLife());
			monsters.add(monster);
		}
		playerArea.setMonsters(monsters);
	}
	
	public GameView getGameView(PlayersRequest playersRequest) {
		String player1 = playersRequest.getPlayer1();
		String player2 = playersRequest.getPlayer2();
		return new GameView(getGameState(playersRequest), player1, player2);
	}
	
	public GameState getGameState(PlayersRequest playersRequest) {
		String player1 = playersRequest.getPlayer1();
		String player2 = playersRequest.getPlayer2();
		GameState gameState = gameStates.get(player1 + player2);
		if (gameState == null) {
			gameState = gameStates.get(player2 + player1);
		}
		return gameState;
	}
	
}
