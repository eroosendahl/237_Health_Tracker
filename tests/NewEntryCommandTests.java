package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import commands.ChangeUsernameCommand;
import commands.NewEntryCommand;
import commands.NewUserCommand;
import commands.SwitchUserCommand;
import main.CommandPrompt;
import main.HealthTracker;
import main.HealthTrackerGeneralVariables;
import main.User;

public class NewEntryCommandTests {
	
	CommandPrompt commandPrompt;
	NewEntryCommand newEntry;
	NewUserCommand newUser;
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
		switchUser = new SwitchUserCommand(commandPrompt);
		commandPrompt.addCommand(switchUser);
		
	}
	
	@Test
	void testNewEntryInitial() throws IOException {
		
		String exampleUserName = "testUser";
		String exampleEntry = "09/01/2001 run 500";
		
		newUser.execute(exampleUserName);
		switchUser.execute(exampleUserName);
		
		newEntry.execute(exampleEntry);
		
		String expectedEntry = "09/01/2001 run(500)";
		
		
		//boolean found = searchForEntry(testFile,originalUser,expectedEntry);
		boolean found = searchForEntry(testFile, expectedEntry);
		
		assertEquals(true,found);
	}
	
	@Test
	void testNewEntrySameDateAndActivity() throws IOException {
		
		String exampleUserName = "testUser";
		
		String exampleEntry = "09/01/2001 run 500";
		
		newUser.execute(exampleUserName);
		switchUser.execute(exampleUserName);
		
		newEntry.execute(exampleEntry);
		newEntry.execute(exampleEntry);
		
		String expectedEntry = "09/01/2001 run(1000)";
		
		
		//boolean found = searchForEntry(testFile,originalUser,expectedEntry);
		boolean found = searchForEntry(testFile,expectedEntry);
		
		deleteTestFile(testFile);
		assertEquals(true,found);
		
	}
	
	@Test
	void testNewEntrySameDateDifferentActivity() throws IOException {
		
		String exampleUserName = "testUser";
		
		String exampleEntry = "09/01/2001 run 500";
		String exampleEntrySecond = "09/01/2001 eat 500";
		
		
		newUser.execute(exampleUserName);
		switchUser.execute(exampleUserName);
		
		newEntry.execute(exampleEntry);
		newEntry.execute(exampleEntrySecond);
		
		String expectedEntry = "09/01/2001 run(500) eat(500)";
		//boolean found = searchForEntry(testFile,originalUser,expectedEntry);
		boolean found = searchForEntry(testFile,expectedEntry);
		
		deleteTestFile(testFile);
		
		assertEquals(true,found);
	}
	
	
	
	@Test
	void testNewEntryMiddleUser() throws IOException {
		
		String exampleUserName = "testUser";
		User originalUser = new User(exampleUserName, 0);
		
		String exampleUserNameSecond = "testUser2";
		User originalUserSecond = new User(exampleUserNameSecond, 1);
		
		String exampleUserNameThird = "testUser3";
		User originalUserThird = new User(exampleUserNameThird, 2);
		
		String exampleEntry = "09/01/2001 run 500";
		
		newUser.execute(exampleUserName);
		newUser.execute(exampleUserNameSecond);
		newUser.execute(exampleUserNameThird);
		switchUser.execute(exampleUserNameSecond);
		
		newEntry.execute(exampleEntry);
		String expectedEntry = "09/01/2001 run(500)";
		//boolean found = searchForEntry(testFile,originalUserSecond,expectedEntry);
		boolean found = searchForEntry(testFile, expectedEntry);
		
		deleteTestFile(testFile);
		assertEquals(true,found);
	}
	
	
	
	public boolean createTestFile(String fileName) throws IOException {
		
		File testFile = new File(fileName);
		
		return testFile.createNewFile();
	}
	
	public boolean deleteTestFile(String fileName) {
		
		File testFile = new File(fileName);
		
		return testFile.delete();
	}
	
	public boolean searchForEntry(String fileName, String entry) throws IOException {
		User user = commandPrompt.getCurrentUser();
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
