package com.purnima.jain.liquibase.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.purnima.jain.liquibase.exception.MissingTagException;
import com.purnima.jain.liquibase.service.RollbackService;

@RestController
public class LiquibaseSpringBootRestController {

	private static final Logger logger = LoggerFactory.getLogger(LiquibaseSpringBootRestController.class);

	private final RollbackService rollbackService;

	@Autowired
	public LiquibaseSpringBootRestController(RollbackService rollbackService) {
		this.rollbackService = rollbackService;
	}

	@GetMapping("/rollback/tag/{tagToRollBackTo}")
	public ResponseEntity<String> rollback(@PathVariable String tagToRollBackTo) {
		logger.info("Tag to rollback to is: " + tagToRollBackTo);

		try {
			rollbackService.rollbackToTag(tagToRollBackTo);
			return ResponseEntity.ok().body("Rollback to tag " + tagToRollBackTo + " is successful.");
		} catch (MissingTagException missingTagException) {
			return ResponseEntity.badRequest().body(missingTagException.getMessage());
		} catch (Exception ex) {
			return ResponseEntity.internalServerError().body(ex.getMessage());
		}
	}

}
