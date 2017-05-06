package card.manager;

import java.util.Map;

import org.springframework.stereotype.Component;

import card.model.GameState;

@Component("gameManager")
public class GameManager {

	public Map<String, GameState> gameStates;
	
	public GameState startup(String player1, String player2) {
		GameState gameState = new GameState();
		gameStates.put(player1 + player2, gameState);
		return gameState;
	}
}
