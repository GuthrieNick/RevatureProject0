package com.revature.cli;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

import com.revature.exceptions.UserExitException;

/**
 * 
 * @author guthr
 *
 */
public abstract class CommandSystem {
	private CommandLineInterpreter cli;

	/**
	 * No argument constructor. Checks to make sure no commands have any parameters,
	 * otherwise throws an error stopping program execution until the parameters are
	 * removed.
	 */
	public CommandSystem() {
		List<Method> badCommands = new ArrayList<Method>();
		for (Method method : getClass().getMethods()) {
			if (method.getAnnotation(Command.class) != null && method.getParameterCount() > 0)
				badCommands.add(method);
		}
		if (badCommands.size() != 0) {
			StringBuilder sb = new StringBuilder(
					"Error: Cannot continue. The following methods must have their arguments removed:\n");
			for (Method method : badCommands)
				sb.append(method.toString()).append('\n');
			throw new java.lang.Error(sb.toString());
		}
	}

	/**
	 * Tell the system what command line interpreter to use for input/output. Used
	 * in the CommandLineInterpreter constructor.
	 * 
	 * @param cli Command line interpreter associated with this command system.
	 */
	void SetCLI(CommandLineInterpreter cli) {
		this.cli = cli;
	}

	/**
	 * The command the user can use to exit the command system
	 * 
	 * @return Command string
	 */
	public abstract String TerminatingCommand();

	/**
	 * This is printed at the start of every line when a command is not currently
	 * being processed.
	 * 
	 * @return String to print to the user
	 */
	public abstract String InputPrompt();

	/**
	 * Prompts the user to answer a yes or no question
	 * 
	 * @param prompt Question to answer
	 * @return True if they answer yes, false if no
	 */
	protected final boolean YesOrNo(String prompt) {
		String input = GetInput(prompt + "(Y/N) ");
		return Character.toLowerCase(input.charAt(0)) == 'y';
	}

	protected final int GetInt(String prompt) throws UserExitException {
		String input = GetInput(prompt);
		if (input.equals("exit"))
			throw new UserExitException();
		if (!Commands.getValueType(input).equals("integer")) {
			TellUser("Error: Input must be an integer number.");
			return GetInt(prompt);
		}

		return Integer.parseInt(input);
	}

	protected final double GetDouble(String prompt) throws UserExitException {
		String input = GetInput(prompt);
		if (input.equals("exit"))
			throw new UserExitException();
		if (Commands.getValueType(input).equals("string")) {
			TellUser("Error: Input must be a number.");
			return GetDouble(prompt);
		}

		return Double.parseDouble(input);
	}

	/**
	 * Prompt user with a message and retrieve their input from the command line
	 * interpreter.
	 * 
	 * @param message Message to send to the user
	 * @return Line entered by the user
	 */
	protected final String GetInput(String message) {
		return cli.GetInputFromPrompt(message + " ");
	}

	protected final void TellUser(String message) {
		CommandLineInterpreter.OutStream().println(message);
	}

	/**
	 * Displays the list of commands available to the user
	 * 
	 * @return String holding contents of the help message
	 */
	@Command(brief = "Displays the list of available commands")
	public final String help() {
		StringBuilder msg = new StringBuilder('\n');
		Command annotation;
		int longest = 4;
		int x;

		for (Method method : getClass().getMethods()) {
			annotation = method.getDeclaredAnnotation(Command.class);
			if (annotation != null) {
				x = Commands.MethodToCommand(method).length();
				if (x > longest)
					longest = x;
			}
		}

		String cmd;
		// Iterate through every method skipping ones that don't have the Command
		// annotation
		// Add the command version of the method and its command brief to the string
		// builder
		for (Method method : getClass().getMethods()) {
			annotation = method.getDeclaredAnnotation(Command.class);
			if (annotation != null) {
				cmd = Commands.MethodToCommand(method);
				msg.append(cmd).append(" ".repeat(longest - cmd.length() + 2)).append(annotation.brief())
						.append('\n');
			}
		}

		msg.append(TerminatingCommand()).append(" ".repeat(longest - TerminatingCommand().length() + 2))
				.append("Exit the system\n");

		return msg.toString();
	}
}
