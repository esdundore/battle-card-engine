package card.model.view;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import card.model.requests.PlayersRequest;

@XmlRootElement
public class DefendView extends PlayersRequest {
	
	ArrayList<DefendViewTarget> cardsAndTargets;

	public ArrayList<DefendViewTarget> getCardAndTargets() {
		return cardsAndTargets;
	}
	@XmlElement
	public void setCardAndTargets(ArrayList<DefendViewTarget> cardsAndTargets) {
		this.cardsAndTargets = cardsAndTargets;
	}
	
}
