
import java.util.Scanner;

import com.revature.bankingSystem.cli.AdminCommandSystem;
import com.revature.bankingSystem.cli.CustomerCommandSystem;
import com.revature.bankingSystem.cli.EmployeeCommandSystem;
import com.revature.bankingSystem.dao.UserDao;
import com.revature.bankingSystem.models.Bank;
import com.revature.bankingSystem.models.User;
import com.revature.cli.CommandLineInterpreter;
import com.revature.exceptions.InvalidUsernameException;

/**
 * 
 * @author guthr
 *
 */
public class BankingSystem {
	
	public static final Scanner scanner = new Scanner(System.in);
	
	private static User check(String user, String pass) { return null; }
	
	
	
	/**
	 * Program execution starts here. Prompt user to enter credentials or create an account, then start the CLI.
	 * @param args
	 */
	public static void main(String[] args) {
		User person;
		
		System.out.println("Testing getting all users:");
		UserDao udao = new UserDao();
		for (User user : udao.getAllUsers())
			System.out.println(user);
		
		if (args[1] == "-n") {
			System.out.println("Welcome new user!");
			String user, pass;
			
			// Get username
			do {
				System.out.print("Enter your desired username: ");
				user = scanner.nextLine();
				try { Bank.CheckUsername(user); }
				catch (InvalidUsernameException e) {
					System.out.println("Error: " + e.getMessage());
					continue;
				}
			} while (false);
			
			// Get password
			do {
				System.out.print("Enter your desired password: ");
				pass = scanner.nextLine();
				if (!Bank.ValidPassword(pass)) {
					System.out.println("Error: password cannot be longer than 64 characters.");
					continue;
				}
			} while (false);
			
			person = Bank.CreateAccount(user, pass);
		}
		
		// Log in loop
		else while (true) {
			System.out.print("Username: ");
			String username = scanner.nextLine();
			System.out.print("Password: ");
			String password = scanner.nextLine();
			
			person = check(username, password);
			
			if (person == null)
				System.out.println("Error: Invalid username/password. Try again.");
			else break;
		}
		
		
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
