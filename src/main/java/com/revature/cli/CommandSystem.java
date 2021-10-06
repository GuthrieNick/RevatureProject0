package com.revature.cli;

import java.lang.reflect.*;

/**
 * 
 * @author guthr
 *
 */
public abstract class CommandSystem {
	private CommandLineInterpreter cli;

	/**
	 * Constructor that takes in a command line interpreter.
	 * 
	 * @param cli Command line interpreter associated with this command system.
	 */
	public void SetCLI(CommandLineInterpreter cli) {
		this.cli = cli;
	}

	public abstract String TerminatingCommand();

	public abstract String InputPrompt();

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
		cli.OutStream().println(message);
	}

	/**
	 * Displays the list of commands available to the user
	 * @return String holding contents of the help message
	 */
	@Command(brief="Displays the list of commands available to the user.")
	public final String help() {
		StringBuilder msg = new StringBuilder();
		Command annotation;
		
		// Iterate through every method skipping ones that don't have the Command annotation
		// Add the command version of the method and its command brief to the string builder
		for (Method method : getClass().getMethods()) {
			annotation = method.getDeclaredAnnotation(Command.class);
			if (annotation != null)
				msg.append(Commands.MethodToCommand(method))
					.append(": ")
					.append(annotation.brief())
					.append('\n');
		}
		
		return msg.toString();
	}
}
