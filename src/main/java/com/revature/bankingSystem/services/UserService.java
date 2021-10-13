package com.revature.bankingSystem.services;

import java.sql.SQLException;
import com.revature.bankingSystem.dao.UserDao;
import com.revature.bankingSystem.models.User;
import com.revature.exceptions.InvalidCredentialsException;
import com.revature.exceptions.UsernameExistsException;
import com.revature.log.Logging;

public class UserService {
	private UserDao uDao = new UserDao();
	/**
	 * Log in an existing user with their username and password
	 * @param username Username of the user
	 * @param password Password of the user
	 * @return User object created from the database
	 * @throws InvalidCredentialsException If the username or password is incorrect
	 */
	public User logIn(String username, String password) throws InvalidCredentialsException {
		User user = uDao.getUserByUsername(username);
		if (user == null) {
			Logging.logger.warn("User supplied invalid username");
			throw new InvalidCredentialsException("Invalid username");
		}
		
		if (!password.equals(user.getPassword())) {
			Logging.logger.warn("User supplied invalid password");
			throw new InvalidCredentialsException("Invalid password");
		}
		Logging.logger.info(user.getUsername() + " successfully signed in");
		return user;
	}
	
	/**
	 * Create a new customer in the database
	 * @param username Username for the customer
	 * @param password Password for the customer
	 * @return User object representing new customer
	 * @throws UsernameExistsException If the username is already in use
	 */
	public User signUpCustomer(String username, String password) throws UsernameExistsException {
		try {
			uDao.createUser(new User(username, password, User.Level.Customer));
			Logging.logger.info("Customer " + username + " created");
			return uDao.getUserByUsername(username);
		} catch (SQLException e) {
			Logging.logger.warn(username + " already used as a username in database");
			throw new UsernameExistsException();
		}
	}
	
	/**
	 * Create a new employee in the database
	 * @param username Username for the employee
	 * @param password Password for the employee
	 * @return
	 * @throws UsernameExistsException
	 */
	public User assignEmployee(String username, String password) throws UsernameExistsException {
		try {
			uDao.createUser(new User(username, password, User.Level.Employee));
			Logging.logger.info("Employee " + username + " created");
			return uDao.getUserByUsername(username);
		} catch (SQLException e) {
			Logging.logger.warn(username + " already used as a username in database");
			throw new UsernameExistsException();
		}
	}
}
