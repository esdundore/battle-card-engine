package card.model.game;

import java.util.Date;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import card.model.cards.SkillCard;
import card.model.requests.AttackRequest;
import card.model.requests.DefendRequest;

@XmlRootElement
public class GameState {

	String sessionId;
	int turnCount = 0;
	Map<String, PlayerArea> players;
	Date currentTime = new Date();
	SkillCard environmentCard;
	String phase;
	String currentPlayer;
	AttackRequest attackRequest;
	DefendRequest defendRequest;
	
	public String getSessionId() {
		return sessionId;
	}
	@XmlElement
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public int getTurnCount() {
		return turnCount;
	}
	@XmlElement
	public void setTurnCount(int turnCount) {
		this.turnCount = turnCount;
	}
	public Map<String, PlayerArea> getPlayers() {
		return players;
	}
	@XmlElement
	public void setPlayers(Map<String, PlayerArea> players) {
		this.players = players;
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
	public AttackRequest getAttackRequest() {
		return attackRequest;
	}
	@XmlElement
	public void setAttackRequest(AttackRequest attackRequest) {
		this.attackRequest = attackRequest;
	}
	public DefendRequest getDefendRequest() {
		return defendRequest;
	}
	@XmlElement
	public void setDefendRequest(DefendRequest defendRequest) {
		this.defendRequest = defendRequest;
	}
	
}
