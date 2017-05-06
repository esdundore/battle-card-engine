package card.controller;

import org.springframework.web.bind.annotation.RestController;

import card.manager.CardCache;
import card.model.BattleCard;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class AdminRequests {
	
	@Autowired
	CardCache cardCache;

    @RequestMapping("/")
    public String index() {
        return "Battle Card Engine is Online";
    }
    
    @RequestMapping("/cards")
    public Map<String, BattleCard> viewCards() {
    	return cardCache.getBattleCards();
    }
    
    @RequestMapping("/card")
    public BattleCard viewCard(
    		@RequestParam(value="id") String id) {
    	return cardCache.getBattleCards().get(id);
    }
    
}