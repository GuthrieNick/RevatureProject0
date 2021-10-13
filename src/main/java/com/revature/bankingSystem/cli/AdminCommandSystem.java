package com.revature.bankingSystem.cli;

import com.revature.bankingSystem.models.Account;
import com.revature.bankingSystem.models.User;
import com.revature.cli.Command;
import com.revature.exceptions.UserExitException;
import com.revature.exceptions.UsernameExistsException;

public class AdminCommandSystem extends EmployeeCommandSystem {
	public AdminCommandSystem(User person) {
		super(person);
	}

	@Command(brief = "Assign login credentials to a new employee")
	public String assignEmployee() {
		String username = GetInput("Enter the username of the new employee: ");
		String password = GetInput("Enter the password of the new employee: ");

		try {
			userService.assignEmployee(username, password);
			return "Login credentials successfully added to the database.";
		} catch (UsernameExistsException e) {
			return "Error: Username already exists in the database. New employee could not be created.";
		}
	}

	@Command(brief = "Transfer money between two accounts")
	public String transfer() {
		int origin = getAccountNumber("Enter the account number to withdraw from: ");
		if (origin == -1)
			return "Transaction cancelled.";
		
		int destination = getAccountNumber("Enter the account to deposit into: ");
		if (destination == -1)
			return "Transaction cancelled.";

		String input;
		double amount;
		while (true) {
			input = GetInput("Enter amount to transfer: ");

			if (input.equals("exit"))
				return "Transfer cancelled.";

			try {
				amount = Double.parseDouble(input);
				if (amount < 0 && YesOrNo(
						"You entered a negative number. Would you like to swap the sending and receiving accounts?")) {
					int temp = origin;
					origin = destination;
					destination = temp;
				}
				break;
			} catch (NumberFormatException e) {
				TellUser("Error: that is not a valid number. Please try again.");
			}
		}

		if (YesOrNo(
				String.format("Sending $%.2f from account #%d to account #%d, correct?", amount, origin, destination)))
			if (accountService.transfer(origin, destination, amount))
				return "Transfer completed.";
			else
				return "Error: An issue was encountered. Transfer could not be completed.";

		return "Transaction cancelled.";

	}

	@Command(brief = "Close one of a customer's accounts")
	public String cancelAccount() {
		int acct;
		Account account_to_close = null;

		// Get account id
		acct = getAccountNumber("Enter the number for the account you wish to close: ");
		if (acct == -1)
			return "Account closing cancelled.";
		else account_to_close = acctDao.getAccount(acct);

		// If balance is nonzero, select account to put remaining balance into
		if (account_to_close.getBalance() > 0) {
			int receiver_id;
			TellUser("This account has a nonzero balance.");
			
			while (true) {
				
				try {
					receiver_id = GetInt("Enter a destination account to send the remainding funds: ");
					Account receiver = acctDao.getAccount(receiver_id);
					if (receiver == null) {
						TellUser("Error: Account #" + receiver_id + " could not be found. Please try again.");
						continue;
					} else {
						if (accountService.closeAccount(account_to_close, receiver, getUser())) {
							return "Account successfully closed.";
						}
						else return "Error: An issue prevented the account from being closed. Please try again.";
					}
				} catch (UserExitException e) {
					if (YesOrNo("Do you wish to end closing an account?"))
						return "Account not closed.";
					else
						continue;
				}
			}
		}
		
		else if (accountService.closeAccount(account_to_close, getUser())) {
			return "Account successfully closed.";
		}
		
		else return "Error: An issue prevented the account from being closed. Please try again.";
	}
	
	@Command(brief = "Log a deposit")
	public String deposit() {
		String input;
		Account account;
		
		while (true) {
			input = GetInput("Enter account to deposit funds into: ");
			if (input == "exit")
				return "Deposit cancelled.";
			
			try {
				account = acctDao.getAccount(Integer.parseInt(input));
				if (account == null)
					TellUser("Error: Could not find account #" + input + ". Please try again.");
				else break;
				
			} catch (NumberFormatException e) {
				TellUser("Error: Invalid account format. Account numbers are positive integers.");
			}
		}
		
		double amount;
		try {
			amount = GetDouble("Enter amount to deposit into this account: ");
		} catch (UserExitException e) {
			return "Deposit cancelled.";
		}
		
		if (YesOrNo(String.format("Deposit $%.2f into account #%d?", amount, account.getId())))
			account = accountService.deposit(account, amount);

		if (account == null)
			return "Error: An issue was encountered in the database.\nDeposit cancelled.";

		return String.format("Deposit processed. New balance for account #%d: $%.2f.", account.getId(),
				account.getBalance());
	}

	@Command(brief = "Log a withdrawal")
	public String withdraw() {
		String input;
		Account account;
		
		while (true) {
			input = GetInput("Enter account to withdraw funds from: ");
			if (input == "exit")
				return "Withdrawal cancelled.";
			
			try {
				account = acctDao.getAccount(Integer.parseInt(input));
				if (account == null)
					TellUser("Error: Could not find account #" + input + ". Please try again.");
				else break;
				
			} catch (NumberFormatException e) {
				TellUser("Error: Invalid account format. Account numbers are positive integers.");
			}
		}
		double amount;
		try {
			amount = GetDouble("Enter amount to withdraw from this account: ");
		} catch (UserExitException e) {
			return "Withdrawal cancelled.";
		}

		if (amount > account.getBalance())
			return "Error: Insufficient balance.\nWithdrawal cancelled.";

		if (YesOrNo(String.format("Withdraw $%.2f from account #%d?", amount, account.getId())))
			account = accountService.withdraw(account, amount);

		if (account == null)
			return "Error: An issue was encountered in the database.\nWithdrawal cancelled.";

		return String.format("Withdrawal processed. New balance for account #%d: $%.2f.", account.getId(),
				account.getBalance());
	}
}
