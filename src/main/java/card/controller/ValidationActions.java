package card.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import card.manager.ValidationManager;
import card.model.requests.PlayableRequest;
import card.model.view.PlayableCardView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@RestController
public class ValidationActions {

	@Autowired
	ValidationManager validationManager;
	
    @RequestMapping(value = "/playable-cards",
    		method = RequestMethod.POST, 
    		consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public PlayableCardView findPlayableCards(
    		@RequestBody PlayableRequest playableRequest) {
        return validationManager.findPlayableCards(playableRequest);
    }
    
}