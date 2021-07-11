package card.manager;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import card.enums.GamePhase;
import card.model.cards.SkillCard;
import card.model.game.GameState;
import card.model.game.PlayerArea;
import card.model.requests.PlayersRequest;
import card.model.requests.SkillRequest;
import card.model.requests.TargetRequest;
import card.model.view.PlayableCard;
import card.model.view.PlayableView;

@Component
public class DecisionManager {
	
	@Autowired
	GameManager gameManager;
	
	@Autowired
	PlayValidationManager playValidationManager;
	
	public void makeDecision(PlayersRequest playersRequest, GameState gameState) {
		PlayableView playableView = playValidationManager.findPlayables(playersRequest, gameState);
		if (GamePhase.ATTACK == gameState.getPhase()) attackDecision(playersRequest, gameState, playableView);
		else if (GamePhase.DEFENSE == gameState.getPhase()) defenseDecision(playersRequest, gameState, playableView);
		else if (GamePhase.GUTS == gameState.getPhase()) gutsDecision(playersRequest, gameState, playableView);
	}
	
	public void attackDecision(PlayersRequest playersRequest, GameState gameState, PlayableView playableView) {
		for (PlayableCard playableCard : playableView.getPlayableCards()) {
			for (Integer user : playableCard.getUsers()) {
				SkillRequest skillRequest = new SkillRequest(playersRequest);
				skillRequest.setUser(user);
				skillRequest.setHandIndex(playableCard.getHandIndex());
				gameManager.useSkill(skillRequest, gameState);
				return;
			}
		}
		if (playableView.getPlayableTargets().size() > 0) {
		    TargetRequest targetRequest = new TargetRequest(playersRequest);
			int randomTarget = new Random().nextInt(playableView.getPlayableTargets().size());
			targetRequest.setTarget(playableView.getPlayableTargets().get(randomTarget));
			gameManager.declareAttackTarget(targetRequest, gameState);
			return;
		}
		gameManager.endAttack(playersRequest, gameState);
	}
	
	public void defenseDecision(PlayersRequest playersRequest, GameState gameState, PlayableView playableView) {
		for (Integer target : playableView.getPlayableTargets()) {
			TargetRequest targetRequest = new TargetRequest(playersRequest);
			targetRequest.setTarget(target);
			gameManager.declareDefenseTarget(targetRequest, gameState);
			return;
		}
		for (PlayableCard playableCard : playableView.getPlayableCards()) {
			for (Integer user : playableCard.getUsers()) {
				SkillRequest skillRequest = new SkillRequest(playersRequest);
				skillRequest.setUser(user);
				skillRequest.setHandIndex(playableCard.getHandIndex());
				gameManager.useSkill(skillRequest, gameState);
				return;
			}
		}
		gameManager.endDefense(playersRequest, gameState);
	}

	public void gutsDecision(PlayersRequest playersRequest, GameState gameState, PlayableView playableView) {
		PlayerArea playerArea = gameState.getPlayerArea(playersRequest.getPlayer1());
		boolean endTurn = true;
		SkillRequest skillRequest = new SkillRequest(playersRequest);
		for (PlayableCard playableCard : playableView.getPlayableCards()) {
			Integer handIndex = playableCard.getHandIndex();
			SkillCard skillCard = playerArea.getHand().get(handIndex);
			if (skillCard.determineAttack())
			{
				skillRequest.setHandIndex(handIndex);
				gameManager.makeGuts(skillRequest, gameState);
				endTurn = false;
				break;
			}
		}

		if (endTurn) gameManager.endTurn(playersRequest, gameState);
		else makeDecision(playersRequest, gameState);
		
	}
	
}
