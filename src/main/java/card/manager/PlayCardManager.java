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
	
	public ArrayList<PlayableCard> findPlayableCards(PlayersRequest playersRequest, GameState gameState) {
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
				if (playerArea.getBreeder().getGutsMade() >= 2 && (gameState.getTurnCount() == 0 
						|| SkillKeyword.LONG_BATTLE.equals(gameState.getEnvironmentCard().getSkillKeyword()))) {
					playableCards.add(null);
					continue;
				}
				// any card can become guts - breeder is the user
				playableCard.setUsers(new ArrayList<>());
				playableCard.getUsers().add(playerArea.getMonsters().size());
				playableCards.add(playableCard);
			}
			else {
				// find which cards are playable, null otherwise
				playableCards.add(findPlayableSkill(playersRequest, gameState, skillCard, playableCard));
			}
		}
		// remove null cards
		playableCards.removeAll(Collections.singleton(null));
		return playableCards;
	}
	
	public PlayableCard findPlayableSkill(PlayersRequest playersRequest, GameState gameState, SkillCard skillCard, PlayableCard playableCard) {
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
		}
		
		// can't do anything this turn
		if (playerArea.getAttacksThisTurn() == -1) {
			return null;
		}
		// player does not have required guts to use
		if ((playerArea.getBreeder().getGutsSpent() + skillCard.getGutsCost()) > playerArea.getBreeder().getGuts()) {
			return null;
		}
		
		// find the users for this skill
		Boolean prideAttack = false;
		if (null != skillArea.getAttackMonster()) {
			prideAttack = skillArea.getAttackMonster().isPurebreed() && SkillKeyword.PRIDE == envKeyword;
		}
		if (skillArea.allAttackKeywords().contains(SkillKeyword.COPY_CAT) && !skillArea.isResolved()) {
			skillArea.getAttackMonster().addStatusDuration(MonsterStatus.COPY_CAT, 1);
		}
		playableCard.setUsers(findUsers(skillCard, playerArea.getMonsters(), playerArea.getBreeder(), prideAttack));
		if (null != skillArea.getAttackMonster()) {
			skillArea.getAttackMonster().getStatusDuration().remove(MonsterStatus.COPY_CAT);
		}
		if (playableCard.getUsers().isEmpty()) return null;
		
		if (GamePhase.ATTACK == gameState.getPhase() && skillCard.determineAttack()) {
			if (SkillType.SPE == skillCard.getSkillType() && SkillKeyword.ENSNARE == envKeyword) return null;
			else if (skillArea.isResolved()) {
				return findPlayableAttack(playersRequest, gameState, skillCard, playableCard);
			}
			else {
				// subsequent attacks must be able to combo, Cannot combo with skill if the attacker cannot use
				Integer attackerIndex = playerArea.getMonsters().indexOf(skillArea.getAttackMonster());
				if (attackerIndex == -1) attackerIndex = playerArea.getMonsters().size();
				if (!playableCard.getUsers().contains(attackerIndex)) return null;
				else playableCard.setUsers(new ArrayList<>(Arrays.asList(attackerIndex)));
				return findPlayableCombo(playersRequest, gameState, skillCard, playableCard);
			}
			
		}
		else if (GamePhase.DEFENSE == gameState.getPhase() && !skillCard.determineAttack()) {
			return isPlayableDefense(skillCard, skillArea) ? playableCard : null;
		}
		
		// cannot play this skill on this phase
		return null;
	}
	
	public ArrayList<Integer> findUsers(SkillCard skillCard, ArrayList<Monster> monsters, Breeder breeder, Boolean prideAttack) {
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
			if (canUseSkill(users.get(i), skillCard, prideAttack)) userIndexes.add(i);
		}
		return userIndexes;
	}
	
	public boolean canUseSkill(Monster monster, SkillCard skillCard, Boolean prideAttack) {
		MonsterBreed lineage = skillCard.determineAttack() ? monster.getMainLineage() : monster.getSubLineage();
		if (!monster.isAlive()) return false;
		if (lineage != skillCard.getUserBreed()) {
			if (MonsterBreed.Breeder == lineage) return false;
			else if (MonsterBreed.Any != skillCard.getUserBreed() 
					&& !monster.getStatusDuration().containsKey(MonsterStatus.COPY_CAT)) return false;
		}
		if (monster.getStatusDuration().containsKey(MonsterStatus.COCOON) 
				&& SkillKeyword.EMERGE != skillCard.getSkillKeyword()) return false;
		
		if (SkillType.BLK == skillCard.getSkillType()) {
			if (monster.getStatusDuration().containsKey(MonsterStatus.NOBLOCK)) return false;
			else if (SkillKeyword.GRIT == skillCard.getSkillKeyword() 
					&& monster.getStatusDuration().containsKey(MonsterStatus.GRIT)) return false;
			else if (!KeywordUtil.UTIL_DEFENSE.contains(skillCard.getSkillKeyword()) 
					&& monster.getTempDamage() == null) return false;
		}
		else if (SkillType.DGE == skillCard.getSkillType()) {
			if (monster.getStatusDuration().containsKey(MonsterStatus.NODODGE)) return false;
			else if (prideAttack && !monster.isPurebreed()) return false;
			else if (monster.getTempDamage() == null) return false;
		}
		else {
			if (monster.getStatusDuration().containsKey(MonsterStatus.STUNNED)) return false;
			else if ((SkillKeyword.LIFE_THRESHOLD == skillCard.getSkillKeyword() 
					|| SkillKeyword.WILL_POWER == skillCard.getSkillKeyword()) 
					&& monster.getCurrentLife() > 2) return false;
			else if (!monster.getCanAttack() && !monster.getStatusDuration().containsKey(MonsterStatus.BERSERK)) {
				if (monster.getStatusDuration().containsKey(MonsterStatus.MULTI) 
						&& SkillKeyword.MULTI != skillCard.getSkillKeyword()) return false;
				else if (!monster.getStatusDuration().containsKey(MonsterStatus.MULTI)) return false;
			}
			else if (monster.getStatusDuration().containsKey(MonsterStatus.COPY_CAT) 
					&& SkillType.POW != skillCard.getSkillType()) return false;
		}
		return true;
	}
	
	public PlayableCard findPlayableAttack(PlayersRequest playersRequest, GameState gameState, SkillCard skillCard, PlayableCard playableCard) {
		PlayerArea playerArea = gameState.getPlayerArea(playersRequest.getPlayer1());
		if (SkillKeyword.GRAND_END == skillCard.getSkillKeyword() && playerArea.getDeck().getSkillCards().size() > 0) return null;
		else if (SkillKeyword.TWINKLING == skillCard.getSkillKeyword() || SkillKeyword.LAST_MAN == skillCard.getSkillKeyword()) {
			Integer monstersAlive = playerArea.getMonsters().stream().filter(m -> m.isAlive()).collect(Collectors.toList()).size();
			if (monstersAlive > 1) return null;
		}
		else if (SkillKeyword.WILD_RUSH == skillCard.getSkillKeyword() 
				&& playerArea.getAttacksThisTurn() > 0) return null;
		else if (SkillKeyword.COMBO_DRAGON == skillCard.getSkillKeyword()) {
			ArrayList<String> cardNames = playerArea.getHand().stream().map(SkillCard::getName).collect(Collectors.toCollection(ArrayList::new));
			if (!cardNames.contains(CardCache.DRAGON_BITE) || !cardNames.contains(CardCache.DRAGON_TAIL_ATTACK)) return null;
		}
		else if (!canTargetOrCombo(playersRequest, gameState, skillCard, playableCard)) return null;
		return playableCard;
	}
	
	public PlayableCard findPlayableCombo(PlayersRequest playersRequest, GameState gameState, SkillCard skillCard, PlayableCard playableCard) {
		SkillArea skillArea = gameState.getSkillArea();
		
		// Check base card combos
		if (SkillKeyword.COMBO_TIGER == skillCard.getSkillKeyword()) {
			if (skillArea.allAttackKeywords().contains(SkillKeyword.COMBO_TIGER) &&
				!skillArea.allAttackNames().contains(skillCard.getName())) {
				return playableCard;
			}
		}
		else if (SkillKeyword.COMBO_HENGER == skillCard.getSkillKeyword()) {
			Integer comboHengerCount = skillArea.allAttackKeywords().stream().filter(k -> k == (SkillKeyword.COMBO_HENGER)).collect(Collectors.toList()).size();
			if (comboHengerCount == 1) return playableCard;
		}
		else if (SkillKeyword.COMBO_WORM == skillCard.getSkillKeyword()) {
			Integer comboWormCount = skillArea.allAttackKeywords().stream().filter(k -> k == (SkillKeyword.COMBO_WORM)).collect(Collectors.toList()).size();
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
			else if (canTargetOrCombo(playersRequest, gameState, skillCard, playableCard)) return playableCard;
		}
		
		// Already contains "must combo" skills
		boolean canCombo = true;
		for (SkillKeyword attackKeyword : skillArea.allAttackKeywords()) {
			if (!KeywordUtil.COMBO.contains(attackKeyword)) canCombo = false;
		}
		if (canCombo) {
			if (skillArea.allAttackKeywords().contains(SkillKeyword.SUPPORT)) {
				if (MonsterBreed.Breeder == skillCard.getUserBreed() && skillCard.getGutsCost() < 4) return playableCard;
			}
			else if (!KeywordUtil.COMBO_POW_INT.containsAll(skillArea.allAttackKeywords()) 
					&& SkillType.POW == skillCard.getSkillType()) return playableCard;
			else if (SkillType.INT == skillCard.getSkillType() 
					|| SkillType.POW == skillCard.getSkillType()) return playableCard;
		}

		return null;
	}
	
	public boolean isPlayableDefense(SkillCard defenseSkill, SkillArea skillArea) {
		if (SkillKeyword.PRAY == defenseSkill.getSkillKeyword()) {
			if (!skillArea.allAttackTypes().contains(SkillType.ENV)) return false;
			else return true;
		}
		else if (TargetArea.ENEMY == skillArea.getTargetArea()) {
			if (skillArea.allAttackKeywords().contains(SkillKeyword.CONCENTRATE) 
					&& defenseSkill.getGutsCost() == 0) return false;
			else if ((skillArea.allAttackKeywords().contains(SkillKeyword.UNDODGABLE) 
					|| skillArea.allAttackKeywords().contains(SkillKeyword.COMBO_UNDGE))
					&& SkillType.DGE == defenseSkill.getSkillType()) return false;
			else if (SkillKeyword.DGE_GUTS == defenseSkill.getSkillKeyword() 
					&& defenseSkill.getKeywordValue() >= skillArea.totalAttackCost()) return false;
			else if (!skillArea.allAttackTypes().contains(SkillType.POW) 
					&& KeywordUtil.POW_DEFENSE.contains(defenseSkill.getSkillKeyword())) return false;
			else if (!skillArea.allAttackTypes().contains(SkillType.INT) 
					&& KeywordUtil.INT_DEFENSE.contains(defenseSkill.getSkillKeyword())) return false;
			else if (SkillKeyword.RETREAT == defenseSkill.getSkillKeyword() 
					&& Collections.disjoint(skillArea.allAttackKeywords(), KeywordUtil.AOE)) return false;
			else return true;
		}
		return false;
	}
	
	public boolean canTargetOrCombo(PlayersRequest playersRequest, GameState gameState, SkillCard skillCard, PlayableCard playableCard) {
		PlayerArea playerArea = gameState.getPlayerArea(playersRequest.getPlayer1());
		for (Integer user : playableCard.getUsers()) {
			// create a temp game state
			GameState tempGameState = gameState.copy();
			// remove the card temp
			PlayerArea tempPlayerArea = tempGameState.getPlayerArea(playersRequest.getPlayer1());
			tempPlayerArea.getHand().set(playableCard.getHandIndex(), null);
			tempPlayerArea.getBreeder().setGutsSpent(tempPlayerArea.getBreeder().getGutsSpent() + skillCard.getGutsCost());
			// create a new skill area
			if (tempGameState.getSkillArea().isResolved()) {
				tempGameState.getSkillArea().clearSkillArea(playersRequest, skillCard.getTargetArea());
			}
			else if (TargetArea.SELF != tempGameState.getSkillArea().getTargetArea()) {
				tempGameState.getSkillArea().setTargetArea(skillCard.getTargetArea());
			}
			// add this attack as a skill
			Monster monster = new Monster();
			if (user >= playerArea.getMonsters().size()) monster = playerArea.getBreeder().createTempMonster();
			else monster = playerArea.getMonsters().get(user);
			tempGameState.getSkillArea().getAttacks().add(new ActiveSkill(skillCard, monster, null, playableCard.getHandIndex()));
			boolean mustCombo = KeywordUtil.COMBO.contains(skillCard.getSkillKeyword());
			if (!mustCombo && !playTargetManager.findPlayableTargets(playersRequest, tempGameState).isEmpty()) return true;
			else if (mustCombo && !findPlayableCards(playersRequest, tempGameState).isEmpty()) return true;
		}
		return false;
	}
	
}
