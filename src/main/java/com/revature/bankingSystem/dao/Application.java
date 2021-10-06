package com.revature.bankingSystem.dao;

public class Application extends BankDao {
	private int user_id;
	private Account.Type type;
	
	@Override
	public String getValues() {
		return String.format("(%d, %d)", user_id, type.ordinal());
	}
}
