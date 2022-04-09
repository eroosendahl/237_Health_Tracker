package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import commands.ChangeUsernameCommand;
import commands.NewEntryCommand;
import commands.NewUserCommand;
import main.CommandPrompt;
import main.User;

public class NewEntryCommandTests {
	
	
	@Test
	void testNewEntryInitial() throws IOException {
		
		String exampleUserName = "testUser";
		User originalUser = new User(exampleUserName, 0);
		
		String exampleEntry = "09/01/2001 run 500";
		
		String testFile = "testFile";
		
		createTestFile(testFile);
		
		CommandPrompt commandPrompt = new CommandPrompt();
		commandPrompt.setFile(testFile);
		
		NewEntryCommand newEntry = new NewEntryCommand(commandPrompt);
		commandPrompt.addCommand(newEntry);
		
		
		
		NewUserCommand newUser = new NewUserCommand(commandPrompt);
		commandPrompt.addCommand(newUser);
		
		newUser.execute(exampleUserName);
		commandPrompt.setCurrentUser(originalUser);
		
		//System.out.println(new File(testFile).delete());
		
		newEntry.execute(exampleEntry);
		
		String expectedEntry = "09/01/2001 run(500)";
		
		
		boolean found = searchForEntry(testFile,originalUser,expectedEntry);
		
		System.out.println((deleteTestFile(testFile)));
		
		assertEquals(true,found);
	}
	
	@Test
	void testNewEntrySameDateAndActivity() throws IOException {
		
		String exampleUserName = "testUser";
		User originalUser = new User(exampleUserName, 0);
		
		String exampleEntry = "09/01/2001 run 500";
		
		String testFile = "testFile";
		
		createTestFile(testFile);
		
		CommandPrompt commandPrompt = new CommandPrompt();
		commandPrompt.setFile(testFile);
		
		NewEntryCommand newEntry = new NewEntryCommand(commandPrompt);
		commandPrompt.addCommand(newEntry);
		
		NewUserCommand newUser = new NewUserCommand(commandPrompt);
		commandPrompt.addCommand(newUser);
		
		newUser.execute(exampleUserName);
		commandPrompt.setCurrentUser(originalUser);
		
		newEntry.execute(exampleEntry);
		newEntry.execute(exampleEntry);
		
		String expectedEntry = "09/01/2001 run(1000)";
		
		
		boolean found = searchForEntry(testFile,originalUser,expectedEntry);
		
		
		
		
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
