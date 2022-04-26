package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import commands.AbstractCommand;
import commands.ChangeUsernameCommand;
import commands.NewUserCommand;
import main.CommandPrompt;
import main.HealthTrackerGeneralVariables;
import main.User;

public class NewUserTest {
	String testFile = "testUserInfo.csv";
	CommandPrompt commandPrompt;
	NewUserCommand newUserCommand;
	
	@BeforeEach
	void setup() {
		User originalUser = new User("originalUser");
		commandPrompt = new CommandPrompt();
		commandPrompt.setFile(HealthTrackerGeneralVariables.generateTestFile().getName());
		commandPrompt.setCurrentUser(originalUser);
		newUserCommand = new NewUserCommand(commandPrompt);
		commandPrompt.addCommand(newUserCommand);
	}

	@Test
	public void testValidNewUser() {
		newUserCommand.execute("newUser");
		commandPrompt.loadExistantUsers();
		assertTrue(commandPrompt.containsUser("newUser"));
		deleteTestFile(testFile);
	}
	
	@Test
	public void testInvalidNewUser() {
		newUserCommand.execute("newUsername!");
		String newUser = commandPrompt.getCurrentUser().getName();
		deleteTestFile(testFile);
		assertEquals("originalUser", newUser);
	}
	
	@Test
	public void testDuplicateChangeUsername() {
		User firstUser = new User("firstUser");
		commandPrompt.addUser(firstUser);
		newUserCommand.execute("firstUser");
		String newUser = commandPrompt.getCurrentUser().getName();
		deleteTestFile(testFile);
		assertEquals("originalUser", newUser);
		
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
