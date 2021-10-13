package com.revature.bankingSystem.services;

import com.revature.bankingSystem.dao.AccountDao;
import com.revature.bankingSystem.dao.ApplicationDao;
import com.revature.bankingSystem.models.Account;
import com.revature.bankingSystem.models.Application;
import com.revature.bankingSystem.models.JointAccount;
import com.revature.bankingSystem.models.JointApplication;
import com.revature.log.Logging;

public class ApplicationService {
	private ApplicationDao appDao = new ApplicationDao();
	private AccountDao acctDao = new AccountDao();

	public boolean submitApplication(Application app) {

		if ((app.getClass() == JointApplication.class && appDao.submitJointApplication((JointApplication) app))
				|| appDao.submitApplication(app)) {
			Logging.logger.info(app.toString() + " submitted");
			return true;
		}

		Logging.logger.warn("Failed to submit " + app.toString());
		return false;
	}

	/**
	 * Create a new account and notify the user
	 * 
	 * @param app Application to approve
	 * @return If the application was successfully approved
	 */
	public boolean approveApplication(Application app) {
		Account acct = acctDao.createAccount(new Account(app.getUser(), app.getAcctType()));
		if (acct != null) {
			Logging.logger.info(String.format("%s Account #%d successfully created for %s", acct.getType(),
					acct.getId(), acct.getOwner().getUsername()));
		} else {
			Logging.logger.warn(String.format("Attempt to create %s Account for %s unsuccessful", app.getAcctType(),
					app.getUser().getUsername()));
			return false;
		}

		return appDao.approveApplication(app, acct.getId());
	}

	public boolean approveJointApplication(JointApplication app) {
		JointAccount acct = acctDao
				.createJointAccount(new JointAccount(app.getUser(), app.getSecondUser(), app.getAcctType()));
		if (acct != null) {
			Logging.logger.info(String.format("Joing %s Account #%d successfully created for %s and %s", acct.getType(),
					acct.getId(), acct.getOwner().getUsername(), acct.getSecondOwner()));
		} else {
			Logging.logger.warn(String.format("Attempt to create Joint %s Account for %s and %s unsuccessful",
					app.getAcctType(), app.getUser().getUsername(), app.getSecondUser().getUsername()));
			return false;
		}

		return appDao.approveJointApplication(app, acct.getId());
	}

	public boolean denyApplication(Application app) {
		if (appDao.denyApplication(app)) {
			Logging.logger.info(app.toString() + " denied");
			return true;
		}

		Logging.logger.warn("Unable to update database for denying" + app.toString());
		return false;
	}

	public boolean denyJointApplication(JointApplication app) {
		if (appDao.denyJointApplication(app)) {
			Logging.logger.info(app.toString() + " denied");
			return true;
		}

		Logging.logger.warn("Unable to update database for denying" + app.toString());
		return false;
	}

	public boolean secondJointApplication(JointApplication app) {
		if (appDao.secondJointApplication(app.getId())) {
			Logging.logger.info(app.toString() + " seconded");
			return true;
		}

		Logging.logger.warn("Failed to second " + app.toString());
		return false;
	}
}
