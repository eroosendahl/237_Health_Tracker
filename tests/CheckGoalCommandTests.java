package tests;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import commands.SetGoalCommand;
import main.CommandPrompt;
import main.HealthTrackerGeneralVariables;

public class CheckGoalCommandTests {
	
	@Test
	void testCheckGoalCommand() {
		CommandPrompt cp = new CommandPrompt();
		File testFile = HealthTrackerGeneralVariables.generateTestFile();
		cp.setFile(testFile.getName());
		
		SetGoalCommand setGoalCommand = new SetGoalCommand(cp);
		cp.addCommand(setGoalCommand);
		
		// Uncomment these after implementing the command.
		//CheckGoalCommand checkGoalCommand = new CheckGoalCommand(cp);
		//cp.addCommand(setGoalCommand);
		
		setGoalCommand.execute("run 5");
		
		ByteArrayOutputStream newOut = new ByteArrayOutputStream();
		PrintStream oldOut = System.out;
		System.setOut(new PrintStream(newOut));
		//checkGoalCommand.execute("run");
		assertTrue(newOut.toString().contains("5"));
	}

}
