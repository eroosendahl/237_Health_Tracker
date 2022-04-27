package tests;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import commands.CheckGoalCommand;
import commands.SetGoalCommand;
import commands.SummarizeCommand;
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
		SummarizeCommand summary = new SummarizeCommand(cp);
		cp.addCommand(summary);
		
		CheckGoalCommand checkGoalCommand = new CheckGoalCommand(cp);
		cp.addCommand(setGoalCommand);
		
		String activityName = "run";
		String goalValue = "5";
		String startDate = "01/01/2000";
		String endDate = "03/01/2000";
		String executionMod = activityName + " " + startDate + " " + endDate;
		
		setGoalCommand.execute(activityName + " " + goalValue);
		
		int[] summaryStats = summary.calculateStatistics(executionMod);
		String total = String.valueOf(summaryStats[1]);
		String mean = String.valueOf(summaryStats[2]);
		
		ByteArrayOutputStream newOut = new ByteArrayOutputStream();
		PrintStream oldOut = System.out;
		System.setOut(new PrintStream(newOut));
		checkGoalCommand.execute("run");
		assertTrue(newOut.toString().contains(goalValue));
		assertTrue(newOut.toString().contains(total));
		//assertTrue(newOut.toString().contains(mean));	// uncomment if we want to include the mean value in the printed statement from checkGoalCommand
	}

}
