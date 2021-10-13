package com.revature.bankingSystem.services;

import com.revature.bankingSystem.dao.AccountDao;
import com.revature.bankingSystem.models.Account;
import com.revature.bankingSystem.models.JointAccount;
import com.revature.bankingSystem.models.User;
import com.revature.log.Logging;

/**
 * @author Nick Guthrie
 *
 */
public class AccountService {
	private AccountDao acctDao = new AccountDao();
	/**
	 * Transfer money from one account to another
	 * 
	 * @param sender   Account from which the amount is taken
	 * @param receiver Account to which the amount is given
	 * @param amount   Amount to transfer
	 * @return Array of updated accounts or null if transfer failed
	 */
	public boolean transfer(int sender, int receiver, double amount) {
		if (acctDao.transfer(sender, receiver, amount)) {
			Logging.logger
					.info(String.format("$%.2f transferred from Account #%d to Account #%d", amount, sender, receiver));
			return true;
		}

		Logging.logger.warn(String.format("Unsuccessful attempt to transfer $%.2f from Account #%d to Account #%d",
				amount, sender, receiver));
		return false;
	}

	/**
	 * Withdraw money from a user's account
	 * 
	 * @param account Account to update
	 * @param amount  Amount to remove from user's account
	 * @return Account with updated balance or null if failed
	 */
	public Account withdraw(Account account, double amount) {
		if (amount < 0)
			return null;
		if (acctDao.transfer(account.getId(), -amount)) {
			account.setBalance(account.getBalance() - amount);
			Logging.logger.info(String.format("$%.2f withdrawn from %s Account #%d. New balance: $%.2f", amount,
					account.getType(), account.getId(), account.getBalance()));
			return account;
		}

		Logging.logger.warn(String.format("Unsuccessful attempt to withdraw $%.2f from %s Account #%d", amount,
				account.getType(), account.getId()));

		return null;
	}

	/**
	 * Deposit money into a user's account
	 * 
	 * @param account Account to update
	 * @param amount  Amount to add to from user's account
	 * @return Account with updated balance or null if failed
	 */
	public Account deposit(Account account, double amount) {
		if (amount < 0)
			return null;
		if (acctDao.transfer(account.getId(), amount)) {
			account.setBalance(account.getBalance() + amount);
			Logging.logger.info(String.format("$%.2f deposited into %s Account #%d. New balance: $%.2f", amount,
					account.getType(), account.getId(), account.getBalance()));
			return account;
		}

		Logging.logger.warn(String.format("Unsuccessful attempt to deposit $%.2f into %s Account #%d", amount,
				account.getType(), account.getId()));

		return null;
	}

	
	
	private static String acctInfo(Account account) {
		return String.format("%s Account #%d owned by %s", account.getType(), account.getId(),
				(account.getClass() == JointAccount.class)
						? account.getOwner().getUsername() + " and "
								+ ((JointAccount) account).getSecondOwner().getUsername()
						: account.getOwner().getUsername());
	}

	/**
	 * Close an account and transfer remaining from it to another account
	 * 
	 * @param account  Account to close
	 * @param receiver Account to receive the remaining balance
	 * @param closer   User who closed the account
	 * @return If the account was successfully closed
	 */
	public boolean closeAccount(Account account, Account receiver, User closer) {
		if (acctDao.closeAccount(account, receiver)) {
			Logging.logger.info(String.format("%s remaining balance transfered to %s and closed by %s",
					acctInfo(account), acctInfo(receiver), closer.getUsername()));
			return true;
		}

		Logging.logger.warn(String.format("Failed to close account %s as requested by %s", acctInfo(account),
				closer.getUsername()));
		return false;
	}

	/**
	 * Close an account
	 * 
	 * @param account Account to close
	 * @param closer  User who closed the account
	 * @return If the account was closed
	 */
	public boolean closeAccount(Account account, User closer) {
		if (acctDao.closeAccount(account.getId())) {
			Logging.logger.info(String.format("%s closed by %s.", acctInfo(account), closer.getUsername()));
			return true;
		} else {
			Logging.logger
					.warn(String.format("%s unsuccessfully attempted to close %s", closer.getUsername(), acctInfo(account)));
			return false;
		}
	}
}
