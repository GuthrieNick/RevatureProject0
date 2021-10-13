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
import com.revature.bankingSystem.models.User;

public class ApplicationDao {
	private static ConnectionUtil conUtil = ConnectionUtil.getConnectionUtil();

	/**
	 * Get list of approved applications for a user from the DB
	 * 
	 * @param userId ID of user
	 * @return List of new account IDs
	 */
	public List<Integer> getApprovedApplications(int userId) {
		Connection con = conUtil.getConnection();
		List<Integer> approved_apps = new ArrayList<Integer>();

		try {
			PreparedStatement ps = con
					.prepareStatement("DELETE FROM approved_applications WHERE user_id=? RETURNING acct_id");
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
	 * 
	 * @param userId ID of user
	 * @return List of account types
	 */
	public List<Account.Type> getDeniedApplications(int userId) {
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
	public Application getApplication() {
		Connection con = conUtil.getConnection();
		UserDao uDao = new UserDao();

		try {
			con.setAutoCommit(false);
			CallableStatement cs = con.prepareCall("{? = call get_application()}");

			cs.registerOutParameter(1, Types.OTHER);
			cs.execute();

			ResultSet rs = (ResultSet) cs.getObject(1);
			rs.next();
			Application application;
			if (rs.getInt(3) != 0)
				application = new JointApplication(rs.getInt(1), uDao.getUserByID(rs.getInt(2)),
						uDao.getUserByID(rs.getInt(3)), Account.Type.values()[rs.getInt(4)]);
			else
				application = new Application(rs.getInt(1), uDao.getUserByID(rs.getInt(2)),
						Account.Type.values()[rs.getInt(4)]);

			con.setAutoCommit(true);
			return application;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * View a user's active applications without removing them from the database.
	 * 
	 * @param userId ID of the user to search for
	 * @return All of the applications the user currently has.
	 */
	public List<Application> peekApplications(int userId) {
		Connection con = conUtil.getConnection();
		List<Application> apps = new ArrayList<Application>();
		String sql = "select a.id, a.acct_type, u1.*, u2.* " + "FROM applications a "
				+ "left join joint_applications j on a.id=j.app_id and (seconded=NULL OR seconded=true) "
				+ "inner join users u1 on u1.id=a.user_id " + "left join users u2 on u2.id=j.user_id";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			User user1, user2;
			while (rs.next()) {
				user1 = new User(rs.getInt(3), rs.getString(4), rs.getString(5), User.getLevel(rs.getInt(6)));
				if (rs.getInt(7) != 0) {
					user2 = new User(rs.getInt(7), rs.getString(8), rs.getString(9), User.getLevel(rs.getInt(10)));
					apps.add(new JointApplication(rs.getInt(1), user1, user2, Account.getType(rs.getInt(2))));
				} else
					apps.add(new Application(rs.getInt(1), user1, Account.getType(rs.getInt(2))));

			}
			return apps;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Set the seconded attribute of a row to true.
	 * 
	 * @param appId ID of application to second
	 * @return If the statement was executed
	 */
	public boolean secondJointApplication(int appId) {
		Connection con = conUtil.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE joint_applications set seconded=TRUE where app_id=?");
			ps.setInt(1, appId);
			ps.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * A user refuses to create a joint account with another
	 * 
	 * @param app Application to refuse
	 * @return If the application refusal was registered by the database
	 */
	public boolean refuseJointApplication(JointApplication app) {
		Connection con = conUtil.getConnection();

		try {
			// Notify first user of application denial
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO denied_applications (user_id, type) VALUES (?, ?)");
			ps.setInt(1, app.getUserId());
			ps.setInt(2, app.getAcctType().ordinal());
			ps.execute();

			// Remove application from joint_applications table
			ps = con.prepareStatement("DELETE FROM joint_applications WHERE id=?");
			ps.setInt(1, app.getId());
			ps.execute();

			// Remove application from applications table
			ps = con.prepareStatement("DELETE FROM applications WHERE id=?");
			ps.setInt(1, app.getId());
			ps.execute();

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public List<JointApplication> getPendingJointApplications(int userId) {
		Connection con = conUtil.getConnection();
		List<JointApplication> apps = new ArrayList<JointApplication>();
		String sql = "SELECT * FROM joint_applications j "
				+ "INNER JOIN applications a ON a.id=j.app_id AND j.user_id=? AND j.seconded=false, "
				+ "users u1, users u2 where u1.id=a.user_id and u2.id=j.user_id";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			User user1, user2;
			while (rs.next()) {
				user1 = new User(rs.getInt(7), rs.getString(8), rs.getString(9), User.getLevel(rs.getInt(10)));
				user2 = new User(rs.getInt(11), rs.getString(12), rs.getString(13), User.getLevel(rs.getInt(14)));
				apps.add(new JointApplication(rs.getInt(1), user1, user2, Account.getType(rs.getInt(5))));
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
	public boolean submitApplication(Application application) {
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
	public boolean submitJointApplication(JointApplication application) {
		Connection con = conUtil.getConnection();

		try {
			CallableStatement cs = con.prepareCall("CALL create_joint_application(?, ?, ?)");
			cs.setInt(1, application.getUserId());
			cs.setInt(2, application.getSecondUserId());
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
	public boolean approveApplication(Application app, int acct_id) {
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
	 * Approve a joint application
	 * 
	 * @param app     Application to approve
	 * @param acct_id ID of account created by the application
	 * @return If the approval was registered in the database
	 */
	public boolean approveJointApplication(JointApplication app, int acct_id) {
		Connection con = conUtil.getConnection();

		try {
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO approved_applications (user_id, acct_id) VALUES (?, ?)");
			ps.setInt(1, app.getUserId());
			ps.setInt(2, acct_id);
			ps.execute();

			ps = con.prepareStatement("INSERT INTO approved_applications (user_id, acct_id) VALUES (?, ?)");
			ps.setInt(1, app.getSecondUserId());
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
	public boolean denyApplication(Application app) {
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
	 * 
	 * @param app JointApplication object to deny
	 * @return If the denial was added to the table
	 */
	public boolean denyJointApplication(JointApplication app) {
		boolean first = denyApplication(app);
		User temp = app.getUser();
		app.setUser(app.getSecondUser());
		app.setSecondUser(temp);
		return denyApplication(app) && first;
	}
}
