package com.revature.bankingSystem.dao;

public class User extends BankDao{
	public enum Level { Customer, Employee, Admin };
	
	private Level level;
	private int id;
	private String username;
	private String password;
	
	public User(int id) {
		// Look up user by id in bank
	}
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public User(String username, String password, Level level) {
		this(username, password);
		this.level = level;
	}
	
	
	public String getUsername() {
		return username;
	}
	
	public int getID() {
		return id;
	}
	
	public Level getLevel() {
		return level;
	}
	
	/**
	 * Create a new user in the database
	 * @param username
	 * @param password
	 * @return
	 */
	public User createPerson(String username, String password) {
		return new User(username, password);
	}

	@Override
	public String getValues() {
		return String.format("(%s, %s, %d", username, password, level.ordinal());
	}
}
