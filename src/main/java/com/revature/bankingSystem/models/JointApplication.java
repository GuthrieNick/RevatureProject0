package com.revature.bankingSystem.models;

public class JointApplication extends Application {
	User secondUser;

	public JointApplication(User firstUser, User secondUser, Account.Type type) {
		super(firstUser, type);
		this.secondUser = secondUser;
	}
	
	public JointApplication(int id, User firstUser, User secondUser, Account.Type type) {
		super(id, firstUser, type);
		this.secondUser = secondUser;
	}

	public int getSecondUserId() {
		return secondUser.getId();
	}
	
	public User getSecondUser() {
		return this.secondUser;
	}

	public void setSecondUser(User user) {
		this.secondUser = user;
	}

	@Override
	public String toString() {
		return "Joint " + super.toString() + " and User " + secondUser.getUsername();
	}
}
