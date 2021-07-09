package card.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import card.enums.MonsterStatus;
import card.enums.MonsterType;
import card.enums.SkillKeyword;
import card.enums.SkillType;
import card.model.cards.SkillCard;
import card.model.game.ActiveSkill;
import card.model.game.Monster;
import card.model.game.PlayerArea;
import card.model.game.SkillArea;
import card.util.KeywordUtil;

@Component
public class DamageCalculator {
	
	public void calculateDamages(SkillArea skillArea, PlayerArea attackerArea, PlayerArea defenderArea, SkillCard envCard) {
		ArrayList<ActiveSkill> attacks = skillArea.getAttacks();
		ArrayList<SkillKeyword> attackKeywords = attacks.stream()
				.map(ActiveSkill::getCard)
				.map(SkillCard::getSkillKeyword)
				.collect(Collectors.toCollection(ArrayList::new));
		ActiveSkill firstAttack = attacks.iterator().next();
		Monster attacker = firstAttack.getUser();
		Monster targetMonster = defenderArea.getMonsters().get(firstAttack.getTarget());

		// recalculate damage
		attacker.setTempDamage(null);
		targetMonster.setTempDamage(null);
		// Apply damage to the target
		targetMonster.setTempDamage(calculateTargetDamage(attacks, targetMonster, envCard));
		// Apply damage to other enemy targets if AOE
		for (Monster monster : defenderArea.getMonsters()) {
			if (monster != targetMonster) {
				monster.setTempDamage(null);
				if (!Collections.disjoint(attackKeywords, KeywordUtil.AOE)) 
					monster.setTempDamage(calculateTargetDamage(attacks, monster, envCard));
			}
		}
		// Apply damage to other ally monsters if ALL
		for (Monster monster : attackerArea.getMonsters()) {
			if (monster != attacker) {
				monster.setTempDamage(null);
				if (!Collections.disjoint(attackKeywords, KeywordUtil.ALL)) 
					monster.setTempDamage(calculateTargetDamage(attacks, monster, envCard));
			}
		}
		
		// Calculate damages after applying Defenses
		Boolean isDodged = false;
		for (ActiveSkill defenseSkill : skillArea.getDefenses()) {
			SkillCard defenseCard = defenseSkill.getCard();
			Monster target = defenderArea.getMonsters().get(defenseSkill.getTarget());
			if (SkillType.DGE == defenseCard.getSkillType()) {
				isDodged = true;
				Boolean isHalfDodge = attackKeywords.contains(SkillKeyword.HALF_DODGE);
				target.setTempDamage(calculateDodgeDamage(target, isHalfDodge));
				if (SkillKeyword.RETREAT == defenseCard.getSkillKeyword()) {
					for (Monster monster : defenderArea.getMonsters()) {
						if (monster != defenseSkill.getUser()) monster.setTempDamage(calculateDodgeDamage(monster, isHalfDodge));
					}
				}
			}
			else if (SkillType.BLK == defenseCard.getSkillType()) {
				if (SkillKeyword.BOUNCE == defenseCard.getSkillKeyword()) {
					if (attacker.getTempDamage() == null) attacker.setTempDamage(0);
					attacker.setTempDamage(attacker.getTempDamage() + defenseSkill.getUser().getTempDamage());
				}
				else if (SkillKeyword.COUNTER == defenseCard.getSkillKeyword()) {
					if (attacker.getTempDamage() == null) attacker.setTempDamage(0);
					attacker.setTempDamage(attacker.getTempDamage() + defenseSkill.getUser().getTempDamage() / 2);
				}
				else if (SkillKeyword.DEFLECT == defenseCard.getSkillKeyword()) {
					if (attacker.getTempDamage() == null) attacker.setTempDamage(0);
					attacker.setTempDamage(attacker.getTempDamage() + defenseSkill.getUser().getTempDamage());
				}
				
				if (!attackKeywords.contains(SkillKeyword.UNBLOCKABLE)) {
					target.setTempDamage(calculateBlockDamage(defenseSkill, target));
				}
				
				if (SkillKeyword.JUMP_IN == defenseCard.getSkillKeyword()) {
					target.setTempDamage(target.getTempDamage() * 2);
				}
				else if (SkillKeyword.HIDE == defenseCard.getSkillKeyword()) {
					target.setTempDamage(defenseSkill.getUser().getTempDamage());
					defenseSkill.getUser().setTempDamage(0);
				}
				else if (SkillKeyword.INTERCEPT == defenseCard.getSkillKeyword()) {
					if (defenseSkill.getUser().getTempDamage() == null) defenseSkill.getUser().setTempDamage(0);
					defenseSkill.getUser().setTempDamage(defenseSkill.getUser().getTempDamage() + target.getTempDamage());
					target.setTempDamage(0);
				}
			}
		}
		
		// Adjusted for non-lethal damage
		if (attackKeywords.contains(SkillKeyword.HELP)) {
			for (Monster monster : defenderArea.getMonsters()) {
				int tempDamage = monster.getTempDamage() == null ? 0 : monster.getTempDamage();
				if (tempDamage >= monster.getCurrentLife()) monster.setTempDamage(monster.getCurrentLife() - 1);;
			}
		}
		
		// Calculate recoil damage
		attacker.setTempDamage(calculateRecoilDamage(attacks, isDodged));
	}

	public Integer calculateTargetDamage(ArrayList<ActiveSkill> attacks, Monster monster, SkillCard envCard) {
		Integer baseDamage = 0;
		Integer multiplier = 1;

		// This monster is invulnerable to damage
		if (monster.getStatusDuration().containsKey(MonsterStatus.INVULNERABLE)) {
			return null;
		}
		// Calculate base damage and multipliers from all attacks
		Integer comboCount = 0;
		for (ActiveSkill attack : attacks) {
			SkillKeyword skillKeyword = attack.getCard().getSkillKeyword();
			baseDamage += attack.getCard().getDamage();
			if (KeywordUtil.HIT_AIR_ONLY.contains(skillKeyword) && MonsterType.AIR != monster.getMonsterType()) return null;
			else if (KeywordUtil.HIT_GROUND_ONLY.contains(skillKeyword) && MonsterType.GRD != monster.getMonsterType()) return null;
			else if (SkillKeyword.COMBO_TIGER == skillKeyword) {
				comboCount++;
				if (comboCount == 2) baseDamage += 1;
				else if (comboCount == 3) baseDamage += 3;
			}
			else if (SkillKeyword.AIR_DOUBLE == skillKeyword && MonsterType.AIR == monster.getMonsterType()) multiplier *= 2;
			else if (SkillKeyword.WILL_POWER == skillKeyword) multiplier *= 2;
			else if (SkillKeyword.GRAND_END == skillKeyword) multiplier *= 3;
		}

		// Calculate multipliers based on attacker buffs
		ArrayList<SkillType> attackTypes = attacks.stream()
				.map(ActiveSkill::getCard).map(SkillCard::getSkillType)
				.collect(Collectors.toCollection(ArrayList::new));
		Set<MonsterStatus> attackerStatus = attacks.iterator().next().getUser().getStatusDuration().keySet();
		if (attackerStatus.contains(MonsterStatus.DMGx2)) multiplier *= 2;
		if (attackTypes.contains(SkillType.POW)) {
			if (attackerStatus.contains(MonsterStatus.POWx3)) multiplier *= 3;
			if (attackerStatus.contains(MonsterStatus.POWx2)) multiplier *= 2;
		}
		if (attackTypes.contains(SkillType.INT)) {
			if (attackerStatus.contains(MonsterStatus.INTx2)) multiplier *= 2;
		}
		
		Integer finalDamage = baseDamage * multiplier;
		if (SkillKeyword.LOCK_TIGHT == envCard.getSkillKeyword() && finalDamage > 3) return 0;
		else if (SkillKeyword.NICE_FIGHT == envCard.getSkillKeyword() && finalDamage < 4) return 0;
		else return finalDamage;
	}
	
	public Integer calculateBlockDamage(ActiveSkill block, Monster target) {
		Integer tempDamage = target.getTempDamage();
		SkillKeyword skillKeyword = block.getCard().getSkillKeyword();
		if (KeywordUtil.BLOCK.contains(skillKeyword)) {
			tempDamage = target.getTempDamage() - block.getCard().getKeywordValue();
		}
		else if (SkillKeyword.BOUNCE == skillKeyword || SkillKeyword.DEFLECT == skillKeyword) {
			tempDamage = 0;
		}
		else if (SkillKeyword.COUNTER == skillKeyword || SkillKeyword.SPONGE == skillKeyword) {
			tempDamage = target.getTempDamage() / 2;
		}
		else if (SkillKeyword.GRIT == skillKeyword) {
			tempDamage = target.getCurrentLife() - 1;
		}
		else if (SkillKeyword.LEAF_ZERO == skillKeyword) {
			tempDamage = target.getCurrentLife() / 2;
		}
		return tempDamage;
	}
	
	public Integer calculateDodgeDamage(Monster target, Boolean isHalfDodge) {
		Integer tempDamage = 0;
		if (isHalfDodge) tempDamage = target.getTempDamage() / 2;
		else tempDamage = null;
		return tempDamage;
	}
	
	public Integer calculateRecoilDamage(ArrayList<ActiveSkill> attacks, Boolean isDodged) {
		Monster attacker = attacks.iterator().next().getUser();
		int tempDamage = 0;
		for (ActiveSkill attack : attacks) {
			if (SkillKeyword.RECOIL == attack.getCard().getSkillKeyword()) {
				tempDamage += attack.getCard().getKeywordValue();
			}
			else if (SkillKeyword.RECOIL_DGE == attack.getCard().getSkillKeyword() && isDodged) {
				tempDamage += attack.getCard().getKeywordValue();
			}
			else if (SkillKeyword.RECOIL_HIT == attack.getCard().getSkillKeyword() && !isDodged) {
				tempDamage += attack.getCard().getKeywordValue();
			}
		}
		if (tempDamage == 0) return attacker.getTempDamage();
		tempDamage = null==attacker.getTempDamage() ? tempDamage : tempDamage + attacker.getTempDamage();
		return tempDamage;
	}
	
}
