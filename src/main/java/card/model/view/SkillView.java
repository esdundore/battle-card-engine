package card.model.view;

import java.util.ArrayList;

import card.model.game.ActiveSkill;
import card.model.game.Monster;
import card.model.game.PlayerArea;

public class SkillView {

	public SkillCardView skillCard;
	public Integer user;
	public Integer target;
	public Integer handIndex;
	
	public SkillView() { }
	public SkillView(ActiveSkill activeSkill, PlayerArea playerArea) {
		ArrayList<Monster> monsters = playerArea.getMonsters();
		skillCard = new SkillCardView(activeSkill.getCard());
		user = monsters.indexOf(activeSkill.getUser());
		if (user == -1) user = monsters.size();
		target = activeSkill.getTarget();
		handIndex = activeSkill.getHandIndex();
	}

	public SkillCardView getSkillCard() {
		return skillCard;
	}
	public void setSkillCard(SkillCardView skillCard) {
		this.skillCard = skillCard;
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
