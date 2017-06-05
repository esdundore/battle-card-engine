package card.model.view;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PlayableCardView {

	ArrayList<PlayableCard> playableCards;

	public ArrayList<PlayableCard> getPlayableCards() {
		return playableCards;
	}
	@XmlElement
	public void setPlayableCards(ArrayList<PlayableCard> playableCards) {
		this.playableCards = playableCards;
	}
	
}
