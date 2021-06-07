package card.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

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
		startupDeck(player1Area, playersRequest.getPlayer1());
		startupDeck(player2Area, playersRequest.getPlayer2());
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
	
	public GameState startupTest(PlayersRequest playersRequest, String cardName, String monsterName) {
		SkillCard skillCard = cardCache.getSkillCard(cardName.replace("_", CardCache.DEL)).copy();
		MonsterCard monsterCard = cardCache.getMonsterCard(monsterName).copy();
		Monster monster = new Monster(monsterCard);
		GameState testGameState = startup(playersRequest);
		testGameState.setCurrentPlayer(playersRequest.getPlayer1());
		testGameState.getPlayerArea(playersRequest.getPlayer1()).getBreeder().setGuts(10);
		testGameState.getPlayerArea(playersRequest.getPlayer2()).getBreeder().setGuts(10);
		testGameState.getPlayerArea(playersRequest.getPlayer2()).getHand().set(0, skillCard);
		testGameState.getPlayerArea(playersRequest.getPlayer2()).getMonsters().set(0, monster);
		return testGameState;
	}
	
	public void startupDeck(PlayerArea playerArea, String deckName) {
		Deck deckRecipe = deckCache.getDeck(deckName);
		Deck deck = new Deck();
		deck.setDeckName(deckRecipe.getDeckName());
		deck.setDeckAvatar(deckRecipe.getDeckAvatar());
		deck.setOwnerID(deckRecipe.getOwnerID());
		deck.setMonsterCards(new ArrayList<MonsterCard>());
		deck.setSkillCards(new LinkedList<SkillCard>());
		playerArea.setDeck(deck);
		playerArea.setMonsters(new ArrayList<Monster>());
		for (MonsterCard card : deckRecipe.getMonsterCards()) {
			MonsterCard monsterCard = cardCache.getMonsterCard(card.getName()).copy();
			deck.getMonsterCards().add(monsterCard);
		}
		for (SkillCard card : deckRecipe.getSkillCards()) {
			SkillCard skillCard = cardCache.getSkillCard(card.getName()).copy();
			deck.getSkillCards().add(skillCard);
		}
		for (MonsterCard monsterCard : deck.getMonsterCards()) {
			playerArea.getMonsters().add(new Monster(monsterCard));
		}
		
		Collection<MonsterBreed> monsterBreeds = playerArea.getMonsters().stream()
				.map(Monster::getMainLineage)
				.collect(Collectors.toCollection(ArrayList::new));
		if (monsterBreeds.contains(MonsterBreed.Worm)) {
			for (MonsterCard monsterCard : cardCache.getMonsterCards().values()) {
				if (MonsterBreed.Worm == monsterCard.getSubLineage()) {
					playerArea.getSubMonsters().add(new Monster(monsterCard));
				}
			}
		}
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
