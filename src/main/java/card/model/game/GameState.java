package card.model.game;

import java.util.HashMap;
import java.util.Map;

import card.enums.GamePhase;
import card.model.cards.SkillCard;

public class GameState {
	
	public String sessionId;
	public Integer turnCount = 0;
	public String winner;
	public String currentPlayer;
	public GamePhase phase;
	public SkillCard environmentCard = new SkillCard();
	public Map<String, PlayerArea> playerArea;
	public SkillArea skillArea = new SkillArea();
	
	public GameState copy() {
		GameState copy = new GameState();
		copy.setSessionId(sessionId);
		copy.setTurnCount(turnCount);;
		copy.setWinner(winner);
		copy.setCurrentPlayer(currentPlayer);
		copy.setPhase(phase);
		copy.setEnvironmentCard(environmentCard);
		copy.setPlayerArea(new HashMap<>());
		for (Map.Entry<String,PlayerArea> playArea : playerArea.entrySet()) {
			copy.getPlayerArea().put(playArea.getKey(), playArea.getValue().copy());
		}
		copy.setSkillArea(skillArea.copy());
		return copy;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public Integer getTurnCount() {
		return turnCount;
	}
	public void setTurnCount(Integer turnCount) {
		this.turnCount = turnCount;
	}
	public String getWinner() {
		return winner;
	}
	public void setWinner(String winner) {
		this.winner = winner;
	}
	public String getCurrentPlayer() {
		return currentPlayer;
	}
	public void setCurrentPlayer(String currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	public GamePhase getPhase() {
		return phase;
	}
	public void setPhase(GamePhase phase) {
		this.phase = phase;
	}
	public SkillCard getEnvironmentCard() {
		return environmentCard;
	}
	public void setEnvironmentCard(SkillCard environmentCard) {
		this.environmentCard = environmentCard;
	}
	public Map<String, PlayerArea> getPlayerArea() {
		return playerArea;
	}
	public PlayerArea getPlayerArea(String player) {
		return playerArea.get(player);
	}
	public void setPlayerArea(Map<String, PlayerArea> playerArea) {
		this.playerArea = playerArea;
	}
	public SkillArea getSkillArea() {
		return skillArea;
	}
	public void setSkillArea(SkillArea skillArea) {
		this.skillArea = skillArea;
	}

}
