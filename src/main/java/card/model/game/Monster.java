package card.model.game;

import java.util.HashMap;
import java.util.Map;

import card.enums.MonsterStatus;
import card.enums.MonsterType;
import card.model.cards.MonsterCard;

public class Monster extends MonsterCard {

	public Integer currentLife;
	public Integer tempDamage = null;
	public Boolean canAttack = true;
	public Map<MonsterStatus, Integer> statusDuration = new HashMap<MonsterStatus, Integer>();
	
	public Monster() { }
	public Monster(MonsterCard monsterCard) {
		name = monsterCard.getName();
		maxLife = monsterCard.getMaxLife();
		currentLife = monsterCard.getMaxLife();
		monsterType = monsterCard.getMonsterType();
		baseMonsterType = monsterCard.getMonsterType();
		mainLineage = monsterCard.getMainLineage();
		subLineage = monsterCard.getSubLineage();
		currentLife = monsterCard.getMaxLife();
	}
	
	public Integer getCurrentLife() {
		return currentLife;
	}
	/** current life cannot be set to less than 0 or greater than max life **/
	public void setCurrentLife(Integer currentLife) {
		currentLife = currentLife > 0 ? currentLife : 0;
		currentLife = currentLife > this.maxLife ? this.maxLife : currentLife;
		this.currentLife = currentLife;
	}
	public Integer getTempDamage() {
		return tempDamage;
	}
	/** temp damage cannot be set to less than 0 **/
	public void setTempDamage(Integer tempDamage) {
		if (null == tempDamage) {
			this.tempDamage = null;
		}
		else {
			this.tempDamage = tempDamage > 0 ? tempDamage : 0;
		}
	}
	/** overridden based on status **/
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
	public void addStatusDuration(MonsterStatus status, Integer duration) {
		this.statusDuration.put(status, duration);
	}
	/** check if current life is > 0 **/
	public Boolean isAlive() {
		return currentLife > 0;
	}
	/** overridden based on status **/
	public MonsterType getMonsterType() {
		if (statusDuration.containsKey(MonsterStatus.AIRBORNE)) {
			return MonsterType.AIR;
		}
		else {
			return monsterType;
		}
	}
	public Boolean isPurebreed() {
		return mainLineage == subLineage;
	}

}
