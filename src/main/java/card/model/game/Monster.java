package card.model.game;

import java.util.HashMap;
import java.util.Map;

import card.enums.MonsterStatus;
import card.model.cards.MonsterCard;

public class Monster extends MonsterCard {

	public Integer currentLife;
	public Boolean canAttack = true;
	public Map<MonsterStatus, Integer> statusDuration = new HashMap<MonsterStatus, Integer>();
	
	public Monster() { }
	public Monster(MonsterCard monsterCard) {
		this.id = monsterCard.getId();
		this.maxLife = monsterCard.getMaxLife();
		this.monsterType = monsterCard.getMonsterType();
		this.mainLineage = monsterCard.getMainLineage();
		this.subLineage = monsterCard.getSubLineage();
		this.currentLife = this.maxLife;
	}
	
	public Integer getCurrentLife() {
		return currentLife;
	}
	public void setCurrentLife(Integer currentLife) {
		this.currentLife = currentLife;
	}
	public Boolean getCanAttack() {
		return canAttack;
	}
	public void setCanAttack(Boolean canAttack) {
		this.canAttack = canAttack;
	}
	public Map<MonsterStatus, Integer> getStatusDuration() {
		return statusDuration;
	}
	public void setStatusDuration(Map<MonsterStatus, Integer> statusDuration) {
		this.statusDuration = statusDuration;
	}
	public Boolean isAlive() {
		return currentLife > 0;
	}

}