package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import commands.NewEntryCommand;
import commands.NewUserCommand;
import commands.PrintProgressCommand;
import main.CommandPrompt;
import main.User;

public class PrintProgressTests {
	
	CommandPrompt commandPrompt;
	NewEntryCommand newEntry;
	NewUserCommand newUser;
	PrintProgressCommand printProgress;
	String testFile = "testFile";
	
	@BeforeEach
	void setup() throws IOException {
		
		commandPrompt = new CommandPrompt();
		createTestFile(testFile);
		commandPrompt.setFile(testFile);
		newEntry = new NewEntryCommand(commandPrompt);
		commandPrompt.addCommand(newEntry);
		newUser = new NewUserCommand(commandPrompt);
		commandPrompt.addCommand(newUser);
		printProgress = new PrintProgressCommand(commandPrompt);
		commandPrompt.addCommand(printProgress);
		
	}
	
	@Test
	void testPrintProgressMultipleEntries() throws IOException {
		
		String exampleUserName = "testUser";
		User originalUser = new User(exampleUserName, 0);
		
		String exampleEntry = "09/01/2001 run 500";
		String exampleEntry2 = "10/01/2001 ate 1000";
		String exampleEntry3 = "09/02/2001 walk 500";
		String exampleEntry4 = "11/01/2001 run 500";
		
		
		newUser.execute(exampleUserName);
		commandPrompt.setCurrentUser(originalUser);
		
		newEntry.execute(exampleEntry);
		newEntry.execute(exampleEntry2);
		newEntry.execute(exampleEntry3);
		newEntry.execute(exampleEntry4);
		
		String exampleProgressEntry = "run";
		
		int[] results = printProgress.printMatchingEntries(exampleProgressEntry);
		
		deleteTestFile(testFile);
		
		assertEquals(2,results[1]);
	}
	
	@Test
	void testPrintProgressSingleFoundEntry() throws IOException {
		
		String exampleUserName = "testUser";
		User originalUser = new User(exampleUserName, 0);
		
		String exampleEntry = "09/01/2001 run 500";
		String exampleEntry2 = "10/01/2001 ate 1000";
		String exampleEntry3 = "09/02/2001 walk 500";
		String exampleEntry4 = "11/01/2001 swam 500";
		
		
		newUser.execute(exampleUserName);
		commandPrompt.setCurrentUser(originalUser);
		
		newEntry.execute(exampleEntry);
		newEntry.execute(exampleEntry2);
		newEntry.execute(exampleEntry3);
		newEntry.execute(exampleEntry4);
		
		String exampleProgressEntry = "run";
		
		int[] results = printProgress.printMatchingEntries(exampleProgressEntry);
		
		deleteTestFile(testFile);
		
		assertEquals(1,results[1]);
	}
	@Test
	void testPrintProgressNoFoundEntries() throws IOException {
		
		String exampleUserName = "testUser";
		User originalUser = new User(exampleUserName, 0);
		
		String exampleEntry = "09/01/2001 bike 500";
		String exampleEntry2 = "10/01/2001 ate 1000";
		String exampleEntry3 = "09/02/2001 walk 500";
		String exampleEntry4 = "11/01/2001 swam 500";
		
		
		newUser.execute(exampleUserName);
		commandPrompt.setCurrentUser(originalUser);
		
		newEntry.execute(exampleEntry);
		newEntry.execute(exampleEntry2);
		newEntry.execute(exampleEntry3);
		newEntry.execute(exampleEntry4);
		
		String exampleProgressEntry = "run";
		
		int[] results = printProgress.printMatchingEntries(exampleProgressEntry);
		
		deleteTestFile(testFile);
		
		assertEquals(0,results[1]);
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