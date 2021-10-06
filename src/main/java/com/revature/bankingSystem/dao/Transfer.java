package com.revature.bankingSystem.dao;

public class Transfer extends BankDao {
	private int sender_id;
	private int receiver_id;
	private double amount;
	private String datetime;
	
	
	
	public Transfer(int sender_id, int receiver_id, double amount, String datetime) {
		super();
		this.sender_id = sender_id;
		this.receiver_id = receiver_id;
		this.amount = amount;
		this.datetime = datetime;
	}



	@Override
	public String getValues() {
		return String.format("(%d, %d, %f, %s)", sender_id, receiver_id, amount, datetime);
	}

}
