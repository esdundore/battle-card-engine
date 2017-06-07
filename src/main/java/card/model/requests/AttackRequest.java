package card.model.requests;

import java.util.ArrayList;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AttackRequest extends PlayersRequest {
	
	int user;
	ArrayList<String> cardNames;
	ArrayList<Integer> cardsPlayed;
	Map<Integer, Integer> targetsAndDamage;
	
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
	public Map<Integer, Integer> getTargetsAndDamage() {
		return targetsAndDamage;
	}
	@XmlElement
	public void setTargetsAndDamage(Map<Integer, Integer> targetsAndDamage) {
		this.targetsAndDamage = targetsAndDamage;
	}


}
