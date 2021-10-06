package com.revature.cli;

/**
 * Interface detailing commands that must be implemented by CommandSystem.
 * @author Nick Guthrie
 *
 */
public interface CLISystem {
	/**
	 * Command meant to stop the interpreter.
	 * @return The command as a string
	 */
	public String TerminatingCommand();
	
	/**
	 * What does the user see at the beginning of every line?
	 * @return Prompt as a string
	 */
	public String InputPrompt();
}
