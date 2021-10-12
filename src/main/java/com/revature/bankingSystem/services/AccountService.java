package com.revature.bankingSystem.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.revature.bankingSystem.dao.AccountDao;
import com.revature.bankingSystem.dao.ConnectionUtil;
import com.revature.bankingSystem.models.Account;
import com.revature.bankingSystem.models.Application;

/**
 * @author Nick Guthrie
 *
 */
public class AccountService {
	private static ConnectionUtil conUtil = ConnectionUtil.getConnectionUtil();
	
	/**
	 * Create a new account and notify the user
	 * @param app Application to approve
	 * @return If the application was successfully approved
	 */
	public static boolean approveApplication(Application app) {
		Account acct = AccountDao.createAccount(new Account(app.getUserId(), app.getAcctType()));
		Connection con = conUtil.getConnection();
		
		try {
			PreparedStatement ps = con.prepareStatement("INSERT INTO approved_applications (user_id, acct_id) VALUES (?, ?)");
			ps.setInt(1, app.getUserId());
			ps.setInt(1, acct.getId());
			return ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Transfer money from one account to another
	 * @param sender Account from which the amount is taken
	 * @param receiver Account to which the amount is given
	 * @param amount Amount to transfer
	 * @return Array of updated accounts or null if transfer failed
	 */
	public static boolean transfer(int sender, int receiver, double amount) {
		return AccountDao.transfer(sender, receiver, amount);
	}
	
	/**
	 * Withdraw money from a user's account
	 * @param account Account to update
	 * @param amount Amount to remove from user's account
	 * @return Account with updated balance or null if failed
	 */
	public static Account withdraw(Account account, double amount) {
		if (amount < 0)
			return null;
		if (AccountDao.transfer(account.getId(), -amount)) {
			account.setBalance(account.getBalance() - amount);
			return account;
		}
		
		return null;
	}
	
	/**
	 * Deposit money into a user's account
	 * @param account Account to update
	 * @param amount Amount to add to from user's account
	 * @return Account with updated balance or null if failed
	 */
	public static Account deposit(Account account, double amount) {
		if (amount < 0)
			return null;
		if (AccountDao.transfer(account.getId(), amount)) {
			account.setBalance(account.getBalance() + amount);
			return account;
		}
		
		return null;
	}
}
