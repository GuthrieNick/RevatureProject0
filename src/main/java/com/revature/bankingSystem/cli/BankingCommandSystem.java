package com.revature.bankingSystem.cli;

import com.revature.bankingSystem.dao.UserDao;
import com.revature.bankingSystem.models.User;
import com.revature.cli.Command;
import com.revature.cli.CommandSystem;
import com.revature.log.Logging;

public abstract class BankingCommandSystem extends CommandSystem {
	private User user;
	
	public BankingCommandSystem(User person) {
		this.user = person;
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
		if (UserDao.updateUser(user)) {
			Logging.logger.info("User successfully changed password");
			return "Password successfully changed.";
		}
		user.setPassword(cur_password);
		Logging.logger.warn("An error was encountered while trying to change user's password");
		return "Error: There was an issue changing your password. Please try again.";
	}

}
