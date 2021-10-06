package com.revature.bankingSystem.dao;

public class Account extends BankDao {
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
	
	@Override
	public String getValues() {
		return String.format("(%d, %d, %f)", owner_id, type.ordinal(), balance);
	}
}
