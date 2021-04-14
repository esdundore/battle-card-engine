package card.model.view;

import java.util.ArrayList;

public class PlayableCard {
	
	public Integer handIndex;
	public ArrayList<Integer> users;
	
	public Integer getHandIndex() {
		return handIndex;
	}
	public void setHandIndex(Integer handIndex) {
		this.handIndex = handIndex;
	}
	public ArrayList<Integer> getUsers() {
		return users;
	}
	public void setUsers(ArrayList<Integer> users) {
		this.users = users;
	}

}