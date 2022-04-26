package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import commands.NewUserCommand;
import commands.SetGoalCommand;
import commands.SwitchUserCommand;
import main.CommandPrompt;
import main.HealthTrackerGeneralVariables;
import main.User;

class SetGoalCommandTests {
	CommandPrompt commandPrompt;
	SetGoalCommand newGoal;
	NewUserCommand newUser;
	SwitchUserCommand switchUser;
	String testFileName = "testUserInfo.csv";
	
	@BeforeEach
	void setup() throws IOException {
		commandPrompt = new CommandPrompt();
		commandPrompt.setFile(HealthTrackerGeneralVariables.generateTestFile().getName());
		newGoal = new SetGoalCommand(commandPrompt);
		commandPrompt.addCommand(newGoal);
		newUser = new NewUserCommand(commandPrompt);
		commandPrompt.addCommand(newUser);
		switchUser = new SwitchUserCommand(commandPrompt);
		commandPrompt.addCommand(switchUser);
		
		String exampleUserName = "testUser";
		User originalUser = new User(exampleUserName, 0);
		newUser.execute(exampleUserName);
		switchUser.execute(exampleUserName);
	}
	
	@Test
	void validGoalTest() throws IOException {
		String exampleGoal = "run 500";
		newGoal.execute(exampleGoal);
		String expectedNewGoal = "goal run(500)";
		boolean foundNewGoal = searchForEntry(testFileName, commandPrompt.getCurrentUser(), expectedNewGoal);
		deleteTestFile(testFileName);
		assertTrue(foundNewGoal);
	}
	
	@Test
	void invalidGoalTest() throws IOException {
		String exampleGoal = "run";
		newGoal.execute(exampleGoal);
		String expectedNewGoal = "goal run(500)";
		boolean foundnewGoal = searchForEntry(testFileName, commandPrompt.getCurrentUser(), expectedNewGoal);
		deleteTestFile(testFileName);
		assertFalse(foundnewGoal);
	}
	
	
	public boolean createTestFile(String fileName) throws IOException {
		
		File testFile = new File(fileName);
		
		return testFile.createNewFile();
	}
	
	public boolean deleteTestFile(String fileName) {
		
		File testFile = new File(fileName);
		
		return testFile.delete();
	}
	
	public boolean searchForEntry(String fileName, User user ,String goal) throws IOException {
		
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
					if(entries[i].contains(goal)) {
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
