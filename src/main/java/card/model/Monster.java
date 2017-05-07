package card.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Monster extends MonsterCard {

	protected int currentLife;

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
	
}
