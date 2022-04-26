package tests;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import commands.AppendEntryCommand;
import commands.DisplayEntryCommand;
import main.CommandPrompt;
import main.HealthTrackerGeneralVariables;
import main.HealthTrackerGeneralVariables.endState;

class AppendEntryCommandTests {
	CommandPrompt commandPrompt;
	AppendEntryCommand appendEntryCommand;
	DisplayEntryCommand displayEntryCommand;
	String testFileName = "testUserInfo.csv";
	PrintStream oldOut = System.out;

	@BeforeEach
	void setup() throws IOException {
		commandPrompt = new CommandPrompt();
		HealthTrackerGeneralVariables.generateTestFile();
		commandPrompt.setFile(testFileName);
		
		appendEntryCommand = new AppendEntryCommand(commandPrompt);
		commandPrompt.addCommand(appendEntryCommand);
		displayEntryCommand = new DisplayEntryCommand(commandPrompt);
		commandPrompt.addCommand(displayEntryCommand);
	}
	
	@Test
	void testAppendToExistingActivity() {
		String testDate = "01/01/2000";
		String originalEntry = gatherEntry(testDate);
		String activity = "run";
		String value = "1";
		
		appendEntryCommand.execute(testDate + " " + activity + " " + value);
		
		String appendedEntry = gatherEntry(testDate);
		
		int originalValue = getActivityValue(originalEntry, activity);
		int appendedValue = getActivityValue(appendedEntry, activity);
		
		assertEquals(appendedValue-originalValue, Integer.parseInt(value));
	}

	@Test
	void testAppendToUnexistingActivity() {
		String testDate = "01/01/2000";
		String originalEntry = gatherEntry(testDate);
		String activity = "study";
		String value = "1";
		
		appendEntryCommand.execute(testDate + " " + activity + " " + value);
		
		String appendedEntry = gatherEntry(testDate);
		
		int originalValue = getActivityValue(originalEntry, activity);
		int appendedValue = getActivityValue(appendedEntry, activity);
		
		assertEquals(originalValue, endState.GENERAL_FAILURE.value());
		assertEquals(appendedValue, Integer.parseInt(value));
		
	}
	
	private int getActivityValue(String entry, String activity) {
		String[] miniEntries = entry.split(" ");
		for (String str : miniEntries) {
			if (str.contains(activity)) {
				return Integer.parseInt(str.substring(str.indexOf("(")+1, str.indexOf(")")));
			}
		}
		return endState.GENERAL_FAILURE.value();
	}

	private String gatherEntry(String date) {
		ByteArrayOutputStream newOut = new ByteArrayOutputStream();
		System.setOut(new PrintStream(newOut));
		displayEntryCommand.execute(date);
		System.setOut(oldOut);
		return newOut.toString();
	}
	
	
}
