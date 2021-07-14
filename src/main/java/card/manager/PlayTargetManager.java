package card.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import card.enums.GamePhase;
import card.enums.MonsterBreed;
import card.enums.MonsterStatus;
import card.enums.MonsterType;
import card.enums.SkillKeyword;
import card.enums.SkillType;
import card.enums.TargetArea;
import card.model.cards.SkillCard;
import card.model.game.ActiveSkill;
import card.model.game.GameState;
import card.model.game.Monster;
import card.model.game.PlayerArea;
import card.model.game.SkillArea;
import card.model.requests.PlayersRequest;
import card.util.KeywordUtil;

@Component
public class PlayTargetManager {
	
	public ArrayList<Integer> findPlayableTargets(PlayersRequest playersRequest, GameState gameState) {
		ArrayList<Integer> playableTargets = new ArrayList<Integer>();
		PlayerArea playerArea = gameState.getPlayerArea(playersRequest.getPlayer1());
		PlayerArea opponentArea = gameState.getPlayerArea(playersRequest.getPlayer2());
		if (GamePhase.ATTACK == gameState.getPhase()) {
			playableTargets = findPlayableAttackTargets(playerArea, opponentArea, gameState.getSkillArea());
		}
		else if (GamePhase.DEFENSE == gameState.getPhase()) {
			playableTargets = findPlayableDefenseTargets(playerArea, gameState.getSkillArea());
		}
		return playableTargets;
	}
	
	public ArrayList<Integer> findPlayableAttackTargets(PlayerArea playerArea, PlayerArea opponentArea, SkillArea skillArea) {
		if (skillArea.isResolved()) {
			return new ArrayList<>();
		}
		else if (TargetArea.SELF == skillArea.getTargetArea()) {
			return new ArrayList<>(Arrays.asList(-1));
		}
		else if (TargetArea.HAND == skillArea.getTargetArea()) { 
			return findCardTargets(playerArea.getHand(), skillArea);
		}
		else if (TargetArea.DISCARD == skillArea.getTargetArea()) {
			return findCardTargets(playerArea.getDiscards(), skillArea);
		}
		else if (TargetArea.ALLY == skillArea.getTargetArea()) {
			return findMonsterTargets(playerArea.getMonsters(), skillArea, false);
		}
		else if (TargetArea.ENEMY == skillArea.getTargetArea()) {
			return findMonsterTargets(opponentArea.getMonsters(), skillArea, true);
		}
		else if (TargetArea.SUBMONS == skillArea.getTargetArea()) {
			return findMonsterTargets(playerArea.getSubMonsters(), skillArea, true);
		}
		return new ArrayList<>();
	}
	
	public ArrayList<Integer> findPlayableDefenseTargets(PlayerArea playerArea, SkillArea skillArea) {
		ArrayList<Integer> playableTargets = new ArrayList<>();
		ArrayList<ActiveSkill> defenses = skillArea.getDefenses();
		Integer defenseIndex = defenses.size() - 1;
		if (defenseIndex >= 0) {
			ActiveSkill lastPlayedDefense = defenses.get(defenseIndex);
			if (TargetArea.ALLY == lastPlayedDefense.card.targetArea && null == lastPlayedDefense.getTarget()) {
				ArrayList<Monster> otherMonsters = new ArrayList<>();
				otherMonsters.addAll(playerArea.getMonsters());
				int index = otherMonsters.indexOf(lastPlayedDefense.getUser());
				otherMonsters.set(index, null);
				int targetIndex = -1;
				for (Monster otherMonster : otherMonsters) {
					targetIndex++;
					if (otherMonster == null) continue;
					else if (!otherMonster.isAlive()) continue;
					playableTargets.add(targetIndex);
				}
			}
		}
		return playableTargets;
	}
	
	public ArrayList<Integer> findCardTargets(List<SkillCard> cards, SkillArea skillArea) {
		ArrayList<Integer> playableTargets = new ArrayList<Integer>();
		Integer targetIndex = -1;
		for (SkillCard card : cards) {
			targetIndex++;
			if (null == card) continue;
			if (skillArea.allAttackKeywords().contains(SkillKeyword.DODGE_PROGRAM) && SkillType.DGE != card.getSkillType()) continue;
			playableTargets.add(targetIndex);
		}
		return playableTargets;
	}
	
	public ArrayList<Integer> findMonsterTargets(ArrayList<Monster> monsters, SkillArea skillArea, Boolean enemyTarget) {
		ArrayList<Integer> playableTargets = new ArrayList<Integer>();
		ArrayList<SkillKeyword> attackKeywords = skillArea.allAttackKeywords();
		ArrayList<SkillType> attackTypes = skillArea.allAttackTypes();
		// combo on the stack is non-terminal
		if (attackKeywords.contains(SkillKeyword.SUPPORT) && !attackKeywords.contains(SkillKeyword.HELP)) return playableTargets;
		else if (!Collections.disjoint(attackKeywords, KeywordUtil.COMBO_POW) && !attackTypes.contains(SkillType.POW)) return playableTargets;
		else if (!Collections.disjoint(attackKeywords, KeywordUtil.COMBO_POW_INT)) {
			if (!attackTypes.contains(SkillType.POW) && !attackTypes.contains(SkillType.INT)) return playableTargets;
		}	
		Integer targetIndex = -1;
		// find target-ability by monster
		if (monsters != null)
		{
			for (Monster monster : monsters) {
				targetIndex++;
				if (monster == null) continue;
				else if (attackKeywords.contains(SkillKeyword.REBORN) || attackKeywords.contains(SkillKeyword.TAKE_OVER)) {
					if (monster.isAlive()) continue;
				}
				else if (!monster.isAlive()) continue;
				else if (monster.getStatusDuration().containsKey(MonsterStatus.UNTARGETABLE) && enemyTarget) continue;
				else if (attackKeywords.contains(SkillKeyword.RESTORE) && monster.getCurrentLife() == monster.getMaxLife()) continue;
				else if (attackKeywords.contains(SkillKeyword.NUTS_OIL) && monster.getCanAttack()) continue;
				else if (attackKeywords.contains(SkillKeyword.TARGET_GRD) && MonsterType.GRD != monster.getMonsterType()) continue;
				playableTargets.add(targetIndex);
			}
		}
		return playableTargets;
	}
	
}
