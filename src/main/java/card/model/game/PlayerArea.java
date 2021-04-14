package card.model.game;

import java.util.ArrayList;

import card.model.cards.Deck;
import card.model.cards.SkillCard;

public class PlayerArea {

	public Deck deck;
	public ArrayList<SkillCard> hand = new ArrayList<SkillCard>();
	public ArrayList<String> discard = new ArrayList<String>();
	public ArrayList<Monster> monsters;
	public Integer guts = 0;
	public Boolean canBreederAttack = true;
	
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
	public ArrayList<String> getDiscard() {
		return discard;
	}
	public void setDiscard(ArrayList<String> discard) {
		this.discard = discard;
	}
	public ArrayList<Monster> getMonsters() {
		return monsters;
	}
	public void setMonsters(ArrayList<Monster> monsters) {
		this.monsters = monsters;
	}
	public Integer getGuts() {
		return guts;
	}
	public void setGuts(Integer guts) {
		this.guts = guts >= 0 ? guts : 0;
	}
	public Boolean getCanBreederAttack() {
		return canBreederAttack;
	}
	public void setCanBreederAttack(Boolean canBreederAttack) {
		this.canBreederAttack = canBreederAttack;
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
