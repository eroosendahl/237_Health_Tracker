package tests;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import commands.SwitchUserCommand;
import main.CommandPrompt;
import main.User;

public class SwitchUserCommandTests {
	
	@Test
	void testSwitchUserCommand() {
		User originalUser = new User("originalUser", 0);
		CommandPrompt commandPrompt = new CommandPrompt(originalUser);
		
		User secondUser = new User("secondUser", 1);
		commandPrompt.addUser(secondUser);
		
		SwitchUserCommand switchUserCommand = new SwitchUserCommand(commandPrompt);
		commandPrompt.addCommand(switchUserCommand);
		
		switchUserCommand.execute(secondUser.getName());
		
		User userSwitchedTo = commandPrompt.getCurrentUser();
		
		assertEquals(secondUser, userSwitchedTo);
	}

}
 