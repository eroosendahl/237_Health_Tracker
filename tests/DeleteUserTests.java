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
	String testFileName = "testFile";
	CommandPrompt commandPrompt;
	NewUserCommand newUserCommand;
	DeleteUserCommand deleteUserCommand;
	
	@BeforeEach
	void setup() {
		User arthur = new User("Arthur");
		this.commandPrompt = new CommandPrompt();
		
		try { createTestFile(testFileName); }
		catch (IOException e) { e.printStackTrace(); }
		
		commandPrompt.setFile(testFileName);
		newUserCommand = new NewUserCommand(commandPrompt);
		commandPrompt.addCommand(newUserCommand);
		deleteUserCommand = new DeleteUserCommand(commandPrompt);
		commandPrompt.addCommand(deleteUserCommand);
		
		newUserCommand.execute("Arthur");
		commandPrompt.setCurrentUser(arthur);
		newUserCommand.execute("Bertha");
		newUserCommand.execute("Clara");
		commandPrompt.loadExistantUsers();
	}
	
	@Test
	public void testValidDeleteUser() {
		int numUsers = commandPrompt.getUsers().size();
		
		deleteUserCommand.execute("Clara");
		commandPrompt.loadExistantUsers();
		int newNumUsers = commandPrompt.getUsers().size();
		boolean found = commandPrompt.containsUser("Clara");
		deleteTestFile(testFileName);
		
		assertFalse(found);
		assertEquals(newNumUsers, numUsers-1);
	}
	
	@Test
	public void testInvalidDeleteUser() {
		int numUsers = commandPrompt.getUsers().size();
		
		deleteUserCommand.execute("Daniel");
		commandPrompt.loadExistantUsers();
		int newNumUsers = commandPrompt.getUsers().size();
		boolean found = commandPrompt.containsUser("Daniel");
		deleteTestFile(testFileName);
		
		assertFalse(found);
		assertEquals(newNumUsers, numUsers);
	}
	
	@Test
	public void testNoSelfDelete() {
		int numUsers = commandPrompt.getUsers().size();
		
		deleteUserCommand.execute("Arthur");
		commandPrompt.loadExistantUsers();
		int newNumUsers = commandPrompt.getUsers().size();
		boolean found = commandPrompt.containsUser("Arthur");
		deleteTestFile(testFileName);
		
		assertTrue(found);
		assertEquals(newNumUsers, numUsers);
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
