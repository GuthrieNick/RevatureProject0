package com.revature.bankingSystem.cli;

import java.util.List;

import com.revature.bankingSystem.dao.AccountDao;
import com.revature.bankingSystem.dao.ApplicationDao;
import com.revature.bankingSystem.dao.UserDao;
import com.revature.bankingSystem.models.Account;
import com.revature.bankingSystem.models.Application;
import com.revature.bankingSystem.models.JointAccount;
import com.revature.bankingSystem.models.JointApplication;
import com.revature.bankingSystem.models.User;
import com.revature.cli.Command;

public class EmployeeCommandSystem extends BankingCommandSystem {
	public EmployeeCommandSystem(User person) {
		super(person);
	}
	
	@Command(brief="Retrieve an account application from the database to approve or deny")
	public String application() {
		Application application = ApplicationDao.getApplication();
		if (application.getId() == 0)
			return "There are currently no open applications."
					+ "";
		Account account = null;
		
		TellUser(application.toString());
		if (YesOrNo("Approve account?")) {
			if (application.getClass() == JointApplication.class)
				account = AccountDao.createAccount(new JointAccount((JointApplication)application));
			else
				account = AccountDao.createAccount(new Account(application));
			
			if (account != null) { 
				ApplicationDao.approveApplication(application, account.getId());
				return "Application approved.";
			}
			return "Error: Application could not be approved.";
		} else {
			if (ApplicationDao.denyApplication(application))
				return "Application denied.";
		}
		
		return "Error: An issue was encountered. Application could not be approved or denied.";
	}
	
	@Command(brief="View information about a customer's accounts")
	public String viewInfo() {
		String username = GetInput("Enter customer's username: ");
		User user = UserDao.getUserByUsername(username);
		if (user == null)
			return "Error: User could not be found.";
		else TellUser("User found. Retrieving information...");
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("\nAccounts:\n");
		for (Account account : AccountDao.getUserAccounts(user.getId()))
			sb.append(account.toString()).append('\n');
		
		sb.append("\nCurrent applications:\n");
		List<Application> apps = ApplicationDao.peekApplications(user.getId());
		if (apps.size() == 0)
			sb.append("None\n");
		else for (Application app : ApplicationDao.peekApplications(user.getId()))
			sb.append(app.toString()).append('\n');
		
		return sb.toString();
	}
	
	
}
