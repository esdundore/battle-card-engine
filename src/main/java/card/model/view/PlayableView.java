package card.model.view;

import java.util.ArrayList;

public class PlayableView {

	public ArrayList<PlayableCard> playableCards;
	public ArrayList<Integer> playableTargets;

	public ArrayList<PlayableCard> getPlayableCards() {
		return playableCards;
	}
	public void setPlayableCards(ArrayList<PlayableCard> playableCards) {
		this.playableCards = playableCards;
	}
	public ArrayList<Integer> getPlayableTargets() {
		return playableTargets;
	}
	public void setPlayableTargets(ArrayList<Integer> playableTargets) {
		this.playableTargets = playableTargets;
	}
	
}
