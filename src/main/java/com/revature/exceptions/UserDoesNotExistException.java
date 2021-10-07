package com.revature.exceptions;

public class UserDoesNotExistException extends Exception {

	private static final long serialVersionUID = -5341879575243147990L;

	public UserDoesNotExistException() {
		super("User does not exist");
	}
}
