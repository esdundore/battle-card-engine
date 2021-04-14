package card.model.game;

import java.util.ArrayList;
import java.util.Arrays;

import card.model.requests.SkillRequest;
import card.model.requests.TargetRequest;
import card.enums.TargetArea;
import card.model.cards.SkillCard;
import card.model.requests.PlayersRequest;

public class SkillArea extends PlayersRequest {
	
	public Integer attackId = 0;
	public Boolean resolved = true;
	public TargetArea targetArea;
	public ArrayList<Integer> damages;
	public ArrayList<ActiveSkill> attacks;
	public ArrayList<ActiveSkill> defenses;
	
	public void newAttack(SkillRequest attackRequest, SkillCard card) {
		this.player1 = attackRequest.getPlayer1();
		this.player2 = attackRequest.getPlayer2();
		attackId++;
		this.resolved = false;
		this.targetArea = card.getTargetArea();
		this.damages = null;
		ActiveSkill activeSkill = new ActiveSkill(card, attackRequest.getUser(), null, attackRequest.getHandIndex());
		this.attacks = new ArrayList<ActiveSkill>(Arrays.asList(activeSkill));
	}
	
	public void addAttack(SkillRequest attackRequest, SkillCard card) {
		attacks.add(new ActiveSkill(card, attackRequest.getUser(), null, attackRequest.getHandIndex()));
	}
	
	public void addAttackTarget(TargetRequest targetRequest) {
		for(ActiveSkill attack : attacks) {
			attack.setTarget(targetRequest.getTarget());
		}
	}
	
	public void addDefense(SkillRequest defenseRequest, SkillCard card) {
		defenses.add(new ActiveSkill(card, defenseRequest.getUser(), null, defenseRequest.getHandIndex()));
	}
	
	public void addDefenseTarget(TargetRequest targetRequest) {
		defenses.get(defenses.size()).setTarget(targetRequest.getTarget());
	}

	public Integer getAttackId() {
		return attackId;
	}
	public void setAttackId(Integer attackId) {
		this.attackId = attackId;
	}
	public Boolean isResolved() {
		return resolved;
	}
	public void setResolved(Boolean resolved) {
		this.resolved = resolved;
	}
	public TargetArea getTargetArea() {
		return targetArea;
	}
	public void setTargetArea(TargetArea targetArea) {
		this.targetArea = targetArea;
	}
	public ArrayList<Integer> getDamages() {
		return damages;
	}
	public void setDamages(ArrayList<Integer> damages) {
		this.damages = damages;
	}
	public ArrayList<ActiveSkill> getAttacks() {
		return attacks;
	}
	public void setAttacks(ArrayList<ActiveSkill> attacks) {
		this.attacks = attacks;
	}
	public ArrayList<ActiveSkill> getDefenses() {
		return defenses;
	}
	public void setDefenses(ArrayList<ActiveSkill> defenses) {
		this.defenses = defenses;
	}
}