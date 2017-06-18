package card.model.requests;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AttackRequestNoMap extends PlayersRequest {
	
	int user;
	ArrayList<String> cardNames;
	ArrayList<Integer> cardsPlayed;
	ArrayList<Integer> targets;
	ArrayList<Integer> damages;
	
	public int getUser() {
		return user;
	}
	@XmlElement
	public void setUser(int user) {
		this.user = user;
	}
	public ArrayList<String> getCardNames() {
		return cardNames;
	}
	@XmlElement
	public void setCardNames(ArrayList<String> cardNames) {
		this.cardNames = cardNames;
	}
	public ArrayList<Integer> getCardsPlayed() {
		return cardsPlayed;
	}
	@XmlElement
	public void setCardsPlayed(ArrayList<Integer> cardsPlayed) {
		this.cardsPlayed = cardsPlayed;
	}
	public ArrayList<Integer> getTargets() {
		return targets;
	}
	@XmlElement
	public void setTargets(ArrayList<Integer> targets) {
		this.targets = targets;
	}
	public ArrayList<Integer> getDamages() {
		return damages;
	}
	@XmlElement
	public void setDamages(ArrayList<Integer> damages) {
		this.damages = damages;
	}

}
