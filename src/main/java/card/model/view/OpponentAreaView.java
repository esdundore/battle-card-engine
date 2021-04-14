package card.model.view;

import java.util.ArrayList;

import card.model.game.Monster;
import card.model.game.PlayerArea;

public class OpponentAreaView {

	public Integer deckSize;
	public ArrayList<String> hand;
	public ArrayList<Monster> monsters;
	public Integer guts;
	
	public OpponentAreaView() { }
	public OpponentAreaView(PlayerArea playerArea) {
		deckSize = playerArea.getDeck().getSkillCards().size();
		guts = playerArea.getGuts();
		hand = new ArrayList<String>();
		for(int i = 0; i < playerArea.getHand().size(); i++) {
			// Add a dummy card
			if (playerArea.getHand().get(i) != null) {
				hand.add("Breeder_Help");
			}
			else {
				hand.add(null);
			}
		}
		monsters = playerArea.getMonsters();
	}
	
	public Integer getDeckSize() {
		return deckSize;
	}
	public void setDeckSize(Integer deckSize) {
		this.deckSize = deckSize;
	}
	public ArrayList<String> getHand() {
		return hand;
	}
	public void setHand(ArrayList<String> hand) {
		this.hand = hand;
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
		this.guts = guts;
	}
	
}