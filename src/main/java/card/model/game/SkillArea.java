package card.model.game;

import java.util.ArrayList;
import java.util.stream.Collectors;

import card.enums.MonsterBreed;
import card.enums.SkillKeyword;
import card.enums.SkillType;
import card.enums.TargetArea;
import card.model.cards.BattleCard;
import card.model.cards.SkillCard;
import card.model.requests.PlayersRequest;

public class SkillArea {
	
	public String attacker;
	public String defender;
	public Integer attackId = 0;
	public Boolean resolved = true;
	public TargetArea targetArea;
	public ArrayList<ActiveSkill> attacks;
	public ArrayList<ActiveSkill> defenses;

	public void clearSkillArea(PlayersRequest playerRequest, TargetArea targetArea) {
		attacker = playerRequest.getPlayer1();
		defender = playerRequest.getPlayer2();
		this.targetArea = targetArea;
		attacks = new ArrayList<ActiveSkill>();
		defenses = new ArrayList<ActiveSkill>();
		resolved = false;
	}

	public SkillArea copy() {
		SkillArea copy = new SkillArea();
		copy.setAttacker(attacker);
		copy.setDefender(defender);
		copy.setAttackId(attackId);
		copy.setResolved(resolved);
		copy.setTargetArea(targetArea);
		copy.setAttacks(new ArrayList<>());
		for (ActiveSkill attack : attacks) copy.getAttacks().add(attack);
		copy.setDefenses(new ArrayList<>());
		for (ActiveSkill defense : defenses) copy.getDefenses().add(defense);
		return copy;
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
	
	public Monster getAttackMonster() {
		if (attacks.isEmpty() || isResolved()) {
			return null;
		}
		return attacks.iterator().next().getUser();
	}
	public Integer getTarget() {
		if (attacks.isEmpty() || isResolved()) {
			return null;
		}
		return attacks.iterator().next().getTarget();
	}
	public ActiveSkill getBaseAttack() {
		if (isResolved()) {
			return null;
		}
		for (ActiveSkill attack : attacks) {
			SkillType skillType = attack.getCard().getSkillType();
			if (SkillType.POW == skillType || SkillType.INT == skillType) {
				return attack;
			}
		}
		return null;
	}
	
	
	public ArrayList<SkillCard> allAttackCards() {
		return attacks.stream().map(ActiveSkill::getCard).collect(Collectors.toCollection(ArrayList::new));
	}
	public ArrayList<SkillKeyword> allAttackKeywords() {
		return allAttackCards().stream().map(SkillCard::getSkillKeyword).collect(Collectors.toCollection(ArrayList::new));
	}
	public ArrayList<SkillType> allAttackTypes() {
		return allAttackCards().stream().map(SkillCard::getSkillType).collect(Collectors.toCollection(ArrayList::new));
	}
	public ArrayList<MonsterBreed> allAttackBreeds() {
		return allAttackCards().stream().map(SkillCard::getUserBreed).collect(Collectors.toCollection(ArrayList::new));
	}
	public ArrayList<String> allAttackNames() {
		return allAttackCards().stream().map(BattleCard::getName).collect(Collectors.toCollection(ArrayList::new));
	}
	public Integer totalAttackCost() {
		return allAttackCards().stream().map(SkillCard::getGutsCost).collect(Collectors.summingInt(Integer::intValue));
	}
	public ArrayList<SkillCard> allDefenseCards() {
		return defenses.stream().map(ActiveSkill::getCard).collect(Collectors.toCollection(ArrayList::new));
	}
	public ArrayList<SkillKeyword> allDefenseKeywords() {
		return allDefenseCards().stream().map(SkillCard::getSkillKeyword).collect(Collectors.toCollection(ArrayList::new));
	}
	
}
