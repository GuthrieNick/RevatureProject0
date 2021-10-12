package com.revature.exceptions;

public class UserExitException extends Exception {

	private static final long serialVersionUID = -8915058546177933385L;
	
	
	public UserExitException() {
		super("User entered 'exit'");
	}
}
