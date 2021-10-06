package com.revature.bankingSystem.cli;

import com.revature.bankingSystem.dao.User;
import com.revature.cli.Command;

public class CustomerCommandSystem extends BankingCommandSystem {
	public CustomerCommandSystem(User person) {
		super(person);
	}
	
	@Command(brief="Submit an application for a new account")
	public String newAccount() {
		char type = 0;
		while (type == 0) {
			type = GetInput("Enter 'S' for a new savings account or 'C' for a new checking account: ").toLowerCase().charAt(0);
			if (type == 'c')
				break;
			else if (type == 's')
				break;
			else
				TellUser("Error: Input must be 'S' or 'C'.");
		}
		return null;
		
		
	}
}
