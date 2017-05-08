package card.model.game;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PlayerArea {

	int gutsPool = 0;
	Deck deck;
	ArrayList<String> hand = new ArrayList<String>();
	ArrayList<String> discard = new ArrayList<String>();
	ArrayList<Monster> monsters;
	
	public int getGutsPool() {
		return gutsPool;
	}
	@XmlElement
	public void setGutsPool(int gutsPool) {
		this.gutsPool = gutsPool;
	}
	public Deck getDeck() {
		return deck;
	}
	@XmlElement
	public void setDeck(Deck deck) {
		this.deck = deck;
	}
	public ArrayList<String> getHand() {
		return hand;
	}
	@XmlElement
	public void setHand(ArrayList<String> hand) {
		this.hand = hand;
	}
	public ArrayList<String> getDiscard() {
		return discard;
	}
	@XmlElement
	public void setDiscard(ArrayList<String> discard) {
		this.discard = discard;
	}
	public ArrayList<Monster> getMonsters() {
		return monsters;
	}
	@XmlElement
	public void setMonsters(ArrayList<Monster> monsters) {
		this.monsters = monsters;
	}
	
}
