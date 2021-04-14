package card.model.cards;

import java.util.ArrayList;
import java.util.LinkedList;

public class Deck {

	public String ownerId;
	public ArrayList<String> monsterCards;
	public LinkedList<String> skillCards;
	
	public Deck copy() {
		Deck deck = new Deck();
		deck.setOwnerId(new String(ownerId));
		ArrayList<String> monsterCards = new ArrayList<>();
		for (String monsterCard : this.monsterCards) {
			monsterCards.add(new String(monsterCard));
		}
		deck.setMonsterCards(monsterCards);
		LinkedList<String> skillCards = new LinkedList<>();
		for (String skillCard : this.skillCards) {
			skillCards.add(new String(skillCard));
		}
		deck.setSkillCards(skillCards);
		return deck;
	}
	
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public ArrayList<String> getMonsterCards() {
		return monsterCards;
	}
	public void setMonsterCards(ArrayList<String> monsterCards) {
		this.monsterCards = monsterCards;
	}
	public LinkedList<String> getSkillCards() {
		return skillCards;
	}
	public void setSkillCards(LinkedList<String> skillCards) {
		this.skillCards = skillCards;
	}

}