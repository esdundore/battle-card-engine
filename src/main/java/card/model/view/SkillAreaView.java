package card.model.view;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

import card.enums.TargetArea;
import card.model.game.GameState;
import card.model.game.PlayerArea;
import card.model.game.SkillArea;

public class SkillAreaView{
	
	public String attacker;
	public String defender;
	public Integer attackId;
	public Boolean resolved;
	public TargetArea targetArea;
	public ArrayList<SkillView> attacks;
	public ArrayList<SkillView> defenses;

	public SkillAreaView() { }
	public SkillAreaView(GameState gameState) {
		SkillArea skillArea = gameState.getSkillArea();
		attacker = skillArea.getAttacker();
		defender = skillArea.getDefender();
		attackId = skillArea.getAttackId();
		resolved = skillArea.isResolved();
		targetArea = skillArea.getTargetArea();
		PlayerArea attackerArea = gameState.getPlayerArea(attacker);
		PlayerArea defenderArea = gameState.getPlayerArea(defender);
		attacks = skillArea.getAttacks().stream().filter(Objects::nonNull)
				.map(skill -> new SkillView(skill, attackerArea))
				.collect(Collectors.toCollection(ArrayList::new));
		defenses = skillArea.getDefenses().stream().filter(Objects::nonNull)
				.map(skill -> new SkillView(skill, defenderArea))
				.collect(Collectors.toCollection(ArrayList::new));
	}
	
	public String getAttacker() {
		return attacker;
	}
	public void setAttacker(String attacker) {
		this.attacker = attacker;
	}
	public String getDefender() {
		return defender;
	}
	public void setDefender(String defender) {
		this.defender = defender;
	}
	public Integer getAttackId() {
		return attackId;
	}
	public void setAttackId(Integer attackId) {
		this.attackId = attackId;
	}
	public Boolean getResolved() {
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
	public ArrayList<SkillView> getAttacks() {
		return attacks;
	}
	public void setAttacks(ArrayList<SkillView> attacks) {
		this.attacks = attacks;
	}
	public ArrayList<SkillView> getDefenses() {
		return defenses;
	}
	public void setDefenses(ArrayList<SkillView> defenses) {
		this.defenses = defenses;
	}
	
}
