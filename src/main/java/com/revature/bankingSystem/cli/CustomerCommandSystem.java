package com.revature.bankingSystem.cli;

import java.util.ArrayList;
import java.util.List;

import com.revature.bankingSystem.models.Account;
import com.revature.bankingSystem.models.Application;
import com.revature.bankingSystem.models.JointApplication;
import com.revature.bankingSystem.models.User;
import com.revature.cli.Command;
import com.revature.cli.Commands;
import com.revature.exceptions.UserExitException;

/**
 * Command system instantiated for customer users.
 *
 */
public class CustomerCommandSystem extends BankingCommandSystem {
	private List<Account> accounts;
	private List<JointApplication> pendingJoints = null;

	/**
	 * One argument constructor. Passes User param to base class.
	 * 
	 * @param customer User object to use for this command system.
	 */
	public CustomerCommandSystem(User customer) {
		super(customer);
		accounts = acctDao.getUserAccounts(customer.getId());
		for (int id : appDao.getApprovedApplications(customer.getId()))
			TellUser(getAccount(id).getType() + " account # " + id + " was approved!");

		for (Account.Type type : appDao.getDeniedApplications(customer.getId()))
			TellUser("Unfortunately your requested " + type + " account was not approved.");

		pendingJoints = appDao.getPendingJointApplications(customer.getId());
		if (pendingJoints != null && pendingJoints.size() > 0)
			TellUser("You have pending joint applications to approve. Use the 'joint-apps' command to view them.");
	}

	private Account getAccount(int id) {
		for (Account account : accounts)
			if (account.getId() == id)
				return account;
		return null;
	}

	private void setAccount(Account account) {
		for (int i = 0; i < accounts.size(); i++)
			if (accounts.get(i).getId() == account.getId()) {
				accounts.set(i, account);
				break;
			}
	}

	private void removeAccount(int id) {
		for (int i = 0; i < accounts.size(); i++)
			if (accounts.get(i).getId() == id) {
				accounts.remove(i);
				break;
			}
	}

	/**
	 * @return
	 */
	@Command(brief = "Approve or reject pending joint applications")
	public String jointApps() {
		if (pendingJoints.size() == 0)
			return "You have no pending joint applications currently.";

		List<Integer> remove = new ArrayList<Integer>();

		for (int i = 0; i < pendingJoints.size(); i++) {
			TellUser(pendingJoints.get(i).toString());
			if (YesOrNo("Submit this application?")) {
				if (applicationService.secondJointApplication(pendingJoints.get(i))) {
					TellUser("Application created and waiting for approval.");
				}
			} else {
				TellUser("Error: Failed to submit application. Use 'joint-apps' command to resubmit later.");
			}
		}

		for (int i = remove.size() - 1; i > 0; i--) {
			pendingJoints.remove(remove.get(i).intValue());
		}

		return "All joint applications have been considered.";
	}

	/**
	 * @return
	 */
	@Command(brief = "View list of accounts")
	public String accounts() {
		StringBuilder sb = new StringBuilder();
		for (Account account : accounts)
			sb.append(account.toString()).append('\n');
		return sb.toString();
	}

	@Command(brief = "Submit an application for a new account")
	public String newAccount() {
		char type;
		while (true) {
			type = GetInput("Enter 'S' for a new savings account or 'C' for a new checking account: ").toLowerCase()
					.charAt(0);

			if (type == 'c')
				break;
			else if (type == 's')
				break;
			else
				TellUser("Error: Input must be 'S' or 'C'.");
		}

		Application app;

		if (YesOrNo("Is this a joint account?")) {
			User joint_owner = null;
			while (true) {
				String username = GetInput("Enter the username of the joint owner: ");
				joint_owner = uDao.getUserByUsername(username);
				if (joint_owner == null)
					TellUser("Error: User with that name could not be found. Try again.");
				else {
					TellUser("Creating joint application with " + username + "...");
					break;
				}
			}

			app = new JointApplication(getUser(), joint_owner,
					type == 'c' ? Account.Type.Checking : Account.Type.Savings);
		} 
		
		else app = new Application(getUser(), type == 'c' ? Account.Type.Checking : Account.Type.Savings);
		
		if (applicationService.submitApplication(app))
			return "Application submitted successfully.";

		return "Error: An issue was encountered submitting the application. Please try again.";
	}

	/**
	 * Transfer money between two accounts.
	 * 
	 * @return If the money was transferred successfully.
	 */
	@Command(brief = "Transfer money from one account to another. Can be between two of your accounts or with an account owned by someone else if you know its number.")
	public String transfer() {
		int origin;
		while (true) {
			origin = getAccountNumber("Enter the account number to withdraw from: ");

			if (getAccount(origin) == null)
				return "Error: You must withdraw from one of your own accounts.";
			else
				break;
		}

		int destination = getAccountNumber("Enter the account to deposit into: ");

		String input;
		double amount;
		while (true) {
			input = GetInput("Enter amount to transfer: ");

			if (input.equals("exit"))
				return "Transfer cancelled.";

			else if (!Commands.getValueType(input).equals("string")) {
				amount = Double.parseDouble(input);
				if (amount < 0 && YesOrNo(
						"You entered a negative number. Would you like to swap the sending and receiving accounts?")) {
					if (getAccount(destination) == null)
						return "Error: You must withdraw from one of your own accounts.\nTransaction cancelled.";
					int temp = origin;
					origin = destination;
					destination = temp;
				}
				break;
			}

			else
				TellUser("Error: That is not a valid number. Please try again.");
		}

		if (YesOrNo(
				String.format("Sending $%.2f from account #%d to account #%d, correct?", amount, origin, destination)))
			for (Account account : accounts)
				if (account.getId() == origin)

					if (accountService.transfer(origin, destination, amount)) {
						Account a = getAccount(origin);
						a.addToBalance(-amount);
						setAccount(a);

						Account b = getAccount(destination);
						if (b != null) {
							b.addToBalance(amount);
							setAccount(b);
						}

						return "Transfer completed.";
					} else
						return "Error: An issue was encountered. Transfer could not be completed.";

		return "Transaction cancelled.";
	}

	@Command(brief = "Log a deposit")
	public String deposit() {
		Account account = getAccount(getAccountNumber("Enter account to deposit funds into: "));
		if (account == null)
			return "Error: That is not one of your accounts.\nDeposit cancelled.";

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
		Account account = getAccount(getAccountNumber("Enter account to withdraw funds from: "));
		if (account == null)
			return "Error: That is not one of your accounts.\nWithdrawal cancelled.";

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

	@Command(brief = "Close one of your accounts")
	public String closeAccount() {
		String acct;
		Account account_to_close = null;

		// Get account id
		while (true) {
			acct = GetInput("Enter the name of the account you wish to close or 'list' to view your accounts: ");
			if (acct.equals("exit"))
				return "Account closing cancelled.";
			else if (acct.equals("list")) {
				for (Account account : accounts)
					TellUser(account.toString());
				continue;
			} else if (!Commands.getValueType(acct).equals("integer"))
				TellUser("Error: '" + acct + "' is not an account number. Please try again.");
			else if ((account_to_close = getAccount(Integer.parseInt(acct))) == null)
				TellUser("Error: You do not have an account with ID " + acct + ".");
			else
				break;
		}

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
							receiver.addToBalance(account_to_close.getBalance());
							removeAccount(account_to_close.getId());
							return "Account successfully closed.";
						} else
							return "Error: An issue prevented the account from being closed. Please try again.";
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
			removeAccount(account_to_close.getId());
			return "Account successfully closed.";
		}

		else
			return "Error: An issue prevented the account from being closed. Please try again.";
	}
}
