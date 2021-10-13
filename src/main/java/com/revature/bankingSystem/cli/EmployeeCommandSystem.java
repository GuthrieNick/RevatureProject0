package com.revature.bankingSystem.cli;

import java.util.List;

import com.revature.bankingSystem.models.Account;
import com.revature.bankingSystem.models.Application;
import com.revature.bankingSystem.models.JointApplication;
import com.revature.bankingSystem.models.User;
import com.revature.cli.Command;

public class EmployeeCommandSystem extends BankingCommandSystem {
	public EmployeeCommandSystem(User person) {
		super(person);
	}

	@Command(brief = "Retrieve an account application from the database to approve or deny")
	public String application() {
		Application application = appDao.getApplication();
		if (application.getId() == 0)
			return "There are currently no open applications.";

		TellUser(application.toString());
		if (YesOrNo("Approve account?")) {
			if (application.getClass() == JointApplication.class) {
				if (applicationService.approveJointApplication((JointApplication) application))
					return "Joint application approved.";

				return "Error: Application could not be approved.";
			}

			else {
				if (applicationService.approveApplication(application))
					return "Application approved.";
				return "Error: Application could not be approved.";
			}

		} else {
			if (application.getClass() == JointApplication.class) {
				if (applicationService.denyJointApplication((JointApplication) application))
					return "Joint application denied.";
			} else if (applicationService.denyApplication(application))
				return "Application denied.";
		}

		return "Error: An issue was encountered. Application could not be approved or denied.";
	}

	@Command(brief = "View information about a customer's accounts")
	public String viewInfo() {
		String username = GetInput("Enter customer's username: ");
		User user = uDao.getUserByUsername(username);
		if (user == null)
			return "Error: User could not be found.";
		else
			TellUser("User found. Retrieving information...");

		StringBuilder sb = new StringBuilder();

		sb.append("\nAccounts:\n");
		for (Account account : acctDao.getUserAccounts(user.getId()))
			sb.append(account.toString()).append('\n');

		sb.append("\nCurrent applications:\n");
		List<Application> apps = appDao.peekApplications(user.getId());
		if (apps.size() == 0)
			sb.append("None\n");
		else
			for (Application app : appDao.peekApplications(user.getId()))
				sb.append(app.toString()).append('\n');

		return sb.toString();
	}

}
