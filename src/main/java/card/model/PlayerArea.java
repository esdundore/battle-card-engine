package card.model;

import java.util.ArrayList;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PlayerArea {

	int gutsPool = 0;
	Deck deck;
	LinkedList<String> hand = new LinkedList<String>();
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
	public LinkedList<String> getHand() {
		return hand;
	}
	@XmlElement
	public void setHand(LinkedList<String> hand) {
		this.hand = hand;
	}
	public ArrayList<Monster> getMonsters() {
		return monsters;
	}
	@XmlElement
	public void setMonsters(ArrayList<Monster> monsters) {
		this.monsters = monsters;
	}
	
}
