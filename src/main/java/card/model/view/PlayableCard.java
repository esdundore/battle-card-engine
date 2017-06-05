package card.model.view;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PlayableCard {
	
	int cardIndex;
	ArrayList<Integer> users;
	ArrayList<Integer> targets;
	
	public int getCardIndex() {
		return cardIndex;
	}
	@XmlElement
	public void setCardIndex(int cardIndex) {
		this.cardIndex = cardIndex;
	}
	public ArrayList<Integer> getUsers() {
		return users;
	}
	@XmlElement
	public void setUsers(ArrayList<Integer> users) {
		this.users = users;
	}
	public ArrayList<Integer> getTargets() {
		return targets;
	}
	@XmlElement
	public void setTargets(ArrayList<Integer> targets) {
		this.targets = targets;
	}

}
