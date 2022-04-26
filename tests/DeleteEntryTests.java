package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import commands.DeleteEntryCommand;
import commands.NewEntryCommand;
import commands.NewUserCommand;
import commands.SwitchUserCommand;
import main.CommandPrompt;
import main.HealthTrackerGeneralVariables;
import main.User;

public class DeleteEntryTests {
	CommandPrompt commandPrompt;
	NewEntryCommand newEntry;
	NewUserCommand newUser;
	SwitchUserCommand switchUser;
	DeleteEntryCommand deleteEntryCommand;
	String testFileName = "testUserInfo.csv";

	@BeforeEach
	void setup() throws IOException {
		commandPrompt = new CommandPrompt();
		commandPrompt.setFile(HealthTrackerGeneralVariables.generateTestFile().getName());
		newEntry = new NewEntryCommand(commandPrompt);
		commandPrompt.addCommand(newEntry);
		switchUser = new SwitchUserCommand(commandPrompt);
		commandPrompt.addCommand(switchUser);
		newUser = new NewUserCommand(commandPrompt);
		commandPrompt.addCommand(newUser);
		deleteEntryCommand = new DeleteEntryCommand(commandPrompt);
		commandPrompt.addCommand(deleteEntryCommand);
		
		String exampleUserName = "testUser";

		String entry1 = "09/01/2001 run 500";
		String entry2 = "09/01/2001 eat 2000";
		String entry3 = "10/01/2001 run 500";
		
		newUser.execute(exampleUserName);
		switchUser.execute(exampleUserName);
		newEntry.execute(entry1);
		newEntry.execute(entry2);
		newEntry.execute(entry3);
	}
	
	@Test
	void testDeleteDate() throws IOException {
		String exampleEntry = "09/01/2001";
		deleteEntryCommand.execute(exampleEntry);
		
		String oldEntry1 = "09/01/2001 run(500) eat(2000)";
		boolean foundOldEntry1 = searchForEntry(testFileName, commandPrompt.getCurrentUser(), oldEntry1);
		String oldEntry2 = "10/01/2001 run(500)";
		boolean foundOldEntry2 = searchForEntry(testFileName, commandPrompt.getCurrentUser(), oldEntry2);
		deleteTestFile(testFileName);
		
		assertFalse(foundOldEntry1);
		assertTrue(foundOldEntry2);
	}
	
	@Test
	void testDeleteDateAndActivity() throws IOException {
		String exampleEntry = "09/01/2001 run";
		deleteEntryCommand.execute(exampleEntry);
		
		String oldEntry1 = "09/01/2001 run(500) eat(2000)";
		boolean foundOldEntry1 = searchForEntry(testFileName, commandPrompt.getCurrentUser(), oldEntry1);
		String oldEntry2 = "09/01/2001 eat(2000)";
		boolean foundOldEntry2 = searchForEntry(testFileName, commandPrompt.getCurrentUser(), oldEntry2);
		String oldEntry3 = "10/01/2001 run(500)";
		boolean foundOldEntry3 = searchForEntry(testFileName, commandPrompt.getCurrentUser(), oldEntry3);
		deleteTestFile(testFileName);
		
		assertFalse(foundOldEntry1);
		assertTrue(foundOldEntry2);
		assertTrue(foundOldEntry3);
	}
	
	public boolean createTestFile(String fileName) throws IOException {
		File testFile = new File(fileName);
		return testFile.createNewFile();
	}
	
	public boolean deleteTestFile(String fileName) {
		File testFile = new File(fileName);
		return testFile.delete();
	}
	
	public boolean searchForEntry(String fileName, User user ,String entry) throws IOException {
		File csvFile = new File(fileName);
		FileReader csvReader = new FileReader(csvFile);
		BufferedReader csvBufferedReader = new BufferedReader(csvReader);
		String line = null;
		boolean found = false;

		int count = 0;
		while ((line = csvBufferedReader.readLine()) != null) {
			if(user.getRow()==count) {
				String[] entries = line.split(",");
				for(int i =1; i <entries.length; i++) {
					if(entries[i].contains(entry)) {
						found = true;
						csvReader.close();
						csvBufferedReader.close();
						return found;
					}
				}
			}
			count++;
		}
		csvReader.close();
		csvBufferedReader.close();
		
		return found;
	}
}
