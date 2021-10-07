package com.revature.exceptions;

public class UsernameExistsException extends Exception {

	private static final long serialVersionUID = 4836818490443331095L;
	
	public UsernameExistsException(String msg) {
		super(msg);
	}
	
	public UsernameExistsException() {
		super("Username already exists");
	}

}
