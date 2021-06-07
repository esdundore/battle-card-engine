package card.manager;

import java.util.ArrayList;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import card.dao.CardCache;
import card.enums.GamePhase;
import card.enums.MonsterBreed;
import card.enums.MonsterStatus;
import card.enums.MonsterType;
import card.enums.SkillKeyword;
import card.enums.TargetArea;
import card.model.cards.SkillCard;
import card.model.game.ActiveSkill;
import card.model.game.Breeder;
import card.model.game.GameState;
import card.model.game.Monster;
import card.model.game.PlayerArea;
import card.model.game.SkillArea;
import card.model.requests.SkillRequest;
import card.model.requests.TargetRequest;
import card.model.requests.PlayersRequest;
import card.util.CardUtil;

@Component
public class GameManager {
	
	@Autowired
	AttackResolver attackResolver;
	
	@Autowired
	DamageCalculator damageCalculator;
	
	@Autowired
	DecisionManager decisionManager;
	
	@Autowired
	CardCache cardCache;
	
	private static String AI_PLAYER = "AI";
	
	public void useSkill(SkillRequest skillRequest, GameState gameState) {
		SkillArea skillArea = gameState.getSkillArea();
		PlayerArea playerArea = gameState.getPlayerArea(skillRequest.getPlayer1());
		
		Integer cardIndex = skillRequest.getHandIndex();
		SkillCard card = playerArea.getHand(cardIndex);
		Monster user = playerArea.getMonsters().get(skillRequest.getUser());
		Breeder breeder = playerArea.getBreeder();
		
		// Create a new skill area or add to this existing skill area
		if (skillArea.isResolved()) skillArea.incrementSkillArea(skillRequest, card.getTargetArea());
		else if (TargetArea.SELF != skillArea.getTargetArea()) skillArea.setTargetArea(card.getTargetArea());
		ArrayList<ActiveSkill> activeSkills = skillArea.getAttacks();
		if (GamePhase.ATTACK == gameState.getPhase()) activeSkills = skillArea.getAttacks();
		else if (GamePhase.DEFENSE == gameState.getPhase()) activeSkills = skillArea.getDefenses();
		
		SkillCard baseAttackCard = new SkillCard();
		baseAttackCard.setSkillKeyword(SkillKeyword.NORMAL);
		if (skillArea.getBaseAttack() != null) baseAttackCard = skillArea.getBaseAttack().getCard();
		if (SkillKeyword.HIT_CARD != baseAttackCard.getSkillKeyword()) {
			breeder.setGutsSpent(breeder.getGutsSpent() + card.getGutsCost());
			if (SkillKeyword.POWER_OF_SUN == card.getSkillKeyword()) {
				breeder.setGutsSpent(breeder.getGutsSpent() - 2);
			}
			if (SkillKeyword.SUPPORT == card.getSkillKeyword()) {
				breeder.setGutsSpent(breeder.getGutsSpent() - 3);
			}
			CardUtil.playCard(playerArea.getHand(), user, null, cardIndex, activeSkills);
			resolvePlay(playerArea, card, user);
			// automatically declare target if self targeted skill
			if (TargetArea.SELF == card.getTargetArea()) {
				TargetRequest targetRequest = new TargetRequest(skillRequest);
				targetRequest.setTarget(playerArea.getMonsters().indexOf(user));
				if (GamePhase.ATTACK == gameState.getPhase()) declareAttackTarget(targetRequest, gameState);
				else if (GamePhase.DEFENSE == gameState.getPhase()) declareDefenseTarget(targetRequest, gameState);
			}
		}
		else {
			baseAttackCard.setDamage(baseAttackCard.getDamage() + 1);
			CardUtil.discardFromHand(playerArea.getHand(), cardIndex, playerArea.getDiscards());
		}
		autoResponse(skillRequest, gameState);
	}
	
	public void resolvePlay(PlayerArea playerArea, SkillCard card, Monster user) {
		// Remove the other cards on dragon combo
		if (SkillKeyword.COMBO_DRAGON == card.getSkillKeyword()) {
			Integer biteIndex = -1;
			Integer tailAttackIndex = -1;
			Integer handIndex = -1;
			for (SkillCard handCard : playerArea.getHand()) {
				handIndex++;
				if (CardCache.DRAGON_BITE.equals(handCard.getName())) biteIndex = handIndex;
				if (CardCache.DRAGON_TAIL_ATTACK.equals(handCard.getName())) tailAttackIndex = handIndex;
			}
			CardUtil.discardFromHand(playerArea.getHand(), biteIndex, playerArea.getDiscards());
			CardUtil.discardFromHand(playerArea.getHand(), tailAttackIndex, playerArea.getDiscards());
		}
		// Your other monsters become AIR.
		else if (SkillKeyword.BLOW_HOLE == card.getSkillKeyword()) {
			ArrayList<Monster> otherMonsters = new ArrayList<Monster>();
			otherMonsters.addAll(playerArea.getMonsters());
			otherMonsters.remove(user);
			for (Monster monster : otherMonsters) {
				monster.setMonsterType(MonsterType.AIR);
			}
		}
	}
	
	public void declareAttackTarget(TargetRequest targetRequest, GameState gameState) {
		SkillArea skillArea = gameState.getSkillArea();
		PlayerArea playerArea =  gameState.getPlayerArea(targetRequest.getPlayer1());
		PlayerArea opponentArea = gameState.getPlayerArea(targetRequest.getPlayer2());
		Breeder breeder = playerArea.getBreeder();
		
		// reduce guts (cannot spend negative guts)
		Integer gutsSpent = breeder.getGutsSpent() > 0 ? breeder.getGutsSpent() : 0;
		breeder.setGuts(breeder.getGuts() - gutsSpent);
		breeder.setGutsSpent(0);
		
		// apply target to all attacks and set can attack to false
		for(ActiveSkill attack : skillArea.getAttacks()) {
			attack.setTarget(targetRequest.getTarget());
			attack.getUser().setCanAttack(false);
			if (MonsterBreed.Breeder == attack.getUser().getMainLineage()) {
				breeder.setCanAttack(false);
			}
		}
		
		// find targets and calculate damages
		if (TargetArea.ENEMY == skillArea.getTargetArea()) {
			damageCalculator.calculateDamages(skillArea, playerArea, opponentArea, gameState.getEnvironmentCard());
		}
		
		// go to next phase
		gameState.setCurrentPlayer(targetRequest.getPlayer2());
		gameState.setPhase(GamePhase.DEFENSE);
		autoResponse(targetRequest, gameState);
	}
	
	public void endAttack(PlayersRequest playersRequest, GameState gameState) {
		// All monsters on attacking team lose 1 life point at the end of the attack phase.
		if (SkillKeyword.ARID_LAND == gameState.getEnvironmentCard().getSkillKeyword()) {
			for (Monster monster : gameState.getPlayerArea(playersRequest.getPlayer1()).getMonsters()) {
				monster.setCurrentLife(monster.getCurrentLife() - 1);
			}
		}
		// go to next phase
		gameState.setPhase(GamePhase.GUTS);
		autoResponse(playersRequest, gameState);
	}
	
	public void declareDefenseTarget(TargetRequest targetRequest, GameState gameState) {
		SkillArea skillArea = gameState.getSkillArea();
		PlayerArea playerArea =  gameState.getPlayerArea(targetRequest.getPlayer1());
		PlayerArea opponentArea = gameState.getPlayerArea(targetRequest.getPlayer2());
		
		// apply target to the last played defense
		ArrayList<ActiveSkill> defenses = skillArea.getDefenses();
		Integer lastDefenseIndex = defenses.size() - 1;
		defenses.get(lastDefenseIndex).setTarget(targetRequest.getTarget());
		
		// recalculate damages
		damageCalculator.calculateDamages(skillArea, opponentArea, playerArea, gameState.getEnvironmentCard());
		autoResponse(targetRequest, gameState);
	}
	
	public void endDefense(PlayersRequest playersRequest, GameState gameState) {
		SkillArea skillArea = gameState.getSkillArea();
		PlayerArea playerArea = gameState.getPlayerArea(playersRequest.getPlayer1());
		PlayerArea opponentArea = gameState.getPlayerArea(playersRequest.getPlayer2());

		// resolve attack
		attackResolver.resolveAttack(playersRequest, gameState);
		gameState.getSkillArea().setResolved(true);
		
		// find winners; check the defenders monsters first
		if (playerArea.allMonstersDead()) {
			gameState.setWinner(playersRequest.getPlayer2());
		}
		else if (opponentArea.allMonstersDead()) {
			gameState.setWinner(playersRequest.getPlayer1());
		}
		
		// discard attack and defense cards from skill area
		Boolean endAttack = false;
		for (ActiveSkill attackSkill : skillArea.getAttacks()) {
			if (SkillKeyword.WILD_RUSH == attackSkill.getCard().getSkillKeyword()) {
				endAttack = true;
			}
			CardUtil.putOnTop(opponentArea.getDiscards(), attackSkill.getCard());
		}
		for (ActiveSkill defenseSkill : skillArea.getDefenses()) {
			CardUtil.putOnTop(playerArea.getDiscards(), defenseSkill.getCard());
		}
		
		if (endAttack) {
			PlayersRequest reversedPlayersRequest = new PlayersRequest();
			reversedPlayersRequest.setPlayer1(playersRequest.getPlayer2());
			reversedPlayersRequest.setPlayer2(playersRequest.getPlayer1());
			endAttack(reversedPlayersRequest, gameState);
		}
		else {
			gameState.setCurrentPlayer(playersRequest.getPlayer2());
			gameState.setPhase(GamePhase.ATTACK);
		}
		autoResponse(playersRequest, gameState);
	}
	
	public void makeGuts(SkillRequest skillRequest, GameState gameState) {
		PlayerArea playerArea = gameState.getPlayerArea(skillRequest.getPlayer1());
		Breeder breeder = playerArea.getBreeder();
		
		// discard cards and add guts
		CardUtil.discardFromHand(playerArea.getHand(), skillRequest.getHandIndex(), playerArea.getDiscards());
		breeder.setGuts(breeder.getGuts() + 1);
		breeder.setGutsMade(breeder.getGutsMade() + 1);
		
	}
	
	public void endTurn(PlayersRequest playersRequest, GameState gameState) {
		PlayerArea playerArea = gameState.getPlayerArea(playersRequest.getPlayer1());
		PlayerArea opponentArea = gameState.getPlayerArea(playersRequest.getPlayer2());
		Breeder breeder = playerArea.getBreeder();
		
		breeder.setGutsMade(0);
		// Players discard all cards in their hand after their GUTS phase.
		if (SkillKeyword.CLOSE_UP == gameState.getEnvironmentCard().getSkillKeyword()) {
			CardUtil.discardHand(playerArea.getHand(), playerArea.getDiscards());
		}
		
		// remove expired statuses from your monsters
		for (Monster monster : playerArea.getMonsters()) {
			ArrayList<MonsterStatus> expiredStatuses = new ArrayList<>();
			for (Map.Entry<MonsterStatus, Integer> statusDuration : monster.getStatusDuration().entrySet()) {
				statusDuration.setValue(statusDuration.getValue() - 1);
				if (statusDuration.getValue() < 1) {
					expiredStatuses.add(statusDuration.getKey());
				}
			}
			for (MonsterStatus expiredStatus : expiredStatuses) {
				monster.getStatusDuration().remove(expiredStatus);
			}
		}
		
		// remove expired statuses from breeder
		ArrayList<MonsterStatus> expiredStatuses = new ArrayList<>();
		for (Map.Entry<MonsterStatus, Integer> statusDuration : breeder.getStatusDuration().entrySet()) {
			statusDuration.setValue(statusDuration.getValue() - 1);
			if (statusDuration.getValue() < 1) {
				expiredStatuses.add(statusDuration.getKey());
			}
		}
		for (MonsterStatus expiredStatus : expiredStatuses) {
			breeder.getStatusDuration().remove(expiredStatus);
		}
		
		// set your monsters and breeder to canAttack
		for (Monster monster : playerArea.getMonsters()) {
			monster.setCanAttack(true);
		}
		breeder.setCanAttack(true);

		gameState.setTurnCount(gameState.getTurnCount() + 1);
		if (breeder.getStatusDuration().containsKey(MonsterStatus.EXTRA_TURN)) {
			gameState.setCurrentPlayer(playersRequest.getPlayer1());
			drawPhase(gameState, playerArea, playersRequest.getPlayer2());
		}
		else {
			gameState.setCurrentPlayer(playersRequest.getPlayer2());
			drawPhase(gameState, opponentArea, playersRequest.getPlayer1());
		}
		gameState.setPhase(GamePhase.ATTACK);
		autoResponse(playersRequest, gameState);
		
	}
	
	public void drawPhase(GameState gameState, PlayerArea playerArea, String opponent) {
		Breeder breeder = playerArea.getBreeder();
		
		// Discard two cards from the top of each player's deck on their draw phase.
		if (SkillKeyword.CLOSE_UP == gameState.getEnvironmentCard().getSkillKeyword()) {
			CardUtil.discardFromDeck(playerArea.getDeck().getSkillCards(), playerArea.getDiscards());
			CardUtil.discardFromDeck(playerArea.getDeck().getSkillCards(), playerArea.getDiscards());
		}
		
		// Draw from deck (not on the first turn)
		if (gameState.getTurnCount() > 1) {
			try {
				// Draw until full or discard the top card of your deck to make 1 GUTS
				if (!CardUtil.drawUntilFull(playerArea.getDeck().getSkillCards(), playerArea.getDiscards(), playerArea.getHand())) {
					breeder.setGuts(breeder.getGuts() + 1);
				}
			} catch (NoSuchElementException nsee) {
				// if you can't draw your opponent wins
				gameState.setWinner(opponent);
			}
		}
	}
	
	public void autoResponse(PlayersRequest playersRequest, GameState gameState) {
		if (AI_PLAYER.equals(gameState.getCurrentPlayer())) {
			if (AI_PLAYER.equals(playersRequest.getPlayer2())) {
				PlayersRequest aiRequest = new PlayersRequest();
				aiRequest.setPlayer1(playersRequest.getPlayer2());
				aiRequest.setPlayer2(playersRequest.getPlayer1());
				Thread newThread = new Thread(() -> {decisionManager.makeDecision(aiRequest, gameState);});
				newThread.start();
			}
			else if (AI_PLAYER.equals(playersRequest.getPlayer1())) {
				Thread newThread = new Thread(() -> {decisionManager.makeDecision(playersRequest, gameState);});
				newThread.start();
			}
		}
	}

}
