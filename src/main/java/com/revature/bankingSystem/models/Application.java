package com.revature.bankingSystem.models;

import com.revature.bankingSystem.models.Account.Type;

public class Application {
	private int id;
	private int userId;
	private Account.Type acctType;

	public Application(int id, int user_id, Type acct_type) {
		super();
		this.id = id;
		this.userId = user_id;
		this.acctType = acct_type;
	}

	public Application(int user_id, Type acct_type) {
		super();
		this.userId = user_id;
		this.acctType = acct_type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int user_id) {
		this.userId = user_id;
	}

	public Account.Type getAcctType() {
		return acctType;
	}

	public void setAcctType(Account.Type acct_type) {
		this.acctType = acct_type;
	}

	@Override
	public String toString() {
		return "Application [id=" + id + ", userId=" + userId + ", acctType=" + acctType + "]";
	}
}
