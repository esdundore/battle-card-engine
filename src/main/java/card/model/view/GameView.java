package card.model.view;

import java.util.Date;

import card.model.game.GameState;

public class GameView {

	public Date currentTime;
	public String currentPlayer;
	public String winner;
	public String phase;
	public String environmentCard;
	public PlayerAreaView playerArea;
	public OpponentAreaView opponentArea;
	public SkillAreaView skillArea;

	public GameView() { }
	public GameView(GameState gameState, String player, String opponent) {
		// Basic info mapping
		currentPlayer = gameState.getCurrentPlayer();
		winner = gameState.getWinner();
		phase = gameState.getPhase().name();
		currentTime = gameState.getCurrentTime();
		environmentCard = gameState.getEnvironmentCard() == null ? null : gameState.getEnvironmentCard().getId();
		
		// Player info mapping
		this.playerArea = new PlayerAreaView(gameState.getPlayerArea().get(player));
		
		// Opponent info mapping
		this.opponentArea = new OpponentAreaView(gameState.getPlayerArea().get(opponent));
		
		// Attack Model mapping
		if(gameState.getSkillArea() != null) {
			skillArea = new SkillAreaView(gameState.getSkillArea());
		}
	}
	
	public String getCurrentPlayer() {
		return currentPlayer;
	}
	public void setCurrentPlayer(String currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	public String getWinner() {
		return winner;
	}
	public void setWinner(String winner) {
		this.winner = winner;
	}
	public String getPhase() {
		return phase;
	}
	public void setPhase(String phase) {
		this.phase = phase;
	}
	public String getEnvironmentCard() {
		return environmentCard;
	}
	public void setEnvironmentCard(String environmentCard) {
		this.environmentCard = environmentCard;
	}
	public PlayerAreaView getPlayerArea() {
		return playerArea;
	}
	public void setPlayerArea(PlayerAreaView playerArea) {
		this.playerArea = playerArea;
	}
	public OpponentAreaView getOpponentArea() {
		return opponentArea;
	}
	public void setOpponentArea(OpponentAreaView opponentArea) {
		this.opponentArea = opponentArea;
	}
	public SkillAreaView getSkillArea() {
		return skillArea;
	}
	public void setSkillArea(SkillAreaView skillArea) {
		this.skillArea = skillArea;
	}
	
}