package com.revature.bankingSystem.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.revature.bankingSystem.models.Account;
import com.revature.bankingSystem.models.Application;
import com.revature.bankingSystem.models.JointApplication;

public class ApplicationDao {
	private static ConnectionUtil conUtil = ConnectionUtil.getConnectionUtil();
	
	/**
	 * Get list of approved applications for a user from the DB
	 * @param userId ID of user
	 * @return List of new account IDs
	 */
	public static List<Integer> getApprovedApplications(int userId) {
		Connection con = conUtil.getConnection();
		List<Integer> approved_apps = new ArrayList<Integer>();
		
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM approved_applications WHERE user_id=? RETURNING acct_id");
			ps.setInt(1, userId);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			while (rs.next())
				approved_apps.add(rs.getInt(1));
			return approved_apps;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * Get list of denied applications for a user from the DB
	 * @param userId ID of user
	 * @return List of account types
	 */
	public static List<Account.Type> getDeniedApplications(int userId) {
		Connection con = conUtil.getConnection();
		List<Account.Type> approved_apps = new ArrayList<Account.Type>();

		try {
			PreparedStatement ps = con
					.prepareStatement("DELETE FROM denied_applications WHERE user_id=? RETURNING type");
			ps.setInt(1, userId);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			while (rs.next())
				approved_apps.add(Account.Type.values()[rs.getInt(1)]);
			return approved_apps;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Get an application from the database
	 * 
	 * @return Application object created from the returned db row
	 */
	public static Application getApplication() {
		Connection con = conUtil.getConnection();
		
		try {
			con.setAutoCommit(false);
			CallableStatement cs = con.prepareCall("{? = call get_application()}");
			
			cs.registerOutParameter(1, Types.OTHER);
			cs.execute();
			
			ResultSet rs = (ResultSet)cs.getObject(1);
			rs.next();
			Application application;
			if (rs.getInt(3) != 0)
				application = new JointApplication(rs.getInt(1), rs.getInt(2), rs.getInt(3), Account.Type.values()[rs.getInt(4)]);
			else
				application = new Application(rs.getInt(1), rs.getInt(2), Account.Type.values()[rs.getInt(4)]);
			
			con.setAutoCommit(true);
			return application;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * View a user's active applications without removing them from the database.
	 * @param userId ID of the user to search for
	 * @return All of the applications the user currently has.
	 */
	public static List<Application> peekApplications(int userId) {
		Connection con = conUtil.getConnection();
		List<Application> apps = new ArrayList<Application>();
		
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM applications WHERE user_id=?");
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				apps.add(new Application(rs.getInt(3), rs.getInt(1), Account.Type.values()[rs.getInt(2)]));
			}
			return apps;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * Create a new application row in the database
	 * 
	 * @param application Application to submit
	 * @return If the application was submitted successfully
	 */
	public static boolean submitApplication(Application application) {
		Connection con = conUtil.getConnection();

		try {
			PreparedStatement ps = con.prepareStatement("INSERT INTO applications (user_id, acct_type) VALUES (?, ?)");
			ps.setInt(1, application.getUserId());
			ps.setInt(2, application.getAcctType().ordinal());
			ps.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Create a new joint application in the database. Inserts a row in
	 * 'applications' and a row in 'joint_applications' with the first application's
	 * ID and the second user's ID.
	 * 
	 * @param application Joint application to submit
	 * @return If the application was submitted
	 */
	public static boolean submitJointApplication(JointApplication application) {
		Connection con = conUtil.getConnection();

		try {
			CallableStatement cs = con.prepareCall("CALL create_joint_application(?, ?, ?)");
			cs.setInt(1, application.getUserId());
			cs.setInt(2, application.getSecond_user_id());
			cs.setInt(3, application.getAcctType().ordinal());
			cs.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Add a row to the approved_applications table
	 * 
	 * @param app     Application that was approved
	 * @param acct_id Account created from the application
	 * @return If the application was successfully approved
	 */
	public static boolean approveApplication(Application app, int acct_id) {
		Connection con = conUtil.getConnection();

		try {
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO approved_applications (user_id, acct_id) VALUES (?, ?)");
			ps.setInt(1, app.getUserId());
			ps.setInt(2, acct_id);
			ps.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Add a row to the denied_applications table.
	 * 
	 * @param app
	 * @return
	 */
	public static boolean denyApplication(Application app) {
		Connection con = conUtil.getConnection();

		try {
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO denied_applications (user_id, type) VALUES (?, ?)");
			ps.setInt(1, app.getUserId());
			ps.setInt(2, app.getAcctType().ordinal());
			ps.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	/**
	 * Add two rows to the denied_applications table
	 * @param app JointApplication object to deny
	 * @return If the denial was added to the table
	 */
	public static boolean denyJointApplication(JointApplication app) {
		boolean first = denyApplication(app);
		int temp = app.getUserId();
		app.setUserId(app.getSecond_user_id());
		app.setSecond_user_id(temp);
		return denyApplication(app) && first;
	}
}
