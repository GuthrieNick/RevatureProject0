package com.revature.bankingSystem.cli;

import com.revature.bankingSystem.dao.User;
import com.revature.cli.CommandSystem;

public abstract class BankingCommandSystem extends CommandSystem {
	private User person;
	
	public BankingCommandSystem(User person) {
		this.person = person;
	}

	@Override
	public String InputPrompt() { return person.getLevel() + ":" + person.getUsername() + ">>>"; }
	@Override
	public String TerminatingCommand() { return "logout"; }

}
