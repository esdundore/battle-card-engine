package card.model.game;

import java.util.ArrayList;
import java.util.LinkedList;

import card.model.cards.Deck;
import card.model.cards.SkillCard;

public class PlayerArea {

	public Deck deck;
	public ArrayList<SkillCard> hand = new ArrayList<SkillCard>();
	public LinkedList<SkillCard> discards = new LinkedList<SkillCard>();
	public ArrayList<Monster> monsters;
	public ArrayList<Monster> subMonsters;
	public Breeder breeder = new Breeder();
	
	public PlayerArea() {}
	public PlayerArea(String playerName) {
		breeder.setPlayerName(playerName);
	}
	
	public PlayerArea copy() {
		PlayerArea copy = new PlayerArea();
		copy.setDeck(deck);
		copy.setHand(new ArrayList<SkillCard>());
		for (SkillCard card : hand) copy.getHand().add(card);
		copy.setDiscards(discards);
		copy.setMonsters(monsters);
		copy.setSubMonsters(subMonsters);
		copy.setBreeder(breeder.copy());
		return copy;
	}
	
	public Deck getDeck() {
		return deck;
	}
	public void setDeck(Deck deck) {
		this.deck = deck;
	}
	public ArrayList<SkillCard> getHand() {
		return hand;
	}
	public SkillCard getHand(Integer index) {
		return hand.get(index);
	}
	public void setHand(ArrayList<SkillCard> hand) {
		this.hand = hand;
	}
	public LinkedList<SkillCard> getDiscards() {
		return discards;
	}
	public void setDiscards(LinkedList<SkillCard> discards) {
		this.discards = discards;
	}
	public ArrayList<Monster> getMonsters() {
		return monsters;
	}
	public void setMonsters(ArrayList<Monster> monsters) {
		this.monsters = monsters;
	}
	public ArrayList<Monster> getSubMonsters() {
		return subMonsters;
	}
	public void setSubMonsters(ArrayList<Monster> subMonsters) {
		this.subMonsters = subMonsters;
	}
	public Breeder getBreeder() {
		return breeder;
	}
	public void setBreeder(Breeder breeder) {
		this.breeder = breeder;
	}
	public Boolean allMonstersDead() {
		for (Monster monster : monsters) {
			if (monster.getCurrentLife() > 0) {
				return false;
			}
		}
		return true;
	}

}
