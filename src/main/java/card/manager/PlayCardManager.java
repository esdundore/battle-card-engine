package card.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import card.dao.CardCache;
import card.enums.GamePhase;
import card.enums.MonsterBreed;
import card.enums.MonsterStatus;
import card.enums.SkillKeyword;
import card.enums.SkillType;
import card.enums.TargetArea;
import card.model.cards.SkillCard;
import card.model.game.ActiveSkill;
import card.model.game.Breeder;
import card.model.game.GameState;
import card.model.game.Monster;
import card.model.game.PlayerArea;
import card.model.game.SkillArea;
import card.model.requests.PlayersRequest;
import card.model.view.PlayableCard;
import card.util.KeywordUtil;

@Component
public class PlayCardManager {
	
	@Autowired
	PlayTargetManager playTargetManager;
	
	public ArrayList<PlayableCard> findPlayableCards(PlayersRequest playersRequest, GameState gameState, boolean lastDefenseResolved) {
		ArrayList<PlayableCard> playableCards = new ArrayList<PlayableCard>();
		PlayerArea playerArea = gameState.getPlayerArea(playersRequest.getPlayer1());
		Integer handIndex = -1;
		for (SkillCard skillCard : playerArea.getHand()) {
			handIndex++;
			if (null == skillCard) {
				// no card here
				playableCards.add(null);
				continue;
			}
			PlayableCard playableCard = new PlayableCard();
			playableCard.setHandIndex(handIndex);
			if (GamePhase.GUTS == gameState.getPhase()) {
				if (playerArea.getBreeder().getGutsMade() >= 2) {
					if (gameState.getTurnCount() == 0 || SkillKeyword.LONG_BATTLE.equals(gameState.getEnvironmentCard().getSkillKeyword())) {
						playableCards.add(null);
						continue;
					}
				}
				// any card can become guts - breeder is the user
				playableCard.setUsers(new ArrayList<>());
				playableCard.getUsers().add(playerArea.getMonsters().size());
				playableCards.add(playableCard);
			}
			else {
				// find which cards are playable, null otherwise
				playableCards.add(findPlayableSkill(playersRequest, gameState, skillCard, playableCard, lastDefenseResolved));
			}
		}
		// remove null cards
		playableCards.removeAll(Collections.singleton(null));
		return playableCards;
	}
	
	public PlayableCard findPlayableSkill(PlayersRequest playersRequest, GameState gameState, SkillCard skillCard, PlayableCard playableCard, boolean lastDefenseResolved) {
		PlayerArea playerArea = gameState.getPlayerArea(playersRequest.getPlayer1());
		SkillArea skillArea = gameState.getSkillArea();
		SkillKeyword envKeyword = gameState.getEnvironmentCard().getSkillKeyword();
		
		// Can combo Ghost "Hit Card" with any number of cards.
		if (GamePhase.ATTACK == gameState.getPhase() && skillArea.getBaseAttack() != null) {
			if (SkillKeyword.HIT_CARD == skillArea.getBaseAttack().getCard().getSkillKeyword()) {
				Integer attackerIndex = playerArea.getMonsters().indexOf(skillArea.getAttackMonster());
				playableCard.setUsers(new ArrayList<>(Arrays.asList(attackerIndex)));
				return playableCard;
			}
			if (SkillKeyword.COPY_CAT == skillArea.getBaseAttack().getCard().getSkillKeyword()) {
				Integer attackerIndex = playerArea.getMonsters().indexOf(skillArea.getAttackMonster());
				playableCard.setUsers(new ArrayList<>(Arrays.asList(attackerIndex)));
				if (SkillType.POW == skillCard.getSkillType() && SkillKeyword.ENSNARE != envKeyword) {
					return playableCard;
				}
				else {
					return null;
				}
			}
		}
		
		// player does not have required guts to use
		if ((playerArea.getBreeder().getGutsSpent() + skillCard.getGutsCost()) > playerArea.getBreeder().getGuts()) {
			return null;
		}
		
		// find the users for this skill
		Boolean purebreedAttack = true;
		if (null != skillArea.getAttackMonster()) {
			purebreedAttack = skillArea.getAttackMonster().isPurebreed();
		}
		playableCard.setUsers(findUsers(skillCard, playerArea.getMonsters(), playerArea.getBreeder(), envKeyword, purebreedAttack));
		
		// no valid users
		if (playableCard.getUsers().isEmpty()) {
			return null;
		}
		
		if (GamePhase.ATTACK == gameState.getPhase() && skillCard.determineAttack()) {
			if (skillArea.isResolved()) {
				Boolean alreadyAttacked = false;
				if (skillArea.getAttacker() == playersRequest.getPlayer1()) {
					alreadyAttacked = true;
				}
				return findPlayableAttack(playersRequest, gameState, skillCard, playableCard, alreadyAttacked);
			}
			else {
				// subsequent attacks must be able to combo
				Monster attacker = skillArea.getAttackMonster();
				Integer attackerIndex = playerArea.getMonsters().indexOf(attacker);
				if (attackerIndex == -1) attackerIndex = playerArea.getMonsters().size();
				return findPlayableCombo(playersRequest, gameState, skillCard, playableCard, attackerIndex);
			}
			
		}
		else if (GamePhase.DEFENSE == gameState.getPhase() && !skillCard.determineAttack()) {
			if (lastDefenseResolved) return findPlayableDefense(skillArea, skillCard, playableCard);
		}
		
		// cannot play this skill on this phase
		return null;
	}
	
	public ArrayList<Integer> findUsers(SkillCard skillCard, ArrayList<Monster> monsters, Breeder breeder, SkillKeyword envKeyword, Boolean purebreedAttack) {
		// make a list of all entities that can use skills
		ArrayList<Monster> users = new ArrayList<Monster>();
		// add a user for each monster 
		for (Monster monster : monsters) users.add(monster);
		// add a user to represent the breeder
		Monster monster = breeder.createTempMonster();
		users.add(monster);

		// find the users for the skillCard
		ArrayList<Integer> userIndexes = new ArrayList<>();
		for (int i = 0; i < users.size(); i++) {
			if (canUseSkill(users.get(i), skillCard, envKeyword, purebreedAttack)) userIndexes.add(i);
		}
		return userIndexes;
	}
	
	public boolean canUseSkill(Monster monster, SkillCard skillCard, SkillKeyword envKeyword, Boolean purebreedAttack) {
		// can't use because monster lineage doesn't match the skill card user
		MonsterBreed lineage = skillCard.determineAttack() ? monster.getMainLineage() : monster.getSubLineage();
		if (MonsterBreed.Breeder == lineage && lineage != skillCard.getUserBreed()) return false;
		if (MonsterBreed.Any != skillCard.getUserBreed() && lineage != skillCard.getUserBreed()) return false;
		// can't use because that monster is dead
		if (!monster.isAlive()) return false;
		// can't use because that monster is cocooned
		if (monster.getStatusDuration().containsKey(MonsterStatus.COCOON)) {
			if (SkillKeyword.EMERGE != skillCard.getSkillKeyword()) return false;
		}
		
		if (SkillType.BLK == skillCard.getSkillType()) {
			if (monster.getStatusDuration().containsKey(MonsterStatus.NOBLOCK)) return false;
			if (monster.getStatusDuration().containsKey(MonsterStatus.GRIT)) {
				if (SkillKeyword.GRIT == skillCard.getSkillKeyword()) return false;
			}
			if (monster.getTempDamage() == null) {
				if (KeywordUtil.UTIL_DEFENSE.contains(skillCard.getSkillKeyword())) return true;
				else return false;
			}
		}
		else if (SkillType.DGE == skillCard.getSkillType()) {
			if (monster.getStatusDuration().containsKey(MonsterStatus.NODODGE)) return false;
			if (SkillKeyword.PRIDE == envKeyword) {
				if (purebreedAttack && !monster.isPurebreed()) return false;
			}
			if (monster.getTempDamage() == null) return false;
		}
		else {
			if (monster.getStatusDuration().containsKey(MonsterStatus.STUNNED)) return false;
			if (monster.getStatusDuration().containsKey(MonsterStatus.MULTI) && SkillKeyword.MULTI == skillCard.getSkillKeyword()) return true;
			if (!monster.getStatusDuration().containsKey(MonsterStatus.BERSERK) && !monster.getCanAttack()) return false;
			if (SkillType.SPE == skillCard.getSkillType() && SkillKeyword.ENSNARE == envKeyword) return false;
			if (SkillKeyword.LIFE_THRESHOLD == skillCard.getSkillKeyword() || SkillKeyword.WILL_POWER == skillCard.getSkillKeyword()) {
				if (monster.getCurrentLife() > 2) return false;
			}
		}

		return true;
	}
	
	public PlayableCard findPlayableAttack(PlayersRequest playersRequest, GameState gameState, SkillCard skillCard, PlayableCard playableCard, Boolean alreadyAttacked) {
		PlayerArea playerArea = gameState.getPlayerArea(playersRequest.getPlayer1());
		if (SkillKeyword.GRAND_END == skillCard.getSkillKeyword() && playerArea.getDeck().getSkillCards().size() > 0) return null;
		else if (SkillKeyword.TWINKLING == skillCard.getSkillKeyword() || SkillKeyword.LAST_MAN == skillCard.getSkillKeyword()) {
			Integer monstersAlive = playerArea.getMonsters().stream().filter(m -> m.isAlive()).collect(Collectors.toList()).size();
			if (monstersAlive > 1) return null;
		}
		else if (SkillKeyword.WILD_RUSH == skillCard.getSkillKeyword() && alreadyAttacked) return null;
		else if (SkillKeyword.COMBO_DRAGON == skillCard.getSkillKeyword()) {
			Boolean hasBite = false;
			Boolean hasTailAttack = false;
			for (SkillCard handCard : playerArea.getHand()) {
				if (CardCache.DRAGON_BITE.equals(handCard.getName())) hasBite = true;
				if (CardCache.DRAGON_TAIL_ATTACK.equals(handCard.getName())) hasTailAttack = true;
			}
			if (!hasBite || !hasTailAttack) return null;
		}
		
		
		if (KeywordUtil.COMBO.contains(skillCard.getSkillKeyword())) {
			// Must be able to combo with an attack in hand
			if (!findTargetCombo(playersRequest, gameState, skillCard, playableCard, false)) return null;
		}
		else {
			// Must be able to target something
			if (!findTargetCombo(playersRequest, gameState, skillCard, playableCard, true)) return null;
		}
		return playableCard;
	}
	
	public PlayableCard findPlayableCombo(PlayersRequest playersRequest, GameState gameState, SkillCard skillCard, PlayableCard playableCard, Integer attackerIndex) {
		SkillArea skillArea = gameState.getSkillArea();
		ArrayList<SkillKeyword> attackKeywords = skillArea.allAttackKeywords();

		// Cannot combo with skill if the attacker cannot use
		if (!playableCard.getUsers().contains(attackerIndex)) return null;
		else playableCard.setUsers(new ArrayList<>(Arrays.asList(attackerIndex)));
		
		if (SkillKeyword.COMBO_TIGER == skillCard.getSkillKeyword()) {
			if (attackKeywords.contains(SkillKeyword.COMBO_TIGER) &&
				!skillArea.allAttackNames().contains(skillCard.getName())) {
				return playableCard;
			}
		}
		else if (SkillKeyword.COMBO_HENGER == skillCard.getSkillKeyword()) {
			Integer comboHengerCount = attackKeywords.stream().filter(k -> k == (SkillKeyword.COMBO_HENGER)).collect(Collectors.toList()).size();
			if (comboHengerCount == 1) return playableCard;
		}
		else if (SkillKeyword.COMBO_WORM == skillCard.getSkillKeyword()) {
			Integer comboWormCount = attackKeywords.stream().filter(k -> k == (SkillKeyword.COMBO_WORM)).collect(Collectors.toList()).size();
			if (comboWormCount == 1) return playableCard;
		}
		else if (KeywordUtil.COMBO.contains(skillCard.getSkillKeyword())) {
			ArrayList<SkillType> attackTypes = skillArea.allAttackTypes();
			Integer totalGutsCost = skillArea.allAttackCards().stream().map(SkillCard::getGutsCost).reduce(0, Integer::sum);
			if (SkillKeyword.SUPPORT == skillCard.getSkillKeyword() 
					&& skillArea.allAttackBreeds().contains(MonsterBreed.Breeder) 
					&& totalGutsCost < 4) return playableCard;
			else if (KeywordUtil.COMBO_POW.contains(skillCard.getSkillKeyword()) && attackTypes.contains(SkillType.POW)) return playableCard;
			else if (KeywordUtil.COMBO_POW_INT.contains(skillCard.getSkillKeyword()) && 
					(attackTypes.contains(SkillType.POW) || attackTypes.contains(SkillType.INT))) return playableCard;
			else if (findTargetCombo(playersRequest, gameState, skillCard, playableCard, false)) return playableCard;
		}
		
		// Already contains "must combo" skills
		boolean comboWithBreeder = false;
		boolean comboWithPOW = true;
		boolean comboWithINT = true;
		for (SkillKeyword attackKeyword : attackKeywords) {
			if (SkillKeyword.SUPPORT == attackKeyword) comboWithBreeder = true;
			else if (KeywordUtil.COMBO_POW.contains(attackKeyword)) comboWithINT = false;
			else if (KeywordUtil.COMBO_POW_INT.contains(attackKeyword)) continue;
			else return null;
		}
		if (MonsterBreed.Breeder == skillCard.getUserBreed() && skillCard.getGutsCost() < 4 && comboWithBreeder) return playableCard;
		else if (SkillType.POW == skillCard.getSkillType() && comboWithPOW) return playableCard;
		else if (SkillType.INT == skillCard.getSkillType() && comboWithINT) return playableCard;

		return null;
	}
	
	public PlayableCard findPlayableDefense(SkillArea skillArea, SkillCard skillCard, PlayableCard playableCard) {
		ArrayList<SkillKeyword> attackKeywords = skillArea.allAttackKeywords();
		ArrayList<SkillType> attackTypes = skillArea.allAttackTypes();
		if (SkillKeyword.PRAY == skillCard.getSkillKeyword() && skillArea.allAttackTypes().contains(SkillType.ENV)) return playableCard;
		else if (TargetArea.ENEMY == skillArea.getTargetArea()) {
			if (attackKeywords.contains(SkillKeyword.CONCENTRATE) && skillCard.getGutsCost() == 0) return null;
			if (KeywordUtil.UTIL_DEFENSE.contains(skillCard.getSkillKeyword())) return playableCard;
			if (attackKeywords.contains(SkillKeyword.UNDODGABLE) || attackKeywords.contains(SkillKeyword.COMBO_UNDGE)) {
				if (SkillType.DGE == skillCard.getSkillType()) return null;
			}
			Integer attackCost = skillArea.allAttackCards().stream().map(SkillCard::getGutsCost).collect(Collectors.summingInt(Integer::intValue));
			if (SkillKeyword.DGE_GUTS == skillCard.getSkillKeyword() && skillCard.getKeywordValue() >= attackCost) return null;
			if (!attackTypes.contains(SkillType.POW) && KeywordUtil.POW_DEFENSE.contains(skillCard.getSkillKeyword())) return null;
			if (!attackTypes.contains(SkillType.INT) && KeywordUtil.INT_DEFENSE.contains(skillCard.getSkillKeyword())) return null;
			if (Collections.disjoint(attackKeywords, KeywordUtil.AOE) && SkillKeyword.RETREAT == skillCard.getSkillKeyword()) return null;
			return playableCard;
		}
		return null;
	}
	
	public boolean findTargetCombo(PlayersRequest playersRequest, GameState gameState, SkillCard skillCard, PlayableCard playableCard, boolean findTarget) {
		PlayerArea playerArea = gameState.getPlayerArea(playersRequest.getPlayer1());
		for (Integer user : playableCard.getUsers()) {
			Monster monster = new Monster();
			if (user >= playerArea.getMonsters().size()) {
				monster = playerArea.getBreeder().createTempMonster();
			}
			else {
				monster = playerArea.getMonsters().get(user);
			}
			ActiveSkill thisAttack = new ActiveSkill(skillCard, monster, null, playableCard.getHandIndex());
			GameState tempGameState = gameState.copy();
			tempGameState.getPlayerArea(playersRequest.getPlayer1()).getHand().set(playableCard.getHandIndex(), null);
			tempGameState.getSkillArea().getAttacks().add(thisAttack);
			if (tempGameState.getSkillArea().isResolved()) {
				tempGameState.getSkillArea().setAttacker(playersRequest.getPlayer1());
				tempGameState.getSkillArea().setDefender(playersRequest.getPlayer2());
				tempGameState.getSkillArea().setAttacks(new ArrayList<ActiveSkill>());
				tempGameState.getSkillArea().getAttacks().add(thisAttack);
				tempGameState.getSkillArea().setDefenses(new ArrayList<ActiveSkill>());
				tempGameState.getSkillArea().setResolved(false);
				tempGameState.getSkillArea().setTargetArea(skillCard.getTargetArea());
			}
			else if (TargetArea.SELF != tempGameState.getSkillArea().getTargetArea()) tempGameState.getSkillArea().setTargetArea(skillCard.getTargetArea());
			if (findTarget && !playTargetManager.findPlayableTargets(playersRequest, tempGameState).isEmpty()) return true;
			if (!findTarget && !findPlayableCards(playersRequest, tempGameState, true).isEmpty()) return true;
		}
		return false;
	}
	
}
