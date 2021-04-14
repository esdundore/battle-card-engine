package card.controller;

import org.springframework.web.bind.annotation.RestController;

import card.enums.GamePhase;
import card.manager.GameManager;
import card.model.requests.SkillRequest;
import card.model.requests.TargetRequest;
import card.model.requests.GutsRequest;
import card.model.requests.PlayersRequest;
import card.model.view.GameView;
import card.model.view.PlayableView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
public class GameActions {
	
	@Autowired
	GameManager gameManager;
    
    @RequestMapping(value = "/attack",
    		method = RequestMethod.POST, 
    		consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public PlayableView attack(
    		@RequestBody SkillRequest skillRequest) throws Exception {
    	return gameManager.useSkill(skillRequest, GamePhase.ATTACK);
    }
    
    @RequestMapping(value = "/defend",
    		method = RequestMethod.POST, 
    		consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public PlayableView defend(
    		@RequestBody SkillRequest skillRequest) throws Exception {
    	return gameManager.useSkill(skillRequest, GamePhase.DEFENSE);
    }
    
    @RequestMapping(value = "/attack-target",
    		method = RequestMethod.POST, 
    		consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public void attackTarget(
    		@RequestBody TargetRequest targetRequest) throws Exception {
    	gameManager.declareAttackTarget(targetRequest);
    }
    
    @RequestMapping(value = "/defense-target",
    		method = RequestMethod.POST, 
    		consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public PlayableView defenseTarget(
    		@RequestBody TargetRequest targetRequest) throws Exception {
    	return gameManager.declareDefenseTarget(targetRequest);
    }
     
    @RequestMapping(value = "/end-defense",
    		method = RequestMethod.POST, 
    		consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public GameView endDefensePhase(
    		@RequestBody PlayersRequest playersRequest) throws Exception {
        return gameManager.endDefense(playersRequest);
    }
    
    @RequestMapping(value = "/end-turn",
    		method = RequestMethod.POST, 
    		consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public void endTurn(
    		@RequestBody GutsRequest gutsRequest) throws Exception {
        gameManager.endTurn(gutsRequest);
    }

    @RequestMapping(value = "/find-plays",
    		method = RequestMethod.POST, 
    		consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public PlayableView findPlayableCardsAndTargets(
    		@RequestBody PlayersRequest playersRequest) {
        return gameManager.findPlayables(playersRequest);
    }
    
    @RequestMapping(value = "/get-view",
    		method = RequestMethod.POST, 
    		consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public GameView getGame(
    		@RequestBody PlayersRequest playersRequest) {
        return gameManager.findGameView(playersRequest);
    }

}