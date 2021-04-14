package card.manager;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import card.dao.CardCache;
import card.enums.GamePhase;
import card.enums.MonsterStatus;
import card.enums.TargetArea;
import card.model.cards.SkillCard;
import card.model.game.ActiveSkill;
import card.model.game.GameState;
import card.model.game.Monster;
import card.model.requests.PlayersRequest;
import card.model.requests.SkillRequest;
import card.model.requests.TargetRequest;
import card.model.view.PlayableCard;
import card.model.view.PlayableView;

@Component("validationManager")
public class ValidationManager {

	@Autowired
	CardCache cardCache;
	
	public static final String ANY_USER = "Any";
	public static final String BREEDER_USER = "Breeder";
	
	public Boolean validRequest(SkillRequest skillRequest, GameState gameState, GamePhase phase) {
		if(!validGameState(skillRequest, gameState, phase)) {
			return false;
		}

		ArrayList<PlayableCard> playableCards = findPlayables(gameState, skillRequest).getPlayableCards();
		for (PlayableCard playableCard : playableCards) {
			if (playableCard.getHandIndex() == skillRequest.getHandIndex() &&
					playableCard.getUsers().contains(skillRequest.getUser())) {
				return true;
			}
		}
		
		return false;
	}
	
	public Boolean validRequest(TargetRequest targetRequest, GameState gameState, GamePhase phase) {
		if(!validGameState(targetRequest, gameState, phase)) {
			return false;
		}

		ArrayList<Integer> playableTargets = findPlayables(gameState, targetRequest).getPlayableTargets();
		if (playableTargets.contains(targetRequest.getTarget())) {
			return true;
		}
		
		return false;
	}
	
	public Boolean validGameState(PlayersRequest playersRequest, GameState gameState, GamePhase phase) {
		// game is already over
		if (gameState.getWinner() != null) {
			return false;
		}
		// not this players turn
		if (!gameState.getCurrentPlayer().equals(playersRequest.getPlayer1())) {
			return false;
		}
		// not the given phase
		if (!phase.equals(gameState.getPhase())) {
			return false;
		}
		
		return true;
	}
	
	public PlayableView findPlayables(GameState gameState, PlayersRequest playersRequest) {
		PlayableView playableView = new PlayableView();
		ArrayList<Integer> playableTargets = new ArrayList<Integer>();
		if (GamePhase.ATTACK == gameState.getPhase()) {
			playableTargets = findPlayableAttackTargets(gameState, playersRequest);
		}
		else if (GamePhase.DEFENSE == gameState.getPhase()) {
			playableTargets = findPlayableDefenseTargets(gameState, playersRequest);
		}
		playableView.setPlayableCards(findPlayableCards(gameState, playersRequest, playableTargets));
		playableView.setPlayableTargets(playableTargets);
		return playableView;
	}
	
	public ArrayList<PlayableCard> findPlayableCards(GameState gameState, PlayersRequest playersRequest, ArrayList<Integer> playableTargets) {
		ArrayList<PlayableCard> playableCards = new ArrayList<PlayableCard>();
		Integer handIndex = -1;
		for (SkillCard skillCard : gameState.getPlayerArea(playersRequest.getPlayer1()).getHand()) {
			handIndex++;
			if (skillCard == null) {
				// no card here
				continue;
			}
			PlayableCard playableCard = new PlayableCard();
			playableCard.setHandIndex(handIndex);
			if (GamePhase.GUTS == gameState.getPhase()) {
				playableCards.add(playableCard);
			}
			else {
				playableCards.add(findPlayableSkill(gameState, playersRequest, skillCard, playableCard, playableTargets));
			}
		}
		return playableCards;
	}
	
	public PlayableCard findPlayableSkill(GameState gameState, PlayersRequest playersRequest, SkillCard skillCard, PlayableCard playableCard, ArrayList<Integer> playableTargets) {
		String player = playersRequest.getPlayer1();
		if (skillCard.getGutsCost() > gameState.getPlayerArea(player).getGuts()) {
			// player does not have required guts to use
			return null;
		}
		playableCard.setUsers(findUsers(skillCard, 
				gameState.getPlayerArea(player).getMonsters(), 
				gameState.getPlayerArea(player).getCanBreederAttack()));
		if (playableCard.getUsers().isEmpty()) {
			// no valid users
			return null;
		}
		if (GamePhase.ATTACK == gameState.getPhase() && skillCard.isAttack()) {
			// validate attack
			return findPlayableAttack(gameState, playersRequest, skillCard, playableCard);
		}
		else if (GamePhase.DEFENSE == gameState.getPhase() && !skillCard.isAttack()) {
			// validate defense
			return findPlayableDefense(gameState, playersRequest, skillCard, playableCard, playableTargets);
		}
		return null;
	}
	
	public ArrayList<Integer> findUsers(SkillCard skillCard, ArrayList<Monster> monsters, Boolean breederAttack) {
		// make a user list and add a monster to represent the breeder
		ArrayList<Monster> users = new ArrayList<Monster>();
		for (Monster monster : monsters) {
			users.add(monster);
		}
		Monster breeder = new Monster();
		breeder.setMainLineage(BREEDER_USER);
		breeder.setSubLineage(BREEDER_USER);
		breeder.setCurrentLife(1);
		breeder.setCanAttack(breederAttack);
		users.add(breeder);

		// find the users
		ArrayList<Integer> userIndexes = new ArrayList<>();
		Integer userIndex = 0;
		for (Monster user : users) {
			if (canUseSkill(user, skillCard)) {
				userIndexes.add(userIndex);
			}
			userIndex++;
		}

		return userIndexes;
	}
	
	public Boolean canUseSkill(Monster monster, SkillCard skillCard) {
		String lineage = skillCard.isAttack() ? monster.getMainLineage() : monster.getSubLineage();
		if (!ANY_USER.equals(skillCard.getUserId()) && !lineage.equals(skillCard.getUserId())) {
			// can't use because of lineage
			return false;
		}
		if (!monster.isAlive()) {
			// can't use because that monster is dead
			return false;
		}
		if (monster.getStatusDuration().containsKey(MonsterStatus.STUNNED)) {
			// can't use because that monster is stunned
			return false;
		}
		if (skillCard.isAttack() && !monster.getCanAttack()) {
			// can't use because of monster already attacked
			return false;
		}

		return true;
	}
	
	public PlayableCard findPlayableAttack(GameState gameState, PlayersRequest playersRequest, SkillCard skillCard, PlayableCard playableCard) {
		// combo attack rules
		if (!gameState.getSkillArea().isResolved()) {
			ArrayList<ActiveSkill> attacks = gameState.getSkillArea().getAttacks();
			// the attack cannot be used by the current attacker
			if (!playableCard.getUsers().contains(attacks.get(0).getUser())) {
				return null;
			}
			playableCard.setUsers((ArrayList<Integer>) Arrays.asList(attacks.get(0).getUser()));
			// must be able to combo
		}
		return null;
	}
	
	public PlayableCard findPlayableDefense(GameState gameState, PlayersRequest playersRequest, SkillCard skillCard, PlayableCard playableCard, ArrayList<Integer> playableTargets) {
		// valid defense rules
		if (!playableTargets.isEmpty()) {
			return null;
		}
		return playableCard;
	}
	
	public ArrayList<Integer> findPlayableAttackTargets(GameState gameState, PlayersRequest playersRequest) {
		String player = playersRequest.getPlayer1();
		String opponent = playersRequest.getPlayer2();
		ArrayList<Integer> playableTargets = new ArrayList<>();
		TargetArea targetArea = gameState.getSkillArea().getTargetArea();
		Integer targetIndex = -1;
		
		switch(targetArea) {
			case HAND:
				for (SkillCard skillCard : gameState.getPlayerArea(player).getHand()) {
					targetIndex++;
					if (skillCard != null) {
						playableTargets.add(targetIndex);
					}

				}
			case DISCARD:
				for (String skillCard : gameState.getPlayerArea(player).getDiscard()) {
					targetIndex++;
					if (skillCard != null) {
						playableTargets.add(targetIndex);
					}
				}
			case ALLY:
				for (Monster targetMonster : gameState.getPlayerArea(player).getMonsters()) {
					targetIndex++;
					if (targetMonster.isAlive()) {
						playableTargets.add(targetIndex);
					}
				}
			case ENEMY:
				for (Monster targetMonster : gameState.getPlayerArea(opponent).getMonsters()) {
					targetIndex++;
					if (targetMonster.isAlive()) {
						playableTargets.add(targetIndex);
					}
				}
			default:
				break;
		}
		
		return playableTargets;
	}
	
	public ArrayList<Integer> findPlayableDefenseTargets(GameState gameState, PlayersRequest playersRequest) {
		ArrayList<Integer> playableTargets = new ArrayList<>();
		ArrayList<ActiveSkill> defenses = gameState.getSkillArea().getDefenses();
		ActiveSkill lastPlayedDefense = defenses.get(defenses.size());
		// TODO: check for target defense cards
		if (lastPlayedDefense == new ActiveSkill()) {
			Integer targetIndex = -1;
			for (Monster targetMonster : gameState.getPlayerArea(playersRequest.getPlayer1()).getMonsters()) {
				targetIndex++;
				if (targetMonster.isAlive()) {
					playableTargets.add(targetIndex);
				}
			}
		}
		
		return playableTargets;
	}
	
}