package com.revature.bankingSystem.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.bankingSystem.models.Account;
import com.revature.bankingSystem.models.JointAccount;
import com.revature.bankingSystem.models.User;

public class AccountDao {
	private static ConnectionUtil conUtil = ConnectionUtil.getConnectionUtil();

	/**
	 * Retrieve the account with the specified ID from the database
	 * 
	 * @param id ID of account to look for
	 * @return Account object with values from returned row or null if account could
	 *         not be found
	 */
	public Account getAccount(int id) {
		Connection con = conUtil.getConnection();
		String sql = "select a.id, a.type, a.balance, u1.*, u2.* "
				+ "FROM accounts a "
				+ "left join joint_accounts j on a.id=j.acct_id "
				+ "inner join users u1 on u1.id=a.owner_id "
				+ "left join users u2 on u2.id=j.owner_id "
				+ "where a.id = ?";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();
			rs.next();
			User user1 = new User(rs.getInt(4), rs.getString(5), rs.getString(6), User.getLevel(rs.getInt(7)));
			if (rs.getInt(8) != 0) {
				User user2 = new User(rs.getInt(8), rs.getString(9), rs.getString(10), User.getLevel(rs.getInt(11)));
				return new JointAccount(rs.getInt(1), user1, user2, Account.getType(rs.getInt(2)), rs.getDouble(3));
			}
			return new Account(rs.getInt(1), user1, Account.getType(rs.getInt(2)), rs.getDouble(3));
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Get all the accounts of a user
	 * 
	 * @param user_id User ID to use for retrieving accounts
	 * @return List of Account objects owned by the user
	 */
	public List<Account> getUserAccounts(int user_id) {
		List<Account> user_accts = new ArrayList<Account>();
		Connection con = conUtil.getConnection();

		String sql = "select a.id, a.type, a.balance, u1.*, u2.* " + "FROM accounts a "
				+ "left join joint_accounts j on a.id=j.acct_id " + "inner join users u1 on u1.id=a.owner_id "
				+ "left join users u2 on u2.id=j.owner_id " + "where u1.id = ? or u2.id = ?";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, user_id);
			ps.setInt(2, user_id);

			ResultSet rs = ps.executeQuery();
			User user1, user2;
			while (rs.next()) {
				user1 = new User(rs.getInt(4), rs.getString(5), rs.getString(6), User.getLevel(rs.getInt(7)));
				if (rs.getInt(8) != 0) {
					user2 = new User(rs.getInt(8), rs.getString(9), rs.getString(10), User.getLevel(rs.getInt(11)));
					user_accts.add(new JointAccount(rs.getInt(1), user1, user2, Account.getType(rs.getInt(2)),
							rs.getDouble(3)));
				} else user_accts.add(new Account(rs.getInt(1), user1, Account.getType(rs.getInt(2)), rs.getDouble(3)));
			}
			return user_accts;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Create an account in the database
	 * 
	 * @param account Account object holding user ID and type
	 * @return The account passed in updated with the ID and a balance of 0
	 */
	public Account createAccount(Account account) {
		Connection con = conUtil.getConnection();

		try {
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO accounts (owner_id, type) VALUES (?, ?) RETURNING id");
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

		return null;
	}

	public JointAccount createJointAccount(JointAccount account) {
		System.out.println("Creating joint account");
		Connection con = conUtil.getConnection();

		try {
			PreparedStatement ps = con.prepareStatement("insert into accounts(owner_id, type) values (?, ?) returning id");
			ps.setInt(1, account.getOwnerId());
			ps.setInt(2, account.getType().ordinal());
			
			ps.execute();
			ResultSet rs = ps.getResultSet();
			
			rs.next();
			account.setId(rs.getInt(1));
			
			ps = con.prepareStatement("insert into joint_accounts (acct_id, owner_id) values (?, ?)");
			ps.setInt(1, account.getId());
			ps.setInt(2, account.getSecondOwnerId());
			ps.execute();
			
			return account;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Remove an account from the database
	 * 
	 * @param acct_id ID of the account to delete
	 * @return If the account was removed
	 */
	public boolean closeAccount(int acct_id) {
		Connection con = conUtil.getConnection();

		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM joint_accounts WHERE acct_id=?");
			ps.setInt(1, acct_id);
			ps.execute();			
			
			ps = con.prepareStatement("DELETE FROM accounts WHERE id=?");
			ps.setInt(1, acct_id);
			ps.execute();
			return (getAccount(acct_id) == null);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Transfer remaining funds from one account to another before removing it from
	 * the database.
	 * 
	 * @param account  Account to remove
	 * @param receiver Account to transfer the funds to
	 * @return If the account was successfully removed
	 */
	public boolean closeAccount(Account account, Account receiver) {
		return (account.getBalance() == 0 || transfer(account.getId(), receiver.getId(), account.getBalance()))
				&& closeAccount(account.getId());
	}

	/**
	 * Used to perform a withdrawal/deposit on a user's account
	 * 
	 * @param acct   Account ID to perform transfer on
	 * @param amount Amount to deposit/withdraw from (+ = deposit, - = withdraw)
	 * @return If the transfer was a success
	 */
	public boolean transfer(int acct, double amount) {
		Connection con = conUtil.getConnection();

		try {
			PreparedStatement ps = con.prepareStatement("update accounts set balance = balance + ? where id=?");
			ps.setBigDecimal(1, new BigDecimal(amount));
			ps.setInt(2, acct);
			ps.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Transfer an amount from one account to another
	 * 
	 * @param sender   Account to transfer money from
	 * @param receiver Account to transfer money to
	 * @param amount   Amount to transfer from sender to receiver
	 * @return If the transfer was completed successfully
	 */
	public boolean transfer(int sender, int receiver, double amount) {
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
