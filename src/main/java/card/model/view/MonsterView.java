package card.model.view;


import java.util.Collection;

import card.enums.MonsterStatus;
import card.enums.MonsterType;
import card.model.game.Monster;

public class MonsterView {
	
	public String name;
	public MonsterType monsterType;
	public Integer currentLife;
	public Integer maxLife;
	public Integer tempDamage;
	public Boolean canAttack;
	public Collection<MonsterStatus> statuses;
	
	public MonsterView() { }
	public MonsterView(Monster monster) { 
		name = monster.getName();
		monsterType = monster.getMonsterType();
		currentLife = monster.getCurrentLife();
		maxLife = monster.getMaxLife();
		tempDamage = monster.getTempDamage();
		canAttack = monster.getCanAttack();
		statuses = monster.getStatusDuration().keySet();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public MonsterType getMonsterType() {
		return monsterType;
	}
	public void setMonsterType(MonsterType monsterType) {
		this.monsterType = monsterType;
	}
	public Integer getCurrentLife() {
		return currentLife;
	}
	public void setCurrentLife(Integer currentLife) {
		this.currentLife = currentLife;
	}
	public Integer getMaxLife() {
		return maxLife;
	}
	public void setMaxLife(Integer maxLife) {
		this.maxLife = maxLife;
	}
	public Integer getTempDamage() {
		return tempDamage;
	}
	public void setTempDamage(Integer tempDamage) {
		this.tempDamage = tempDamage;
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
