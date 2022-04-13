package tests;

import static org.junit.Assert.assertEquals;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import commands.ChangeUsernameCommand;
import main.CommandPrompt;
import main.User;

public class ChangeUsernameTests {
	String testFile = "testFile";
	CommandPrompt commandPrompt;
	ChangeUsernameCommand changeUsernameCommand;
	
	@BeforeEach
	void setup() {
		User originalUser = new User("originalUser");
		commandPrompt = new CommandPrompt();
		try {
			createTestFile(testFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		commandPrompt.setFile(testFile);
		commandPrompt.setCurrentUser(originalUser);
		changeUsernameCommand = new ChangeUsernameCommand(commandPrompt);
		commandPrompt.addCommand(changeUsernameCommand);
	}

	@Test
	public void validChangeUsername() {
		String originalUsername = "originalUser";
		changeUsernameCommand.execute("newUsername");
		String newUsername = commandPrompt.getCurrentUser().getName();
		assertNotEquals(originalUsername, newUsername);
		assertEquals("newUsername", newUsername);
	}
	
	@Test
	public void invalidChangeUsername() {
		String originalUsername = "originalUser";
		changeUsernameCommand.execute("newUsername!");
		
		String newUsername = commandPrompt.getCurrentUser().getName();
		
		assertEquals("originalUser", newUsername);
	}
	
	@Test
	public void duplicateChangeUsername() {
		User firstUser = new User("firstUser");
		commandPrompt.addUser(firstUser);
		System.out.println(commandPrompt.getCurrentUser().getName());
		changeUsernameCommand.execute("firstUser!");
		
		String newUsername = commandPrompt.getCurrentUser().getName();
		System.out.println(newUsername);
		assertEquals("originalUser", newUsername);
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
