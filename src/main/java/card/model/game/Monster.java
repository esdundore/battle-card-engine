package card.model.game;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import card.model.cards.MonsterCard;

@XmlRootElement
public class Monster extends MonsterCard {

	protected int currentLife;
	protected boolean canAttack = true;

	public Monster() {
	}
	
	public Monster(MonsterCard monsterCard) {
		this.id = monsterCard.getId();
		this.maxLife = monsterCard.getMaxLife();
		this.type = monsterCard.getType();
		this.mainLineage = monsterCard.getMainLineage();
		this.subLineage = monsterCard.getSubLineage();
		this.currentLife = this.maxLife;
	}
	
	public int getCurrentLife() {
		return currentLife;
	}
	@XmlElement
	public void setCurrentLife(int currentLife) {
		this.currentLife = currentLife;
	}
	public boolean isCanAttack() {
		return canAttack;
	}
	@XmlElement
	public void setCanAttack(boolean canAttack) {
		this.canAttack = canAttack;
	}
	
}
