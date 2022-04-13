package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import commands.DeleteUserCommand;
import commands.NewUserCommand;
import main.CommandPrompt;
import main.User;

public class DeleteUserTests {
	String testFile = "testFile";
	CommandPrompt commandPrompt;
	NewUserCommand newUserCommand;
	DeleteUserCommand deleteUserCommand;
	
	@BeforeEach
	void setup() {
		User alex = new User("Alexander");
		this.commandPrompt = new CommandPrompt();
		
		try { createTestFile(testFile); }
		catch (IOException e) { e.printStackTrace(); }
		
		commandPrompt.setFile(testFile);
		commandPrompt.setCurrentUser(alex);
		newUserCommand = new NewUserCommand(commandPrompt);
		commandPrompt.addCommand(newUserCommand);
		deleteUserCommand = new DeleteUserCommand(commandPrompt);
		commandPrompt.addCommand(deleteUserCommand);
		
		newUserCommand.execute("Bertha");
		newUserCommand.execute("Clara");
		commandPrompt.loadExistantUsers();
	}
	
	@Test
	public void testValidDeleteUser() {
		int numUsers = commandPrompt.getNumUsers();
		
		deleteUserCommand.execute("Clara");
		commandPrompt.loadExistantUsers();
		int newNumUsers = commandPrompt.getNumUsers();
		boolean found = commandPrompt.containsUser("Clara");
		deleteTestFile(testFile);
		
		assertFalse(found);
		assertEquals(newNumUsers, numUsers-1);
	}
	
	@Test
	public void testInvalidDeleteUser() {
		int numUsers = commandPrompt.getNumUsers();
		
		deleteUserCommand.execute("Daniel");
		commandPrompt.loadExistantUsers();
		int newNumUsers = commandPrompt.getNumUsers();
		boolean found = commandPrompt.containsUser("Daniel");
		deleteTestFile(testFile);
		
		assertFalse(found);
		assertEquals(newNumUsers, numUsers);
	}
	
	@Test
	public void testNoDoubleDelete() {
		int numUsers = commandPrompt.getNumUsers();
		
		deleteUserCommand.execute("Clara");
		commandPrompt.loadExistantUsers();
		deleteUserCommand.execute("Clara");
		commandPrompt.loadExistantUsers();
		int newNumUsers = commandPrompt.getNumUsers();
		boolean found = commandPrompt.containsUser("Clara");
		deleteTestFile(testFile);
		
		assertFalse(found);
		assertEquals(newNumUsers, numUsers-1);
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
