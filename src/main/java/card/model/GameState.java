package card.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GameState {

	String sessionId;
	PlayerArea player1;
	PlayerArea player2;
	Date currentTime = new Date();
	SkillCard environmentCard;
	String phase;
	String currentPlayer;
	
	public String getSessionId() {
		return sessionId;
	}
	@XmlElement
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public PlayerArea getPlayer1() {
		return player1;
	}
	@XmlElement
	public void setPlayer1(PlayerArea player1) {
		this.player1 = player1;
	}
	public PlayerArea getPlayer2() {
		return player2;
	}
	@XmlElement
	public void setPlayer2(PlayerArea player2) {
		this.player2 = player2;
	}
	public Date getCurrentTime() {
		return currentTime;
	}
	@XmlElement
	public void setCurrentTime(Date currentTime) {
		this.currentTime = currentTime;
	}
	public SkillCard getEnvironmentCard() {
		return environmentCard;
	}
	@XmlElement
	public void setEnvironmentCard(SkillCard environmentCard) {
		this.environmentCard = environmentCard;
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
	
}
