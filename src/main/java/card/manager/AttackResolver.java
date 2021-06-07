package card.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import card.enums.MonsterStatus;
import card.enums.MonsterType;
import card.enums.SkillKeyword;
import card.enums.SkillType;
import card.model.cards.SkillCard;
import card.model.game.ActiveSkill;
import card.model.game.Expression;
import card.model.game.GameState;
import card.model.game.Monster;
import card.model.game.PlayerArea;
import card.model.game.SkillArea;
import card.model.requests.PlayersRequest;
import card.rules.AttackEffectsEngine;
import card.util.CardUtil;

@Component
public class AttackResolver {
	
	private AttackEffectsEngine attackEffectsEngine = new AttackEffectsEngine();
	
	public void resolveAttack(PlayersRequest playersRequest, GameState gameState) {
		SkillArea skillArea = gameState.getSkillArea();
		PlayerArea attackerArea = gameState.getPlayerArea(playersRequest.getPlayer1());
		PlayerArea defenderArea = gameState.getPlayerArea(playersRequest.getPlayer2());
		
		// Apply damage to defenders
		Integer damageDealt = 0;
		for (Monster monster : defenderArea.getMonsters()) {
			if (null != monster.getTempDamage()) {
				monster.setCurrentLife(monster.getCurrentLife() - monster.getTempDamage());
				damageDealt += monster.getTempDamage();
			}
			monster.setTempDamage(null);
		}
		
		// Apply damage to attackers
		for (Monster monster : attackerArea.getMonsters()) {
			if (null != monster.getTempDamage()) {
				monster.setCurrentLife(monster.getCurrentLife() - monster.getTempDamage());
			}
			monster.setTempDamage(null);
		}
	    
		// Apply defense skill effects
		for (ActiveSkill defenseSkill : skillArea.getDefenses()) {
			Monster defender = defenseSkill.getUser();
			if (SkillKeyword.DODGE == defenseSkill.getCard().getSkillKeyword()) {
				defenderArea.getBreeder().setGuts(0);
			}
			else if (SkillKeyword.FLY_AWAY == defenseSkill.getCard().getSkillKeyword()) {
				defender.addStatusDuration(MonsterStatus.DMGx2, 1);
			}
			else if (SkillKeyword.JUMP == defenseSkill.getCard().getSkillKeyword()) {
				defender.addStatusDuration(MonsterStatus.AIRBORNE, 1);
				defender.addStatusDuration(MonsterStatus.POWx2, 1);
			}
			else if (SkillKeyword.JUMP_IN == defenseSkill.getCard().getSkillKeyword()) {
				defender.addStatusDuration(MonsterStatus.POWx3, 1);
			}
			else if (SkillKeyword.SOAR == defenseSkill.getCard().getSkillKeyword()) {
				defender.addStatusDuration(MonsterStatus.POWx2, 1);
			}
		}
		
		// Apply after effects for each attack skill
		for (ActiveSkill attackSkill : skillArea.getAttacks()) {
			Expression expression = new Expression(gameState, attackerArea, defenderArea, attackSkill, damageDealt);
			attackEffectsEngine.applyEffects(expression);
		}
		
		// Play environment cards
		ArrayList<SkillKeyword> defenseKeywords = skillArea.getDefenses().stream()
				.map(ActiveSkill::getCard)
				.map(SkillCard::getSkillKeyword)
				.collect(Collectors.toCollection(ArrayList::new));
		if (!defenseKeywords.contains(SkillKeyword.PRAY)) {
			for (ActiveSkill attackSkill : skillArea.getAttacks()) {
				if (SkillType.ENV == attackSkill.getCard().getSkillType()) {
					resolveEnvironment(playersRequest, gameState, attackSkill.getCard());
				}
			}
		}

	}

	public void resolveEnvironment(PlayersRequest playersRequest, GameState gameState, SkillCard newEnvCard) {
		PlayerArea attackerArea = gameState.getPlayerArea(playersRequest.getPlayer1());
		// remove the current ENV card and update
		SkillCard oldEnvCard = gameState.getEnvironmentCard();
		if (SkillKeyword.AWE == oldEnvCard.getSkillKeyword()) {
			revertGutsCosts(gameState);
		}
		else if (SkillKeyword.BAD_WEATHER == oldEnvCard.getSkillKeyword()) {
			revertMonsterType(gameState);
		}
		if (SkillKeyword.ENCOURAGE == oldEnvCard.getSkillKeyword()) {
			revertGutsCosts(gameState);
		}
		else if (SkillKeyword.EXCITED == oldEnvCard.getSkillKeyword()) {
			revertDamageType(gameState, SkillType.INT);
		}
		else if (SkillKeyword.HOT_BATTLE == oldEnvCard.getSkillKeyword()) {
			revertDamageType(gameState, SkillType.POW);
			revertDamageType(gameState, SkillType.INT);
		}
		else if (SkillKeyword.SPARK == oldEnvCard.getSkillKeyword()) {
			revertDamageType(gameState, SkillType.INT);
		}
		else if (SkillKeyword.WARP == oldEnvCard.getSkillKeyword()) {
			revertMonsterType(gameState);
		}
		
		// add the new ENV card and update
		gameState.setEnvironmentCard(newEnvCard);
		if (SkillKeyword.AWE == newEnvCard.getSkillKeyword()) {
			modifyGutsCosts(gameState, 1);
		}
		else if (SkillKeyword.BAD_WEATHER == newEnvCard.getSkillKeyword()) {
			modifyMonsterType(gameState, MonsterType.GRD);
		}
		else if (SkillKeyword.COOL_JUDGE == newEnvCard.getSkillKeyword()) {
			CardUtil.draw(attackerArea.getDeck().getSkillCards(), attackerArea.getHand());
		}
		else if (SkillKeyword.ENCOURAGE == newEnvCard.getSkillKeyword()) {
			modifyGutsCosts(gameState, -1);
		}
		else if (SkillKeyword.EXCITED == newEnvCard.getSkillKeyword()) {
			modifyDamageType(gameState, -2, SkillType.INT);
		}
		else if (SkillKeyword.HOT_BATTLE == newEnvCard.getSkillKeyword()) {
			modifyDamageType(gameState, 1, SkillType.POW);
			modifyDamageType(gameState, 1, SkillType.INT);
		}
		else if (SkillKeyword.SPARK == newEnvCard.getSkillKeyword()) {
			modifyDamageType(gameState, 2, SkillType.INT);
		}
		else if (SkillKeyword.WARP == newEnvCard.getSkillKeyword()) {
			modifyMonsterType(gameState, MonsterType.AIR);
		}
	}
	
	public void modifyGutsCosts(GameState gameState, Integer mod) {
		ArrayList<SkillCard> skillCards = getAllSkillCards(gameState);
		for (SkillCard skillCard : skillCards) {
			skillCard.setGutsCost(skillCard.getGutsCost() + mod);
		}
	}
	
	public void revertGutsCosts(GameState gameState) {
		ArrayList<SkillCard> skillCards = getAllSkillCards(gameState);
		for (SkillCard skillCard : skillCards) {
			skillCard.setGutsCost(skillCard.getBaseGutsCost());
		}
	}
	
	public void modifyDamageType(GameState gameState, Integer mod, SkillType type) {
		ArrayList<SkillCard> skillCards = getAllSkillCards(gameState);
		for (SkillCard skillCard : skillCards) {
			if (type == skillCard.getSkillType()) {
				skillCard.setDamage(skillCard.getDamage() + mod);
			}
		}
	}
	
	public void revertDamageType(GameState gameState,  SkillType type) {
		ArrayList<SkillCard> skillCards = getAllSkillCards(gameState);
		for (SkillCard skillCard : skillCards) {
			if (type == skillCard.getSkillType()) {
				skillCard.setDamage(skillCard.getBaseDamage());
			}
		}
	}
	
	public ArrayList<SkillCard> getAllSkillCards(GameState gameState) {
		Collection<PlayerArea> playerAreas = gameState.getPlayerArea().values();
        PlayerArea player1Area = playerAreas.iterator().next();
        PlayerArea player2Area = playerAreas.iterator().next();
        SkillArea skillArea = gameState.getSkillArea();
		ArrayList<SkillCard> attackCards = skillArea.getAttacks().stream()
				.map(ActiveSkill::getCard)
				.collect(Collectors.toCollection(ArrayList::new));
		ArrayList<SkillCard> defenseCards = skillArea.getDefenses().stream()
				.map(ActiveSkill::getCard)
				.collect(Collectors.toCollection(ArrayList::new));
        ArrayList<SkillCard> allSkillCards = new ArrayList<>();
        allSkillCards.addAll(player1Area.getDeck().getSkillCards());
        allSkillCards.addAll(player1Area.getHand());
        allSkillCards.addAll(player1Area.getDiscards());
        allSkillCards.addAll(player2Area.getDeck().getSkillCards());
        allSkillCards.addAll(player2Area.getHand());
        allSkillCards.addAll(player2Area.getDiscards());
        allSkillCards.addAll(attackCards);
        allSkillCards.addAll(defenseCards);
        return allSkillCards;
	}
	
	public void modifyMonsterType(GameState gameState, MonsterType monsterType) {
		ArrayList<Monster> monsters = getAllMonsters(gameState);
		for (Monster monster : monsters) {
			monster.setMonsterType(monsterType);
		}
	}
	
	public void revertMonsterType(GameState gameState) {
		ArrayList<Monster> monsters = getAllMonsters(gameState);
		for (Monster monster : monsters) {
			monster.setMonsterType(monster.getBaseMonsterType());
		}
	}
	public ArrayList<Monster> getAllMonsters(GameState gameState) {
		Collection<PlayerArea> playerAreas = gameState.getPlayerArea().values();
        PlayerArea player1Area = playerAreas.iterator().next();
        PlayerArea player2Area = playerAreas.iterator().next();
        ArrayList<Monster> allMonsters = new ArrayList<>();
        allMonsters.addAll(player1Area.getMonsters());
        allMonsters.addAll(player2Area.getMonsters());
        return allMonsters;
	}
	
}
