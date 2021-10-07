package com.revature.bankingSystem.models;

public class Account {
	public enum Type {
		Savings, Checking
	};

	private int id;
	int owner_id;
	Type type;
	double balance = 0;

	public Account(int id, int owner_id, Type type, double balance) {
		super();
		this.id = id;
		this.owner_id = owner_id;
		this.type = type;
		this.balance = balance;
	}

	public Account(int owner_id, Type type, double balance) {
		super();
		this.owner_id = owner_id;
		this.type = type;
		this.balance = balance;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(int owner_id) {
		this.owner_id = owner_id;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

}
