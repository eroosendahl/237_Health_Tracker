package tests;

import static org.junit.Assert.assertEquals;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import commands.ChangeUsernameCommand;
import commands.NewUserCommand;
import commands.SwitchUserCommand;
import main.CommandPrompt;
import main.HealthTrackerGeneralVariables;
import main.User;

public class ChangeUsernameTests {
	String testFile = "testFile";
	CommandPrompt commandPrompt;
	ChangeUsernameCommand changeUsernameCommand;
	NewUserCommand newUserCommand;
	SwitchUserCommand switchUserCommand;
	
	@BeforeEach
	void setup() {
		commandPrompt = new CommandPrompt();
		commandPrompt.setFile(HealthTrackerGeneralVariables.generateTestFile().getName());	
		newUserCommand = new NewUserCommand(commandPrompt);
		commandPrompt.addCommand(newUserCommand);
		changeUsernameCommand = new ChangeUsernameCommand(commandPrompt);
		commandPrompt.addCommand(changeUsernameCommand);
		switchUserCommand = new SwitchUserCommand(commandPrompt);
		commandPrompt.addCommand(switchUserCommand);
	}

	@Test
	public void validChangeUsername() {
		String originalUsername = commandPrompt.getCurrentUser().getName();
		String newUsername = "newUsername";
		changeUsernameCommand.execute(newUsername);
		
		String changedUsername = commandPrompt.getCurrentUser().getName();
		
		assertNotEquals(originalUsername, changedUsername);
		assertEquals(newUsername, changedUsername);
	}
}
