package card.model.requests;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DefendRequest extends PlayersRequest {
	
	ArrayList<DefendTarget> cardsAndTargets;

	public ArrayList<DefendTarget> getCardAndTargets() {
		return cardsAndTargets;
	}
	@XmlElement
	public void setCardAndTargets(ArrayList<DefendTarget> cardsAndTargets) {
		this.cardsAndTargets = cardsAndTargets;
	}
	
}
