package com.revature.bankingSystem.models;

public class JointApplication {
	int app_id;
	int user_id;

	public JointApplication(int app_id, int user_id) {
		super();
		this.app_id = app_id;
		this.user_id = user_id;
	}

	public int getApp_id() {
		return app_id;
	}

	public void setApp_id(int app_id) {
		this.app_id = app_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

}
