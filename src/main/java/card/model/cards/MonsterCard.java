package card.model.cards;

import card.enums.MonsterBreed;
import card.enums.MonsterType;

public class MonsterCard extends BattleCard {
	
	public MonsterType monsterType;
	public MonsterType baseMonsterType;
	public MonsterBreed mainLineage;
	public MonsterBreed subLineage;
	public Integer maxLife;
	
	public MonsterCard copy() {
		MonsterCard monsterCard = new MonsterCard();
		monsterCard.setName(name);
		monsterCard.setMonsterType(monsterType);
		monsterCard.setBaseMonsterType(baseMonsterType);
		monsterCard.setMainLineage(mainLineage);
		monsterCard.setSubLineage(subLineage);
		monsterCard.setMaxLife(maxLife);
		return monsterCard;
	}

	public MonsterType getMonsterType() {
		return monsterType;
	}
	public void setMonsterType(MonsterType monsterType) {
		this.monsterType = monsterType;
	}
	public MonsterType getBaseMonsterType() {
		return baseMonsterType;
	}
	public void setBaseMonsterType(MonsterType baseMonsterType) {
		this.baseMonsterType = baseMonsterType;
	}
	public MonsterBreed getMainLineage() {
		return mainLineage;
	}
	public void setMainLineage(MonsterBreed mainLineage) {
		this.mainLineage = mainLineage;
	}
	public MonsterBreed getSubLineage() {
		return subLineage;
	}
	public void setSubLineage(MonsterBreed subLineage) {
		this.subLineage = subLineage;
	}
	public Integer getMaxLife() {
		return maxLife;
	}
	public void setMaxLife(Integer maxLife) {
		this.maxLife = maxLife;
	}

}
