package card.model.requests;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PlayableRequest extends PlayersRequest {
	
	ArrayList<Integer> playedCardIndexes;

	public ArrayList<Integer> getPlayedCardIndexes() {
		return playedCardIndexes;
	}
	@XmlElement
	public void setPlayedCardIndexes(ArrayList<Integer> playedCardIndexes) {
		this.playedCardIndexes = playedCardIndexes;
	}

}
