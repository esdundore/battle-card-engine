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
						tempDamage = dodgeDamage(defenseCard.getId(), tempDamage);
					}
					else if (TYPE_BLOCK.equals(defenseCard.getType())) {
						tempDamage = blockDamage(defenseCard.getId(), tempDamage);
					}
				}
			}
			Monster target = defenderArea.getMonsters().get(targetAndDamage.getKey());
			target.setCurrentLife(target.getCurrentLife() - tempDamage);	
			specialEffects(attackCardNames, tempDamage, target);
			
			if (target.getCurrentLife() <= 0) {
				// dead
			}
		}

		// user done attacking
		attackerArea.getMonsters().get(attackRequest.getUser()).setCanAttack(false);
		
	}
	
	public void specialEffects(ArrayList<String> cardName, int tempDamage, Monster target) {
		
	}
	
	public int dodgeDamage(String cardName, int damage) {
		return 0;
	}
	
	public int blockDamage(String cardName, int damage) {
		return 0;
	}
	
}
