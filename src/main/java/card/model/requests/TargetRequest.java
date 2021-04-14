package card.model.requests;

public class TargetRequest extends PlayersRequest {
	
	public Integer target;

	public TargetRequest() { }
	public TargetRequest(PlayersRequest playersRequest) {
		player1 = playersRequest.getPlayer1();
		player2 = playersRequest.getPlayer2();
	}
	
	public Integer getTarget() {
		return target;
	}
	public void setTarget(Integer target) {
		this.target = target;
	}

}