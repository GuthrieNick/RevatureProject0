package com.revature.bankingSystem.models;

import com.revature.bankingSystem.models.Account.Type;

public class Application {
	private int id;
	private int user_id;
	private Account.Type acct_type;

	public Application(int id, int user_id, Type acct_type) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.acct_type = acct_type;
	}

	public Application(int user_id, Type acct_type) {
		super();
		this.user_id = user_id;
		this.acct_type = acct_type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public Account.Type getAcctType() {
		return acct_type;
	}

	public void setAcctType(Account.Type acct_type) {
		this.acct_type = acct_type;
	}

}
