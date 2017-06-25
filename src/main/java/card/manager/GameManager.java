package card.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import card.dao.CardCache;
import card.dao.DeckCache;
import card.model.cards.MonsterCard;
import card.model.game.Deck;
import card.model.game.GameState;
import card.model.game.Monster;
import card.model.game.PlayerArea;
import card.model.requests.AttackRequest;
import card.model.requests.AttackRequestNoMap;
import card.model.requests.DefendRequest;
import card.model.requests.DefendTarget;
import card.model.requests.GutsRequest;
import card.model.requests.PlayersRequest;
import card.model.view.GameView;
import card.util.CardUtil;
import card.util.ViewMapper;

@Component("gameManager")
public class GameManager {

	@Autowired
	CardCache cardCache;
	
	@Autowired
	DeckCache deckCache;
	
	@Autowired
	AttackResolver attackResolver;
	
	@Autowired
	ValidationManager validationManager;
	
	public Map<String, GameState> gameStates = new HashMap<String, GameState>();
	
	public final String GUTS_PHASE = "guts";
	public final String ATTACK_PHASE = "attack";
	public final String DEFEND_PHASE = "defend";

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
		
		// return game state before making changes if not the proper player/phase
		if (!validationManager.isValidPlayerPhase(gameState, player, GUTS_PHASE)) {
			return gameState;
		}
		
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
			try {
				CardUtil.drawUntilFull(opponentArea);
			} catch (NoSuchElementException nsee) {
				// TODO: can't draw a card - game lost
			}
		}
		
		// switch to next player and change phase
		gameState.setTurnCount(gameState.getTurnCount() + 1);
		gameState.setCurrentPlayer(opponent);
		gameState.setPhase(ATTACK_PHASE);
		return gameState;
	}
	
	public GameState attack(AttackRequestNoMap attackRequestNoMap) {
		// convert the non map request type
		AttackRequest attackRequest = new AttackRequest();
		attackRequest.setPlayer1(attackRequestNoMap.getPlayer1());
		attackRequest.setPlayer2(attackRequestNoMap.getPlayer2());
		attackRequest.setUser(attackRequestNoMap.getUser());
		attackRequest.setCardsPlayed(attackRequestNoMap.getCardsPlayed());
		Map<Integer, Integer> targetsAndDamage = new HashMap<Integer, Integer>();
		for (int i = 0; i < attackRequestNoMap.getTargets().size(); i++) {
			targetsAndDamage.put(attackRequestNoMap.getTargets().get(i), attackRequestNoMap.getDamages().get(i));
		}
		attackRequest.setTargetsAndDamage(targetsAndDamage);
				
		String player = attackRequest.getPlayer1();
		String opponent = attackRequest.getPlayer2();
		GameState gameState = getGameState(player, opponent);
		
		// return game state before making changes if not the proper player/phase
		if (!validationManager.isValidPlayerPhase(gameState, player, ATTACK_PHASE)) {
			return gameState;
		}

		// set the card names of the attack request
		ArrayList<String> cardNames = new ArrayList<String>();
		for (int card : attackRequest.getCardsPlayed()) {
			cardNames.add(gameState.getPlayers().get(player).getHand().get(card));
		}
		attackRequest.setCardNames(cardNames);
		
		// add the attack request
		gameState.setDefendRequest(null);
		gameState.setAttackRequest(attackRequest);
		gameState.setAttackId(gameState.getAttackId() + 1);
		
		// switch to next player and change phase
		gameState.setCurrentPlayer(opponent);
		gameState.setPhase(DEFEND_PHASE);
		return gameState;
	}
	
	public GameState defend(DefendRequest defendRequest) {
		String player = defendRequest.getPlayer1();
		String opponent = defendRequest.getPlayer2();
		GameState gameState = getGameState(player, opponent);
		
		// return game state before making changes if not the proper player/phase
		if (!validationManager.isValidPlayerPhase(gameState, player, DEFEND_PHASE)) {
			return gameState;
		}

		// set the card names of the defend request
		for (DefendTarget defendTarget : defendRequest.getCardAndTargets()) {
			String cardName = gameState.getPlayers().get(player).getHand().get(defendTarget.getCard());
			defendTarget.setCardName(cardName);
		}
		
		// add the defend request
		gameState.setDefendRequest(defendRequest);
		gameState.setDefendId(gameState.getDefendId() + 1);
		
		// resolve attack
		attackResolver.resolveAttack(gameState);
		
		AttackRequest attackRequest = gameState.getAttackRequest();
		PlayerArea playerArea = gameState.getPlayers().get(player);
		PlayerArea opponentArea = gameState.getPlayers().get(opponent);
		// discard attack and defense cards
		for (int cardIndex : attackRequest.getCardsPlayed()) {
			CardUtil.discard(opponentArea.getHand(), opponentArea.getDiscard(), cardIndex);
		}
		for (DefendTarget defendTarget : defendRequest.getCardAndTargets()) {
			CardUtil.discard(playerArea.getHand(), playerArea.getDiscard(), defendTarget.getCard());
		}
		
		// TODO: check if all monsters are dead - for both players

		// switch to next player and change phase
		gameState.setCurrentPlayer(opponent);
		gameState.setPhase(ATTACK_PHASE);
		return gameState;
	}
	
	public GameState endAttack(PlayersRequest playersRequest) {
		String player = playersRequest.getPlayer1();
		String opponent = playersRequest.getPlayer2();
		GameState gameState = getGameState(player, opponent);
		
		// return game state before making changes if not the proper player/phase
		if (!validationManager.isValidPlayerPhase(gameState, player, ATTACK_PHASE)) {
			return gameState;
		}
		
		// remove statuses from player monsters
		for (Monster monster : gameState.getPlayers().get(player).getMonsters()) {
			monster.getStatus().clear();
		}
		
		// set opponent monsters to canAttack
		for (Monster monster : gameState.getPlayers().get(opponent).getMonsters()) {
			monster.setCanAttack(true);
		}
		
		// switch to next phase
		gameState.setPhase(GUTS_PHASE);
		return gameState;
	}
	
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
	
	public GameView getGameView (PlayersRequest playersRequest) {
		String player1 = playersRequest.getPlayer1();
		String player2 = playersRequest.getPlayer2();
		return ViewMapper.convertToView(getGameState(player1, player2), player1, player2);
	}
	
	public GameState getGameState(String player1, String player2) {
		GameState gameState = gameStates.get(player1 + player2);
		if (gameState == null) {
			gameState = gameStates.get(player2 + player1);
		}
		return gameState;
	}

}
