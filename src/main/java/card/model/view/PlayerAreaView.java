package card.model.view;

import java.util.ArrayList;
import java.util.stream.Collectors;

import card.model.cards.SkillCard;
import card.model.game.Monster;
import card.model.game.PlayerArea;

public class PlayerAreaView {
	
	public Integer deckSize;
	public ArrayList<String> hand;
	public ArrayList<String> discard;
	public ArrayList<Monster> monsters;
	public Integer guts;
	
	public PlayerAreaView() { }
	public PlayerAreaView(PlayerArea playerArea) {
		deckSize = playerArea.getDeck().getSkillCards().size();
		guts = playerArea.getGuts();
		hand = playerArea.getHand().stream()
				.map(SkillCard::getId).collect(Collectors.toCollection(ArrayList::new));
		monsters = playerArea.getMonsters();
		discard = playerArea.getDiscard();
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
		this.guts = guts;
	}
	
}