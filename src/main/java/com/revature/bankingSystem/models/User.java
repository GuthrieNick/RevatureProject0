package com.revature.bankingSystem.models;

public class User {
	public enum Level {
		Customer, Employee, Admin
	};

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

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [level=" + level + ", id=" + id + ", username=" + username + ", password=" + password + "]";
	}

}
