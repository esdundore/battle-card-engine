package card.manager;

import java.util.ArrayList;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import card.dao.GameCache;
import card.enums.GamePhase;
import card.enums.MonsterStatus;
import card.model.cards.SkillCard;
import card.model.game.ActiveSkill;
import card.model.game.GameState;
import card.model.game.Monster;
import card.model.game.PlayerArea;
import card.model.requests.SkillRequest;
import card.model.requests.TargetRequest;
import card.model.view.GameView;
import card.model.view.PlayableView;
import card.model.requests.GutsRequest;
import card.model.requests.PlayersRequest;
import card.util.CardUtil;

@Component("gameManager")
public class GameManager {
	
	@Autowired
	GameCache gameCache;
	
	@Autowired
	AttackResolver attackResolver;
	
	@Autowired
	ValidationManager validationManager;
	
	public GameView findGameView(PlayersRequest playersRequest) {
		return gameCache.getGameView(playersRequest);
	}
	
	public PlayableView findPlayables(PlayersRequest playersRequest) {
		return findPlayables(gameCache.getGameState(playersRequest), playersRequest);
	}
	
	public PlayableView findPlayables(GameState gameState, PlayersRequest playersRequest) {
		if (!validationManager.validGameState(playersRequest, gameState, gameState.getPhase())) {
			return null;
		}
		return validationManager.findPlayables(gameState, playersRequest);
	}
	
	public PlayableView useSkill(SkillRequest skillRequest, GamePhase phase) throws Exception {
		GameState gameState = gameCache.getGameState(skillRequest);
		if (!validationManager.validRequest(skillRequest, gameState, phase)) {
			throw new Exception();
		}
		
		PlayerArea playerArea = gameState.getPlayerArea(skillRequest.getPlayer1());
		// find the skill card from hand index
		SkillCard card = playerArea.getHand(skillRequest.getHandIndex());
		// add to the skill area
		if (GamePhase.ATTACK == phase) {
			if (gameState.getSkillArea().isResolved()) {
				gameState.getSkillArea().newAttack(skillRequest, card);
			}
			else {
				gameState.getSkillArea().addAttack(skillRequest, card);
			}
			attackResolver.calculateDamages(gameState.getSkillArea(), 3);
		}
		if (GamePhase.DEFENSE == phase) {
			gameState.getSkillArea().addDefense(skillRequest, card);
		}
		// subtract guts cost
		playerArea.setGuts(playerArea.getGuts() - card.getGutsCost());

		// find playable cards and targets
		return findPlayables(gameState, skillRequest);
	}
	
	public void declareAttackTarget(TargetRequest targetRequest) throws Exception {
		GameState gameState = gameCache.getGameState(targetRequest);
		if (!validationManager.validRequest(targetRequest, gameState, GamePhase.ATTACK)) {
			throw new Exception();
		}

		// apply target
		gameState.getSkillArea().addAttackTarget(targetRequest);
		
		// go to next phase
		gameState.setCurrentPlayer(targetRequest.getPlayer2());
		gameState.setPhase(GamePhase.DEFENSE);
	}
	
	public PlayableView declareDefenseTarget(TargetRequest targetRequest) throws Exception {
		GameState gameState = gameCache.getGameState(targetRequest);
		if (!validationManager.validRequest(targetRequest, gameState, GamePhase.DEFENSE)) {
			throw new Exception();
		}

		// apply target
		gameState.getSkillArea().addDefenseTarget(targetRequest);
		
		// find playable cards and targets
		return findPlayables(gameState, targetRequest);
	}
	
	public GameView endDefense(PlayersRequest playersRequest) throws Exception {
		GameState gameState = gameCache.getGameState(playersRequest);
		if (!validationManager.validGameState(playersRequest, gameState, GamePhase.DEFENSE)) {
			throw new Exception();
		}
		
		String player = playersRequest.getPlayer1();
		String opponent = playersRequest.getPlayer2();
		
		// resolve attack
		attackResolver.resolveAttack(gameState, playersRequest);
		
		// find winners
		if (gameState.getPlayerArea(player).allMonstersDead()) {
			declareWinner(gameState, opponent);
		}
		else if (gameState.getPlayerArea(opponent).allMonstersDead()) {
			declareWinner(gameState, player);
		}
		
		// discard attack and defense cards
		PlayerArea playerArea = gameState.getPlayerArea(player);
		PlayerArea opponentArea = gameState.getPlayerArea(opponent);
		for (ActiveSkill attackSkill : gameState.getSkillArea().getAttacks()) {
			CardUtil.discard(opponentArea.getHand(), opponentArea.getDiscard(), attackSkill.getHandIndex());
		}
		for (ActiveSkill defenseSkill : gameState.getSkillArea().getDefenses()) {
			CardUtil.discard(playerArea.getHand(), playerArea.getDiscard(), defenseSkill.handIndex);
		}
		
		// go to next phase
		gameState.setCurrentPlayer(playersRequest.getPlayer2());
		gameState.setPhase(GamePhase.ATTACK);
		return findGameView(playersRequest);
	}
	
	public GameView endTurn(GutsRequest gutsRequest) throws Exception {
		GameState gameState = gameCache.getGameState(gutsRequest);
		if (!validationManager.validGameState(gutsRequest, gameState, GamePhase.GUTS)) {
			throw new Exception();
		}
		
		// can only make 2 guts on the first phase
		if(gameState.getTurnCount() == 0 && gutsRequest.getDiscards().size() > 2) {
			throw new Exception();
		}
		
		// discard cards and add guts
		PlayerArea playerArea = gameState.getPlayerArea(gutsRequest.getPlayer1());
		for (Integer discard : gutsRequest.getDiscards()) {
			if (CardUtil.discard(playerArea.getHand(), playerArea.getDiscard(), discard)) {
				playerArea.setGuts(playerArea.getGuts() + 1);
			}
		}
		
		// remove statuses from your monsters
		for (Monster monster : gameState.getPlayerArea(gutsRequest.getPlayer1()).getMonsters()) {
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
		
		// set opponent monsters to canAttack
		for (Monster monster : gameState.getPlayerArea(gutsRequest.getPlayer2()).getMonsters()) {
			monster.setCanAttack(true);
		}
		
		// increase turn count
		gameState.setTurnCount(gameState.getTurnCount() + 1);
		gameState.setCurrentPlayer(gutsRequest.getPlayer2());
		gameState.setPhase(GamePhase.ATTACK);
		
		// opponent draws (not on the first turn)
		if (gameState.getTurnCount() > 1) {
			try {
				// draw until you have 5 cards
				CardUtil.drawUntilFull(gameState.getPlayerArea(gutsRequest.getPlayer1()));
			} catch (NoSuchElementException nsee) {
				// if you can't draw your opponent wins
				declareWinner(gameState, gutsRequest.getPlayer2());
			}
		}
		
		return findGameView(gutsRequest);
	}
	
	public void declareWinner(GameState gameState, String winner) {
		//declare the winner and end the match
		gameState.setWinner(winner);
	}

}