package com.revature.bankingSystem.dao;

public class Transaction extends BankDao {
	// TODO Fix datetime
	private int sender_id;
	private String receiver;
	private double amount;
	private String datetime;
	
	
	
	public Transaction(int sender_id, String receiver, double amount, String datetime) {
		super();
		this.sender_id = sender_id;
		this.receiver = receiver;
		this.amount = amount;
		this.datetime = datetime;
	}



	@Override
	public String getValues() {
		return String.format("(%d, %s, %f, %s", sender_id, receiver, amount, datetime);
	}

}
