package card.model.requests;

public class SkillRequest extends PlayersRequest {
	
	public Integer user;
	public Integer handIndex;

	public SkillRequest() {}
	public SkillRequest(PlayersRequest playersRequest) {
		player1 = playersRequest.getPlayer1();
		player2 = playersRequest.getPlayer2();
	}
	
	public Integer getUser() {
		return user;
	}
	public void setUser(Integer user) {
		this.user = user;
	}
	public Integer getHandIndex() {
		return handIndex;
	}
	public void setHandIndex(Integer handIndex) {
		this.handIndex = handIndex;
	}

}
