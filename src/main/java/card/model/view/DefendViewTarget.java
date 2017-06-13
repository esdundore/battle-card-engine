package card.model.view;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DefendViewTarget {
	
	String card;
	int handIndex;
	int user;
	int transfer;
	
	public String getCard() {
		return card;
	}
	@XmlElement
	public void setCard(String card) {
		this.card = card;
	}
	public int getHandIndex() {
		return handIndex;
	}
	@XmlElement
	public void setHandIndex(int handIndex) {
		this.handIndex = handIndex;
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
