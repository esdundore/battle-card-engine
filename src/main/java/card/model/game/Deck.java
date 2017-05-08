package card.model.game;

import java.util.ArrayList;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Deck {
	
	String ownerId;
	ArrayList<String> monsterCards;
	LinkedList<String> skillCards;
	
	public Deck() { }
	public Deck(Deck deck) {
		this.ownerId = new String(deck.getOwnerId());
		this.monsterCards = new ArrayList<String>();
		for(String monsterCard : deck.getMonsterCards()) {
			monsterCards.add(new String(monsterCard));
		}
		this.skillCards = new LinkedList<String>();
		for(String skillCard : deck.getSkillCards()) {
			skillCards.add(new String(skillCard));
		}
	}
	public String getOwnerId() {
		return ownerId;
	}
	@XmlElement
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public ArrayList<String> getMonsterCards() {
		return monsterCards;
	}
	@XmlElement
	public void setMonsterCards(ArrayList<String> monsterCards) {
		this.monsterCards = monsterCards;
	}
	public LinkedList<String> getSkillCards() {
		return skillCards;
	}
	@XmlElement
	public void setSkillCards(LinkedList<String> skillCards) {
		this.skillCards = skillCards;
	}

}
