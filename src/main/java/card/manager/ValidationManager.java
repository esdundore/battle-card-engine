package card.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import card.dao.CardCache;
import card.model.cards.SkillCard;
import card.model.game.GameState;
import card.model.game.Monster;
import card.model.requests.PlayableRequest;
import card.model.view.PlayableCard;
import card.model.view.PlayableCardView;

@Component("validationManager")
public class ValidationManager {

	@Autowired
	CardCache cardCache;
	
	@Autowired
	GameManager gameManager;
	
	public PlayableCardView findPlayableCards(PlayableRequest playableRequest) {
		String player = playableRequest.getPlayer1();
		String opponent = playableRequest.getPlayer2();
		GameState gameState = gameManager.getGameState(player, opponent);
		ArrayList<Integer> playedCards = playableRequest.getPlayedCardIndexes();
		Integer userId = playableRequest.getUserId();
		
		// not the player's turn or game over
		if (!gameState.getCurrentPlayer().equals(player) || gameState.getWinner() != null) {
			return null;
		}
		
		ArrayList<PlayableCard> playableCards = new ArrayList<PlayableCard>();
		ArrayList<String> hand = gameState.getPlayers().get(player).getHand();
		ArrayList<Monster> monsters = gameState.getPlayers().get(player).getMonsters();
		ArrayList<Monster> opponentMonsters = gameState.getPlayers().get(opponent).getMonsters();
		int guts = gameState.getPlayers().get(player).getGutsPool();
		boolean breederAttack = gameState.getPlayers().get(player).isCanAttack();
		
		// find the attackRequest cards
		ArrayList<SkillCard> attackCards = new ArrayList<SkillCard>();
		if (gameState.getAttackRequest() != null) {
			for (String attackCardName : gameState.getAttackRequest().getCardNames()) {
				attackCards.add((SkillCard) cardCache.getCard(attackCardName));
			}
		}
		
		if(playedCards == null) playedCards = new ArrayList<Integer>();
		
		for (int i = 0; i < hand.size(); i++) {
			if (!(hand.get(i) == null)) {
				PlayableCard playableCard = new PlayableCard();
				playableCard.setCardIndex(i);
				if (playedCards.contains(i)) {
					// card is already played do not add to return list
				}
				else if (gameManager.GUTS_PHASE.equals(gameState.getPhase())) {
					if (playedCards.size() < 2 || gameState.getTurnCount() > 0) {
						playableCards.add(playableCard);
					}
				}
				else {
					SkillCard skillCard = (SkillCard) cardCache.getCard(hand.get(i));
					ArrayList<SkillCard> playedSkillCards = new ArrayList<SkillCard>();
					for (int cardIndex : playedCards) {
						SkillCard otherCard = (SkillCard) cardCache.getCard(gameState.getPlayers().get(player).getHand().get(cardIndex));
						playedSkillCards.add(otherCard);
					}
					if (gameManager.ATTACK_PHASE.equals(gameState.getPhase())) {
						playableCard.setUsers(findUsers(skillCard, monsters, breederAttack, true, userId));
						playableCard.setTargets(findCommonTargets(skillCard, playedSkillCards, monsters, opponentMonsters));
						if (isPlayableAttack(guts, playableCard, skillCard, playedSkillCards)) {
							playableCards.add(playableCard);
						}
					}
					else if (gameManager.DEFEND_PHASE.equals(gameState.getPhase())) {
						playableCard.setUsers(findUsers(skillCard, monsters, breederAttack, false, null));
						if (isPlayableDefense(guts, playableCard, skillCard, playedSkillCards, attackCards)) {
							playableCards.add(playableCard);
						}
					}
				}
			}
		}
		
		PlayableCardView playableCardView = new PlayableCardView();
		playableCardView.setPlayableCards(playableCards);
		
		return playableCardView;
	}
	
	
	public ArrayList<Integer> findUsers(SkillCard skillCard, ArrayList<Monster> monsters, 
			boolean breederAttack, boolean isAttack, Integer user) {
		ArrayList<Integer> users = new ArrayList<Integer>();
		
		// make a temporary monster list and add a monster to represent the breeder
		ArrayList<Monster> tempMonsters = new ArrayList<Monster>();
		for (Monster monster : monsters) {
			tempMonsters.add(monster);
		}
		Monster breeder = new Monster();
		breeder.setMainLineage("Breeder");
		breeder.setSubLineage("Breeder");
		breeder.setCanAttack(breederAttack);
		tempMonsters.add(breeder);
		
		ArrayList<String> lineages = new ArrayList<String>();
		for (Monster monster : tempMonsters) {
			String lineage = isAttack ? monster.getMainLineage() : monster.getSubLineage();
			lineages.add(lineage);
		}
		
		// compare lineages to the designated card user
		for (int i = 0; i < lineages.size(); i++) {
			if (lineages.get(i).equals(skillCard.getUserId()) || skillCard.anyUser()) {
				if (!isAttack || (tempMonsters.get(i).canAttack())) {
					users.add(i);
				}
			}
		}
		if (user != null) {
			users.retainAll(new ArrayList<Integer>(user));
		}

		return users;
	}
	
	public ArrayList<Integer> findCommonTargets (SkillCard skillCard, ArrayList<SkillCard> skillCards, 
			ArrayList<Monster> monsters, ArrayList<Monster> opponentMonsters) {
		ArrayList<SkillCard> allSkillCards = new ArrayList<SkillCard>();
		allSkillCards.add(skillCard);
		allSkillCards.addAll(skillCards);
		ArrayList<Integer> commonTargets = new ArrayList<Integer>();
		for (SkillCard anySkillCard : allSkillCards) {
			commonTargets.addAll(findTargets(anySkillCard, monsters, opponentMonsters));
		}
		commonTargets = (ArrayList<Integer>) commonTargets.stream().distinct().collect(Collectors.toList());
		return commonTargets;
	}
	
	public ArrayList<Integer> findTargets (SkillCard skillCard, 
			ArrayList<Monster> monsters, ArrayList<Monster> opponentMonsters) {
		ArrayList<Integer> targets = new ArrayList<Integer>();
		
		// target enemy monsters
		if (skillCard.targetEnemy()) {
			for (int i = 0; i < opponentMonsters.size(); i++) {
				if (opponentMonsters.get(i).getCurrentLife() > 0) {
					targets.add(i);
				}
			}
		}
		// target friendly monsters
		else if (skillCard.targetFriend()) {
			for (int i = 0; i < monsters.size(); i++) {
				if (monsters.get(i).getCurrentLife() > 0) {
					targets.add(i);
				}
			}
		}
		
		return targets;
	}
	
	public boolean isPlayableAttack(int guts, PlayableCard playableCard, SkillCard skillCard, ArrayList<SkillCard> playedCards) {
		
		// check that the move is an attack
		if (!skillCard.typeAttack()) {
			return false;
		}
		
		// check that the card has users
		if (playableCard.getUsers().isEmpty()) {
			return false;
		}
		
		// check the player has enough guts
		int totalGutsCost = 0;
		totalGutsCost += skillCard.getGutsCost();
		for (SkillCard otherCard : playedCards) {
			totalGutsCost += otherCard.getGutsCost();
		}
		if (guts < totalGutsCost) {
			return false;
		}
		
		// check that the card can or must combo
		if (!canCombo(skillCard, playedCards)) {
			return false;
		}
		
		return true;
	}
	
	public boolean isPlayableDefense(int guts, PlayableCard playableCard, SkillCard skillCard, ArrayList<SkillCard> playedCards, ArrayList<SkillCard> attackCards) {
		
		// check that the move is a defense
		if (!skillCard.typeDefense()) {
			return false;
		}
		
		// check that the card has users
		if (playableCard.getUsers().isEmpty()) {
			return false;
		}
		
		// check the player has enough guts
		int totalGutsCost = 0;
		totalGutsCost += skillCard.getGutsCost();
		for (SkillCard otherCard : playedCards) {
			totalGutsCost += otherCard.getGutsCost();
		}
		if (guts < totalGutsCost) {
			return false;
		}
		
		// cannot dodge the attack
		for (SkillCard attackCard : attackCards) {
			if (attackCard.keywordUndodgable() && skillCard.typeDGE()) {
				return false;
			}
		}
		
		// can use defense card only after certain attack type
		List<String> attackCardTypes = attackCards.stream()
				.map(SkillCard::getType)
				.collect(Collectors.toCollection(ArrayList::new));
		if (!attackCardTypes.contains("POW") && skillCard.keywordDodgePOW()) {
			return false;
		}
		if (!attackCardTypes.contains("INT") && skillCard.keywordDodgeINT()) {
			return false;
		}
		
		return true;
	}
	
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
	
	public static boolean canCombo(SkillCard skillCard, ArrayList<SkillCard> skillCards) {
		if (skillCards.isEmpty()) {
			// cannot use these cards first
			if (!skillCard.isCombo()) {
				return true;
			}
		}
		else {
			for (SkillCard otherCard : skillCards) {
				// Tiger Combo
				if (skillCard.keywordComboTiger() && otherCard.keywordComboTiger()
						&& !skillCard.getId().equals(otherCard.getId())) {
					return true;
				}
				// POW Combo
				//else if (skillCard.keywordPOW() && otherCard.typePOW()) {
				//	return true;
				//}
			}
		}
		return false;
	}
	
}
