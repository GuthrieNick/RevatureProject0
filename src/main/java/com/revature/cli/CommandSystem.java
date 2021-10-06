package com.revature.cli;
import java.lang.reflect.*;

/**
 * 
 * @author guthr
 *
 */
public abstract class CommandSystem implements CLISystem {
	private CommandLineInterpreter cli;
	
	/**
	 * Constructor that takes in a command line interpreter.
	 * @param cli Command line interpreter associated with this command system.
	 */
	public void SetCLI(CommandLineInterpreter cli) {
		this.cli = cli;
	}
	
	public abstract String TerminatingCommand();
	
	public abstract String InputPrompt();
	
	/**
	 * Prompt user with a message and retrieve their input from the command line interpreter.
	 * @param message Message to send to the user
	 * @return Line entered by the user
	 */
	protected final String GetInput(String message) {
		return cli.GetInputFromPrompt(message + " ");
	}
	
	protected final void TellUser(String message) {
		cli.Out().println(message);
	}

	
	
	/**
	 * Displays the list of commands available to the user
	 * @return String holding contents of the help message
	 */
	@Command(brief="Displays the list of commands available to the user.")
	public final String help() {
		StringBuilder msg = new StringBuilder();
		int longest_name = 6; // Used for formatting
		String command;
		
		// Iterate through list of methods in the class ancestry to get the longest name for formatting
		for (Method method : getClass().getMethods()) {
			command = Commands.MethodToCommand(method);
			if (method.getAnnotation(Command.class) != null && command.length() > longest_name)
				longest_name = command.length();
		}
		
		longest_name += (longest_name % 2 == 0) ? 2 : 3; // Ensure at least 2 spaces from command name
		
		// Reiterate through list of methods in class ancestry to get the list of commands
		for (Method method : getClass().getMethods()) {
			if (method.getAnnotation(Command.class) != null) {
				msg.append(Commands.MethodToCommand(method))
					.append(" ".repeat(longest_name - method.getName().length()));
				for (Parameter param : method.getParameters())
					msg.append(param.getName()).append(", ");
				msg.delete(msg.length() - 2, msg.length());
			}
		}
		
		return msg.toString();
	}
}
