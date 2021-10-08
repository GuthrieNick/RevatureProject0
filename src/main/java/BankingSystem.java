//TODO: Documentation
//TODO: Customers applying for accounts updates the database
//TODO: Test service layer and command line interpreter
//TODO: Employees can approve/deny accounts
//TODO: Implement joint accounts (change model to inherit from accounts)
//TODO: Create a stored procedure or trigger
//TODO: Changing passwords
//TODO: Admins creating new employee accounts
import java.util.Scanner;

import com.revature.bankingSystem.cli.AdminCommandSystem;
import com.revature.bankingSystem.cli.CustomerCommandSystem;
import com.revature.bankingSystem.cli.EmployeeCommandSystem;
import com.revature.bankingSystem.dao.UserDao;
import com.revature.bankingSystem.models.User;
import com.revature.bankingSystem.services.UserService;
import com.revature.cli.CommandLineInterpreter;
import com.revature.exceptions.InvalidCredentialsException;
import com.revature.exceptions.UsernameExistsException;
import com.revature.log.Logging;

/**
 * 
 * @author guthr
 *
 */
public class BankingSystem {

	public static final Scanner scanner = new Scanner(System.in);
	
	public static boolean isValidUsername(String username) {
		for (char i : username.toCharArray())
			if (!Character.isLetterOrDigit(i))
				return false;
		return true;
	}

	/**
	 * Program execution starts here. Prompt user to enter credentials or create an
	 * account, then start the CLI.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		User person = null;
		
		System.out.println("Enter 'exit' to quit program.");

		if (args.length > 0 && args[0] == "-n") {
			System.out.println("Welcome new user!");
			String user, pass;

			// Get username
			do {
				System.out.print("Enter your desired username: ");
				user = scanner.nextLine();
				if (UserDao.getUserByUsername(user) != null) {
					Logging.logger.warn("User supplied username already in DB");
					System.out.println("Error: Username already exists.");
				} else if (!isValidUsername(user)) {
					Logging.logger.warn("User supplied invalid username");
					System.out.println("Error: Usernames must only contain letters and numbers.");
				}else break;
			} while (true);

			// Get password
			do {
				System.out.print("Enter your desired password: ");
				pass = scanner.nextLine();
				if (pass.length() > 64) {
					System.out.println("Error: password cannot be longer than 64 characters.");
				} else break;
			} while (true);

			try {
				person = UserService.signUpCustomer(user, pass);
			} catch (UsernameExistsException e) {
				System.out.println("Error: Username already taken. Account could not be created.");
			}
		}

		// Log in loop
		else while (true) {
			System.out.print("Username: ");
			String username = scanner.nextLine();
			if (username.equals("exit"))
				return;
			
			System.out.print("Password: ");
			String password = scanner.nextLine();
			if (password.equals("exit"))
				return;

			try {
				person = UserService.logIn(username, password);
				break;
			} catch (InvalidCredentialsException e) {
				System.out.println("Error: Invalid username/password. Try again.");
			}
		}
		
		System.out.println("Welcome, " + person.getUsername() + "!");

		// Start command system
		CommandLineInterpreter cli = new CommandLineInterpreter();

		switch (person.getLevel()) {
		case Employee:
			cli.SetCommandSystem(new EmployeeCommandSystem(person));
			break;

		case Admin:
			cli.SetCommandSystem(new AdminCommandSystem(person));
			break;

		case Customer:
			cli.SetCommandSystem(new CustomerCommandSystem(person));
			break;
		}

		try {
			cli.Start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}

		System.out.println("Thank you for using our system!");
	}

}
