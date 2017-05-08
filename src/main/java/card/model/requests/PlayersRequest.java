package card.model.requests;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PlayersRequest {

	protected String player1;
	protected String player2;
	
	public String getPlayer1() {
		return player1;
	}
	@XmlElement
	public void setPlayer1(String player1) {
		this.player1 = player1;
	}
	public String getPlayer2() {
		return player2;
	}
	@XmlElement
	public void setPlayer2(String player2) {
		this.player2 = player2;
	}
	
}
