package card.controller;

import org.springframework.web.bind.annotation.RestController;

import card.dao.GameCache;
import card.enums.GamePhase;
import card.manager.GameManager;
import card.manager.PlayValidationManager;
import card.model.requests.SkillRequest;
import card.model.requests.TargetRequest;
import card.model.game.GameState;
import card.model.requests.PlayersRequest;
import card.model.view.GameView;
import card.model.view.PlayableView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class GameActions {

	@Autowired
	GameCache gameCache;

	@Autowired
	PlayValidationManager playValidationManager;
	
	@Autowired
	GameManager gameManager;


	@RequestMapping(value = "/start-match", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public GameView startup(@RequestBody PlayersRequest playersRequest) {
		GameState gameState = gameCache.startup(playersRequest);
		return getGameView(playersRequest, gameState);
	}
	
	@RequestMapping(value = "/start-test-match", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public GameView startupTest(@RequestParam(value="card1") String card1,
			@RequestParam(value="monster1") String monster1,
			@RequestParam(value="card2") String card2,
			@RequestParam(value="monster2") String monster2,
			@RequestBody PlayersRequest playersRequest) {
		GameState gameState = gameCache.startupTest(playersRequest, card1, monster1, card2, monster2);
		return getGameView(playersRequest, gameState);
	}
	
	@RequestMapping(value = "/attack", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public GameView attack(@RequestBody SkillRequest skillRequest) throws Exception {
		GameState gameState = gameCache.getGameState(skillRequest);
		if (playValidationManager.validRequest(skillRequest, gameState, GamePhase.ATTACK)) {
			gameManager.useSkill(skillRequest, gameState);
		} else {
			throw new Exception();
		}
		GameView gameView = getGameView(skillRequest, gameState);
//		if (gameView.getPlayable().getPlayableTargets().contains(-1)) {
//			TargetRequest targetRequest = new TargetRequest(skillRequest);
//			targetRequest.setTarget(-1);
//			attackTarget(targetRequest);
//		}
		return gameView;
	}

	@RequestMapping(value = "/attack-target", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public GameView attackTarget(@RequestBody TargetRequest targetRequest) throws Exception {
		GameState gameState = gameCache.getGameState(targetRequest);
		if (playValidationManager.validRequest(targetRequest, gameState, GamePhase.ATTACK)) {
			gameManager.declareAttackTarget(targetRequest, gameState);
		} else {
			throw new Exception();
		}
		return getGameView(targetRequest, gameState);
	}

	@RequestMapping(value = "/end-attack", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public GameView endAttackPhase(@RequestBody PlayersRequest playersRequest) throws Exception {
		GameState gameState = gameCache.getGameState(playersRequest);
		if (playValidationManager.validRequest(playersRequest, gameState, GamePhase.ATTACK)) {
			gameManager.endAttack(playersRequest, gameState);
		} else {
			throw new Exception();
		}
		return getGameView(playersRequest, gameState);
	}

	@RequestMapping(value = "/defend", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public GameView defend(@RequestBody SkillRequest skillRequest) throws Exception {
		GameState gameState = gameCache.getGameState(skillRequest);
		if (playValidationManager.validRequest(skillRequest, gameState, GamePhase.DEFENSE)) {
			gameManager.useSkill(skillRequest, gameState);
		} else {
			throw new Exception();
		}
		return getGameView(skillRequest, gameState);
	}

	@RequestMapping(value = "/defense-target", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public GameView defenseTarget(@RequestBody TargetRequest targetRequest) throws Exception {
		GameState gameState = gameCache.getGameState(targetRequest);
		if (playValidationManager.validRequest(targetRequest, gameState, GamePhase.DEFENSE)) {
			gameManager.declareDefenseTarget(targetRequest, gameState);
		} else {
			throw new Exception();
		}
		return getGameView(targetRequest, gameState);
	}

	@RequestMapping(value = "/end-defense", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public GameView endDefensePhase(@RequestBody PlayersRequest playersRequest) throws Exception {
		GameState gameState = gameCache.getGameState(playersRequest);
		if (playValidationManager.validRequest(playersRequest, gameState, GamePhase.DEFENSE)) {
			gameManager.endDefense(playersRequest, gameState);
		} else {
			throw new Exception();
		}
		return getGameView(playersRequest, gameState);
	}

	@RequestMapping(value = "/make-guts", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public GameView makeGuts(@RequestBody SkillRequest skillRequest) throws Exception {
		GameState gameState = gameCache.getGameState(skillRequest);
		if (playValidationManager.validRequest(skillRequest, gameState, GamePhase.GUTS)) {
			gameManager.makeGuts(skillRequest, gameState);
		} else {
			throw new Exception();
		}
		return getGameView(skillRequest, gameState);
	}
	
	@RequestMapping(value = "/end-turn", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public GameView endTurn(@RequestBody PlayersRequest playersRequest) throws Exception {
		GameState gameState = gameCache.getGameState(playersRequest);
		if (playValidationManager.validRequest(playersRequest, gameState, GamePhase.GUTS)) {
			gameManager.endTurn(playersRequest, gameState);
		} else {
			throw new Exception();
		}
		return getGameView(playersRequest, gameState);
	}

	@RequestMapping(value = "/get-view", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public GameView getGameView(@RequestBody PlayersRequest playersRequest) {
		GameState gameState = gameCache.getGameState(playersRequest);
		return getGameView(playersRequest, gameState);
	}

	public GameView getGameView(PlayersRequest playersRequest, GameState gameState) {
		PlayableView playableView = new PlayableView();
		if (gameState.getCurrentPlayer().equals(playersRequest.getPlayer1())) {
			playableView = playValidationManager.findPlayables(playersRequest, gameState);
		}
		return new GameView(playersRequest, gameState, playableView);
	}

}