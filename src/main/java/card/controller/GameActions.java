package card.controller;

import org.springframework.web.bind.annotation.RestController;

import card.manager.GameManager;
import card.model.requests.AttackRequestNoMap;
import card.model.requests.DefendRequest;
import card.model.requests.GutsRequest;
import card.model.requests.PlayersRequest;
import card.model.view.GameView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
public class GameActions {

	@Autowired
	GameManager gameManager;
	
	/*
	 * Takes two player ids and generates a match
	 */
    @RequestMapping(value = "/start-match",
    		method = RequestMethod.POST, 
    		consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public void startup(
    		@RequestBody PlayersRequest playersRequest) {
        gameManager.startup(playersRequest);
    }
    
    @RequestMapping(value = "/make-guts",
    		method = RequestMethod.POST, 
    		consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public void makeGuts(
    		@RequestBody GutsRequest gutsRequest) {
        gameManager.makeGuts(gutsRequest);
    }
    
    @RequestMapping(value = "/attack",
    		method = RequestMethod.POST, 
    		consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public void attack(
    		@RequestBody AttackRequestNoMap attackRequest) {
        gameManager.attack(attackRequest);
    }
    
    @RequestMapping(value = "/defend",
    		method = RequestMethod.POST, 
    		consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public void defend(
    		@RequestBody DefendRequest defendRequest) {
        gameManager.defend(defendRequest);
    }
    
    @RequestMapping(value = "/end-attack",
    		method = RequestMethod.POST, 
    		consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public void endPhase(
    		@RequestBody PlayersRequest playersRequest) {
        gameManager.endAttack(playersRequest);
    }
    
    @RequestMapping(value = "/get-game",
    		method = RequestMethod.POST, 
    		consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public GameView getGame(
    		@RequestBody PlayersRequest playersRequest) {
        return gameManager.getGameView(playersRequest);
    }
    
}