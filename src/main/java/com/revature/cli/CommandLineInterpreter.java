package com.revature.cli;

import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.*;
import java.util.Scanner;

/**
 * Class describing a command line interpreter that retrieves input from a user
 * and parses it based on the CommandSystem passed in.
 * 
 * @author Nick Guthrie
 *
 */
public class CommandLineInterpreter {
	private Scanner in = new Scanner(System.in);

	public Scanner InStream() {
		return in;
	}

	private PrintStream out = System.out;

	public PrintStream OutStream() {
		return out;
	}

	private CommandSystem system = null;

	/** No argument constructor. Sets the input stream to System.in. */
	public CommandLineInterpreter() {
	}

	/**
	 * One argument constructor. Sets input stream to System.in.
	 * 
	 * @param system CommandSystem that this interpreter will be interacting with
	 */
	public CommandLineInterpreter(CommandSystem system) {
		this.system = system;
	}

	/**
	 * Two argument constructor
	 * 
	 * @param system CommandSystem that this interpreter will be interacting with
	 * @param stream Stream where commands will be coming from.
	 */
	public CommandLineInterpreter(CommandSystem system, InputStream stream) {
		this.system = system;
		in = new Scanner(stream);
	}

	/**
	 * Three argument constructor.
	 * 
	 * @param system  CommandSystem that this interpreter will be interacting with
	 * @param istream InputStream from which commands will be parsed; set to
	 *                System.in if null.
	 * @param ostream PrintStream to which command outputs will be sent; set to
	 *                System.out if null.
	 */
	public CommandLineInterpreter(CommandSystem system, InputStream istream, PrintStream ostream) {
		this.system = system;
		if (istream != null)
			in = new Scanner(istream);
		if (out != null)
			out = ostream;
	}

	/**
	 * Start command line execution with the attached CommandSystem. A CommandSystem
	 * must be attached in order to start.
	 * 
	 * @throws IllegalStateException Cannot run if it has no attached CommandSystem.
	 */
	public void Start() throws IllegalStateException {
		if (system == null)
			throw new IllegalStateException("Cannot start without a command system.");
		String command = "";
		boolean command_found;

		while (true) {
			command_found = false;
			command = GetCommandInputFromPrompt();
			if (command.equals(system.TerminatingCommand()))
				break;

			for (Method method : system.getClass().getMethods())
				if (Commands.CommandMatches(command, method) && method.getAnnotation(Command.class) != null) {
					command_found = true;
					try {
						if (method.getReturnType() != void.class) {
							System.out.println(method.invoke(system));
						}
						else method.invoke(system);
						break;
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			if (!command_found)
				System.out.println("Error: command not found. Use 'help' to see a list of available commands.");
		}
	}

	/**
	 * Prompt the user to input a command
	 * 
	 * @return String Formatted contents of the line they entered
	 */
	protected String GetCommandInputFromPrompt() {
		out.print(system.InputPrompt() + " ");
		return in.nextLine().strip().toLowerCase().replaceAll(" +", " ");
	}

	/**
	 * Prompt the user with a message to input a line
	 * 
	 * @param message Message to prompt user
	 * @return Text entered by the user
	 */
	public String GetInputFromPrompt(String message) {
		out.print(message);
		return in.nextLine();
	}

	/**
	 * Set the command system for this CLI
	 * 
	 * @param system System to set for the CLI
	 */
	public void SetCommandSystem(CommandSystem system) {
		this.system = system;
	}
	
	public void SetInputStream(InputStream in) {
		this.in = new Scanner(in);
	}
}
