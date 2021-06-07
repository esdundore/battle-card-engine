package card.model.view;

import card.enums.MonsterStatus;
import card.model.game.GameState;
import card.model.game.PlayerArea;
import card.model.requests.PlayersRequest;

public class GameView {

	public String currentPlayer;
	public String winner;
	public String phase;
	public String environmentCard;
	public PlayerAreaView playerArea;
	public PlayerAreaView opponentArea;
	public SkillAreaView skillArea;
	public PlayableView playable;

	public GameView() { }
	public GameView(PlayersRequest playersRequest, GameState gameState, PlayableView playableView) {
		currentPlayer = gameState.getCurrentPlayer();
		winner = gameState.getWinner();
		phase = gameState.getPhase().name();
		environmentCard = gameState.getEnvironmentCard().getName();
		PlayerArea playerArea = gameState.getPlayerArea(playersRequest.getPlayer1());
		this.playerArea = new PlayerAreaView(playerArea, true, playersRequest.getPlayer1());
		Boolean showHand = false;
		if (playerArea.getBreeder().getStatusDuration().containsKey(MonsterStatus.SCOUTING)) showHand = true;
		PlayerArea opponentArea = gameState.getPlayerArea(playersRequest.getPlayer2());
		this.opponentArea = new PlayerAreaView(opponentArea, showHand, playersRequest.getPlayer2());
		skillArea = new SkillAreaView(gameState);
		this.playable = playableView;
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
	public SkillAreaView getSkillArea() {
		return skillArea;
	}
	public void setSkillArea(SkillAreaView skillArea) {
		this.skillArea = skillArea;
	}
	public PlayableView getPlayable() {
		return playable;
	}
	public void setPlayable(PlayableView playable) {
		this.playable = playable;
	}
	
}
