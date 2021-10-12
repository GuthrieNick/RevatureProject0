package com.revature.bankingSystem.models;

public class JointAccount extends Account {
	public int secondOwnerId;

	public JointAccount(int first_owner_id, int second_owner_id, Type type) {
		super(first_owner_id, type);
		this.secondOwnerId = second_owner_id;
	}
	
	public JointAccount(JointApplication app) {
		super(app.getUserId(), app.getAcctType());
		secondOwnerId = app.getSecond_user_id();
	}

	public int getSecondOwnerId() {
		return secondOwnerId;
	}

	public void setSecondOwnerId(int second_owner_id) {
		this.secondOwnerId = second_owner_id;
	}

}
