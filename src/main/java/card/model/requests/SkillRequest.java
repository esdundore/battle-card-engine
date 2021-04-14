package card.model.requests;

public class SkillRequest extends PlayersRequest {
	
	public Integer user;
	public Integer handIndex;

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