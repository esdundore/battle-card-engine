package card.model.game;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import card.model.cards.MonsterCard;

@XmlRootElement
public class Monster extends MonsterCard {

	protected int currentLife;
	protected boolean canAttack = true;
	protected ArrayList<String> status = new ArrayList<String>();

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
		this.currentLife = currentLife >= 0 ? currentLife : 0;
	}
	public boolean isCanAttack() {
		return canAttack;
	}
	@XmlElement
	public void setCanAttack(boolean canAttack) {
		this.canAttack = canAttack;
	}
	public ArrayList<String> getStatus() {
		return status;
	}
	@XmlElement
	public void setStatus(ArrayList<String> status) {
		this.status = status;
	}
	
	// ----- STATUS EFFECTS ----- //
	
	public static final String STUNNED = "STUNNED";
	public static final String AERIAL = "AERIAL";
	public static final String GROUNDED = "GROUNDED";
	
	public static final String TYPE_AIR = "AIR";
	public static final String TYPE_GRD = "GRD";
	
	public boolean typeAIR() {
		if (getStatus().contains(AERIAL)) {
			return true;
		}
		else if (!getStatus().contains(GROUNDED) && TYPE_AIR.equals(getType())) {
			return true;
		}
		else {
			return false;
		}
	}
	public boolean typeGRD() {
		if (getStatus().contains(GROUNDED)) {
			return true;
		}
		else if (!getStatus().contains(AERIAL) && TYPE_GRD.equals(getType())) {
			return true;
		}
		else {
			return false;
		}
	}
	public boolean canAttack() {
		if (!getStatus().contains(STUNNED) && isCanAttack()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void setStunned() {
		getStatus().add(STUNNED);
	}
	
}
