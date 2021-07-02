package card.model.game;

import java.util.HashMap;

import card.enums.MonsterBreed;
import card.enums.MonsterStatus;

public class Breeder {

	public String playerName;
	public Integer guts = 0;
	public Integer gutsSpent = 0; 
	public Integer gutsMade = 0;
	public String message;
	public Boolean canAttack = true;
	public HashMap<MonsterStatus, Integer> statusDuration = new HashMap<MonsterStatus, Integer>();
	
	public Breeder copy() {
		Breeder copy = new Breeder();
		copy.setGuts(guts);
		copy.setGutsSpent(gutsSpent);
		copy.setMessage(message);
		copy.setCanAttack(canAttack);
		copy.setStatusDuration(statusDuration);
		return copy;
	}
	
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public Integer getGuts() {
		return guts;
	}
	/** guts cannot be less than 0 **/
	public void setGuts(Integer guts) {
		this.guts = guts >= 0 ? guts : 0;
	}
	public Integer getGutsSpent() {
		return gutsSpent;
	}
	public void setGutsSpent(Integer gutsSpent) {
		this.gutsSpent = gutsSpent;
	}
	public Integer getGutsMade() {
		return gutsMade;
	}
	public void setGutsMade(Integer gutsMade) {
		this.gutsMade = gutsMade;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Boolean getCanAttack() {
		return canAttack;
	}
	public void setCanAttack(Boolean canAttack) {
		this.canAttack = canAttack;
	}
	public HashMap<MonsterStatus, Integer> getStatusDuration() {
		return statusDuration;
	}
	public void setStatusDuration(HashMap<MonsterStatus, Integer> statusDuration) {
		this.statusDuration = statusDuration;
	}
	public void addStatusDuration(MonsterStatus status, Integer duration) {
		this.statusDuration.put(status, duration);
	}
	public Monster createTempMonster() {
		Monster monster = new Monster();
		monster.setMainLineage(MonsterBreed.Breeder);
		monster.setSubLineage(MonsterBreed.Breeder);
		monster.setMaxLife(1);
		monster.setCurrentLife(1);
		monster.setCanAttack(getCanAttack());
		monster.setStatusDuration(statusDuration);
		return monster;
	}

}
