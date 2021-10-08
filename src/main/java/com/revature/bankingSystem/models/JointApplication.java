package com.revature.bankingSystem.models;

public class JointApplication extends Application {
	int second_user_id;

	public JointApplication(int first_user_id, int second_user_id, Account.Type type) {
		super(first_user_id, type);
		this.second_user_id = second_user_id;
	}
	
	public JointApplication(int id, int first_user_id, int second_user_id, Account.Type type) {
		super(id, first_user_id, type);
		this.second_user_id = second_user_id;
	}

	public int getSecond_user_id() {
		return second_user_id;
	}

	public void setSecond_user_id(int second_user_id) {
		this.second_user_id = second_user_id;
	}

}
