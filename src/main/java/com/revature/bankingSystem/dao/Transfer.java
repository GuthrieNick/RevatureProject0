package com.revature.bankingSystem.dao;

import java.sql.Timestamp;

public class Transfer {
	private int senderId;
	private int receiverId;
	private double amount;
	private Timestamp datetime;

	public Transfer(int senderId, int receiverId, double amount, Timestamp datetime) {
		super();
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.amount = amount;
		this.datetime = datetime;
	}

	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	public int getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
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
		return "Transfer [senderId=" + senderId + ", receiverId=" + receiverId + ", amount=" + amount + ", datetime="
				+ datetime + "]";
	}

}
