package card.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import card.dao.CardCache;
import card.dao.DeckCache;
import card.model.cards.MonsterCard;
import card.model.cards.SkillCard;
import card.model.game.Deck;
import card.model.game.GameState;
import card.model.game.Monster;
import card.model.game.PlayerArea;
import card.model.requests.AttackRequest;
import card.model.requests.DefendRequest;
import card.model.requests.GutsRequest;
import card.model.requests.PlayersRequest;
import card.util.CardScripts;
import card.util.CardUtil;

@Component("gameManager")
public class GameManager {

	@Autowired
	CardCache cardCache;
	
	@Autowired
	DeckCache deckCache;
	
	@Autowired
	AttackResolver attackResolver;
	
	public Map<String, GameState> gameStates = new HashMap<String, GameState>();
	
	public final String GUTS_PHASE = "guts";
	public final String ATTACK_PHASE = "attack";
	public final String DEFEND_PHASE = "defend";
	
	public final String ANY_USER = "Any";
	
	public final List<String> ATTACK_TYPES = Arrays.asList("POW", "INT", "SPE", "ENV");
	public final List<String> DEFEND_TYPES = Arrays.asList("BLK", "DGE");
	
	public GameState startup(PlayersRequest playersRequest) {
		// get player ids
		String player1 = playersRequest.getPlayer1();
		String player2 = playersRequest.getPlayer2();
		// create new game state
		GameState gameState = new GameState();
		// set state session
		gameState.setSessionId(player1 + player2);
		// initialize player areas
		gameState.setPlayers(new HashMap<String, PlayerArea>());
		gameState.getPlayers().put(player1, new PlayerArea());
		gameState.getPlayers().put(player2, new PlayerArea());
		PlayerArea player1Area = gameState.getPlayers().get(player1);
		PlayerArea player2Area = gameState.getPlayers().get(player2);
		// fetch deck recipes and create a copy
		player1Area.setDeck(new Deck(deckCache.getDeck(player1)));
		player2Area.setDeck(new Deck(deckCache.getDeck(player2)));
		// play monsters
		startupMonsters(player1Area);
		startupMonsters(player2Area);
		// shuffle decks
		CardUtil.shuffle(player1Area.getDeck().getSkillCards());
		CardUtil.shuffle(player2Area.getDeck().getSkillCards());
		// draw hand
		CardUtil.drawUntilFull(player1Area);
		CardUtil.drawUntilFull(player2Area);
		// determine order and go to first guts phase
		gameState.setCurrentPlayer(CardUtil.coinFlip(player1, player2));
		gameState.setPhase(GUTS_PHASE);
		// cache game state
		gameStates.put(player1 + player2, gameState);
		// respond with game state
		return gameState;
	}

	public GameState makeGuts(GutsRequest gutsRequest) {
		String player = gutsRequest.getPlayer1();
		String opponent = gutsRequest.getPlayer2();
		ArrayList<Integer> discards = gutsRequest.getDiscards();
		GameState gameState = getGameState(player, opponent);
		
		PlayerArea playerArea = gameState.getPlayers().get(player);
		PlayerArea opponentArea = gameState.getPlayers().get(opponent);
		// discard cards and add guts
		for (int discard : discards) {
			if (CardUtil.discard(playerArea.getHand(), playerArea.getDiscard(), discard)) {
				playerArea.setGutsPool(playerArea.getGutsPool() + 1);
			}
		}
		// let the next player draw
		if (gameState.getTurnCount() > 0) {
			CardUtil.drawUntilFull(opponentArea);
		}
		
		// switch to next player and change phase
		gameState.setTurnCount(gameState.getTurnCount() + 1);
		gameState.setCurrentPlayer(opponent);
		gameState.setPhase(ATTACK_PHASE);
		return gameState;
	}
	
	public GameState attack(AttackRequest attackRequest) {
		String player = attackRequest.getPlayer1();
		String opponent = attackRequest.getPlayer2();
		GameState gameState = getGameState(player, opponent);

		// add the attack request
		gameState.setAttackRequest(attackRequest);
		
		// switch to next player and change phase
		gameState.setCurrentPlayer(opponent);
		gameState.setPhase(DEFEND_PHASE);
		return gameState;
	}
	
	public GameState defend(DefendRequest defendRequest) {
		String player = defendRequest.getPlayer1();
		String opponent = defendRequest.getPlayer2();
		GameState gameState = getGameState(player, opponent);

		// add the defend request
		gameState.setDefendRequest(defendRequest);
		// resolve attack
		attackResolver.resolveAttack(gameState);
		
		// switch to next player and change phase
		gameState.setCurrentPlayer(opponent);
		gameState.setPhase(DEFEND_PHASE);
		return gameState;
	}
	
	public GameState endAttack(PlayersRequest playersRequest) {
		String player = playersRequest.getPlayer1();
		String opponent = playersRequest.getPlayer2();
		GameState gameState = getGameState(player, opponent);
		
		// switch to next phase
		gameState.setPhase(GUTS_PHASE);
		return gameState;
	}
	
	
	
	
	
	
	// ---------------------------------------------------------------------------- //
	public void startupMonsters(PlayerArea playerArea) {
		ArrayList<Monster> monsters = new ArrayList<Monster>();
		for (String monsterName : playerArea.getDeck().getMonsterCards()) {
			MonsterCard monsterCard = (MonsterCard) cardCache.getCard(monsterName);
			Monster monster = new Monster(monsterCard);
			monster.setCurrentLife(monster.getMaxLife());
			monsters.add(monster);
		}
		playerArea.setMonsters(monsters);
	}
	
	public GameState getGameState(String player1, String player2) {
		GameState gameState = gameStates.get(player1 + player2);
		if (gameState == null) {
			gameState = gameStates.get(player2 + player1);
		}
		return gameState;
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
		if (!CardScripts.canCombo(skillCards)) {
			return false;
		}
		// check that the user can use this move
		if (!user.isCanAttack()) {
			return false;
		}
		return true;
	}

}
