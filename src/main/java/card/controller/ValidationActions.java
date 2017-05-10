package card.controller;

import org.springframework.web.bind.annotation.RestController;

import card.manager.ValidationManager;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class ValidationActions {

	@Autowired
	ValidationManager validationManager;
	
    
}