package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import commands.NewEntryCommand;
import commands.NewUserCommand;
import commands.SummarizeCommand;
import main.CommandPrompt;
import main.User;

public class SummarizeCommandTests {
	
	CommandPrompt commandPrompt;
	NewEntryCommand newEntry;
	NewUserCommand newUser;
	SummarizeCommand summarize;
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
		summarize = new SummarizeCommand(commandPrompt);
		commandPrompt.addCommand(summarize);
		
		
	}
	
	@Test
	void testSummarizeSingleEntry() throws IOException {
		
		String exampleUserName = "testUser";
		User originalUser = new User(exampleUserName, 0);
		
		String exampleEntry = "09/01/2001 run 500";
		
		newUser.execute(exampleUserName);
		commandPrompt.setCurrentUser(originalUser);
		
		newEntry.execute(exampleEntry);
		
		String exampleSummaryEntry = "run 08/01/2001 10/01/2001";
		
		int[] results = summarize.calculateStatistics(exampleSummaryEntry);
		

		//summarize.execute(exampleSummaryEntry);
		
		deleteTestFile(testFile);
		
		assertEquals(0, results[0]);
		assertEquals(500, results[1]);
		assertEquals(500, results[2]);
		
		
		
		
	}
	
	@Test
	void testSummarizeUnchronologicalDates() throws IOException {
		
		String exampleUserName = "testUser";
		User originalUser = new User(exampleUserName, 0);
		
		String exampleEntry = "09/01/2001 run 500";
		
		newUser.execute(exampleUserName);
		commandPrompt.setCurrentUser(originalUser);
		
		newEntry.execute(exampleEntry);
		
		String exampleSummaryEntry = "run 10/01/2001 08/01/2001";
		
		int[] results = summarize.calculateStatistics(exampleSummaryEntry);
		
        //summarize.execute(exampleSummaryEntry);
		deleteTestFile(testFile);
		
		assertEquals(0, results[0]);
		assertEquals(500, results[1]);
		assertEquals(500, results[2]);
		
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
