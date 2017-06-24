package card.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import card.dao.CardCache;
import card.model.cards.SkillCard;
import card.model.game.GameState;
import card.model.game.Monster;
import card.model.requests.PlayableRequest;
import card.model.view.PlayableCard;
import card.model.view.PlayableCardView;
import card.util.CardNameConstants;

@Component("validationManager")
public class ValidationManager {

	@Autowired
	CardCache cardCache;
	
	@Autowired
	GameManager gameManager;
	
	public final String ANY_USER = "Any";
	public final String BREEDER_USER = "Breeder";
	
	public final List<String> ATTACK_TYPES = Arrays.asList("POW", "INT", "SPE", "ENV");
	public final List<String> DEFEND_TYPES = Arrays.asList("BLK", "DGE");
	
	public final String DODGE = "DGE";
	public final String BLOCK = "BLK";
	
	public PlayableCardView findPlayableCards(PlayableRequest playableRequest) {
		String player = playableRequest.getPlayer1();
		String opponent = playableRequest.getPlayer2();
		GameState gameState = gameManager.getGameState(player, opponent);
		ArrayList<Integer> playedCards = playableRequest.getPlayedCardIndexes();
		Integer userId = playableRequest.getUserId();
		
		// not the player's turn
		if (!gameState.getCurrentPlayer().equals(player)) {
			return null;
		}
		
		ArrayList<PlayableCard> playableCards = new ArrayList<PlayableCard>();
		ArrayList<String> hand = gameState.getPlayers().get(player).getHand();
		ArrayList<Monster> monsters = gameState.getPlayers().get(player).getMonsters();
		ArrayList<Monster> opponentMonsters = gameState.getPlayers().get(opponent).getMonsters();
		int guts = gameState.getPlayers().get(player).getGutsPool();
		boolean breederAttack = gameState.getPlayers().get(player).isCanAttack();
		
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
					playedSkillCards.add(skillCard);
					if (gameManager.ATTACK_PHASE.equals(gameState.getPhase())) {
						playableCard.setUsers(findUsers(skillCard, monsters, breederAttack, true, userId));
						playableCard.setTargets(findCommonTargets(playedSkillCards, opponentMonsters));
						if (isPlayableAttack(guts, playableCard, skillCard, playedSkillCards)) {
							playableCards.add(playableCard);
						}
					}
					else if (gameManager.DEFEND_PHASE.equals(gameState.getPhase())) {
						ArrayList<String> attackCards = gameState.getAttackRequest().getCardNames();
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
	
	
	public ArrayList<Integer> findUsers(SkillCard skillCard, ArrayList<Monster> monsters, boolean breederAttack, boolean isAttack, Integer user) {
		ArrayList<Integer> users = new ArrayList<Integer>();
		
		// make a temporary monster list and add a monster to represent the breeder
		ArrayList<Monster> tempMonsters = new ArrayList<Monster>();
		for (Monster monster : monsters) {
			tempMonsters.add(monster);
		}
		Monster breeder = new Monster();
		breeder.setMainLineage(BREEDER_USER);
		breeder.setSubLineage(BREEDER_USER);
		breeder.setCanAttack(breederAttack);
		tempMonsters.add(breeder);
		
		ArrayList<String> lineages = new ArrayList<String>();
		for (Monster monster : tempMonsters) {
			String lineage = isAttack ? monster.getMainLineage() : monster.getSubLineage();
			lineages.add(lineage);
		}
		
		for (int i = 0; i < lineages.size(); i++) {
			System.out.println(lineages.get(i));
			System.out.println(skillCard.getUserId());
			if (lineages.get(i).equals(skillCard.getUserId()) || ANY_USER.equals(skillCard.getUserId())) {
				if (!isAttack || tempMonsters.get(i).isCanAttack()) {
					users.add(i);
				}
			}
		}
		if (user != null) {
			users.retainAll(new ArrayList<Integer>(user));
		}

		return users;
	}
	
	public ArrayList<Integer> findCommonTargets (ArrayList<SkillCard> skillCards, ArrayList<Monster> monster) {
		ArrayList<Integer> commonTargets = new ArrayList<Integer>();
		for (SkillCard skillCard : skillCards) {
			ArrayList<Integer> targets = findTargets(skillCard, monster);
			if (commonTargets.isEmpty()) {
				commonTargets.addAll(targets);
			}
			else {
				commonTargets.retainAll(targets);
			}
		}
		return commonTargets;
	}
	
	public ArrayList<Integer> findTargets (SkillCard skillCard, ArrayList<Monster> monsters) {
		ArrayList<Integer> targets = new ArrayList<Integer>();
		
		// can only attack living targets
		for (int i = 0; i < monsters.size(); i++) {
			if (monsters.get(i).getCurrentLife() > 0) {
				targets.add(i);
			}
		}
		
		return targets;
	}
	
	public boolean isPlayableAttack(int guts, PlayableCard playableCard, SkillCard skillCard, ArrayList<SkillCard> playedCards) {
		
		// check that the move is an attack
		if (!ATTACK_TYPES.contains(skillCard.getType())) {
			return false;
		}
		
		// check that the card has users
		if (playableCard.getUsers() == null) {
			return false;
		}
		
		// check that the card has targets
		if (playableCard.getTargets() == null) {
			return false;
		}
		
		// check the player has enough guts
		int totalGutsCost = 0;
		for (SkillCard otherCard : playedCards) {
			totalGutsCost += otherCard.getGutsCost();
		}
		if (guts < totalGutsCost) {
			return false;
		}
		
		// check that the card can combo
		if (playedCards.size() > 1 && !canCombo(playedCards)) {
			return false;
		}
		
		return true;
	}
	
	public boolean isPlayableDefense(int guts, PlayableCard playableCard, SkillCard skillCard, ArrayList<SkillCard> playedCards, ArrayList<String> attackCards) {
		
		// check that the move is a defense
		if (!DEFEND_TYPES.contains(skillCard.getType())) {
			return false;
		}
		
		// check that the card has users
		if (playableCard.getUsers() == null) {
			return false;
		}
		
		// check the player has enough guts
		int totalGutsCost = 0;
		for (SkillCard otherCard : playedCards) {
			totalGutsCost += otherCard.getGutsCost();
		}
		if (guts < totalGutsCost) {
			return false;
		}
		
		if (!Collections.disjoint(CardNameConstants.CANT_DODGE, attackCards) && DODGE.equals(skillCard.getType())) {
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
	
	public static boolean canCombo(ArrayList<SkillCard> skillCards) {
		return false;
	}
	
}
