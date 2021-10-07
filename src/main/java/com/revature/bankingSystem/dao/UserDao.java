package com.revature.bankingSystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.bankingSystem.models.User;
import com.revature.exceptions.UserDoesNotExistException;


public class UserDao {
	ConnectionUtil conUtil = ConnectionUtil.getConnectionUtil();
	
	public List<User> getAllUsers() {
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
	
	User getUserByUsername(String username) throws UserDoesNotExistException {
		Connection con = conUtil.getConnection();
		String sql = "SELECT * FROM users WHERE username=?";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			
			if (rs.isBeforeFirst())
				return new User(rs.getInt(1), rs.getString(2), rs.getString(3), User.Level.values()[rs.getInt(4)]);
			throw new UserDoesNotExistException();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;		
	}
	
	void createUser(User user) throws SQLException {
		Connection con = conUtil.getConnection();
		String sql = "INSERT INTO users (username, password, level) VALUES (?,?,?)";
		
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(4, user.getUsername());
		ps.setString(5, user.getPassword());
		ps.setInt(3, user.getLevel().ordinal());
		ps.execute();
	}
	
	void updateUser(User user) {
		Connection con = conUtil.getConnection();
		String sql = "UPDATE users SET username=?, password=?, level=? WHERE id=?";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ps.setInt(3, user.getLevel().ordinal());
			ps.setInt(4, user.getId());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	void deleteUser(User user) {
		Connection con = conUtil.getConnection();
		String sql = "DELETE FROM users WHERE id=?";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, user.getId());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
