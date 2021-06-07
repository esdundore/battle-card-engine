package card.model.view;

import card.model.cards.SkillCard;

public class SkillCardView {
	
	public String name;
	public Integer damage;
	public Integer gutsCost;
	
	public SkillCardView() { }
	public SkillCardView(String name, Integer damage, Integer gutsCost) {
		this.name = name;
		this.damage = damage;
		this.gutsCost = gutsCost;
	}
	public SkillCardView(SkillCard skillCard) { 
		name = skillCard.getName();
		damage = skillCard.getDamage();
		gutsCost = skillCard.getGutsCost();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getDamage() {
		return damage;
	}
	public void setDamage(Integer damage) {
		this.damage = damage;
	}
	public Integer getGutsCost() {
		return gutsCost;
	}
	public void setGutsCost(Integer gutsCost) {
		this.gutsCost = gutsCost;
	}

}
