package com.revature.bankingSystem.services;

import java.sql.SQLException;
import com.revature.bankingSystem.dao.UserDao;
import com.revature.bankingSystem.models.User;
import com.revature.exceptions.InvalidCredentialsException;
import com.revature.exceptions.UsernameExistsException;
import com.revature.log.Logging;

public class UserService {
	public static User logIn(String username, String password) throws InvalidCredentialsException {
		User user = UserDao.getUserByUsername(username);
		if (user == null) {
			Logging.logger.warn("User supplied invalid username");
			throw new InvalidCredentialsException("Invalid username");
		}
		
		if (!password.equals(user.getPassword())) {
			Logging.logger.warn("User supplied invalid password");
			throw new InvalidCredentialsException("Invalid password");
		}
		
		return user;
	}
	
	public static User signUpCustomer(String username, String password) throws UsernameExistsException {
		try {
			UserDao.createUser(new User(username, password, User.Level.Customer));
			return UserDao.getUserByUsername(username);
		} catch (SQLException e) {
			Logging.logger.warn(username + " already used as a username in database");
			throw new UsernameExistsException();
		}
	}
}
