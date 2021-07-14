package card.model.view;

import java.util.ArrayList;

public class PlayableView {

	public ArrayList<PlayableCard> playableCards;
	public ArrayList<Integer> playableTargets;
	public Boolean playableContinue = false;

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
	public Boolean getPlayableContinue() {
		return playableContinue;
	}
	public void setPlayableContinue(Boolean playableContinue) {
		this.playableContinue = playableContinue;
	}
	
}
