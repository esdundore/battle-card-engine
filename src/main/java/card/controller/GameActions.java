package card.controller;

import org.springframework.web.bind.annotation.RestController;

import card.manager.GameManager;
import card.model.game.GameState;
import card.model.requests.AttackRequest;
import card.model.requests.DefendRequest;
import card.model.requests.GutsRequest;
import card.model.requests.PlayersRequest;

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
    		consumes = MediaType.APPLICATION_XML_VALUE)
    public GameState startup(
    		@RequestBody PlayersRequest playersRequest) {
        return gameManager.startup(playersRequest);
    }
    
    @RequestMapping(value = "/make-guts",
    		method = RequestMethod.POST, 
    		consumes = MediaType.APPLICATION_XML_VALUE)
    public GameState makeGuts(
    		@RequestBody GutsRequest gutsRequest) {
        return gameManager.makeGuts(gutsRequest);
    }
    
    @RequestMapping(value = "/attack",
    		method = RequestMethod.POST, 
    		consumes = MediaType.APPLICATION_XML_VALUE)
    public GameState attack(
    		@RequestBody AttackRequest attackRequest) {
        return gameManager.attack(attackRequest);
    }
    
    @RequestMapping(value = "/defend",
    		method = RequestMethod.POST, 
    		consumes = MediaType.APPLICATION_XML_VALUE)
    public GameState defend(
    		@RequestBody DefendRequest defendRequest) {
        return gameManager.defend(defendRequest);
    }
    
    @RequestMapping(value = "/end-attack",
    		method = RequestMethod.POST, 
    		consumes = MediaType.APPLICATION_XML_VALUE)
    public GameState endPhase(
    		@RequestBody PlayersRequest playersRequest) {
        return gameManager.endAttack(playersRequest);
    }
    
    @RequestMapping(value = "/get-game",
    		method = RequestMethod.POST, 
    		consumes = MediaType.APPLICATION_XML_VALUE)
    public GameState getGame(
    		@RequestBody PlayersRequest playersRequest) {
        return gameManager.getGameView(playersRequest);
    }
    
}