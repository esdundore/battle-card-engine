package card.manager;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import card.enums.GamePhase;
import card.model.cards.SkillCard;
import card.model.game.ActiveSkill;
import card.model.game.GameState;
import card.model.requests.PlayersRequest;
import card.model.requests.SkillRequest;
import card.model.requests.TargetRequest;
import card.model.view.PlayableCard;
import card.model.view.PlayableView;

@Component
public class PlayValidationManager {
	
	@Autowired
	PlayCardManager playCardManager;
	
	@Autowired
	PlayTargetManager playTargetManager;
	
	public Boolean validRequest(PlayersRequest playersRequest, GameState gameState, GamePhase phase) {
		Boolean isValidRequest = true;
		
		// game is already over
		if (gameState.getWinner() != null) return false;
		// not this players turn
		if (!gameState.getCurrentPlayer().equals(playersRequest.getPlayer1())) return false;
		// not the given phase
		if (!phase.equals(gameState.getPhase())) return false;
		
		if (playersRequest instanceof SkillRequest) {
			isValidRequest = false;
			SkillRequest skillRequest = (SkillRequest) playersRequest;
			// find playable cards and verify the skill request is one of them
			ArrayList<PlayableCard> playableCards = findPlayables(skillRequest, gameState).getPlayableCards();
			for (PlayableCard playableCard : playableCards) {
				if (playableCard.getHandIndex() == skillRequest.getHandIndex() &&
						playableCard.getUsers().contains(skillRequest.getUser())) {
					return true;
				}
			}
		}
		else if (playersRequest instanceof TargetRequest) {
			isValidRequest = false;
			TargetRequest targetRequest = (TargetRequest) playersRequest;
			// find playable targets and verify the target request is one of them
			ArrayList<Integer> playableTargets = findPlayables(targetRequest, gameState).getPlayableTargets();
			if (playableTargets.contains(targetRequest.getTarget())) {
				return true;
			}
		}
		
		return isValidRequest;
	}
	
	public PlayableView findPlayables(PlayersRequest playersRequest, GameState gameState) {
		PlayableView playableView = new PlayableView();
		ArrayList<Integer> playableTargets = playTargetManager.findPlayableTargets(playersRequest, gameState);
		ArrayList<SkillCard> hand = new ArrayList<>();
		hand.addAll(gameState.getPlayerArea(playersRequest.getPlayer1()).getHand());
		ArrayList<ActiveSkill> attacks = new ArrayList<>();
		attacks.addAll(gameState.getSkillArea().getAttacks());
		playableView.setPlayableCards(playCardManager.findPlayableCards(playersRequest, gameState, playableTargets.isEmpty()));
		playableView.setPlayableTargets(playableTargets);
		return playableView;
	}
	
}
