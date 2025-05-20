package com.purnima.jain.liquibase.exception;

public class MissingTagException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MissingTagException(String errorMessage) {
		super(errorMessage);
	}

}
