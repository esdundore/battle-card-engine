package card.model.view;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GameView {

	int attackId;
	int defendId;
	Date currentTime = new Date();
	String phase;
	String currentPlayer;
	String winner;
	AttackView attackView;
	DefendView defendView;
	String environmentCard;
	PlayerView player;
	OpponentView opponent;

	public int getAttackId() {
		return attackId;
	}
	public void setAttackId(int attackId) {
		this.attackId = attackId;
	}
	public int getDefendId() {
		return defendId;
	}
	public void setDefendId(int defendId) {
		this.defendId = defendId;
	}
	public Date getCurrentTime() {
		return currentTime;
	}
	@XmlElement
	public void setCurrentTime(Date currentTime) {
		this.currentTime = currentTime;
	}
	public String getPhase() {
		return phase;
	}
	@XmlElement
	public void setPhase(String phase) {
		this.phase = phase;
	}
	public String getCurrentPlayer() {
		return currentPlayer;
	}
	@XmlElement
	public void setCurrentPlayer(String currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	public String getWinner() {
		return winner;
	}
	public void setWinner(String winner) {
		this.winner = winner;
	}
	public AttackView getAttackView() {
		return attackView;
	}
	@XmlElement
	public void setAttackView(AttackView attackView) {
		this.attackView = attackView;
	}
	public DefendView getDefendView() {
		return defendView;
	}
	@XmlElement
	public void setDefendView(DefendView defendView) {
		this.defendView = defendView;
	}
	public String getEnvironmentCard() {
		return environmentCard;
	}
	@XmlElement
	public void setEnvironmentCard(String environmentCard) {
		this.environmentCard = environmentCard;
	}
	public PlayerView getPlayer() {
		return player;
	}
	@XmlElement
	public void setPlayer(PlayerView player) {
		this.player = player;
	}
	public OpponentView getOpponent() {
		return opponent;
	}
	@XmlElement
	public void setOpponent(OpponentView opponent) {
		this.opponent = opponent;
	}
	
}
