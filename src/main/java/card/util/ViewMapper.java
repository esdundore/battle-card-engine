package card.util;

import java.util.ArrayList;

import card.model.game.GameState;
import card.model.requests.DefendTarget;
import card.model.view.AttackView;
import card.model.view.DefendView;
import card.model.view.DefendViewTarget;
import card.model.view.GameView;
import card.model.view.OpponentView;
import card.model.view.PlayerView;

public class ViewMapper {

	public static GameView convertToView(GameState gameState, String player, String opponent) {
		// Basic info mapping
		GameView gameView = new GameView();
		gameView.setCurrentPlayer(gameState.getCurrentPlayer());
		gameView.setCurrentTime(gameState.getCurrentTime());
		gameView.setPhase(gameState.getPhase());
		
		if (gameState.getEnvironmentCard() != null) {
			gameView.setEnvironmentCard(gameState.getEnvironmentCard().getId());
		}
		
		// Player info mapping
		gameView.setPlayer(new PlayerView());
		gameView.getPlayer().setDeckSize(gameState.getPlayers().get(player).getDeck().getSkillCards().size());
		gameView.getPlayer().setGutsPool(gameState.getPlayers().get(player).getGutsPool());
		gameView.getPlayer().setHand(gameState.getPlayers().get(player).getHand());
		gameView.getPlayer().setMonsters(gameState.getPlayers().get(player).getMonsters());
		
		// Opponent info mapping
		gameView.setOpponent(new OpponentView());
		gameView.getOpponent().setDeckSize(gameState.getPlayers().get(opponent).getDeck().getSkillCards().size());
		gameView.getOpponent().setGutsPool(gameState.getPlayers().get(opponent).getGutsPool());
		gameView.getOpponent().setHandSize(gameState.getPlayers().get(opponent).getHand().size());
		gameView.getOpponent().setMonsters(gameState.getPlayers().get(opponent).getMonsters());
		
		// Attack Request mapping
		if(gameState.getAttackRequest() != null) {
			String attacker = gameState.getAttackRequest().getPlayer1();
			String defender = gameState.getAttackRequest().getPlayer2();
		
			gameView.setAttackView(new AttackView());
			gameView.getAttackView().setPlayer1(attacker);
			gameView.getAttackView().setPlayer2(defender);
			ArrayList<String> cardsPlayed = new ArrayList<String>();
			for (int cardPlayedIndex : gameState.getAttackRequest().getCardsPlayed()) {
				cardsPlayed.add(gameState.getPlayers().get(attacker).getHand().get(cardPlayedIndex));
			}
			gameView.getAttackView().setCardsPlayed(cardsPlayed);
			gameView.getAttackView().setTargetsAndDamage(gameState.getAttackRequest().getTargetsAndDamage());
		}
		
		// Defend Request mapping
		if(gameState.getDefendRequest() != null) {
			String attacker = gameState.getDefendRequest().getPlayer2();
			String defender = gameState.getDefendRequest().getPlayer1();
			
			gameView.setDefendView(new DefendView());
			gameView.getDefendView().setPlayer1(defender);
			gameView.getDefendView().setPlayer2(attacker);
			ArrayList<DefendViewTarget> defendViewTargets = new ArrayList<DefendViewTarget>();
			for (DefendTarget defendTarget : gameState.getDefendRequest().getCardAndTargets()) {
				DefendViewTarget defendViewTarget = new DefendViewTarget();
				defendViewTarget.setCard(gameState.getPlayers().get(defender).getHand().get(defendTarget.getCard()));
				defendViewTarget.setTransfer(defendTarget.getTransfer());
				defendViewTarget.setUser(defendTarget.getUser());
				defendViewTargets.add(defendViewTarget);
			}
			gameView.getDefendView().setCardAndTargets(defendViewTargets);
		}
		
		return gameView;
	}
}
