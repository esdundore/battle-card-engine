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
import card.model.requests.PlayableRequest;
import card.model.view.PlayableCard;
import card.model.view.PlayableCardView;

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
	
	public PlayableCardView findPlayableCards(PlayableRequest playableRequest) {
		String player = playableRequest.getPlayer1();
		String opponent = playableRequest.getPlayer2();
		GameState gameState = gameManager.getGameState(player, opponent);
		ArrayList<Integer> playedCards = playableRequest.getPlayedCardIndexes();
		
		// not the player's turn
		if (!gameState.getCurrentPlayer().equals(player)) {
			return null;
		}
		
		ArrayList<PlayableCard> playableCards = new ArrayList<PlayableCard>();
		ArrayList<String> hand = gameState.getPlayers().get(player).getHand();
		ArrayList<Monster> monsters = gameState.getPlayers().get(player).getMonsters();
		ArrayList<Monster> opponentMonsters = gameState.getPlayers().get(opponent).getMonsters();
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
				else if (gameManager.ATTACK_PHASE.equals(gameState.getPhase())) {
					SkillCard skillCard = (SkillCard) cardCache.getCard(hand.get(i));
					playableCard.setUsers(findUsers(skillCard, monsters, breederAttack, true));
					playableCard.setTargets(findTargets(skillCard, opponentMonsters));
					if (isPlayableAttack(gameState, player, playableCard, skillCard, playedCards)) {
						playableCards.add(playableCard);
					}
				}
				else if (gameManager.DEFEND_PHASE.equals(gameState.getPhase())) {
					SkillCard skillCard = (SkillCard) cardCache.getCard(hand.get(i));
					playableCard.setUsers(findUsers(skillCard, monsters, breederAttack, false));
					if (isPlayableDefense(gameState, player, playableCard, skillCard, playedCards)) {
						playableCards.add(playableCard);
					}
				}
			}
		}
		
		PlayableCardView playableCardView = new PlayableCardView();
		playableCardView.setPlayableCards(playableCards);
		
		return playableCardView;
	}
	
	
	public ArrayList<Integer> findUsers(SkillCard skillCard, ArrayList<Monster> monsters, boolean breederAttack, boolean isAttack) {
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
			if (lineages.get(i).equals(skillCard.getUserId()) || ANY_USER.equals(skillCard.getUserId())) {
				if (!isAttack || tempMonsters.get(i).isCanAttack()) {
					users.add(i);
				}
			}
		}

		return users;
	}
	
	public ArrayList<Integer> findTargets(SkillCard skillCard, ArrayList<Monster> monsters) {
		ArrayList<Integer> targets = new ArrayList<Integer>();
		
		// can only attack living targets
		for (int i = 0; i < monsters.size(); i++) {
			if (monsters.get(i).getCurrentLife() > 0) {
				targets.add(i);
			}
		}
		
		return targets;
	}
	
	public boolean isPlayableAttack(GameState gameState, String player, PlayableCard playableCard, SkillCard skillCard, ArrayList<Integer> playedCards) {
		int guts = gameState.getPlayers().get(player).getGutsPool();
		
		// check that the move is an attack
		if (!ATTACK_TYPES.contains(skillCard.getType())) {
			return false;
		}
		
		// check that the card has users
		if (playableCard.getUsers() == null || playableCard.getTargets() == null) {
			return false;
		}
		
		// check that the card has targets
		if (playableCard.getTargets() == null) {
			return false;
		}
		
		ArrayList<SkillCard> skillCards = new ArrayList<SkillCard>();
		int totalGutsCost = 0;
		for (int cardIndex : playedCards) {
			SkillCard otherCard = (SkillCard) cardCache.getCard(gameState.getPlayers().get(player).getHand().get(cardIndex));
			totalGutsCost += otherCard.getGutsCost();
			skillCards.add(otherCard);
		}
		totalGutsCost += skillCard.getGutsCost();
		skillCards.add(skillCard);
		
		// check the player has enough guts
		if (guts < totalGutsCost) {
			return false;
		}
		
		// check that the card can combo
		if (!canCombo(skillCards)) {
			return false;
		}
		
		return true;
	}
	
	public boolean isPlayableDefense(GameState gameState, String player, PlayableCard playableCard, SkillCard skillCard, ArrayList<Integer> playedCards) {
		int guts = gameState.getPlayers().get(player).getGutsPool();
		
		// check that the move is a defense
		if (!DEFEND_TYPES.contains(skillCard.getType())) {
			return false;
		}
		
		// check that the card has users
		if (playableCard.getUsers() == null) {
			return false;
		}
		
		ArrayList<SkillCard> skillCards = new ArrayList<SkillCard>();
		int totalGutsCost = 0;
		for (int cardIndex : playedCards) {
			SkillCard otherCard = (SkillCard) cardCache.getCard(gameState.getPlayers().get(player).getHand().get(cardIndex));
			totalGutsCost += otherCard.getGutsCost();
			skillCards.add(otherCard);
		}
		totalGutsCost += skillCard.getGutsCost();
		skillCards.add(skillCard);
		
		// check the player has enough guts
		if (guts < totalGutsCost) {
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
