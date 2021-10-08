package com.revature.bankingSystem.cli;

import com.revature.bankingSystem.models.User;
import com.revature.cli.Command;

public class EmployeeCommandSystem extends BankingCommandSystem {
	public EmployeeCommandSystem(User person) {
		super(person);
	}
	
	@Command(brief="Retrieve an account application from the database to approve or deny")
	public void application() {
		
	}
	
	@Command(brief="View information about a customer's accounts")
	public void viewInfo() {
		
	}
	
	
}
