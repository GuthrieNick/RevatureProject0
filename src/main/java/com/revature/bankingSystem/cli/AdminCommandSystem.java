package com.revature.bankingSystem.cli;

import com.revature.bankingSystem.models.User;
import com.revature.cli.Command;

public class AdminCommandSystem extends EmployeeCommandSystem {
	public AdminCommandSystem(User person) {
		super(person);
	}
	
	@Command(brief="Assign login credentials to a new employee")
	public void assignEmployee() {
		//TODO: Implement AdminCommandSystem.assignEmployee()
	}
	
	@Command(brief="Transfer money between two accounts")
	public void transfer() {
		//TODO: Implement AdminCommandSystem.transfer()
		
	}
	
	@Command(brief="Close one of a customer's accounts")
	public void cancelAccount() {
		//TODO: Implement AdminCommandSystem.cancelAccount()
		
	}
}
