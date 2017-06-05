package card.model.view;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import card.model.requests.PlayersRequest;

@XmlRootElement
public class AttackView extends PlayersRequest {
	
	int user;
	ArrayList<String> cardsPlayed;
	ArrayList<Integer> targets;
	ArrayList<Integer> damage;
	
	public int getUser() {
		return user;
	}
	@XmlElement
	public void setUser(int user) {
		this.user = user;
	}
	public ArrayList<String> getCardsPlayed() {
		return cardsPlayed;
	}
	@XmlElement
	public void setCardsPlayed(ArrayList<String> cardsPlayed) {
		this.cardsPlayed = cardsPlayed;
	}
	public ArrayList<Integer> getTargets() {
		return targets;
	}
	@XmlElement
	public void setTargets(ArrayList<Integer> targets) {
		this.targets = targets;
	}
	public ArrayList<Integer> getDamage() {
		return damage;
	}
	@XmlElement
	public void setDamage(ArrayList<Integer> damage) {
		this.damage = damage;
	}


}
