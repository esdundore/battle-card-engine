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
	
	public void resolveAttack(GameState gameState) {
		AttackRequest attackRequest = gameState.getAttackRequest();
		DefendRequest defendRequest = gameState.getDefendRequest();
		
		String attacker = attackRequest.getPlayer1();
		String defender = defendRequest.getPlayer1();
		
		PlayerArea attackerArea = gameState.getPlayers().get(attacker);
		PlayerArea defenderArea = gameState.getPlayers().get(defender);
		
		// get attack cards
		ArrayList<Integer> attackCardIndexes = attackRequest.getCardsPlayed();
		ArrayList<SkillCard> attackCards = new ArrayList<SkillCard>();
		
		int attackGutsCost = 0;
		int tigerComboCards = 0;
		int fullDamage = 0;
		
		// calculate damage and targets
		for (int attackCardIndex : attackCardIndexes) {
			SkillCard attackCard = (SkillCard) cardCache.getCard(attackerArea.getHand().get(attackCardIndex));
			attackCards.add(attackCard);
			attackGutsCost += attackCard.getGutsCost();
			fullDamage += attackCard.getDamage();
			if (attackCard.keyComboTiger()) {
				tigerComboCards++;
				if (tigerComboCards==2) fullDamage += 1;
				if (tigerComboCards==3) fullDamage += 3;
			} 
			if (attackCard.targetAOE()) {
				attackRequest.getTargetsAndDamage().clear();
				ArrayList<Monster> targets = gameState.getPlayers().get(defender).getMonsters();
				for (int i = 0; i < targets.size(); i++) {
					attackRequest.getTargetsAndDamage().put(i, 0);
				}
			}
		}
		
		// get defense cards
		HashMap<Integer, SkillCard> defenseCardMap = new HashMap<Integer, SkillCard>();
		int defenseGutsCost = 0;
		for (DefendTarget defendTarget : defendRequest.getCardAndTargets()) {
			int defenseCardIndex = defendTarget.getCard();
			SkillCard defenseCard = (SkillCard) cardCache.getCard(defenderArea.getHand().get(defenseCardIndex));
			defenseCardMap.put(defenseCardIndex, defenseCard);
			defenseGutsCost += defenseCard.getGutsCost();
		}

		// reduce guts for attacker and defender
		attackerArea.setGutsPool(attackerArea.getGutsPool() - attackGutsCost);
		defenderArea.setGutsPool(defenderArea.getGutsPool() - defenseGutsCost);
		
		// apply damage - for each target
		int totalDamage = 0;
		for (Map.Entry<Integer, Integer> targetAndDamage : attackRequest.getTargetsAndDamage().entrySet()) {
			int targetIndex = targetAndDamage.getKey();
			
			boolean isHit = true;
			int tempDamage = new Integer(fullDamage);
			if (targetAndDamage.getValue() != 0) {
				tempDamage = targetAndDamage.getValue();
			}

			for (DefendTarget defendTarget : defendRequest.getCardAndTargets()) {
				if (defendTarget.getUser() == targetIndex) {
					SkillCard defenseCard = defenseCardMap.get(defendTarget.getCard());
					if (defenseCard.typeDGE()) {
						tempDamage = dodgeDamage(attackCards, defenseCard, tempDamage);
						isHit = false;
					}
					else if (defenseCard.typeBLK()) {
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
			if (target.getCurrentLife() <= 0) {
				// dead
			}
		}
		
		// apply after effects
		Monster user = attackerArea.getMonsters().get(attackRequest.getUser());
		specialAfterEffects(attackCards, totalDamage, user, defenderArea);

		// user done attacking
		user.setCanAttack(false);
		
	}
	
	public int dodgeDamage(ArrayList<SkillCard> attackCards, SkillCard dodgeCard, int damage) {
		for (SkillCard attackCard : attackCards) {
			// half dodge effect
			if (attackCard.keyHalfDodge()) {
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
			if (attackCard.keyStun()) {
				target.setStunned();
			}
		}
	}
	
	public void specialAfterEffects(ArrayList<SkillCard> attackCards, int damage, Monster user, PlayerArea defender) {
		if (damage > 0) {
			for (SkillCard attackCard : attackCards) {
				// recoil
				user.setCurrentLife(user.getCurrentLife() - attackCard.findHitRecoilDamage());
				// guts damage
				defender.setGutsPool(defender.getGutsPool() - attackCard.findGUTSDamage());
			}
		}
	}

}
