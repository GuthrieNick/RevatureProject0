package com.revature.bankingSystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.bankingSystem.models.User;


public class UserDao {
	private static ConnectionUtil conUtil = ConnectionUtil.getConnectionUtil();
	
	public static List<User> getAllUsers() {
		Connection con = conUtil.getConnection();
		String sql = "SELECT * from users";
		List<User> users = new ArrayList<User>();
		
		try {
		Statement statement = con.createStatement();
		ResultSet rs = statement.executeQuery(sql);
		while (rs.next())
			users.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3), User.Level.values()[rs.getInt(4)]));

		return users;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static User getUserByUsername(String username) {
		Connection con = conUtil.getConnection();
		String sql = "SELECT * FROM users WHERE username=?";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next())
				return new User(rs.getInt(1), rs.getString(2), rs.getString(3), User.Level.values()[rs.getInt(4)]);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Create a User object based on the row returned from selecting by an ID.
	 * @param id ID of user to look for
	 * @return User object created from returned row
	 */
	public static User getUserByID(int id) {
		Connection con = conUtil.getConnection();
		String sql = "SELECT * FROM users WHERE id=?";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			
			rs.next();
			return new User(rs.getInt(1), rs.getString(2), rs.getString(3), User.Level.values()[rs.getInt(4)]);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public static boolean createUser(User user) throws SQLException {
		Connection con = conUtil.getConnection();
		String sql = "INSERT INTO users (username, password, level) VALUES (?,?,?)";
		
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(4, user.getUsername());
		ps.setString(5, user.getPassword());
		ps.setInt(3, user.getLevel().ordinal());
		return ps.execute();
	}
	
	public static boolean updateUser(User user) {
		Connection con = conUtil.getConnection();
		String sql = "UPDATE users SET username=?, password=?, level=? WHERE id=?";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ps.setInt(3, user.getLevel().ordinal());
			ps.setInt(4, user.getId());
			return ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean deleteUser(User user) {
		Connection con = conUtil.getConnection();
		String sql = "DELETE FROM users WHERE id=?";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, user.getId());
			return ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
