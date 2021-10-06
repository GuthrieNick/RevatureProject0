package com.revature.exceptions;

public class InvalidCredentialsException extends Exception {

	private static final long serialVersionUID = -6573307333524845568L;
	
	public InvalidCredentialsException() {
		super("Invalid credentials");
	}
	
	public InvalidCredentialsException(String msg) {
		super(msg);
	}

}
