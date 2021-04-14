package card.model.cards;

import card.enums.SkillKeyword;
import card.enums.SkillType;
import card.enums.TargetArea;

public class SkillCard extends BattleCard {
	
	public SkillType skillType;
	public SkillKeyword skillKeyword;
	public Integer keywordValue;
	public String userId;
	public Integer gutsCost;
	public Integer damage;
	public TargetArea targetArea;


	public SkillCard copy() {
		SkillCard skillCard = new SkillCard();
		skillCard.setId(new String(id));
		skillCard.setSkillType(skillType);
		skillCard.setUserId(new String(userId));
		skillCard.setGutsCost(gutsCost);
		skillCard.setDamage(damage);
		skillCard.setSkillKeyword(skillKeyword);
		skillCard.setKeywordValue(keywordValue);
		skillCard.setTargetArea(targetArea);
		return skillCard;
	}
	
	public SkillType getSkillType() {
		return skillType;
	}
	public void setSkillType(SkillType skillType) {
		this.skillType = skillType;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getGutsCost() {
		return gutsCost;
	}
	public void setGutsCost(Integer gutsCost) {
		this.gutsCost = gutsCost;
	}
	public Integer getDamage() {
		return damage;
	}
	public void setDamage(Integer damage) {
		this.damage = damage;
	}
	public TargetArea getTargetArea() {
		return targetArea;
	}
	public void setTargetArea(TargetArea targetArea) {
		this.targetArea = targetArea;
	}

	public Boolean isAttack() {
		Boolean isAttack = false;
        switch (skillType) {
        	case POW:
        		isAttack = true;  
        	case INT:
        		isAttack = true;    
        	case SPE:
        		isAttack = true;    
        	case ENV:
        		isAttack = true;
        	case DGE:
        		isAttack = false;
        	case BLK:
        		isAttack = false;
        }
		return isAttack;
	}
	
}