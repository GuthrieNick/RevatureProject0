package com.revature.cli;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
/**
 * User-accessible commands must have this annotation to be recognized by the interpreter.
 * The interpreter is currently unable to recognize parameters, so commands should not have arguments or errors might occur.
 * @author Nick Guthrie
 *
 */
public @interface Command {
	public String brief();
}
