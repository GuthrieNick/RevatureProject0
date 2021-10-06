package com.revature.exceptions;

public class InvalidCommandException extends Exception {

	private static final long serialVersionUID = -7608780631486940968L;
	
	public InvalidCommandException(String msg) {
		super(msg);
	}
	
	public InvalidCommandException() {
		super("Invalid command");
	}

}
