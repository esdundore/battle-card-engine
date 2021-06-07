package card.model.cards;

import java.util.ArrayList;
import java.util.LinkedList;

public class Deck {

	public String ownerID;
	public String deckName;
	public String deckAvatar;
	public ArrayList<MonsterCard> monsterCards;
	public LinkedList<SkillCard> skillCards;
	
	public Deck copy() {
		Deck deck = new Deck();
		deck.setOwnerID(ownerID);
		ArrayList<MonsterCard> monsterCards = new ArrayList<>();
		for (MonsterCard monsterCard : this.monsterCards) {
			monsterCards.add(monsterCard);
		}
		deck.setMonsterCards(monsterCards);
		LinkedList<SkillCard> skillCards = new LinkedList<>();
		for (SkillCard skillCard : this.skillCards) {
			skillCards.add(skillCard);
		}
		deck.setSkillCards(skillCards);
		return deck;
	}
	
	public String getOwnerID() {
		return ownerID;
	}
	public void setOwnerID(String ownerID) {
		this.ownerID = ownerID;
	}
	public String getDeckName() {
		return deckName;
	}
	public void setDeckName(String deckName) {
		this.deckName = deckName;
	}
	public String getDeckAvatar() {
		return deckAvatar;
	}
	public void setDeckAvatar(String deckAvatar) {
		this.deckAvatar = deckAvatar;
	}
	public ArrayList<MonsterCard> getMonsterCards() {
		return monsterCards;
	}
	public void setMonsterCards(ArrayList<MonsterCard> monsterCards) {
		this.monsterCards = monsterCards;
	}
	public LinkedList<SkillCard> getSkillCards() {
		return skillCards;
	}
	public void setSkillCards(LinkedList<SkillCard> skillCards) {
		this.skillCards = skillCards;
	}

}
