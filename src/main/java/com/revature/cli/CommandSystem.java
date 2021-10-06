package com.revature.cli;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author guthr
 *
 */
public abstract class CommandSystem {
	private CommandLineInterpreter cli;
	
	public CommandSystem() {
		List<Method> badCommands = new ArrayList<Method>();
		for (Method method : getClass().getMethods()) {
			if (method.getAnnotation(Command.class) != null && method.getParameterCount() > 0)
				badCommands.add(method);
		}
		if (badCommands.size() != 0) {
			StringBuilder sb = new StringBuilder("Error: Cannot continue. The following methods must have their arguments removed:\n");
			for (Method method : badCommands)
				sb.append(method.toString()).append('\n');
			throw new java.lang.Error(sb.toString());
		}
	}

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
		
		msg.append(TerminatingCommand()).append(": Exit the system\n");
		
		return msg.toString();
	}
}
