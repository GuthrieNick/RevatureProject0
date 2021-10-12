package com.revature.bankingSystem.models;

public class Account {
	public enum Type {
		Savings, Checking
	};

	private int id;
	int ownerId;
	Type type;
	double balance = 0;

	public Account(int id, int owner_id, Type type, double balance) {
		super();
		this.id = id;
		this.ownerId = owner_id;
		this.type = type;
		this.balance = balance;
	}

	public Account(int owner_id, Type type) {
		super();
		this.ownerId = owner_id;
		this.type = type;
	}
	
	public Account(Application app) {
		this.ownerId = app.getUserId();
		this.type = app.getAcctType();
		this.balance = 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int owner_id) {
		this.ownerId = owner_id;
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
