package card.model.cards;

import card.enums.MonsterBreed;
import card.enums.SkillKeyword;
import card.enums.SkillType;
import card.enums.TargetArea;

public class SkillCard extends BattleCard {
	
	public SkillType skillType;
	public MonsterBreed userBreed;
	public TargetArea targetArea;
	public SkillKeyword skillKeyword;
	public Integer keywordValue;
	public Integer gutsCost;
	public Integer baseGutsCost;
	public Integer damage;
	public Integer baseDamage;
	public Boolean revealed = false;

	public SkillCard copy() {
		SkillCard skillCard = new SkillCard();
		skillCard.setName(name);
		skillCard.setId(id);
		skillCard.setSkillType(skillType);
		skillCard.setUserBreed(userBreed);
		skillCard.setTargetArea(targetArea);
		skillCard.setSkillKeyword(skillKeyword);
		skillCard.setKeywordValue(keywordValue);
		skillCard.setGutsCost(gutsCost);
		skillCard.setBaseGutsCost(baseGutsCost);
		skillCard.setDamage(damage);
		skillCard.setBaseDamage(baseDamage);
		skillCard.setRevealed(revealed);
		return skillCard;
	}
	
	public SkillType getSkillType() {
		return skillType;
	}
	public void setSkillType(SkillType skillType) {
		this.skillType = skillType;
	}
	public MonsterBreed getUserBreed() {
		return userBreed;
	}
	public void setUserBreed(MonsterBreed userBreed) {
		this.userBreed = userBreed;
	}
	public TargetArea getTargetArea() {
		return targetArea;
	}
	public void setTargetArea(TargetArea targetArea) {
		this.targetArea = targetArea;
	}
	public SkillKeyword getSkillKeyword() {
		return skillKeyword;
	}
	public void setSkillKeyword(SkillKeyword skillKeyword) {
		this.skillKeyword = skillKeyword;
	}
	public Integer getKeywordValue() {
		return keywordValue;
	}
	public void setKeywordValue(Integer keywordValue) {
		this.keywordValue = keywordValue;
	}
	public Integer getGutsCost() {
		if (SkillKeyword.POWER_OF_SUN == getSkillKeyword()) {
			return -2;
		}
		if (SkillKeyword.SUPPORT == getSkillKeyword()) {
			return -3;
		}
		return gutsCost;
	}
	/** guts cost cannot be less than 0 **/
	public void setGutsCost(Integer gutsCost) {
		this.gutsCost = gutsCost > 0 ? gutsCost : 0;
	}
	public Integer getBaseGutsCost() {
		return baseGutsCost;
	}
	public void setBaseGutsCost(Integer baseGutsCost) {
		this.baseGutsCost = baseGutsCost;
	}
	public Integer getDamage() {
		return damage;
	}
	/** damage cannot be less than 0 **/
	public void setDamage(Integer damage) {
		this.damage = damage > 0 ? damage : 0;
	}
	public Integer getBaseDamage() {
		return baseDamage;
	}
	public void setBaseDamage(Integer baseDamage) {
		this.baseDamage = baseDamage;
	}

	public Boolean getRevealed() {
		return revealed;
	}

	public void setRevealed(Boolean revealed) {
		this.revealed = revealed;
	}

	public Boolean determineAttack() {
		if (SkillType.POW == skillType || 
				SkillType.INT == skillType || 
				SkillType.SPE == skillType || 
				SkillType.ENV == skillType) {
			return true;
		}
		if (SkillType.BLK == skillType || 
				SkillType.DGE == skillType) {
			return false;
		}
		return false;
	}
	
}
