package card.model.game;

import card.model.cards.SkillCard;

public class ActiveSkill {
	
	public SkillCard card;
	public Integer user;
	public Integer target;
	public Integer handIndex;
	
	public ActiveSkill() { }
	public ActiveSkill(SkillCard card, Integer user, Integer target, Integer handIndex) {
		this.card = card;
		this.user = user;
		this.target = target;
		this.handIndex = handIndex;
	}
	
	public SkillCard getCard() {
		return card;
	}
	public void setCard(SkillCard card) {
		this.card = card;
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