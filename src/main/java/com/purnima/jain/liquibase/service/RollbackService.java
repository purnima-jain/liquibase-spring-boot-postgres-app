package com.purnima.jain.liquibase.service;

public interface RollbackService {

	public void rollbackToTag(String tagToRollBackTo);

}
