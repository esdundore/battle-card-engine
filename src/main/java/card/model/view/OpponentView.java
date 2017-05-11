package card.model.view;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import card.model.game.Monster;

@XmlRootElement
public class OpponentView {

	int gutsPool = 0;
	int handSize;
	ArrayList<Monster> monsters;
	int deckSize;
	
	public int getGutsPool() {
		return gutsPool;
	}
	@XmlElement
	public void setGutsPool(int gutsPool) {
		this.gutsPool = gutsPool;
	}
	public int getHandSize() {
		return handSize;
	}
	@XmlElement
	public void setHandSize(int handSize) {
		this.handSize = handSize;
	}
	public ArrayList<Monster> getMonsters() {
		return monsters;
	}
	@XmlElement
	public void setMonsters(ArrayList<Monster> monsters) {
		this.monsters = monsters;
	}
	public int getDeckSize() {
		return deckSize;
	}
	@XmlElement
	public void setDeckSize(int deckSize) {
		this.deckSize = deckSize;
	}
	
}