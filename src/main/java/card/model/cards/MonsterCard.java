package card.model.cards;

import card.enums.MonsterType;

public class MonsterCard extends BattleCard {
	
	public MonsterType monsterType;
	public String mainLineage;
	public String subLineage;
	public Integer maxLife;
	
	public MonsterCard copy() {
		MonsterCard monsterCard = new MonsterCard();
		monsterCard.setId(new String(id));
		monsterCard.setMonsterType(monsterType);
		monsterCard.setMainLineage(new String(mainLineage));
		monsterCard.setSubLineage(new String(subLineage));
		monsterCard.setMaxLife(maxLife);
		return monsterCard;
	}

	public MonsterType getMonsterType() {
		return monsterType;
	}
	public void setMonsterType(MonsterType monsterType) {
		this.monsterType = monsterType;
	}
	public String getMainLineage() {
		return mainLineage;
	}
	public void setMainLineage(String mainLineage) {
		this.mainLineage = mainLineage;
	}
	public String getSubLineage() {
		return subLineage;
	}
	public void setSubLineage(String subLineage) {
		this.subLineage = subLineage;
	}
	public Integer getMaxLife() {
		return maxLife;
	}
	public void setMaxLife(Integer maxLife) {
		this.maxLife = maxLife;
	}

}