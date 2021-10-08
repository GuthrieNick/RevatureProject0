package com.revature.bankingSystem.cli;

import com.revature.bankingSystem.dao.UserDao;
import com.revature.bankingSystem.models.User;
import com.revature.cli.Command;

public class CustomerCommandSystem extends BankingCommandSystem {
	public CustomerCommandSystem(User person) {
		super(person);
	}

	@Command(brief="Submit an application for a new account")
	public String newAccount() {
		char type;
		while (true) {
			type = GetInput("Enter 'S' for a new savings account or 'C' for a new checking account: ").toLowerCase().charAt(0);
			
			if (type == 'c')
				break;
			else if (type == 's')
				break;
			else
				TellUser("Error: Input must be 'S' or 'C'.");
		}
		
		String joint = GetInput("Is this a joint account? (Y/N) ");
		if (joint.length() > 0 && Character.toLowerCase(joint.charAt(0)) == 'y') {
			while (true) {
				String username = GetInput("Enter the username of the joint owner: ");
				User joint_owner = UserDao.getUserByUsername(username);
				if (joint_owner == null)
					TellUser("Error: User with that name could not be found. Try again.");
				else break;
			}
		}
		
		
		return null;
	}

	@Command(brief = "Transfer money from one account to another. Can be between two of your accounts or with an account owned by someone else.")
	public void transfer() {

	}

	@Command(brief = "Log a new transaction")
	public void newTransaction() {

	}

	@Command(brief = "Log a deposit")
	public void deposit() {

	}

	@Command(brief = "Log a withdrawal")
	public void withdraw() {

	}
}
