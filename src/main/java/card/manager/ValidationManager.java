package card.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import card.dao.CardCache;
import card.model.cards.SkillCard;
import card.model.game.GameState;
import card.model.game.Monster;
import card.model.game.PlayerArea;
import card.model.requests.AttackRequest;

@Component("validationManager")
public class ValidationManager {

	@Autowired
	CardCache cardCache;
	
	public final String ANY_USER = "Any";
	
	public final List<String> ATTACK_TYPES = Arrays.asList("POW", "INT", "SPE", "ENV");
	public final List<String> DEFEND_TYPES = Arrays.asList("BLK", "DGE");
	
	public boolean isValidPlayerPhase(GameState gameState, String player, String phase) {
		// not this players turn
		if (!gameState.getCurrentPlayer().equals(player)) {
			return false;
		}
		// not the guts phase
		if (!gameState.getPhase().equals(phase)) {
			return false;
		}
		return true;
	}
	
	public boolean isValidAttack(AttackRequest attackRequest, PlayerArea playerArea, String player) {
		Monster user = playerArea.getMonsters().get(attackRequest.getUser());
		// look up skill cards
		ArrayList<SkillCard> skillCards = new ArrayList<SkillCard>();
		int gutsCost = 0;
		for (int cardPlayed : attackRequest.getCardsPlayed()) {
			SkillCard skillCard = (SkillCard) cardCache.getCard(playerArea.getHand().get(cardPlayed));
			// check that the move is an attack
			if (!ATTACK_TYPES.contains(skillCard.getType())) {
				return false;
			}
			// check that the user is correct
			if (!ANY_USER.equals(skillCard.getUserId()) && 
					!user.getMainLineage().equals(skillCard.getUserId())) {
				return false;
			}
			gutsCost += skillCard.getGutsCost();
			skillCards.add(skillCard);
		}
		// check the player has enough guts
		if (playerArea.getGutsPool() < gutsCost) {
			return false;
		}
		// check that the cards played together combo
		if (!canCombo(skillCards)) {
			return false;
		}
		// check that the user can use this move
		if (!user.isCanAttack()) {
			return false;
		}
		return true;
	}
	
	public static boolean canCombo(ArrayList<SkillCard> skillCards) {
		return true;
	}
	
}
