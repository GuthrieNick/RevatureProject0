package com.revature.bankingSystem.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.revature.bankingSystem.models.Account;
import com.revature.bankingSystem.models.JointAccount;

public class AccountDao {
	private static ConnectionUtil conUtil = ConnectionUtil.getConnectionUtil();
	
	/**
	 * Retrieve the account with the specified ID from the database
	 * @param id ID of account to look for
	 * @return Account object with values from returned row or null if account could not be found
	 */
	public static Account getAccount(int id) {
		Connection con = conUtil.getConnection();
		
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM accounts WHERE id=?");
			ps.setInt(1, id);
			
			ResultSet rs = ps.executeQuery();
			rs.next();
			return new Account(rs.getInt(1), rs.getInt(1), Account.Type.values()[rs.getInt(3)], rs.getDouble(4));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Get all the accounts of a user
	 * @param user_id User ID to use for retrieving accounts
	 * @return List of Account objects owned by the user
	 */
	public static List<Account> getUserAccounts (int user_id) {
		Connection con = conUtil.getConnection();
		List<Account> user_accts = new ArrayList<Account>();
		
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM accounts WHERE owner_id=?");
			ps.setInt(1, user_id);
			
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				user_accts.add(new Account(rs.getInt(1), rs.getInt(1), Account.Type.values()[rs.getInt(3)], rs.getDouble(4)));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return user_accts;
	}
	
	/**
	 * Create an account in the database
	 * @param account Account object holding user ID and type
	 * @return The account passed in updated with the ID and a balance of 0
	 */
	public static Account createAccount(Account account) {
		Connection con = conUtil.getConnection();
		
		try {
			PreparedStatement ps = con.prepareStatement("INSERT INTO accounts (owner_id, type) VALUES (?, ?) RETURNING id");
			ps.setInt(1, account.getOwnerId());
			ps.setInt(2, account.getType().ordinal());
			
			ResultSet rs = ps.executeQuery(); 
			while (rs.next()) {
				account.setId(rs.getInt(1));
				account.setBalance(0);
				break;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return account;
	}
	
	public static JointAccount createJointAccount(JointAccount account) {
		Connection con = conUtil.getConnection();

		try {
			CallableStatement cs = con.prepareCall("CALL create_joint_account(?, ?, ?, ?)");
			cs.setInt(1, account.getOwnerId());
			cs.setInt(2, account.getSecondOwnerId());
			cs.setInt(3, account.getType().ordinal());
			cs.registerOutParameter(4, Types.INTEGER);
			if (cs.execute()) {
				ResultSet rs = cs.getResultSet();
				rs.next();
				account.setId(rs.getInt(1));
				return account;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * Remove an account from the database
	 * @param acct_id ID of the account to delete
	 * @return
	 */
	public static boolean closeAccount(int acct_id) {
		Connection con = conUtil.getConnection();
		
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM accounts WHERE id=?");
			ps.setInt(1, acct_id);
			return ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Remove an account from the database
	 * @param account Account to remove
	 * @param receiver Account to transfer the funds to
	 * @return If the account was successfully removed
	 */
	public static boolean closeAccount(Account account, Account receiver) {
		if (account.getBalance() == 0)
			return closeAccount(account.getId());
		
		Connection con = conUtil.getConnection();
		
		try {
			PreparedStatement ps = con.prepareStatement("call close_account(?, ?)");
			ps.setInt(1, account.getId());
			ps.setInt(2, receiver.getId());
			if (ps.execute()) {
				receiver.setBalance(receiver.getBalance() + account.getBalance());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Used to perform a withdrawal/deposit on a user's account
	 * @param acct Account ID to perform transfer on
	 * @param amount Amount to deposit/withdraw from (+ = deposit, - = withdraw)
	 * @return If the transfer was a success 
	 */
	public static boolean transfer(int acct, double amount) {
		Connection con = conUtil.getConnection();
		
		try {
			con.setAutoCommit(false);
			
			CallableStatement cs = con.prepareCall("call transfer(?, ?)");
			cs.setInt(1, acct);
			cs.setBigDecimal(2, new BigDecimal(amount));
			
			cs.execute();
			con.setAutoCommit(true);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Transfer an amount from one account to another
	 * @param sender Account to transfer money from
	 * @param receiver Account to transfer money to
	 * @param amount Amount to transfer from sender to receiver
	 * @return If the transfer was completed successfully
	 */
	public static boolean transfer(int sender, int receiver, double amount) {
		Connection con = conUtil.getConnection();
		
		try {
			con.setAutoCommit(false);
			CallableStatement cs = con.prepareCall("call transfer(?, ?, ?)");
			cs.setInt(1, sender);
			cs.setInt(2, receiver);
			cs.setBigDecimal(3, new BigDecimal(amount));
			cs.execute();
			con.setAutoCommit(true);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
}
