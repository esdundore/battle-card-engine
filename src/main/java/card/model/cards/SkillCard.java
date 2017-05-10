package card.model.cards;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SkillCard extends BattleCard {

	protected int gutsCost;
	protected String userId;
	protected String type;
	protected int damage;

	public int getGutsCost() {
		return gutsCost;
	}
	@XmlElement
	public void setGutsCost(int gutsCost) {
		this.gutsCost = gutsCost;
	}
	public String getUserId() {
		return userId;
	}
	@XmlElement
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getType() {
		return type;
	}
	@XmlElement
	public void setType(String type) {
		this.type = type;
	}
	public int getDamage() {
		return damage;
	}
	@XmlElement
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
}
