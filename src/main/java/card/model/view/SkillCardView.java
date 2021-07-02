package card.model.view;

import card.model.cards.SkillCard;

public class SkillCardView {
	
	public String name;
	public Integer id;
	public Integer damage;
	public Integer gutsCost;
	
	public SkillCardView() { }
	public SkillCardView(String name, Integer id, Integer damage, Integer gutsCost) {
		this.name = name;
		this.id = id;
		this.damage = damage;
		this.gutsCost = gutsCost;
	}
	public SkillCardView(SkillCard skillCard) { 
		name = skillCard.getName();
		id = skillCard.getId();
		damage = skillCard.getDamage();
		gutsCost = skillCard.getGutsCost();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
