package card.model.view;

import card.model.game.ActiveSkill;

public class SkillView {

	public String cardName;
	public Integer user;
	public Integer target;
	public Integer handIndex;
	
	public SkillView() { }
	public SkillView(ActiveSkill activeSkill) {
		this.cardName = activeSkill.getCard().getId();
		this.user = activeSkill.getUser();
		this.target = activeSkill.getTarget();
		this.handIndex = activeSkill.getHandIndex();
	}

	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public Integer getUser() {
		return user;
	}
	public void setUser(Integer user) {
		this.user = user;
	}
	public Integer getTarget() {
		return target;
	}
	public void setTarget(Integer target) {
		this.target = target;
	}
	public Integer getHandIndex() {
		return handIndex;
	}
	public void setHandIndex(Integer handIndex) {
		this.handIndex = handIndex;
	}

}