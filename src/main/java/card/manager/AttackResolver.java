package card.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import card.dao.CardCache;
import card.model.cards.SkillCard;
import card.model.game.GameState;
import card.model.game.Monster;
import card.model.game.PlayerArea;
import card.model.requests.AttackRequest;
import card.model.requests.DefendRequest;
import card.model.requests.DefendTarget;

@Component("attackResolver")
public class AttackResolver {

	@Autowired
	CardCache cardCache;
	
	/**
	 * Resolves the cards played as part of an attack
	 * @param gameState
	 */
	public void resolveAttack(GameState gameState) {
		// get info from the game state
		AttackRequest attackRequest = gameState.getAttackRequest();
		DefendRequest defendRequest = gameState.getDefendRequest();
		PlayerArea attackerArea = gameState.getPlayers().get(attackRequest.getPlayer1());
		PlayerArea defenderArea = gameState.getPlayers().get(defendRequest.getPlayer1());
		
		// put attack cards in an array
		ArrayList<SkillCard> attackCards = new ArrayList<SkillCard>();
		for (int attackCardIndex : attackRequest.getCardsPlayed()) {
			String cardName = attackerArea.getHand().get(attackCardIndex);
			SkillCard attackCard = (SkillCard) cardCache.getCard(cardName);
			attackCards.add(attackCard);
		}
		// put defense cards in a map
		HashMap<Integer, SkillCard> defenseCardMap = new HashMap<Integer, SkillCard>();
		for (DefendTarget defendTarget : defendRequest.getCardAndTargets()) {
			int defenseCardIndex = defendTarget.getCard();
			String cardName = defenderArea.getHand().get(defenseCardIndex);
			SkillCard defenseCard = (SkillCard) cardCache.getCard(cardName);
			defenseCardMap.put(defenseCardIndex, defenseCard);
		}
		
		// get attack damage and targets
		int attackGutsCost = 0;
		int tigerComboCards = 0;
		int fullDamage = 0;
		for (SkillCard attackCard : attackCards) {
			attackGutsCost += attackCard.getGutsCost();
			fullDamage += attackCard.getDamage();
			if (attackCard.keywordComboTiger()) {
				tigerComboCards++;
				if (tigerComboCards==2) fullDamage += 1;
				if (tigerComboCards==3) fullDamage += 3;
			} 
			if (attackCard.isAOE()) {
				attackRequest.getTargetsAndDamage().clear();
				ArrayList<Monster> targets = defenderArea.getMonsters();
				for (int i = 0; i < targets.size(); i++) {
					attackRequest.getTargetsAndDamage().put(i, 0);
				}
			}
		}
		
		// get defense attributes
		int defenseGutsCost = 0;
		for (Map.Entry<Integer, SkillCard> defenseCardEntry : defenseCardMap.entrySet()) {
			SkillCard defenseCard = defenseCardEntry.getValue();
			defenseGutsCost += defenseCard.getGutsCost();
		}

		// reduce guts for attacker and defender
		attackerArea.setGutsPool(attackerArea.getGutsPool() - attackGutsCost);
		defenderArea.setGutsPool(defenderArea.getGutsPool() - defenseGutsCost);
		
		// find each target
		boolean isHit = true;
		int totalDamage = 0;
		for (Map.Entry<Integer, Integer> targetAndDamage : attackRequest.getTargetsAndDamage().entrySet()) {
			int targetIndex = targetAndDamage.getKey();
			
			int tempDamage = fullDamage;
			if (targetAndDamage.getValue() != 0) {
				tempDamage = targetAndDamage.getValue();
			}

			// apply all dodges
			for (DefendTarget defendTarget : defendRequest.getCardAndTargets()) {
				if (defendTarget.getUser() == targetIndex) {
					SkillCard defenseCard = defenseCardMap.get(defendTarget.getCard());
					if (defenseCard.typeDGE()) {
						tempDamage = dodgeDamage(attackCards, defenseCard, tempDamage);
						isHit = false;
					}
				}
			}
			
			// apply all blocks
			for (DefendTarget defendTarget : defendRequest.getCardAndTargets()) {
				if (defendTarget.getUser() == targetIndex) {
					SkillCard defenseCard = defenseCardMap.get(defendTarget.getCard());
					if (defenseCard.typeBLK()) {
						tempDamage = blockDamage(defenseCard, tempDamage);
					}
				}
			}
			Monster target = defenderArea.getMonsters().get(targetAndDamage.getKey());
			if (isHit) {
				specialTargetEffects(attackCards, target);
			}
			target.setCurrentLife(target.getCurrentLife() - tempDamage);	
			totalDamage += tempDamage;
		}
		
		// apply after effects
		Monster user = attackerArea.getMonsters().get(attackRequest.getUser());
		specialAfterEffects(attackCards, totalDamage, user, defenderArea, isHit);

		// user done attacking
		user.setCanAttack(false);
		
	}
	
	public int dodgeDamage(ArrayList<SkillCard> attackCards, SkillCard dodgeCard, int damage) {
		for (SkillCard attackCard : attackCards) {
			// half dodge effect
			if (attackCard.keywordHalfDodge()) {
				return damage / 2;
			}
		}
		return 0;
	}
	
	public int blockDamage(SkillCard blockCard, int damage) {
		// block amount
		int newDamage = damage - blockCard.findBlockAmount();
		return newDamage > 0 ? newDamage : 0;
	}
	
	
	public void specialTargetEffects(ArrayList<SkillCard> attackCards, Monster target) {
		for (SkillCard attackCard : attackCards) {
			// stun effect
			if (attackCard.keywordStun()) {
				target.setStunned();
			}
		}
	}
	
	public void specialAfterEffects(ArrayList<SkillCard> attackCards, int damage, Monster user, PlayerArea defender, boolean isHit) {
		for (SkillCard attackCard : attackCards) {
			// on damage
			if (damage > 0) {
				// guts damage
				defender.setGutsPool(defender.getGutsPool() - attackCard.findGUTSDamage());
			}
			// on hit (not dodged)
			if (isHit) {
				// recoil
				user.setCurrentLife(user.getCurrentLife() - attackCard.findRecoilDamage());
			}
				
		}
	}

}
