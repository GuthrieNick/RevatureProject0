package com.revature.bankingSystem.models;

public class Account {
	public enum Type {
		Savings, Checking
	};
	
	/**
	 * Convert a number to its type
	 * @param x
	 * @return
	 */
	public static Type getType(int x) {
		switch (x) {
		case 0: return Type.Savings;
		case 1: return Type.Checking;
		default: return null;
		}
	}

	private int id;
	private User user;
	private Type type;
	private double balance = 0;

	public Account(int id, User owner, Type type, double balance) {
		super();
		this.id = id;
		this.user = owner;
		this.type = type;
		this.balance = balance;
	}

	public Account(User owner, Type type) {
		super();
		this.user = owner;
		this.type = type;
	}
	
	public Account(Application app) {
		this.user = app.getUser();
		this.type = app.getAcctType();
		this.balance = 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public User getOwner() {
		return this.user;
	}

	public int getOwnerId() {
		return user.getId();
	}

	public void setOwner(User owner) {
		this.user = owner;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public void addToBalance(double balance) {
		this.balance += balance;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public String toString() {
		return String.format("%s Account #%d\n    Balance: $%.2f\n", type, id, balance);
	}

}
