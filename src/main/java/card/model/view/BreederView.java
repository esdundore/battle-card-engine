package card.model.view;

import java.util.Collection;

import card.enums.MonsterStatus;
import card.model.game.Breeder;

public class BreederView {

	public String playerName;
	public Integer guts;
	public String message;
	public Boolean canAttack;
	public Collection<MonsterStatus> statuses;

	public BreederView() {}
	public BreederView(Breeder breeder) {
		playerName = breeder.getPlayerName();
		if (breeder.getGutsSpent() > 0 && (breeder.getGuts() - breeder.getGutsSpent() > 0)) {
			guts = breeder.getGuts() - breeder.getGutsSpent();
		}
		else {
			guts = breeder.getGuts();
		}
		message = breeder.getMessage();
		canAttack = breeder.getCanAttack();
		statuses = breeder.getStatusDuration().keySet();
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
	public void setGuts(Integer guts) {
		this.guts = guts;
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
	public Collection<MonsterStatus> getStatuses() {
		return statuses;
	}
	public void setStatuses(Collection<MonsterStatus> statuses) {
		this.statuses = statuses;
	}
	
}
