package card.model.view;

import java.util.ArrayList;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import card.model.requests.PlayersRequest;

@XmlRootElement
public class AttackView extends PlayersRequest {
	
	int user;
	ArrayList<String> cardsPlayed;
	Map<Integer, Integer> targetsAndDamage;
	
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
	public Map<Integer, Integer> getTargetsAndDamage() {
		return targetsAndDamage;
	}
	@XmlElement
	public void setTargetsAndDamage(Map<Integer, Integer> targetsAndDamage) {
		this.targetsAndDamage = targetsAndDamage;
	}


}
