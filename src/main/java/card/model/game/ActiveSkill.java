package card.model.game;

import card.model.cards.SkillCard;

public class ActiveSkill {
	
	public SkillCard card;
	public Monster user;
	public Integer target;
	public Integer handIndex;
	
	public ActiveSkill() { }
	public ActiveSkill(SkillCard card, Monster user, Integer target, Integer handIndex) {
		this.card = card;
		this.user = user;
		this.handIndex = handIndex;
		this.target = target;
	}
	
	public SkillCard getCard() {
		return card;
	}
	public void setCard(SkillCard card) {
		this.card = card;
	}
	public Monster getUser() {
		return user;
	}
	public void setUser(Monster user) {
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
