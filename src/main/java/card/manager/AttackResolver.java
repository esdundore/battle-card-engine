package card.manager;

import java.util.ArrayList;
import java.util.Collections;
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
import card.util.CardNameConstants;

@Component("attackResolver")
public class AttackResolver {

	@Autowired
	CardCache cardCache;
	
	public final String TYPE_DODGE = "DGE";
	public final String TYPE_BLOCK = "BLK";
	
	public void resolveAttack(GameState gameState) {
		AttackRequest attackRequest = gameState.getAttackRequest();
		DefendRequest defendRequest = gameState.getDefendRequest();
		
		String attacker = attackRequest.getPlayer1();
		String defender = defendRequest.getPlayer1();
		
		PlayerArea attackerArea = gameState.getPlayers().get(attacker);
		PlayerArea defenderArea = gameState.getPlayers().get(defender);
		
		// get attack cards
		ArrayList<Integer> attackCardIndexes = attackRequest.getCardsPlayed();
		HashMap<Integer, SkillCard> attackCardMap = new HashMap<Integer, SkillCard>();
		ArrayList<String> attackCardNames = new ArrayList<String>();
		int attackGutsCost = 0;
		int fullDamage = 0;
		for (int attackCardIndex : attackCardIndexes) {
			SkillCard attackCard = (SkillCard) cardCache.getCard(attackerArea.getHand().get(attackCardIndex));
			attackCardMap.put(attackCardIndex, attackCard);
			attackCardNames.add(attackCard.getId());
			attackGutsCost += attackCard.getGutsCost();
			fullDamage += attackCard.getDamage();
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
			
			int tempDamage = new Integer(fullDamage);
			if (targetAndDamage.getValue() != 0) {
				tempDamage = targetAndDamage.getValue();
			}

			for (DefendTarget defendTarget : defendRequest.getCardAndTargets()) {
				if (defendTarget.getUser() == targetIndex) {
					SkillCard defenseCard = defenseCardMap.get(defendTarget.getCard());
					if (TYPE_DODGE.equals(defenseCard.getType())) {
						tempDamage = dodgeDamage(attackCardNames, defenseCard.getId(), tempDamage);
					}
					else if (TYPE_BLOCK.equals(defenseCard.getType())) {
						tempDamage = blockDamage(defenseCard.getId(), tempDamage);
					}
				}
			}
			Monster target = defenderArea.getMonsters().get(targetAndDamage.getKey());
			specialTargetEffects(attackCardNames, tempDamage, target);
			target.setCurrentLife(target.getCurrentLife() - tempDamage);	
			totalDamage += tempDamage;
			if (target.getCurrentLife() <= 0) {
				// dead
			}
		}
		
		// apply after effects
		Monster user = attackerArea.getMonsters().get(attackRequest.getUser());
		specialAfterEffects(attackCardNames, totalDamage, user, defenderArea);

		// user done attacking
		user.setCanAttack(false);
		
	}
	
	public int dodgeDamage(ArrayList<String> attackCardNames, String dodgeCardName, int damage) {
		// 1/2 damage 
		if (!Collections.disjoint(attackCardNames,CardNameConstants.HALF_DODGE)) {
			return damage / 2;
		}
		return 0;
	}
	
	public int blockDamage(String blockCardName, int damage) {
		int damageBlocked = CardNameConstants.BLOCK_AMOUNT.getOrDefault(blockCardName, 0);
		int newDamage = damage - damageBlocked;
		return newDamage > 0 ? newDamage : 0;
	}
	
	
	public void specialTargetEffects(ArrayList<String> cardNames, int tempDamage, Monster target) {
		if (tempDamage > 0) {
			// stun target
		}
	}
	
	public void specialAfterEffects(ArrayList<String> cardNames, int damage, Monster user, PlayerArea defender) {
		if (damage > 0) {
			for (String cardName : cardNames) {
				//recoil
				int recoilDamage = CardNameConstants.RECOIL_DAMAGE.getOrDefault(cardName, 0);
				user.setCurrentLife(user.getCurrentLife() - recoilDamage);
				// guts damage
				int gutsDamage = CardNameConstants.GUTS_DAMAGE.getOrDefault(cardName, 0);
				defender.setGutsPool(defender.getGutsPool() - gutsDamage);
			}
		}
	}

}
