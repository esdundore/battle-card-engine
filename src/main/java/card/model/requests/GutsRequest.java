package card.model.requests;

import java.util.ArrayList;

public class GutsRequest extends PlayersRequest {
	
	public ArrayList<Integer> discards;

	public ArrayList<Integer> getDiscards() {
		return discards;
	}
	public void setDiscards(ArrayList<Integer> discards) {
		this.discards = discards;
	}

}