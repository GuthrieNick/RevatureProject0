package com.revature.bankingSystem.cli;

import com.revature.bankingSystem.dao.AccountDao;
import com.revature.bankingSystem.dao.ApplicationDao;
import com.revature.bankingSystem.dao.UserDao;
import com.revature.bankingSystem.models.User;
import com.revature.bankingSystem.services.AccountService;
import com.revature.bankingSystem.services.ApplicationService;
import com.revature.bankingSystem.services.UserService;
import com.revature.cli.Command;
import com.revature.cli.CommandSystem;
import com.revature.cli.Commands;
import com.revature.log.Logging;

public abstract class BankingCommandSystem extends CommandSystem {
	private User user;
	
	// Services
	protected UserService userService;
	protected AccountService accountService;
	protected ApplicationService applicationService;
	
	// Data access objects
	protected UserDao uDao;
	protected AccountDao acctDao;
	protected ApplicationDao appDao;
	
	
	public BankingCommandSystem(User person) {
		this.user = person;
		userService = new UserService();
		accountService = new AccountService();
		applicationService = new ApplicationService();
		
		uDao = new UserDao();
		acctDao = new AccountDao();
		appDao = new ApplicationDao();
	}
	
	protected boolean UpdateUser(User user) {
		if (user.getId() != this.user.getId())
			return false;
		this.user = user;
		return true;
	}
	
	protected User getUser() {
		return user;
	}
	
	/**
	 * Prompt the user to input an account number and convert it to an integer
	 * @param msg Message to prompt the user
	 * @return User account input converted to an integer
	 */
	protected int getAccountNumber(String msg) {
		String input;
		int acct;
		
		// Input loop
		while (true) {
			input = GetInput(msg);
			
			// Stop looking for an account number
			if (input.equals("exit"))
				return -1;
			
			// Check account number is an integer and positive
			else if (!Commands.getValueType(input).equals("integer") || (acct = Integer.parseInt(input)) < 0) 
				TellUser("Error: That is not a valid account number. Account numbers are positive integers.");
			
			// Make sure account exists
			else if (acctDao.getAccount(acct) == null)
					TellUser("Error: That account does not exist.\n");
			
			// All above is true
			else break;
		}
		
		return acct;
	}

	@Override
	public String InputPrompt() { return user.getLevel() + ":" + user.getUsername() + ">>>"; }
	@Override
	public String TerminatingCommand() { return "logout"; }
	
	@Command(brief= "Change password")
	public String password() {
		String cur_password = GetInput("Enter your current password: ");
		if (!cur_password.equals(user.getPassword())) {
			Logging.logger.warn("User tried to change password, entered wrong password");
			return "Error: That password does not match. Password could not be changed.";
		}
		
		String new_password = GetInput("Enter your new password: ");
		String password_check = GetInput("Enter your new password again: ");
		
		if (!new_password.equals(password_check)) {
			Logging.logger.warn("User tried to change password, entries did not match");
			return "Error: Passwords do not match. Password could not be changed.";
		}
			
		
		user.setPassword(new_password);
		if (uDao.updateUser(user)) {
			Logging.logger.info("User successfully changed password");
			return "Password successfully changed.";
		}
		user.setPassword(cur_password);
		Logging.logger.warn("An error was encountered while trying to change user's password");
		return "Error: There was an issue changing your password. Please try again.";
	}

}
