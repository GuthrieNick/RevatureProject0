package com.revature.bankingSystem.models;

import com.revature.bankingSystem.models.Account.Type;

public class Application {
	private int id;
	private User user;
	private Account.Type acctType;

	public Application(int id, User user, Type acct_type) {
		super();
		this.id = id;
		this.user = user;
		this.acctType = acct_type;
	}

	public Application(User user, Type acct_type) {
		super();
		this.user = user;
		this.acctType = acct_type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return user.getId();
	}

	public Account.Type getAcctType() {
		return acctType;
	}

	public void setAcctType(Account.Type acct_type) {
		this.acctType = acct_type;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return String.format("Application (id #%d): %s for User %s", id, acctType, user.getUsername());
	}
}
