package com.revature.bankingSystem.models;

public class JointAccount extends Account {
	public int second_owner_id;

	public JointAccount(int first_owner_id, int second_owner_id, Type type) {
		super(first_owner_id, type);
		this.second_owner_id = second_owner_id;
	}

	public int getSecond_owner_id() {
		return second_owner_id;
	}

	public void setSecond_owner_id(int second_owner_id) {
		this.second_owner_id = second_owner_id;
	}

}
