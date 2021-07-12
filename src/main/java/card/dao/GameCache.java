package card.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import card.enums.GamePhase;
import card.enums.MonsterBreed;
import card.model.cards.Deck;
import card.model.cards.MonsterCard;
import card.model.cards.SkillCard;
import card.model.game.ActiveSkill;
import card.model.game.GameState;
import card.model.game.Monster;
import card.model.game.PlayerArea;
import card.model.game.SkillArea;
import card.model.requests.PlayersRequest;
import card.util.CardUtil;

/**
* Class run on engine startup to load all decks
 */
@Component
public class GameCache {
	
	@Autowired
	CardCache cardCache;
	
	@Autowired
	DeckCache deckCache;
	
	public Map<String, GameState> gameStates = new HashMap<String, GameState>();
	
	public GameState startup(PlayersRequest playersRequest) {
		// Get player IDs
		String player1 = playersRequest.getPlayer1();
		String player2 = playersRequest.getPlayer2();
		// create new game state
		GameState gameState = new GameState();
		// set state session
		gameState.setSessionId(player1 + player2);
		// initialize player areas
		gameState.setPlayerArea(new HashMap<String, PlayerArea>());
		gameState.getPlayerArea().put(player1, new PlayerArea(player1));
		gameState.getPlayerArea().put(player2, new PlayerArea(player2));
		PlayerArea player1Area = gameState.getPlayerArea().get(player1);
		PlayerArea player2Area = gameState.getPlayerArea().get(player2);
		// initialize skill area
		SkillArea skillArea = new SkillArea();
		gameState.setSkillArea(skillArea);
		gameState.getSkillArea().setAttacks(new ArrayList<ActiveSkill>());
		gameState.getSkillArea().setDefenses(new ArrayList<ActiveSkill>());
		// fetch deck recipes and initialize monsters
		int cardID = 1;
		cardID = startupDeck(player1Area, playersRequest.getPlayer1(), cardID);
		cardID = startupDeck(player2Area, playersRequest.getPlayer2(), cardID);
		// shuffle decks
		CardUtil.shuffle(player1Area.getDeck().getSkillCards());
		CardUtil.shuffle(player2Area.getDeck().getSkillCards());
		// draw hand
		CardUtil.drawUntilFull(player1Area.getDeck().getSkillCards(), player1Area.getDiscards(), player1Area.getHand());
		CardUtil.drawUntilFull(player2Area.getDeck().getSkillCards(), player2Area.getDiscards(), player2Area.getHand());
		// determine order and go to first guts phase
		gameState.setCurrentPlayer(CardUtil.coinFlip(player1, player2));
		gameState.setPhase(GamePhase.GUTS);
		// cache game state
		gameStates.put(player1 + player2, gameState);
		// respond with game state
		return gameState;
	}
	
	public GameState startupTest(PlayersRequest playersRequest, String cardName1, String monsterName1, String cardName2, String monsterName2) {
		GameState testGameState = startup(playersRequest);
		// current player 1
		testGameState.setCurrentPlayer(playersRequest.getPlayer1());
		// each player gets 10 guts
		testGameState.getPlayerArea(playersRequest.getPlayer1()).getBreeder().setGuts(10);
		testGameState.getPlayerArea(playersRequest.getPlayer2()).getBreeder().setGuts(10);
		// each player gets monster in their first monster slot
		MonsterCard monsterCard1 = cardCache.getMonsterCard(monsterName1).copy();
		testGameState.getPlayerArea(playersRequest.getPlayer1()).getMonsters().set(0, new Monster(monsterCard1));
		MonsterCard monsterCard2 = cardCache.getMonsterCard(monsterName2).copy();
		testGameState.getPlayerArea(playersRequest.getPlayer2()).getMonsters().set(0, new Monster(monsterCard2));
		// each player gets skill cards from list
		String[] cardNames1 = cardName1.split(",");
		for (int i = 0; i < cardNames1.length; i++) {
			SkillCard skillCard = cardCache.getSkillCard(cardNames1[i].replace("_", CardCache.DEL)).copy();
			skillCard.setId(i+1000);
			testGameState.getPlayerArea(playersRequest.getPlayer1()).getHand().set(i, skillCard);
		}
		String[] cardNames2 = cardName2.split(",");
		for (int i = 0; i < cardNames2.length; i++) {
			SkillCard skillCard = cardCache.getSkillCard(cardNames2[i].replace("_", CardCache.DEL)).copy();
			skillCard.setId(i+2000);
			testGameState.getPlayerArea(playersRequest.getPlayer2()).getHand().set(i, skillCard);
		}

		
		return testGameState;
	}
	
	public int startupDeck(PlayerArea playerArea, String deckName, int cardID) {
		Deck deckRecipe = deckCache.getDeck(deckName);
		Deck deck = new Deck();
		deck.setDeckName(deckRecipe.getDeckName());
		deck.setDeckAvatar(deckRecipe.getDeckAvatar());
		deck.setOwnerID(deckRecipe.getOwnerID());
		deck.setMonsterCards(new ArrayList<MonsterCard>());
		deck.setSkillCards(new LinkedList<SkillCard>());
		playerArea.setDeck(deck);
		playerArea.setMonsters(new ArrayList<Monster>());
		playerArea.setSubMonsters(new ArrayList<Monster>());
		for (MonsterCard card : deckRecipe.getMonsterCards()) {
			MonsterCard monsterCard = cardCache.getMonsterCard(card.getName()).copy();
			deck.getMonsterCards().add(monsterCard);
		}
		for (SkillCard card : deckRecipe.getSkillCards()) {
			SkillCard skillCard = cardCache.getSkillCard(card.getName()).copy();
			skillCard.setId(cardID++);
			deck.getSkillCards().add(skillCard);
		}
		for (MonsterCard monsterCard : deck.getMonsterCards()) {
			playerArea.getMonsters().add(new Monster(monsterCard));
		}
		for (MonsterCard monsterCard : cardCache.getMonsterCards().values()) {
			if (MonsterBreed.Worm != monsterCard.getMainLineage() 
					&& MonsterBreed.Worm == monsterCard.getSubLineage()) {
				playerArea.getSubMonsters().add(new Monster(monsterCard));
			}
		}
		return cardID;
	}
	
	public GameState getGameState(PlayersRequest playersRequest) {
		String player1 = playersRequest.getPlayer1();
		String player2 = playersRequest.getPlayer2();
		GameState gameState = gameStates.get(player1 + player2);
		if (gameState == null) {
			gameState = gameStates.get(player2 + player1);
		}
		return gameState;
	}
	
}
