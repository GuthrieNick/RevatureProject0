package com.revature.bankingSystem.cli;

import java.sql.SQLException;

import com.revature.bankingSystem.dao.UserDao;
import com.revature.bankingSystem.models.User;
import com.revature.cli.Command;

public class AdminCommandSystem extends EmployeeCommandSystem {
	public AdminCommandSystem(User person) {
		super(person);
	}
	
	@Command(brief="Assign login credentials to a new employee")
	public String assignEmployee() {
		String username = GetInput("Enter the username of the new employee: ");
		String password = GetInput("Enter the password of the new employee: ");
		User newUser = new User(username, password, User.Level.Employee);
		
		try {
			UserDao.createUser(newUser);
		} catch (SQLException e) {
			return "Error: An issue was encountered. New employee could not be created.";
		}
		
		return "Login credentials successfully added to the database.";
	}
	
	@Command(brief="Transfer money between two accounts")
	public void transfer() {
		//TODO: Implement AdminCommandSystem.transfer()
		
	}
	
	@Command(brief="Close one of a customer's accounts")
	public void cancelAccount() {
		//TODO: Implement AdminCommandSystem.cancelAccount()
		
	}
}
