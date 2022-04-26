package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import commands.NewEntryCommand;
import commands.NewUserCommand;
import commands.SummarizeCommand;
import commands.SwitchUserCommand;
import main.CommandPrompt;
import main.HealthTrackerGeneralVariables;
import main.User;

public class SummarizeCommandTests {
	
	CommandPrompt commandPrompt;
	NewEntryCommand newEntry;
	NewUserCommand newUser;
	SummarizeCommand summarize;
	SwitchUserCommand switchUser;
	String testFile = "testUserInfo.csv";
	
	@BeforeEach
	void setup() throws IOException {
		
		commandPrompt = new CommandPrompt();
		commandPrompt.setFile(HealthTrackerGeneralVariables.generateTestFile().getName());
		newEntry = new NewEntryCommand(commandPrompt);
		commandPrompt.addCommand(newEntry);
		newUser = new NewUserCommand(commandPrompt);
		commandPrompt.addCommand(newUser);
		summarize = new SummarizeCommand(commandPrompt);
		commandPrompt.addCommand(summarize);
		switchUser = new SwitchUserCommand(commandPrompt);
		commandPrompt.addCommand(switchUser);
		
		
	}
	
	@Test
	void testSummarizeSingleEntry() throws IOException {
		
		String exampleUserName = "testUser";
		
		String exampleEntry = "09/01/2001 run 500";
		
		newUser.execute(exampleUserName);
		switchUser.execute(exampleUserName);
		
		newEntry.execute(exampleEntry);
		
		//3 day period
		String exampleSummaryEntry = "run 08/01/2001 10/01/2001";
		
		int[] results = summarize.calculateStatistics(exampleSummaryEntry);
		

		//summarize.execute(exampleSummaryEntry);
		
		deleteTestFile(testFile);
		
		assertEquals(0, results[0]);
		assertEquals(500, results[1]);
		assertEquals(500/3, results[2]);
		
		
		
		
	}
	
	@Test
	void testSummarizeUnchronologicalDates() throws IOException {
		
		String exampleUserName = "testUser";
		User originalUser = new User(exampleUserName, 0);
		
		String exampleEntry = "09/01/2001 run 500";
		
		newUser.execute(exampleUserName);
		switchUser.execute(exampleUserName);
		
		newEntry.execute(exampleEntry);
		
		//3 day period
		String exampleSummaryEntry = "run 10/01/2001 08/01/2001";
		
		int[] results = summarize.calculateStatistics(exampleSummaryEntry);
		
        //summarize.execute(exampleSummaryEntry);
		deleteTestFile(testFile);
		
		assertEquals(0, results[0]);
		assertEquals(500, results[1]);
		assertEquals(500/3, results[2]);
		
	}
	
	@Test
	void testSummarizeMultipleEntries() throws IOException {
		
		String exampleUserName = "testUser";
		
		String exampleEntry = "09/01/2001 run 500";
		String exampleEntry2 = "10/01/2001 run 1000";
		String exampleEntry3 = "09/01/2001 walk 500";
		String exampleEntry4 = "11/01/2001 run 500";
		
		
		newUser.execute(exampleUserName);
		switchUser.execute(exampleUserName);
		
		newEntry.execute(exampleEntry);
		newEntry.execute(exampleEntry2);
		newEntry.execute(exampleEntry3);
		newEntry.execute(exampleEntry4);
		
		//ten day period
		String exampleSummaryEntry = "run 06/01/2001 15/01/2001";
		
		int[] results = summarize.calculateStatistics(exampleSummaryEntry);
		
        //summarize.execute(exampleSummaryEntry);
		deleteTestFile(testFile);
		
		assertEquals(0, results[0]);
		assertEquals(2000, results[1]);
		assertEquals(2000/10, results[2]);
		
	}
	
	@Test
	void testSummarizeInclusiveDatedEntries() throws IOException {
		
		String exampleUserName = "testUser";
		
		String exampleEntry = "09/01/2001 run 500";
		String exampleEntry2 = "10/01/2001 run 1000";
		String exampleEntry3 = "09/01/2001 walk 500";
		String exampleEntry4 = "11/01/2001 run 500";
		
		
		newUser.execute(exampleUserName);
		switchUser.execute(exampleUserName);
		
		newEntry.execute(exampleEntry);
		newEntry.execute(exampleEntry2);
		newEntry.execute(exampleEntry3);
		newEntry.execute(exampleEntry4);
		
		//ten day period
		String exampleSummaryEntry = "run 09/01/2001 11/01/2001";
		
		int[] results = summarize.calculateStatistics(exampleSummaryEntry);
		
        //summarize.execute(exampleSummaryEntry);
		deleteTestFile(testFile);
		
		assertEquals(0, results[0]);
		assertEquals(2000, results[1]);
		assertEquals(2000/3, results[2]);
		
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
