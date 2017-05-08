package card.model.requests;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GutsRequest extends PlayersRequest {
	
	ArrayList<Integer> discards;

	public ArrayList<Integer> getDiscards() {
		return discards;
	}
	@XmlElement
	public void setDiscards(ArrayList<Integer> discards) {
		this.discards = discards;
	}

}
