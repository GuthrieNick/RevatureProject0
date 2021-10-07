package com.revature.bankingSystem.models;

public class Account {
	public enum Type { Savings, Checking };
	int owner_id;
	Type type;
	double balance = 0;
	
	public Account(int owner_id, Type type, double balance) {
		this.owner_id = owner_id;
		this.type = type;
		this.balance = balance;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public double getBalance() {
		return this.balance;
	}
	
	public double withdraw(double amount) {
		return this.balance -= amount;
	}
	
	public double deposit(double amount) {
		return this.balance += amount;
	}
}
