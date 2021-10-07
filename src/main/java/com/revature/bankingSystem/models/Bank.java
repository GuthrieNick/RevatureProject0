package com.revature.bankingSystem.models;

import com.revature.exceptions.InvalidCredentialsException;
import com.revature.exceptions.UsernameExistsException;

/**
 * Class representing the Bank's database.
 * All interaction with the database will be done through here.
 * @author Nick Guthrie
 *
 */
public class Bank {
	
	public static boolean UserNameExists(String name) { return false; }
	
	/**
	 * Attempt to log in a user with supplied credentials
	 * @param username User's username
	 * @param password User's password
	 * @return Person DAO representing logged in User
	 * @throws InvalidCredentialsException If the credentials don't match to anyone in the DB, an exception is thrown
	 */
	public static User LogIn(String username, String password) throws InvalidCredentialsException {
		return null;
	}
	
	/**
	 * Throws an error if the passed in username is invalid
	 * @param username Username to check
	 * @throws UsernameExistsException If the username is invalid, the message of this error will explain why.
	 */
	public static void CheckUsername(String username) throws UsernameExistsException {
		for (int i = 0; i < username.length(); i++)
			if (Character.isWhitespace(username.charAt(i)))
				throw new UsernameExistsException("Username cannot contain whitespace.");
		
	
		if (username.length() == 0)
			throw new UsernameExistsException("You did not enter a username.");
		
		else if (!Character.isAlphabetic(username.charAt(0)))
			throw new UsernameExistsException("First character must be alphabetic.");
		
		else if (username.length() > 24)
			throw new UsernameExistsException("Username cannot be longer than 24 characters.");
		
		else if (Bank.UserNameExists(username))
			throw new UsernameExistsException("Username is already taken.");
	}
	
	public static boolean ValidPassword(String password) {
		return password.length() < 64;
	}
	
	/**
	 * Prompt a new customer to create an account
	 * @param username Username of the new user
	 * @param password Password of the new user
	 * @return Person Object created with entered fields
	 */
	public static User CreateAccount(String username, String password) {
		return new User(username, password);
	}
	
	
	
	@Override
	/**
	 * Cloning this object is not supported
	 */
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
}
