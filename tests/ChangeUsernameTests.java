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
		String originalUsername = "originalUser";
		String newUsername = "newUser";
		newUserCommand.execute(originalUsername);
		newUserCommand.execute(newUsername);
		switchUserCommand.execute(originalUsername);
		
		String preswitchUsername =commandPrompt.getCurrentUser().getName();
		changeUsernameCommand.execute(newUsername);
		
		String postswitchUsername =commandPrompt.getCurrentUser().getName();
		
		assertNotEquals(preswitchUsername, postswitchUsername);
		assertEquals(newUsername, postswitchUsername);
	}
	
	@Test
	public void invalidChangeUsername() {
		String originalUsername = "originalUser";
		String nonexistantUsername = "nonexistantUser";
		newUserCommand.execute(originalUsername);
		switchUserCommand.execute(originalUsername);
		
		String preswitchUsername =commandPrompt.getCurrentUser().getName();
		changeUsernameCommand.execute(nonexistantUsername);
		
		String postswitchUsername =commandPrompt.getCurrentUser().getName();
		
		assertNotEquals(nonexistantUsername, postswitchUsername);
		assertEquals(preswitchUsername, postswitchUsername);
	}
	
public boolean createTestFile(String fileName) throws IOException {
		
		File testFile = new File(fileName);
		
		return testFile.createNewFile();
	}
	
	public boolean deleteTestFile(String fileName) {
		
		File testFile = new File(fileName);
		
		return testFile.delete();
	}

}
