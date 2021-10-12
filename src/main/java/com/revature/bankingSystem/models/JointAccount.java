package com.revature.bankingSystem.models;

public class JointAccount extends Account {
	public User secondUser;
	
	public JointAccount(User firstUser, User secondUser, Type type) {
		super(firstUser, type);
		this.secondUser = secondUser;
	}
	
	public JointAccount(int id, User firstUser, User secondUser, Type type, double balance) {
		super(id, firstUser, type, balance);
		this.secondUser = secondUser;
	}

	
	public JointAccount(JointApplication app) {
		super(app.getUser(), app.getAcctType());
		this.secondUser = app.getSecondUser();
	}

	public int getSecondOwnerId() {
		return secondUser.getId();
	}
	
	public User getSecondOwner() {
		return this.secondUser;
	}

	public void setSecondOwner(User secondUser){
		this.secondUser = secondUser;
	}

	
	public String toString() {
		return String.format("Joint %s Account #%d\n    with User %s\n    Balance: $%.2f\n", getType(), getId(), secondUser.getUsername(), getBalance());
	}
}
