package card.controller;

import org.springframework.web.bind.annotation.RestController;

import card.manager.GameManager;
import card.model.GameState;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class GameRequests {

	@Autowired
	GameManager gameManager;
	
	/*
	 * Takes two player ids and generates a match
	 */
    @RequestMapping("/start-match")
    public GameState startup(
    		@RequestParam(value="player1") String player1, 
    		@RequestParam(value="player2") String player2) {
        return gameManager.startup(player1, player2);
    }
    
}