package com.revature.cli;

import java.lang.reflect.Method;
import java.lang.reflect.*;

/**
 * Utility class for CLI package.
 * @author Nick Guthrie
 *
 */
public final class Commands {
	
	/**
	 * Interpret the CLI type of a string's contents
	 * @param value Value to interpret
	 * @return "integer", "float", or "string" depending on how the passed in value is interpreted.
	 */
	private static String getValueType(String value) {
		boolean i = false;
		try {
			Double.parseDouble(value);
			i = true;
			Integer.parseInt(value);
			return "float";
		} catch (NumberFormatException e) {
			if (i) return "integer";
			return "string";
		}
	}
	
	/**
	 * Convert a Java type to a CLI type
	 * @param type String value of the type to convert
	 * @return "integer", "float", or "string" depending on how the passed in value is interpreted.
	 */
	private static String getType(String type) {		
		switch (type.toLowerCase()) {
		case "int":
			return "integer";
		case "double":
			return "float";
		default:
			return "string";
		}
	}
	
	/**
	 * Get the CLI type of a parameter
	 * @param param
	 * @return
	 */
	private static String getType(Parameter param) {
		return getType(param.getType().getName());		
	}
	
	
	/**
	 * Check if a command String array matches with a given method.
	 * @param command String array to check against method
	 * @param method Method to check
	 * @return If the command matches the method's signature
	 */
	public static boolean CommandMatches(String[] command, Method method) {
		// Check command name matches
		if (!command[0].equals(MethodToCommand(method)))
			return false;
		
		// Check number of parameters matches
		if (command.length-1 != method.getParameterCount())
			return false;
		
		// Check parameter types match
		int i = 0;
		for (Parameter param : method.getParameters()) {
			if (!getValueType(command[++i]).equals(getType(param)))
				return false;
		}
		
		return true;
	}
	
	
	
	/**
	 * Convert a method name to a command name.
	 * The method is expected to be CamelCase, and the command name sent back is kebab-case
	 * @param method Method name
	 * @return Resulting command name
	 */
	public static String MethodToCommand(String method) {
		StringBuilder command = new StringBuilder();
		for (int i = 0; i < method.length(); i++) {
			if (Character.isUpperCase(method.charAt(i))) {
				if (command.length() != 0)
					command.append("-");
				command.append(Character.toLowerCase(method.charAt(i)));
			}
		}
		
		return command.toString();
	}
	
	
	
	/**
	 * Convert a method name to a command name.
	 * The method is expected to be CamelCase, and the command name sent back is kebab-case
	 * Makes a call to MethodToCommand(String) with the method's name as the input.
	 * @param method Method to turn into a command 
	 * @return Resulting command name
	 */
	public static String MethodToCommand(Method method) {
		return MethodToCommand(method.getName());
	}
}
