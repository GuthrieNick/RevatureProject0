package com.revature.bankingSystem.models;

public class JointAccount {
	public int acct_id;
	public int owner_id;

	public JointAccount(int acct_id, int owner_id) {
		super();
		this.acct_id = acct_id;
		this.owner_id = owner_id;
	}

	public int getAcct_id() {
		return acct_id;
	}

	public void setAcct_id(int acct_id) {
		this.acct_id = acct_id;
	}

	public int getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(int owner_id) {
		this.owner_id = owner_id;
	}

}
