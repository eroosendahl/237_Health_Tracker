package tests;

import static org.junit.Assert.assertEquals;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;

import commands.ChangeUsernameCommand;
import main.CommandPrompt;
import main.User;

public class ChangeUsernameTests {

	@Test
	public void validChangeUsername() {
		User originalUser = new User("originalUser", 0);
		String originalUsername = "originalUser";
		CommandPrompt commandPrompt = new CommandPrompt(originalUser);
		
		ChangeUsernameCommand changeUsernameCommand = new ChangeUsernameCommand(commandPrompt);
		commandPrompt.addCommand(changeUsernameCommand);
		
		changeUsernameCommand.execute("newUsername");
		
		String newUsername = commandPrompt.getCurrentUser().getName();
		
		assertNotEquals(originalUsername, newUsername);
		assertEquals("newUsername", newUsername);
	}
	
	@Test
	public void invalidChangeUsername() {
		User originalUser = new User("originalUser", 0);
		String originalUsername = "originalUser";
		CommandPrompt commandPrompt = new CommandPrompt(originalUser);
		
		ChangeUsernameCommand changeUsernameCommand = new ChangeUsernameCommand(commandPrompt);
		commandPrompt.addCommand(changeUsernameCommand);
		
		changeUsernameCommand.execute("newUsername!");
		
		String newUsername = commandPrompt.getCurrentUser().getName();
		
		assertEquals("originalUser", newUsername);
	}
	
	@Test
	public void duplicateChangeUsername() {
		User originalUser = new User("originalUser", 0);
		CommandPrompt commandPrompt = new CommandPrompt(originalUser);
		User firstUser = new User("firstUser", 0);
		commandPrompt.addUser(firstUser);
		ChangeUsernameCommand changeUsernameCommand = new ChangeUsernameCommand(commandPrompt);
		commandPrompt.addCommand(changeUsernameCommand);
		
		changeUsernameCommand.execute("firstUser!");
		
		String newUsername = commandPrompt.getCurrentUser().getName();
		
		assertEquals("originalUser", newUsername);
	}

}
