package com.revature.bankingSystem.dao;

import java.sql.Timestamp;

public class Transaction {
	private int sender_id;
	private String receiver;
	private double amount;
	private Timestamp datetime;

	public Transaction(int sender_id, String receiver, double amount, Timestamp datetime) {
		super();
		this.sender_id = sender_id;
		this.receiver = receiver;
		this.amount = amount;
		this.datetime = datetime;
	}

	public int getSender_id() {
		return sender_id;
	}

	public void setSender_id(int sender_id) {
		this.sender_id = sender_id;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Timestamp getDatetime() {
		return datetime;
	}

	public void setDatetime(Timestamp datetime) {
		this.datetime = datetime;
	}

	@Override
	public String toString() {
		return "Transaction [sender_id=" + sender_id + ", receiver=" + receiver + ", amount=" + amount + ", datetime="
				+ datetime + "]";
	}

}
