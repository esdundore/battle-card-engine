package card.model.requests;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DefendTarget extends PlayersRequest {
	
	int card;
	int user;
	int transfer;
	
	public int getCard() {
		return card;
	}
	@XmlElement
	public void setCard(int card) {
		this.card = card;
	}
	public int getUser() {
		return user;
	}
	@XmlElement
	public void setUser(int user) {
		this.user = user;
	}
	public int getTransfer() {
		return transfer;
	}
	@XmlElement
	public void setTransfer(int transfer) {
		this.transfer = transfer;
	}

}
